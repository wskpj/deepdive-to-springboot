FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

EXPOSE 8080

COPY app/build/extracted/dependencies/ ./
COPY app/build/extracted/spring-boot-loader/ ./
COPY app/build/extracted/snapshot-dependencies/ ./
COPY app/build/extracted/application/ ./

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]