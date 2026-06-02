# syntax=docker/dockerfile:1

FROM node:22-alpine AS frontend-build
WORKDIR /workspace

COPY frontend/package*.json ./frontend/
WORKDIR /workspace/frontend
RUN npm ci

COPY frontend/ ./
RUN npm run build

FROM maven:3.9-eclipse-temurin-21 AS backend-build
WORKDIR /workspace/backend

COPY backend/pom.xml ./
RUN mvn -B -DskipTests dependency:go-offline

COPY backend/src ./src
COPY --from=frontend-build /workspace/backend/src/main/resources/static ./src/main/resources/static
RUN mvn -B -DskipTests package

FROM eclipse-temurin:21-jre
WORKDIR /app

ENV PORT=8080
EXPOSE 8080

COPY --from=backend-build /workspace/backend/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]