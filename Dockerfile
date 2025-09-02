# 🚀 Dockerfile para Demo API9:2023
FROM maven:3.9.0-eclipse-temurin-17-alpine AS builder

# 📁 Directorio de trabajo
WORKDIR /app

# 📋 Copiar archivos de configuración
COPY pom.xml .
COPY src ./src

# 🔨 Compilar aplicación
RUN mvn clean package -DskipTests

# 🏃‍♂️ Imagen de ejecución
FROM eclipse-temurin:17-jre-alpine

# 📦 Instalar herramientas útiles para debugging
RUN apk add --no-cache curl jq

# 👤 Crear usuario no-root para seguridad
RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

# 📁 Directorio de trabajo
WORKDIR /app

# 📄 Copiar JAR compilado
COPY --from=builder /app/target/*.jar app.jar

# 📝 Crear directorio para logs
RUN mkdir -p /app/logs && chown -R appuser:appgroup /app

# 👤 Cambiar a usuario no-root
USER appuser

# 🌐 Exponer puerto
EXPOSE 8087

# 🏥 Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
    CMD curl -f http://localhost:8087/actuator/health || exit 1

# 🚀 Comando de inicio
ENTRYPOINT ["java", "-jar", "app.jar"]

# 🏷️ Labels para metadata
LABEL maintainer="API Security Demo <demo@vallegrande.edu.pe>"
LABEL description="Demo API9:2023 - Improper Inventory Management"
LABEL version="1.0.0"
LABEL org.opencontainers.image.source="https://github.com/Ronaldinho-cc/ImproperInventoryManagement"