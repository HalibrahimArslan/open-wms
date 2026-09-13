FROM adoptopenjdk:11-jre-hotspot
ENV TZ=Europe/Istanbul
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
ARG JAR_FILE=target/wms.war
COPY ${JAR_FILE} wms.war
COPY elastic-apm-agent-1.52.1.jar /app/elastic-apm-agent-1.52.1.jar
ENTRYPOINT java -javaagent:/app/elastic-apm-agent-1.52.1.jar \
  -Xms${JAVA_MINHEAP} \
  -Xmx${JAVA_MAXHEAP} \
  -Xss${JAVA_STACKSIZE} \
  -XX:MetaspaceSize=256m \
  -XX:MaxMetaspaceSize=512m \
  -XX:+UseG1GC \
  -XX:MaxGCPauseMillis=200 \
  -XX:+UseStringDeduplication \
  -XX:+ParallelRefProcEnabled \
  -Delastic.apm.service_name=$ELASTIC_APM_SERVICE_NAME \
  -Delastic.apm.server_urls=$ELASTIC_APM_SERVER_URL \
  -Delastic.apm.secret_token=$ELASTIC_APM_SECRET_TOKEN \
  -Delastic.apm.environment=$ELASTIC_APM_ENVIRONMENT \
  -Delastic.apm.application_packages=$ELASTIC_APM_APPLICATION_PACKAGES \
  -Dspring.profiles.active=$SPRING_PROFILE \
  -Djdk.tls.client.protocols=TLSv1.2 \
  -jar wms.war