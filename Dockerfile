FROM eclipse-temurin:25-jre
ENV TZ=Europe/Istanbul
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
ARG JAR_FILE=target/wms.war
COPY ${JAR_FILE} wms.war
ENTRYPOINT java \
  -Xms${JAVA_MINHEAP} \
  -Xmx${JAVA_MAXHEAP} \
  -Xss${JAVA_STACKSIZE} \
  -XX:MetaspaceSize=256m \
  -XX:MaxMetaspaceSize=512m \
  -XX:+UseG1GC \
  -XX:MaxGCPauseMillis=200 \
  -XX:+UseStringDeduplication \
  -XX:+ParallelRefProcEnabled \
  -Dspring.profiles.active=$SPRING_PROFILE \
  -Djdk.tls.client.protocols=TLSv1.2 \
  -jar wms.war
