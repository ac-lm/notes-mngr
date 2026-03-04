FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Copia archivos de configuración de Maven
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Copia el código fuente
COPY src ./src

# Da permisos de ejecución al Maven Wrapper y compila
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# Imagen final más liviana
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]