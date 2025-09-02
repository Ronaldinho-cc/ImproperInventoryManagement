# 🚨 API9:2023 Demo - Improper Inventory Management

## 📋 Descripción del Proyecto

Este proyecto es una **demostración completa** de cómo implementar protecciones contra la vulnerabilidad **API9:2023 - Improper Inventory Management** del OWASP API Security Top 10.

Basado en un microservicio real de gestión de calidad de agua, muestra las mejores prácticas para:
- ✅ Mantener un inventario completo de APIs
- ✅ Controlar acceso por ambiente (desarrollo vs producción)
- ✅ Documentar todos los endpoints
- ✅ Monitorear APIs deprecadas y de alto riesgo

## 🚨 ¿Qué es API9:2023 Improper Inventory Management?

Esta vulnerabilidad ocurre cuando:
- **No tienes inventario completo** de tus APIs expuestas
- **Documentación accesible** en producción (Swagger, OpenAPI)
- **APIs obsoletas** siguen activas sin control
- **Endpoints de debug/test** expuestos públicamente
- **Falta de versionado** claro y consistente

### 🎯 Riesgos de Seguridad:
- Atacantes pueden mapear tu infraestructura completa
- Información sensible expuesta en documentación
- APIs olvidadas sin protección
- Superficie de ataque ampliada

## 🛡️ Solución Implementada

### 1. **Sistema de Inventario Automático**
```java
@Service
public class ApiInventoryService {
    // Descubre automáticamente todos los endpoints
    // Genera metadatos completos
    // Identifica endpoints de alto riesgo
}
```

### 2. **Control por Ambiente**
```yaml
# Desarrollo: Swagger y inventario habilitados
APP_ENVIRONMENT=development
SWAGGER_ENABLED=true

# Producción: Endpoints sensibles bloqueados
APP_ENVIRONMENT=production  
SWAGGER_ENABLED=false
```

### 3. **Documentación Completa**
- Cada endpoint documentado con `@Operation`
- Metadatos de seguridad incluidos
- Información de versiones y deprecación

## 🚀 Cómo Ejecutar la Demo

### Prerrequisitos
- Java 17+
- Maven 3.6+
- MongoDB (opcional, usa datos simulados)

### 1. Clonar el Repositorio
```bash
git clone https://github.com/Ronaldinho-cc/ImproperInventoryManagement.git
cd ImproperInventoryManagement
```

### 2. Ejecutar en Modo Desarrollo
```bash
# Todas las funciones habilitadas
mvn spring-boot:run
```

**Endpoints disponibles en desarrollo:**
- 📖 Documentación: http://localhost:8087/swagger-ui.html
- 📋 Inventario: http://localhost:8087/api/inventory
- 🏥 Salud: http://localhost:8087/api/v2/health

### 3. Ejecutar en Modo Producción
```bash
# Endpoints sensibles bloqueados
APP_ENVIRONMENT=production SWAGGER_ENABLED=false mvn spring-boot:run
```

**Verificar seguridad:**
```bash
# Estos deben retornar 404 en producción
curl http://localhost:8087/swagger-ui.html
curl http://localhost:8087/api/inventory

# Este debe funcionar
curl http://localhost:8087/api/v2/health
```

## 📊 Endpoints de la Demo

### 🔍 Inventario de APIs (Solo Desarrollo)
| Endpoint | Descripción |
|----------|-------------|
| `GET /api/inventory` | Inventario completo de endpoints |
| `GET /api/inventory/active` | Solo endpoints activos |
| `GET /api/inventory/deprecated` | Endpoints deprecados |
| `GET /api/inventory/high-risk` | Endpoints de alto riesgo |
| `GET /api/inventory/health` | Estado del inventario |

### 🏥 Salud de la API
| Endpoint | Descripción |
|----------|-------------|
| `GET /api/v2/health` | Estado general (público) |
| `GET /api/v2/health/version` | Información de versión |
| `GET /api/v2/health/security` | Estado de seguridad (solo dev) |

### 👥 APIs de Negocio
| Endpoint | Descripción | Auth |
|----------|-------------|------|
| `GET /api/v2/users` | Listar usuarios | ✅ |
| `POST /api/v2/users` | Crear usuario | ✅ |
| `GET /api/v2/qualitytests` | Listar pruebas | ✅ |
| `POST /api/v2/qualitytests` | Crear prueba | ✅ |

## 🧪 Casos de Prueba para la Demo

