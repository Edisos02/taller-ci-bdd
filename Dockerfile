# ---------- Etapa 1: Build ----------
# Compila el proyecto usando Maven + JDK 17, sin necesitar Maven instalado en la imagen final
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -B clean compile -DskipTests

# ---------- Etapa 2: Runtime ----------
# Imagen final, liviana, solo con el JRE necesario para ejecutar (no todo el JDK+Maven)
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/classes ./classes

EXPOSE 8080

CMD ["java", "-cp", "classes", "cl.empresa.demo.LoginHttpServer"]