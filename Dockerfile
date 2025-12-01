########## RUNTIME STAGE ##########
FROM amazoncorretto:21-alpine

WORKDIR /app

# Install curl to download aspectjweaver
RUN apk add --no-cache curl

# Copy pre-built JAR
COPY target/price-workflow-1.0.0.jar /app/app.jar

EXPOSE 8087

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