### ✅ Caso 1: Desarrollo Seguro
```bash
# 1. Ejecutar en desarrollo
mvn spring-boot:run

# 2. Verificar inventario disponible
curl http://localhost:8087/api/inventory
# ✅ Debe retornar JSON con lista de endpoints

# 3. Verificar documentación disponible
curl http://localhost:8087/swagger-ui.html
# ✅ Debe mostrar interfaz Swagger
```

### 🔒 Caso 2: Producción Segura
```bash
# 1. Ejecutar en producción
APP_ENVIRONMENT=production SWAGGER_ENABLED=false mvn spring-boot:run

# 2. Verificar inventario bloqueado
curl http://localhost:8087/api/inventory
# ❌ Debe retornar 404 Not Found

# 3. Verificar documentación bloqueada
curl http://localhost:8087/swagger-ui.html
# ❌ Debe retornar 404 Not Found

# 4. Verificar salud disponible
curl http://localhost:8087/api/v2/health
# ✅ Debe retornar estado de la API
```

### 📊 Caso 3: Análisis de Inventario
```bash
# Obtener reporte completo de salud
curl http://localhost:8087/api/inventory/health

# Respuesta esperada:
{
  "success": true,
  "data": {
    "status": "HEALTHY",
    "totalEndpoints": 15,
    "activeEndpoints": 13,
    "deprecatedEndpoints": 0,
    "undocumentedEndpoints": 0,
    "highRiskEndpoints": 0,
    "complianceScore": 95.5
  }
}
```

## 📋 Cumplimiento OWASP API9:2023

| Requisito | ✅ Implementado | Descripción |
|-----------|----------------|-------------|
| **Inventario Completo** | ✅ | `ApiInventoryService` descubre automáticamente todos los endpoints |
| **Documentación Actualizada** | ✅ | OpenAPI con `@Operation` en cada endpoint |
| **Control por Ambiente** | ✅ | Swagger y inventario deshabilitados en producción |
| **APIs Obsoletas Controladas** | ✅ | Sistema de estados (ACTIVE, DEPRECATED, etc.) |
| **Monitoreo de Cambios** | ✅ | Endpoint de salud con métricas de inventario |
| **Versionado Consistente** | ✅ | Versión v2 en todos los endpoints de negocio |

**Puntuación de Cumplimiento: 100% ✅**

## 🎓 Para la Presentación

### Puntos Clave a Demostrar:

1. **Problema Identificado:**
   - Mostrar Swagger expuesto en "producción"
   - Explicar riesgos de seguridad

2. **Solución Implementada:**
   - Demostrar inventario automático
   - Mostrar control por ambiente
   - Explicar documentación completa

3. **Beneficios Obtenidos:**
   - Inventario siempre actualizado
   - Configuración segura por ambiente
   - Cumplimiento 100% OWASP

### Script de Demo (5 minutos):
```bash
# 1. Mostrar problema (desarrollo con todo expuesto)
mvn spring-boot:run
curl http://localhost:8087/api/inventory

# 2. Mostrar solución (producción segura)
APP_ENVIRONMENT=production mvn spring-boot:run
curl http://localhost:8087/api/inventory  # 404

# 3. Mostrar beneficios
curl http://localhost:8087/api/v2/health  # Métricas completas
```

## 🔧 Configuración Avanzada

### Variables de Ambiente
```bash
# Configuración básica
APP_ENVIRONMENT=development|production
SWAGGER_ENABLED=true|false
API_INVENTORY_ENABLED=true|false

# Base de datos (opcional)
MONGO_USERNAME=tu_usuario
MONGO_PASSWORD=tu_password
MONGO_DATABASE=tu_base_datos

# Logging
LOG_LEVEL=INFO|DEBUG|WARN
SECURITY_LOG_LEVEL=WARN|INFO
```

### Perfiles de Spring
```bash
# Desarrollo completo
mvn spring-boot:run -Dspring.profiles.active=dev

# Producción segura
mvn spring-boot:run -Dspring.profiles.active=prod
```

## 📚 Recursos Adicionales

- [OWASP API Security Top 10](https://owasp.org/www-project-api-security/)
- [API9:2023 - Improper Inventory Management](https://owasp.org/API-Security/editions/2023/en/0xa9-improper-inventory-management/)
- [Spring Boot Security Best Practices](https://spring.io/guides/topicals/spring-security-architecture/)

## 👨‍💻 Autor

**Demo desarrollado para fines educativos**
- Curso: Seguridad en APIs
- Tema: OWASP API Security Top 10
- Vulnerabilidad: API9:2023 Improper Inventory Management

---

🚨 **Nota Importante:** Este es un proyecto de demostración. En un entorno real, implementar autenticación OAuth2 completa y configurar variables de ambiente apropiadas.