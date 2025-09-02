package pe.edu.vallegrande.api9demo.infrastructure.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.vallegrande.api9demo.application.services.ApiInventoryService;
import pe.edu.vallegrande.api9demo.domain.models.ApiEndpoint;
import pe.edu.vallegrande.api9demo.infrastructure.dto.ResponseDto;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

/**
 * 🚨 CONTROLADOR DE INVENTARIO DE APIs - SOLO DESARROLLO
 * 
 * ⚠️ IMPORTANTE: Este controlador está DESHABILITADO en producción
 * para cumplir con API9:2023 - Improper Inventory Management
 * 
 * Funcionalidades:
 * - Inventario completo de endpoints
 * - Análisis de endpoints deprecados
 * - Reporte de salud del inventario
 * - Identificación de endpoints de alto riesgo
 */
@Slf4j
@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
@Tag(name = "🔍 API Inventory", description = "Inventario y gestión de endpoints (SOLO DESARROLLO)")
@ConditionalOnProperty(
    name = "app.environment", 
    havingValue = "production", 
    matchIfMissing = false
)
public class ApiInventoryController {

    private final ApiInventoryService apiInventoryService;

    @GetMapping
    @Operation(
        summary = "📋 Obtener inventario completo de APIs",
        description = """
            Retorna el inventario completo de todos los endpoints disponibles en el microservicio.
            
            **⚠️ SOLO DISPONIBLE EN DESARROLLO**
            
            Incluye:
            - Método HTTP y path
            - Estado (activo, deprecado, etc.)
            - Requerimientos de autenticación
            - Nivel de riesgo
            - Metadatos completos
            """,
        responses = {
            @ApiResponse(responseCode = "200", description = "Inventario obtenido exitosamente"),
            @ApiResponse(responseCode = "404", description = "Endpoint no disponible en producción")
        }
    )
    public Mono<ResponseEntity<ResponseDto<List<ApiEndpoint>>>> getApiInventory() {
        log.info("🔍 Solicitando inventario completo de APIs");
        
        return apiInventoryService.getApiInventory()
            .map(endpoints -> {
                log.info("📊 Inventario generado: {} endpoints", endpoints.size());
                return ResponseEntity.ok(new ResponseDto<>(true, endpoints));
            })
            .doOnError(error -> log.error("❌ Error generando inventario: {}", error.getMessage()));
    }

    @GetMapping("/active")
    @Operation(
        summary = "✅ Obtener endpoints activos",
        description = "Retorna solo los endpoints que están activos y disponibles",
        responses = {
            @ApiResponse(responseCode = "200", description = "Endpoints activos obtenidos")
        }
    )
    public Mono<ResponseEntity<ResponseDto<List<ApiEndpoint>>>> getActiveEndpoints() {
        log.info("✅ Solicitando endpoints activos");
        
        return apiInventoryService.getActiveEndpoints()
            .collectList()
            .map(endpoints -> {
                log.info("📊 Endpoints activos: {}", endpoints.size());
                return ResponseEntity.ok(new ResponseDto<>(true, endpoints));
            });
    }

    @GetMapping("/deprecated")
    @Operation(
        summary = "⚠️ Obtener endpoints deprecados",
        description = """
            Retorna endpoints marcados como deprecados que deberían ser removidos.
            
            **Importante:** Estos endpoints representan un riesgo de seguridad
            y deberían ser migrados o eliminados.
            """,
        responses = {
            @ApiResponse(responseCode = "200", description = "Endpoints deprecados obtenidos")
        }
    )
    public Mono<ResponseEntity<ResponseDto<List<ApiEndpoint>>>> getDeprecatedEndpoints() {
        log.warn("⚠️ Solicitando endpoints deprecados");
        
        return apiInventoryService.getDeprecatedEndpoints()
            .collectList()
            .map(endpoints -> {
                if (!endpoints.isEmpty()) {
                    log.warn("🚨 Se encontraron {} endpoints deprecados", endpoints.size());
                }
                return ResponseEntity.ok(new ResponseDto<>(true, endpoints));
            });
    }

    @GetMapping("/undocumented")
    @Operation(
        summary = "📝 Obtener endpoints sin documentar",
        description = """
            Retorna endpoints que no tienen documentación completa.
            
            **Problema de seguridad:** Endpoints sin documentar pueden
            representar APIs olvidadas o no controladas.
            """,
        responses = {
            @ApiResponse(responseCode = "200", description = "Endpoints sin documentar obtenidos")
        }
    )
    public Mono<ResponseEntity<ResponseDto<List<ApiEndpoint>>>> getUndocumentedEndpoints() {
        log.info("📝 Solicitando endpoints sin documentar");
        
        return apiInventoryService.getUndocumentedEndpoints()
            .map(endpoints -> {
                if (!endpoints.isEmpty()) {
                    log.warn("📝 Se encontraron {} endpoints sin documentar", endpoints.size());
                }
                return ResponseEntity.ok(new ResponseDto<>(true, endpoints));
            });
    }

    @GetMapping("/high-risk")
    @Operation(
        summary = "🚨 Obtener endpoints de alto riesgo",
        description = """
            Retorna endpoints clasificados como de alto riesgo de seguridad.
            
            **Criterios de alto riesgo:**
            - Endpoints internos expuestos en producción
            - Documentación accesible públicamente
            - Endpoints de debug o desarrollo
            """,
        responses = {
            @ApiResponse(responseCode = "200", description = "Endpoints de alto riesgo obtenidos")
        }
    )
    public Mono<ResponseEntity<ResponseDto<List<ApiEndpoint>>>> getHighRiskEndpoints() {
        log.warn("🚨 Solicitando endpoints de alto riesgo");
        
        return apiInventoryService.getHighRiskEndpoints()
            .collectList()
            .map(endpoints -> {
                if (!endpoints.isEmpty()) {
                    log.error("🚨 ALERTA: Se encontraron {} endpoints de alto riesgo", endpoints.size());
                }
                return ResponseEntity.ok(new ResponseDto<>(true, endpoints));
            });
    }

    @GetMapping("/health")
    @Operation(
        summary = "🏥 Verificación de salud del inventario",
        description = """
            Verifica el estado general del sistema de inventario de APIs.
            
            **Métricas incluidas:**
            - Total de endpoints
            - Endpoints activos vs deprecados
            - Endpoints sin documentar
            - Puntuación de cumplimiento
            - Estado general del inventario
            """,
        responses = {
            @ApiResponse(responseCode = "200", description = "Reporte de salud generado")
        }
    )
    public Mono<ResponseEntity<ResponseDto<Map<String, Object>>>> getInventoryHealth() {
        log.info("🏥 Generando reporte de salud del inventario");
        
        return apiInventoryService.getInventoryHealthReport()
            .map(healthReport -> {
                String status = (String) healthReport.get("status");
                if ("WARNING".equals(status)) {
                    log.warn("⚠️ Inventario con advertencias: {}", healthReport);
                } else {
                    log.info("✅ Inventario saludable: puntuación {}", healthReport.get("complianceScore"));
                }
                return ResponseEntity.ok(new ResponseDto<>(true, healthReport));
            });
    }
}