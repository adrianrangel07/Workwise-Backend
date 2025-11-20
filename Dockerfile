# ======================
# 1) Build stage (Maven + JDK 21)
# ======================
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

# Copia el pom y descarga dependencias (cache build)
COPY pom.xml .
RUN mvn -q -e -B dependency:go-offline

# Copia el código
COPY src ./src

# Compila
RUN mvn -q -e -B clean package -DskipTests


# ======================
# 2) Runtime stage (JRE 21)
# ======================
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/app.jar"]
