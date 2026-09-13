# WMS WebSocket Sunucusu

Lokal gelistirme icin kucuk bir bildirim sunucusu. Backend'deki
`WebSocketClientService` acilista buraya baglanir; sayim ve ERP islemleri
sirasinda gonderilen bildirimler bagli tum istemcilere yayinlanir.

Compose ile birlikte ayaga kalkar:

    docker compose up -d websocket

Disaridan denemek icin (ornegin `wscat`):

    npx wscat -c ws://localhost:8090/websocket

Saglik kontrolu: <http://localhost:8090/health>
