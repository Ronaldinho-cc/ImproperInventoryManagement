# Demo OWASP API9:2023 - Improper Inventory Management

Esta demo muestra la vulnerabilidad **OWASP API Security Top 10 - API9:2023 Improper Inventory Management** y cómo solucionarla.

## 🎯 Objetivo

Demostrar los riesgos de no tener un inventario adecuado de APIs y cómo implementar una gestión correcta de versiones y documentación.

## 🔴 Vulnerabilidades Demostradas

### 1. **API V1 - Versión Vulnerable** (`/api/users`)
- ❌ **Sin versionado explícito** en la URL
- ❌ **Expone información sensible** (passwords en respuestas)
- ❌ **Sin documentación** OpenAPI/Swagger
- ❌ **Sin advertencias de deprecación**

### 2. **APIs Legacy** (`/legacy/*`)
- ❌ **Endpoints obsoletos** sin documentar
- ❌ **Endpoints de prueba** en producción
- ❌ **Endpoints internos** expuestos públicamente
- ❌ **Sin fechas de sunset** definidas

## 🟢 Soluciones Implementadas

### 1. **API V2 - Versión Segura** (`/api/v2/users`)
- ✅ **Versionado explícito** en URLs
- ✅ **DTOs seguros** que no exponen información sensible
- ✅ **Documentación completa** con OpenAPI/Swagger
- ✅ **Respuestas estructuradas** y consistentes

### 2. **Inventario de APIs** (`/api/v2/inventory`)
- ✅ **Lista de todas las versiones** disponibles
- ✅ **Estado de cada versión** (CURRENT, DEPRECATED)
- ✅ **Fechas de deprecación y sunset**
- ✅ **Guías de migración**

## 🚀 Cómo Ejecutar la Demo

### Prerrequisitos
- Java 17+
- Maven 3.6+

### 1. Compilar y Ejecutar
```bash
mvn clean spring-boot:run
```

### 2. Probar las Vulnerabilidades

#### Endpoint V1 que expone passwords:
```bash
curl http://localhost:8080/api/users
```
**Resultado**: Expone passwords en texto plano ❌

#### Endpoints legacy sin documentar:
```bash
curl http://localhost:8080/legacy/old-endpoint
curl http://localhost:8080/legacy/internal/config
```
**Resultado**: Endpoints obsoletos accesibles ❌

### 3. Probar las Soluciones

#### Endpoint V2 seguro:
```bash
curl http://localhost:8080/api/v2/users
```
**Resultado**: No expone passwords ✅

#### Inventario de APIs:
```bash
curl http://localhost:8080/api/v2/inventory
```
**Resultado**: Información completa de versiones ✅

#### Lista de endpoints deprecados:
```bash
curl http://localhost:8080/api/v2/inventory/deprecated
```
**Resultado**: Lista clara de APIs obsoletas ✅

### 4. Ver Documentación Completa
Abrir en el navegador:
```
http://localhost:8080/swagger-ui.html
```

## 🧪 Ejecutar Tests

Los tests demuestran automáticamente las diferencias entre versiones:

```bash
# Ejecutar todos los tests
mvn test

# Ejecutar solo los tests de la vulnerabilidad
mvn test -Dtest=Api9VulnerabilityTest
```

### Tests Incluidos:
- ✅ **testVulnerableV1ExposesPasswords**: Demuestra que V1 expone passwords
- ✅ **testSecureV2DoesNotExposePasswords**: Demuestra que V2 es seguro
- ✅ **testLegacyEndpointsAreStillAccessible**: Muestra endpoints legacy accesibles
- ✅ **testApiInventoryProvidesClearInformation**: Verifica inventario completo
- ✅ **testDeprecatedEndpointsAreListed**: Verifica lista de deprecados
- ✅ **testSwaggerDocumentationIsAvailable**: Verifica documentación disponible

## 📋 Script de Pruebas Automatizado

Para Windows, ejecutar:
```bash
test-api9-demo.bat
```

Este script ejecuta automáticamente todas las pruebas y muestra las diferencias.

## 🔍 Endpoints Disponibles

### Vulnerables (V1)
- `GET /api/users` - Lista usuarios (expone passwords)
- `GET /api/users/{id}` - Usuario por ID (expone password)
- `POST /api/users` - Crear usuario (retorna password)

### Legacy (Obsoletos)
- `GET /legacy/old-endpoint` - Endpoint obsoleto
- `GET /legacy/test` - Endpoint de prueba
- `GET /legacy/internal/config` - Configuración interna

### Seguros (V2)
- `GET /api/v2/users` - Lista usuarios (seguro)
- `GET /api/v2/users/{id}` - Usuario por ID (seguro)
- `POST /api/v2/users` - Crear usuario (seguro)

### Inventario
- `GET /api/v2/inventory` - Inventario completo de APIs
- `GET /api/v2/inventory/deprecated` - Lista de endpoints deprecados

### Documentación
- `GET /swagger-ui.html` - Interfaz Swagger UI
- `GET /api-docs` - Especificación OpenAPI JSON

## 📚 Mejores Prácticas Implementadas

### 1. **Versionado de APIs**
- URLs con versión explícita (`/api/v2/`)
- Información de compatibilidad
- Fechas de deprecación y sunset

### 2. **Seguridad de Datos**
- DTOs separados para entrada y salida
- No exposición de información sensible
- Validación de datos de entrada

### 3. **Documentación**
- OpenAPI 3.0 completo
- Ejemplos en cada endpoint
- Descripciones detalladas

### 4. **Monitoreo**
- Spring Boot Actuator
- Métricas de endpoints
- Health checks

## 🎓 Lecciones Aprendidas

### Problemas de Improper Inventory Management:
1. **Exposición de datos sensibles** por falta de DTOs adecuados
2. **APIs obsoletas activas** sin advertencias
3. **Falta de documentación** que confunde a desarrolladores
4. **Endpoints internos expuestos** por error
5. **Sin gestión del ciclo de vida** de las APIs

### Soluciones Implementadas:
1. **Inventario completo** con estado de cada versión
2. **Versionado explícito** en URLs
3. **Documentación automática** con OpenAPI
4. **DTOs seguros** que protegen información sensible
5. **Gestión del ciclo de vida** con fechas claras

## 📖 Documentación Adicional

- [API9-VULNERABILITY-DEMO.md](API9-VULNERABILITY-DEMO.md) - Explicación detallada
- [OWASP API Security Top 10](https://owasp.org/www-project-api-security/)
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [OpenAPI Specification](https://swagger.io/specification/)

---

**Nota**: Esta demo es solo para fines educativos. No usar en producción sin las medidas de seguridad adecuadas.