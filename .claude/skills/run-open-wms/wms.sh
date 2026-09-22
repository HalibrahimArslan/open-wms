#!/usr/bin/env bash
# open-wms backend'ini host'ta (dev profili, ./mvnw) calistirir ve suren uygulamayi
# curl ile surer. Repo kokunden cagrilir:
#   .claude/skills/run-open-wms/wms.sh <komut> [arguman...]
#
# Komutlar:
#   deps              bagimli konteynerleri (postgres, mail, websocket, minio) kaldirir
#   start             deps + uygulamayi arka planda baslatir, hazir olana kadar bekler, seed yukler
#   stop              8080'i dinleyen uygulamayi durdurur (konteynerlere dokunmaz)
#   status            uygulama ayakta mi
#   token [kul] [sif] JWT basar (varsayilan admin/admin)
#   call METOD YOL [JSON]   token ile istek atar, HTTP kodu ve govdeyi basar
#   smoke             giris + birkac uc; hepsi 200 degilse cikis kodu 1
#   log [desen]       uygulama logu (ANSI temizlenmis); desen verilirse grep
set -euo pipefail

ROOT=$(cd "$(dirname "$0")/../../.." && pwd)
cd "$ROOT"

PORT=8080
BASE="http://localhost:$PORT"
LOG_DIR=${WMS_LOG_DIR:-/tmp/open-wms}
LOG="$LOG_DIR/app.log"
mkdir -p "$LOG_DIR"

