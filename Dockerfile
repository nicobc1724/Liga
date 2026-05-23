FROM openjdk:17
COPY "./target/LIGA-COLOMBIANA-1.jar" "app.jar"
EXPOSE 8123
ENTRYPOINT [ "java", "-jar", "app.jar" ]