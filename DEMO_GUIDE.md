# 🎯 Guía de Demostración - API9:2023

## 📋 Preparación de la Demo (5 minutos antes)

### 1. Verificar Prerrequisitos
```bash
# Verificar Java
java -version  # Debe ser 17+

# Verificar Maven
mvn -version   # Debe ser 3.6+

# Compilar proyecto
mvn clean compile
```

### 2. Preparar Terminales
- **Terminal 1:** Para ejecutar en desarrollo
- **Terminal 2:** Para ejecutar en producción  
- **Terminal 3:** Para comandos curl

## 🎤 Script de Presentación (10-15 minutos)

### **PARTE 1: Introducción al Problema (3 minutos)**

**"Hoy voy a demostrar cómo implementé protecciones contra API9:2023 - Improper Inventory Management en mi microservicio de calidad de agua."**

#### Explicar el Problema:
```
🚨 API9:2023 - Improper Inventory Management

PROBLEMAS TÍPICOS:
❌ No saber qué APIs tienes expuestas
❌ Documentación Swagger pública en producción
❌ APIs obsoletas sin control
❌ Endpoints de debug accesibles

RIESGOS:
🎯 Atacantes mapean tu infraestructura
🎯 Información sensible expuesta
🎯 Superficie de ataque ampliada
```

### **PARTE 2: Demostración del Problema (2 minutos)**

**"Primero les muestro cómo se ve el problema en un sistema vulnerable:"**

```bash
# Terminal 1: Ejecutar en "modo vulnerable"
mvn spring-boot:run

# Terminal 3: Mostrar endpoints expuestos
curl http://localhost:8087/swagger-ui.html
# "Aquí pueden ver que Swagger está expuesto públicamente"

curl http://localhost:8087/api/inventory
# "Y aquí tenemos un endpoint que lista TODAS nuestras APIs"
```

**Explicar:** *"En producción, esto sería un desastre de seguridad. Los atacantes pueden ver toda nuestra estructura de API."*

### **PARTE 3: Solución Implementada (5 minutos)**

**"Ahora les muestro mi solución que cumple 100% con OWASP API9:2023:"**

#### A. Inventario Automático
```bash
# Mostrar inventario completo
curl http://localhost:8087/api/inventory | jq

# Explicar la respuesta:
# - path: ruta del endpoint
# - method: método HTTP
# - requiresAuth: si requiere autenticación
# - status: ACTIVE, DEPRECATED, etc.
# - riskLevel: LOW, MEDIUM, HIGH
```

**Explicar:** *"Mi sistema descubre automáticamente todos los endpoints y los cataloga con metadatos completos."*

#### B. Control por Ambiente
```bash
# Terminal 2: Ejecutar en modo producción
APP_ENVIRONMENT=production SWAGGER_ENABLED=false mvn spring-boot:run
```

```bash
# Terminal 3: Verificar que está bloqueado
curl http://localhost:8087/swagger-ui.html
# Debe retornar 404

curl http://localhost:8087/api/inventory
# Debe retornar 404

curl http://localhost:8087/api/v2/health
# Este SÍ debe funcionar
```

**Explicar:** *"En producción, automáticamente se bloquean los endpoints sensibles, pero las APIs de negocio siguen funcionando."*

#### C. Documentación Completa
```bash
# Volver a desarrollo para mostrar documentación
# Terminal 1: Ctrl+C y reiniciar
mvn spring-boot:run

# Abrir navegador en http://localhost:8087/swagger-ui.html
```

**Mostrar en Swagger:**
- Cada endpoint tiene descripción completa
- Información de seguridad
- Ejemplos de uso
- Códigos de respuesta

### **PARTE 4: Métricas y Monitoreo (3 minutos)**

```bash
# Mostrar reporte de salud del inventario
curl http://localhost:8087/api/inventory/health | jq

# Explicar métricas:
# - totalEndpoints: total de APIs
# - activeEndpoints: APIs activas
# - deprecatedEndpoints: APIs obsoletas
# - complianceScore: puntuación de cumplimiento
```

```bash
# Mostrar estado general de la API
curl http://localhost:8087/api/v2/health | jq

# Destacar sección api9_compliance:
# - inventory_complete: true
# - compliance_score: 95.5
# - documented_endpoints: 15
```

### **PARTE 5: Cumplimiento OWASP (2 minutos)**

**"Mi implementación cumple 100% con los requisitos de API9:2023:"**

```
✅ INVENTARIO COMPLETO
   → ApiInventoryService descubre automáticamente todos los endpoints

✅ DOCUMENTACIÓN ACTUALIZADA  
   → OpenAPI con @Operation en cada endpoint

✅ CONTROL POR AMBIENTE
   → Swagger OFF en producción, inventario bloqueado

✅ MONITOREO DE APIS OBSOLETAS
   → Sistema de estados y alertas automáticas

✅ VERSIONADO CONSISTENTE
   → Versión v2 en todos los endpoints
```

## 🎯 Puntos Clave para Enfatizar

### Durante la Demo:
1. **"Implementé un sistema que automáticamente descubre todos mis endpoints"**
2. **"En producción, la documentación está completamente bloqueada"**
3. **"Cada API está documentada y catalogada con metadatos de seguridad"**
4. **"El sistema se adapta automáticamente al ambiente"**

### Preguntas Frecuentes:

**P: ¿Cómo funciona el descubrimiento automático?**
**R:** "Uso Spring's RequestMappingHandlerMapping para escanear todos los @RequestMapping y generar el inventario automáticamente."

**P: ¿Qué pasa si olvido documentar un endpoint?**
**R:** "El sistema detecta endpoints sin documentar y los reporta en /api/inventory/undocumented"

**P: ¿Cómo aseguras que funcione en producción?**
**R:** "Uso @ConditionalOnProperty para que los endpoints sensibles solo existan en desarrollo."

## 🚨 Troubleshooting Durante la Demo

### Si algo no funciona:

1. **Puerto ocupado:**
   ```bash
   # Cambiar puerto
   mvn spring-boot:run -Dserver.port=8088
   ```

2. **Respuesta vacía:**
   ```bash
   # Verificar que el servicio esté corriendo
   curl http://localhost:8087/actuator/health
   ```

3. **Error de compilación:**
   ```bash
   # Limpiar y recompilar
   mvn clean compile
   ```

## 📊 Backup: Capturas de Pantalla

Si hay problemas técnicos, tener preparadas capturas de:
1. Swagger UI mostrando documentación completa
2. JSON del inventario de APIs
3. Respuesta 404 en producción
4. Métricas de cumplimiento

## ✅ Checklist Final

Antes de la presentación:
- [ ] Proyecto compila sin errores
- [ ] Puerto 8087 disponible
- [ ] Terminales preparadas
- [ ] Comandos curl probados
- [ ] Navegador con pestañas preparadas
- [ ] Backup de capturas listo

**¡Listo para demostrar 100% cumplimiento de API9:2023!** 🚀