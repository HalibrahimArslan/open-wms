# Onceden derlenmis war'dan imaj (CI: mvn clean package, ardindan docker build .).
# Derlemeyi imaj icinde yapan surum icin Dockerfile.local'e bakin.
#
# Ortam degiskenlerinin hepsi istege baglidir:
#   JAVA_MINHEAP / JAVA_MAXHEAP / JAVA_STACKSIZE  -> -Xms / -Xmx / -Xss (orn. 512m)
#   JAVA_OPTS                                     -> ek JVM argumanlari
#   SPRING_PROFILE                                -> -Dspring.profiles.active
#                                                    (SPRING_PROFILES_ACTIVE de calisir)
# Bos birakilan deger JVM'e hic gecilmez; eskiden bos -Xms gibi argumanlarla JVM acilmiyordu.
FROM eclipse-temurin:25-jre
ENV TZ=Europe/Istanbul
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone \
    && apt-get update && apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/*
WORKDIR /app
ARG JAR_FILE=target/wms.war
COPY ${JAR_FILE} /app/wms.war
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "exec java \
  ${JAVA_MINHEAP:+-Xms$JAVA_MINHEAP} \
  ${JAVA_MAXHEAP:+-Xmx$JAVA_MAXHEAP} \
  ${JAVA_STACKSIZE:+-Xss$JAVA_STACKSIZE} \
  -XX:MetaspaceSize=256m \
  -XX:MaxMetaspaceSize=512m \
  -XX:+UseG1GC \
  -XX:MaxGCPauseMillis=200 \
  -XX:+UseStringDeduplication \
  -XX:+ParallelRefProcEnabled \
  $JAVA_OPTS \
  ${SPRING_PROFILE:+-Dspring.profiles.active=$SPRING_PROFILE} \
  -Djava.security.egd=file:/dev/./urandom \
  -Djdk.tls.client.protocols=TLSv1.2 \
  -jar /app/wms.war"]
