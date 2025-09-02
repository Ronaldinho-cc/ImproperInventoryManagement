#!/bin/bash

# 🔧 Script para ejecutar demo en modo DESARROLLO
# Todas las funciones habilitadas para mostrar el inventario completo

echo "🚀 Iniciando Demo API9:2023 - Modo DESARROLLO"
echo "================================================"
echo ""
echo "🔧 Configuración:"
echo "   - Ambiente: DEVELOPMENT"
echo "   - Swagger: HABILITADO"
echo "   - Inventario: HABILITADO"
echo "   - Puerto: 8087"
echo ""
echo "📋 Endpoints disponibles:"
echo "   - Documentación: http://localhost:8087/swagger-ui.html"
echo "   - Inventario: http://localhost:8087/api/inventory"
echo "   - Salud: http://localhost:8087/api/v2/health"
echo ""

# Configurar variables de ambiente
export APP_ENVIRONMENT=development
export SWAGGER_ENABLED=true
export API_INVENTORY_ENABLED=true
export LOG_LEVEL=INFO

# Ejecutar aplicación
echo "🔄 Iniciando aplicación..."
mvn spring-boot:run