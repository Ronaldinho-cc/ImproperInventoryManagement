@echo off
echo ========================================
echo Demo API9:2023 - Improper Inventory Management
echo ========================================
echo.

echo Iniciando aplicacion...
echo Ejecuta en otra terminal: mvn spring-boot:run
echo Espera a que la aplicacion inicie completamente
echo.
pause

echo ========================================
echo PROBANDO VULNERABILIDADES (V1)
echo ========================================
echo.

echo 1. Endpoint vulnerable que expone passwords:
curl -X GET http://localhost:8080/api/users
echo.
echo.

echo 2. Endpoint legacy sin documentar:
curl -X GET http://localhost:8080/legacy/old-endpoint
echo.
echo.

echo 3. Endpoint interno expuesto:
curl -X GET http://localhost:8080/legacy/internal/config
echo.
echo.

pause

echo ========================================
echo PROBANDO SOLUCION SEGURA (V2)
echo ========================================
echo.

echo 1. Endpoint seguro (sin passwords):
curl -X GET http://localhost:8080/api/v2/users
echo.
echo.

echo 2. Inventario de APIs:
curl -X GET http://localhost:8080/api/v2/inventory
echo.
echo.

echo 3. Lista de endpoints deprecados:
curl -X GET http://localhost:8080/api/v2/inventory/deprecated
echo.
echo.

echo 4. Crear usuario (version segura):
curl -X POST http://localhost:8080/api/v2/users ^
  -H "Content-Type: application/json" ^
  -d "{\"username\":\"testuser\",\"email\":\"test@example.com\",\"password\":\"secret123\"}"
echo.
echo.

echo ========================================
echo DOCUMENTACION DISPONIBLE EN:
echo http://localhost:8080/swagger-ui.html
echo ========================================
echo.

pause