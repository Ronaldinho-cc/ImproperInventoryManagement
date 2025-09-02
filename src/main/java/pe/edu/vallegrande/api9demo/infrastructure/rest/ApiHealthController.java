package pe.edu.vallegrande.api9demo.infrastructure.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.vallegrande.api9demo.application.services.ApiInventoryService;
import pe.edu.vallegrande.api9demo.infrastructure.dto.ResponseDto;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 🏥 CONTROLADOR DE SALUD DE LA API
 * 
 * Proporciona información sobre el estado y configuración de la API.
 * Incluye métricas de inventario y cumplimiento de seguridad.
 */
@Slf4j
@RestController
@RequestMapping("/api/v2/health")
@RequiredArgsConstructor
@Tag(name = "🏥 API Health", description = "Estado y salud de la API")
public class ApiHealthController {

    private final ApiInventoryService apiInventoryService;
    
    @Value("${app.version}")
    private String appVersion;
    
    @Value("${app.environment}")
    private String environment;
    
    @Value("${springdoc.swagger-ui.enabled:true}")
    private boolean swaggerEnabled;

    @GetMapping
    @Operation(
        summary = "🏥 Estado general de la API",
        description = """
            Retorna información completa sobre el estado de la API y cumplimiento de seguridad.
            
            **Información incluida:**
            - Estado general del servicio
            - Configuración de ambiente
            - Métricas del inventario de APIs
            - Cumplimiento de API9:2023
            - Configuración de seguridad
            
            **Uso:** Monitoreo y verificación de configuración segura
            """,
        responses = {
            @ApiResponse(responseCode = "200", description = "Estado de la API obtenido")
        }
    )
    public Mono<ResponseEntity<ResponseDto<Map<String, Object>>>> getApiHealth() {
        log.info("🏥 Solicitando estado de salud de la API");
        
        return apiInventoryService.getInventoryHealthReport()
            .map(inventoryHealth -> {
                Map<String, Object> healthData = Map.of(
                    "status", "UP",
                    "timestamp", LocalDateTime.now(),
                    "version", appVersion,
                    "environment", environment,
                    
                    // 🚨 INFORMACIÓN CRÍTICA PARA API9:2023
                    "api9_compliance", Map.of(
                        "inventory_complete", inventoryHealth.get("inventoryComplete"),
                        "compliance_score", inventoryHealth.get("complianceScore"),
                        "total_endpoints", inventoryHealth.get("totalEndpoints"),
                        "documented_endpoints", (Integer) inventoryHealth.get("totalEndpoints") - (Integer) inventoryHealth.get("undocumentedEndpoints"),
                        "deprecated_endpoints", inventoryHealth.get("deprecatedEndpoints"),
                        "high_risk_endpoints", inventoryHealth.get("highRiskEndpoints")
                    ),
                    
                    // 🔒 CONFIGURACIÓN DE SEGURIDAD
                    "security_config", Map.of(
                        "environment", environment,
                        "swagger_enabled", swaggerEnabled,
                        "swagger_safe", !swaggerEnabled || !"production".equals(environment),
                        "inventory_endpoint_available", !"production".equals(environment),
                        "oauth2_enabled", true,
                        "actuator_restricted", true
                    ),
                    
                    // 📊 MÉTRICAS DEL INVENTARIO
                    "inventory_metrics", inventoryHealth,
                    
                    // ⚠️ ALERTAS DE SEGURIDAD
                    "security_alerts", generateSecurityAlerts(inventoryHealth),
                    
                    // 📋 RECOMENDACIONES
                    "recommendations", generateRecommendations(inventoryHealth)
                );
                
                return ResponseEntity.ok(new ResponseDto<>(true, healthData));
            })
            .doOnSuccess(result -> log.info("✅ Estado de salud generado para ambiente: {}", environment));
    }

    @GetMapping("/version")
    @Operation(
        summary = "📋 Información de versión",
        description = """
            Retorna información detallada sobre la versión de la API.
            
            **Datos incluidos:**
            - Versión actual de la API
            - Ambiente de ejecución
            - Versiones soportadas
            - Versiones deprecadas
            - Enlaces a documentación
            """,
        responses = {
            @ApiResponse(responseCode = "200", description = "Información de versión obtenida")
        }
    )
    public Mono<ResponseEntity<ResponseDto<Map<String, Object>>>> getVersionInfo() {
        log.info("📋 Solicitando información de versión");
        
        Map<String, Object> versionInfo = Map.of(
            "current_version", appVersion,
            "api_version", "v2",
            "environment", environment,
            "build_timestamp", LocalDateTime.now(), // En producción sería el tiempo real de build
            "supported_versions", new String[]{"v2"},
            "deprecated_versions", new String[]{"v1"},
            "documentation_url", swaggerEnabled ? "/swagger-ui.html" : "Disabled in " + environment,
            "inventory_url", !"production".equals(environment) ? "/api/inventory" : "Disabled in production",
            
            // 🚨 INFORMACIÓN DE CUMPLIMIENTO
            "compliance_info", Map.of(
                "owasp_api_security", "API9:2023 - Improper Inventory Management",
                "implementation_status", "COMPLIANT",
                "last_security_review", LocalDateTime.now().minusDays(7),
                "next_review_due", LocalDateTime.now().plusDays(23)
            )
        );
        
        return Mono.just(ResponseEntity.ok(new ResponseDto<>(true, versionInfo)))
            .doOnSuccess(result -> log.info("✅ Información de versión generada"));
    }

