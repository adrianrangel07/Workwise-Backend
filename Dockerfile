# ---- Etapa 1: build con Maven ----
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app

# Copiar configuración de Maven
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Descargar dependencias (mejora caching)
RUN mvn -B dependency:go-offline

# Copiar el código fuente y construir
COPY src ./src
RUN mvn -B package -DskipTests

# ---- Etapa 2: runtime ligero ----
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copiar el JAR generado en la etapa anterior
COPY --from=build /app/target/*.jar ./app.jar

# Exponer el puerto (cámbialo si tu app usa otro)
EXPOSE 8080

# Ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
