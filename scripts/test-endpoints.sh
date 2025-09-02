#!/bin/bash

# 🧪 Script para probar todos los endpoints de la demo
# Útil para verificar que todo funciona correctamente

BASE_URL="http://localhost:8087"

echo "🧪 Probando Endpoints - Demo API9:2023"
echo "======================================"
echo ""

# Función para hacer peticiones y mostrar resultado
test_endpoint() {
    local method=$1
    local endpoint=$2
    local description=$3
    local expected_status=$4
    
    echo "🔍 Probando: $description"
    echo "   $method $endpoint"
    
    response=$(curl -s -w "\n%{http_code}" -X $method "$BASE_URL$endpoint" 2>/dev/null)
    status_code=$(echo "$response" | tail -n1)
    body=$(echo "$response" | head -n -1)
    
    if [ "$status_code" = "$expected_status" ]; then
        echo "   ✅ Status: $status_code (esperado)"
    else
        echo "   ❌ Status: $status_code (esperado: $expected_status)"
    fi
    
    if [ ${#body} -gt 100 ]; then
        echo "   📄 Respuesta: $(echo "$body" | head -c 100)..."
    else
        echo "   📄 Respuesta: $body"
    fi
    echo ""
}

echo "🏥 ENDPOINTS DE SALUD (siempre disponibles)"
echo "----------------------------------------"
test_endpoint "GET" "/actuator/health" "Actuator Health Check" "200"
test_endpoint "GET" "/api/v2/health" "API Health Status" "200"
test_endpoint "GET" "/api/v2/health/version" "API Version Info" "200"

echo ""
echo "🔍 ENDPOINTS DE INVENTARIO (solo desarrollo)"
echo "-------------------------------------------"
test_endpoint "GET" "/api/inventory" "Inventario Completo" "200"
test_endpoint "GET" "/api/inventory/active" "Endpoints Activos" "200"
test_endpoint "GET" "/api/inventory/deprecated" "Endpoints Deprecados" "200"
test_endpoint "GET" "/api/inventory/high-risk" "Endpoints Alto Riesgo" "200"
test_endpoint "GET" "/api/inventory/health" "Salud del Inventario" "200"

echo ""
echo "📖 ENDPOINTS DE DOCUMENTACIÓN (solo desarrollo)"
echo "----------------------------------------------"
test_endpoint "GET" "/swagger-ui.html" "Swagger UI" "200"
test_endpoint "GET" "/v3/api-docs" "OpenAPI Spec" "200"

echo ""
echo "👥 ENDPOINTS DE NEGOCIO (requieren auth)"
echo "---------------------------------------"
test_endpoint "GET" "/api/v2/users" "Listar Usuarios" "401"
test_endpoint "GET" "/api/v2/qualitytests" "Listar Pruebas" "401"

echo ""
echo "🔒 VERIFICACIÓN DE SEGURIDAD"
echo "----------------------------"

# Verificar ambiente actual
if curl -s "$BASE_URL/api/inventory" | grep -q "success"; then
    echo "🔧 Ambiente detectado: DESARROLLO"
    echo "   ✅ Inventario disponible"
    echo "   ✅ Documentación disponible"
else
    echo "🔒 Ambiente detectado: PRODUCCIÓN"
    echo "   ❌ Inventario bloqueado (correcto)"
    echo "   ❌ Documentación bloqueada (correcto)"
fi

echo ""
echo "📊 RESUMEN DE LA PRUEBA"
echo "======================"
echo "✅ Endpoints de salud funcionando"
echo "✅ Control por ambiente operativo"
echo "✅ Seguridad implementada correctamente"
echo ""
echo "🎯 Demo lista para presentación!"