# .env docker compose icin yazilmistir (POSTGRES_DB, MINIO_ACCESS_KEY ...). Uygulama ise
# POSTGRESQL_CONNECTION_URL, MINIO_ACCESSKEY gibi farkli adlar okur; compose bunlari
# app servisinde uretir. Host'ta calisirken ayni esleme burada, adresler localhost'a
# cevrilerek yapilir.
load_env() {
    # .env compose sozdizimindedir (JAVA_OPTS=-Xms256m -Xmx1024m gibi tirnaksiz bosluklu
    # degerler); bash ile source edilemez, satir satir okunur.
    if [ -f .env ]; then
        local key value
        while IFS='=' read -r key value; do
            [[ $key =~ ^[A-Z_][A-Z0-9_]*$ ]] || continue
            value=${value%$'\r'}
            value=${value#\"}; value=${value%\"}
            value=${value#\'}; value=${value%\'}
            export "$key=$value"
        done < .env
    fi
    export POSTGRESQL_CONNECTION_URL="jdbc:postgresql://localhost:${POSTGRES_PORT:-5432}/${POSTGRES_DB:-wms}"
    export POSTGRESQL_DB_USERNAME="${POSTGRES_USER:-wms}"
    export POSTGRESQL_DB_PASSWORD="${POSTGRES_PASSWORD:-wms}"
    export MAIL_HOST=localhost MAIL_PORT="${MAILPIT_SMTP_PORT:-1025}" MAIL_USERNAME= MAIL_PASSWORD=
    export MAIL_URL="$BASE"
    export WEBSOCKET_URI="ws://localhost:${WEBSOCKET_PORT:-8090}/websocket"
    export METABASE_SECRET_URL="${METABASE_SECRET_URL:-http://localhost:3000}"
    export METABASE_SECRET_KEY="${METABASE_SECRET_KEY:-local-dev-dummy-secret}"
    export METABASE_DASHBOARD_CODE="${METABASE_DASHBOARD_CODE:-1}"
    export SENTRY_DSN="${SENTRY_DSN:-}"
    export MINIO_URL="http://localhost:${MINIO_API_PORT:-9000}"
    export MINIO_ACCESSKEY="${MINIO_ACCESS_KEY:-wmsadmin}"
    export MINIO_SECRETKEY="${MINIO_SECRET_KEY:-wmsadmin123}"
    export MINIO_BUCKETNAME="${MINIO_BUCKET:-wms}"
    # .env'deki bu ikisi konteyner icindir; ./mvnw'ye sizarsa profil/JVM ayari degisir.
    unset SPRING_PROFILE JAVA_OPTS
}

listener_pid() {
    ss -ltnpH "sport = :$PORT" 2>/dev/null | grep -o 'pid=[0-9]*' | head -1 | cut -d= -f2
}

clean_log() {
    sed 's/\x1b\[[0-9;]*m//g' "$LOG"
}

cmd_deps() {
    docker compose up -d --wait postgres mail websocket minio
}

cmd_start() {
    if [ -n "$(listener_pid)" ]; then
        echo "8080 zaten dinleniyor (pid $(listener_pid)); once 'stop'." >&2
        exit 1
    fi
    cmd_deps
    load_env
    : > "$LOG"
    nohup ./mvnw > "$LOG" 2>&1 &
    echo "baslatildi, log: $LOG"
    for _ in $(seq 1 300); do
        if clean_log | grep -q "Started WmsApp"; then
            clean_log | grep "Started WmsApp"
            # Idempotent; sema Liquibase ile acilista kurulduktan sonra yuklenebilir.
            docker exec -i wms-postgres psql -q -U "${POSTGRES_USER:-wms}" -d "${POSTGRES_DB:-wms}" < seed/local-seed.sql > /dev/null
            echo "seed yuklendi; hazir: $BASE"
            return 0
        fi
        if clean_log | grep -qE "Application run failed|BUILD FAILURE"; then
            clean_log | grep -E "Caused by|BUILD FAILURE|ERROR\]" | tail -5 >&2
            exit 1
        fi
        sleep 1
    done
    echo "300 sn icinde hazir olmadi; log: $LOG" >&2
    exit 1
}

cmd_stop() {
    local pid
    pid=$(listener_pid)
    if [ -z "$pid" ]; then
        echo "calisan uygulama yok"
        return 0
    fi
    kill "$pid"
    # Port, JVM kapanisini bitirmeden bosalir; surecin kendisi beklenir.
    for _ in $(seq 1 30); do
        kill -0 "$pid" 2>/dev/null || { echo "durduruldu"; return 0; }
        sleep 1
    done
    echo "durmadi (pid $pid)" >&2
    exit 1
}

cmd_status() {
    if [ -n "$(listener_pid)" ]; then
        echo "calisiyor (pid $(listener_pid)) $BASE"
    else
        echo "calismiyor"
        exit 1
    fi
}

cmd_token() {
    local user=${1:-admin} pass=${2:-admin}
    curl -sf -X POST "$BASE/api/authenticate" -H 'Content-Type: application/json' \
        -d "{\"username\":\"$user\",\"password\":\"$pass\"}" |
        python3 -c 'import sys, json; print(json.load(sys.stdin)["id_token"])'
}

cmd_call() {
    local method=$1 path=$2 body=${3:-}
    local token
    token=$(cmd_token)
    local args=(-s -X "$method" -H "Authorization: Bearer $token" -w '\nHTTP %{http_code}\n')
    [ -n "$body" ] && args+=(-H 'Content-Type: application/json' -d "$body")
    curl "${args[@]}" "$BASE$path"
}

cmd_smoke() {
    local token fail=0 code
    token=$(cmd_token) || { echo "giris basarisiz" >&2; exit 1; }
    echo "giris: admin OK"
    for path in /api/account /api/aur-depo-urun-adres-stok /management/health; do
        code=$(curl -s -o /dev/null -w '%{http_code}' -H "Authorization: Bearer $token" "$BASE$path")
        echo "$code $path"
        [ "$code" = 200 ] || fail=1
    done
    exit $fail
}

cmd_log() {
    if [ $# -gt 0 ]; then clean_log | grep -E "$1"; else clean_log; fi
}

case "${1:-}" in
    deps) cmd_deps ;;
    start) cmd_start ;;
    stop) cmd_stop ;;
    status) cmd_status ;;
    token) shift; cmd_token "$@" ;;
    call) shift; cmd_call "$@" ;;
    smoke) cmd_smoke ;;
    log) shift; cmd_log "$@" ;;
    *) sed -n '2,14p' "$0"; exit 2 ;;
esac
