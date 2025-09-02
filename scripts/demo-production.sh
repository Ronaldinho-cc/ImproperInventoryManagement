#!/bin/bash

# 🔒 Script para ejecutar demo en modo PRODUCCIÓN
# Endpoints sensibles bloqueados para mostrar seguridad

echo "🚀 Iniciando Demo API9:2023 - Modo PRODUCCIÓN"
echo "================================================"
echo ""
echo "🔒 Configuración SEGURA:"
echo "   - Ambiente: PRODUCTION"
echo "   - Swagger: DESHABILITADO"
echo "   - Inventario: BLOQUEADO"
echo "   - Puerto: 8087"
echo ""
echo "❌ Endpoints BLOQUEADOS:"
echo "   - http://localhost:8087/swagger-ui.html (404)"
echo "   - http://localhost:8087/api/inventory (404)"
echo ""
echo "✅ Endpoints DISPONIBLES:"
echo "   - http://localhost:8087/api/v2/health"
echo "   - http://localhost:8087/actuator/health"
echo ""

# Configurar variables de ambiente para producción
export APP_ENVIRONMENT=production
export SWAGGER_ENABLED=false
export API_INVENTORY_ENABLED=false
export LOG_LEVEL=WARN
export SECURITY_LOG_LEVEL=INFO

# Ejecutar aplicación
echo "🔄 Iniciando aplicación en modo SEGURO..."
mvn spring-boot:run