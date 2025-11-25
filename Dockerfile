########## RUNTIME STAGE ##########
FROM amazoncorretto:21-alpine

WORKDIR /app

# Install curl to download aspectjweaver
RUN apk add --no-cache curl

# Download AspectJ Weaver
RUN curl -L -o /app/aspectjweaver.jar \
    https://repo1.maven.org/maven2/org/aspectj/aspectjweaver/1.9.22/aspectjweaver-1.9.22.jar

# Copy pre-built JAR
COPY target/price-workflow-1.0.0.jar /app/app.jar

EXPOSE 8087

ENTRYPOINT ["java", "-javaagent:/app/aspectjweaver.jar", "-jar", "/app/app.jar"]