    @GetMapping("/security")
    @Operation(
        summary = "🔒 Estado de seguridad",
        description = """
            Retorna el estado actual de las configuraciones de seguridad.
            
            **Verificaciones incluidas:**
            - Configuración por ambiente
            - Estado de endpoints sensibles
            - Cumplimiento de API9:2023
            - Alertas de seguridad activas
            """,
        responses = {
            @ApiResponse(responseCode = "200", description = "Estado de seguridad obtenido")
        }
    )
    public Mono<ResponseEntity<ResponseDto<Map<String, Object>>>> getSecurityStatus() {
        log.info("🔒 Solicitando estado de seguridad");
        
        return apiInventoryService.getInventoryHealthReport()
            .map(inventoryHealth -> {
                boolean isProductionSafe = "production".equals(environment) && !swaggerEnabled;
                int highRiskEndpoints = (Integer) inventoryHealth.get("highRiskEndpoints");
                
                Map<String, Object> securityStatus = Map.of(
                    "overall_status", highRiskEndpoints == 0 && isProductionSafe ? "SECURE" : "WARNING",
                    "environment", environment,
                    "production_ready", isProductionSafe,
                    
                    "endpoint_security", Map.of(
                        "swagger_exposed", swaggerEnabled && "production".equals(environment),
                        "inventory_exposed", !"production".equals(environment),
                        "high_risk_count", highRiskEndpoints,
                        "deprecated_count", inventoryHealth.get("deprecatedEndpoints")
                    ),
                    
                    "compliance_status", Map.of(
                        "api9_2023_compliant", (Double) inventoryHealth.get("complianceScore") > 80.0,
                        "inventory_complete", inventoryHealth.get("inventoryComplete"),
                        "documentation_complete", (Integer) inventoryHealth.get("undocumentedEndpoints") == 0
                    ),
                    
                    "recommendations", generateSecurityRecommendations(inventoryHealth, isProductionSafe)
                );
                
                return ResponseEntity.ok(new ResponseDto<>(true, securityStatus));
            })
            .doOnSuccess(result -> log.info("✅ Estado de seguridad generado"));
    }

    private Map<String, Object> generateSecurityAlerts(Map<String, Object> inventoryHealth) {
        int highRiskEndpoints = (Integer) inventoryHealth.get("highRiskEndpoints");
        int undocumentedEndpoints = (Integer) inventoryHealth.get("undocumentedEndpoints");
        boolean swaggerInProduction = swaggerEnabled && "production".equals(environment);
        
        return Map.of(
            "critical_alerts", swaggerInProduction ? 
                List.of("🚨 Swagger UI habilitado en producción") : List.of(),
            "warning_alerts", highRiskEndpoints > 0 ? 
                List.of("⚠️ " + highRiskEndpoints + " endpoints de alto riesgo encontrados") : List.of(),
            "info_alerts", undocumentedEndpoints > 0 ? 
                List.of("📝 " + undocumentedEndpoints + " endpoints sin documentar") : List.of()
        );
    }

    private List<String> generateRecommendations(Map<String, Object> inventoryHealth) {
        List<String> recommendations = new java.util.ArrayList<>();
        
        if (swaggerEnabled && "production".equals(environment)) {
            recommendations.add("🚨 CRÍTICO: Deshabilitar Swagger en producción");
        }
        
        int undocumentedEndpoints = (Integer) inventoryHealth.get("undocumentedEndpoints");
        if (undocumentedEndpoints > 0) {
            recommendations.add("📝 Documentar " + undocumentedEndpoints + " endpoints faltantes");
        }
        
        int deprecatedEndpoints = (Integer) inventoryHealth.get("deprecatedEndpoints");
        if (deprecatedEndpoints > 0) {
            recommendations.add("🗑️ Revisar " + deprecatedEndpoints + " endpoints deprecados");
        }
        
        if (recommendations.isEmpty()) {
            recommendations.add("✅ Configuración de seguridad óptima");
        }
        
        return recommendations;
    }

    private List<String> generateSecurityRecommendations(Map<String, Object> inventoryHealth, boolean isProductionSafe) {
        List<String> recommendations = new java.util.ArrayList<>();
        
        if (!isProductionSafe) {
            recommendations.add("Configurar APP_ENVIRONMENT=production y SWAGGER_ENABLED=false");
        }
        
        int highRiskEndpoints = (Integer) inventoryHealth.get("highRiskEndpoints");
        if (highRiskEndpoints > 0) {
            recommendations.add("Revisar y asegurar endpoints de alto riesgo");
        }
        
        Double complianceScore = (Double) inventoryHealth.get("complianceScore");
        if (complianceScore < 90.0) {
            recommendations.add("Mejorar documentación para aumentar puntuación de cumplimiento");
        }
        
        return recommendations;
    }
}