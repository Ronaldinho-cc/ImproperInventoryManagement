package pe.edu.vallegrande.api9demo.application.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.reactive.result.method.RequestMappingInfo;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping;
import pe.edu.vallegrande.api9demo.domain.models.ApiEndpoint;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 🚨 SERVICIO PRINCIPAL PARA INVENTARIO DE APIs
 * 
 * Mitiga API9:2023 - Improper Inventory Management
 * 
 * Funcionalidades:
 * - Descubrimiento automático de endpoints
 * - Catalogación con metadatos completos
 * - Identificación de endpoints deprecados
 * - Control de acceso por ambiente
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ApiInventoryService {

    private final RequestMappingHandlerMapping handlerMapping;
    
    @Value("${app.version:1.0.0}")
    private String appVersion;
    
    @Value("${app.environment:development}")
    private String environment;

    /**
     * 📋 Obtiene el inventario completo de endpoints
     */
    public Mono<List<ApiEndpoint>> getApiInventory() {
        return Mono.fromCallable(() -> {
            log.info("🔍 Generando inventario de APIs para ambiente: {}", environment);
            
            Map<RequestMappingInfo, HandlerMethod> handlerMethods = 
                handlerMapping.getHandlerMethods();
            
            List<ApiEndpoint> endpoints = handlerMethods.entrySet().stream()
                .map(this::mapToApiEndpoint)
                .toList();
                
            log.info("📊 Inventario generado: {} endpoints encontrados", endpoints.size());
            return endpoints;
        });
    }

    /**
     * ✅ Obtiene solo endpoints activos
     */
    public Flux<ApiEndpoint> getActiveEndpoints() {
        return getApiInventory()
            .flatMapMany(Flux::fromIterable)
            .filter(endpoint -> endpoint.getStatus() == ApiEndpoint.ApiStatus.ACTIVE);
    }

    /**
     * ⚠️ Obtiene endpoints deprecados
     */
    public Flux<ApiEndpoint> getDeprecatedEndpoints() {
        return getApiInventory()
            .flatMapMany(Flux::fromIterable)
            .filter(ApiEndpoint::isDeprecated);
    }

    /**
     * 📝 Verifica endpoints sin documentar
     */
    public Mono<List<ApiEndpoint>> getUndocumentedEndpoints() {
        return getApiInventory()
            .map(endpoints -> endpoints.stream()
                .filter(endpoint -> endpoint.getDescription() == null || 
                                  endpoint.getDescription().isEmpty())
                .toList());
    }

    /**
     * 🔒 Obtiene endpoints de alto riesgo
     */
    public Flux<ApiEndpoint> getHighRiskEndpoints() {
        return getApiInventory()
            .flatMapMany(Flux::fromIterable)
            .filter(endpoint -> "HIGH".equals(endpoint.getRiskLevel()));
    }

    /**
     * 🏥 Genera reporte de salud del inventario
     */
    public Mono<Map<String, Object>> getInventoryHealthReport() {
        return getApiInventory()
            .map(endpoints -> {
                long totalEndpoints = endpoints.size();
                long activeEndpoints = endpoints.stream()
                    .filter(e -> e.getStatus() == ApiEndpoint.ApiStatus.ACTIVE)
                    .count();
                long deprecatedEndpoints = endpoints.stream()
                    .filter(ApiEndpoint::isDeprecated)
                    .count();
                long undocumentedEndpoints = endpoints.stream()
                    .filter(e -> e.getDescription() == null || e.getDescription().isEmpty())
                    .count();
                long highRiskEndpoints = endpoints.stream()
                    .filter(e -> "HIGH".equals(e.getRiskLevel()))
                    .count();

                return Map.<String, Object>of(
                    "status", undocumentedEndpoints == 0 ? "HEALTHY" : "WARNING",
                    "timestamp", LocalDateTime.now(),
                    "environment", environment,
                    "totalEndpoints", totalEndpoints,
                    "activeEndpoints", activeEndpoints,
                    "deprecatedEndpoints", deprecatedEndpoints,
                    "undocumentedEndpoints", undocumentedEndpoints,
                    "highRiskEndpoints", highRiskEndpoints,
                    "inventoryComplete", undocumentedEndpoints == 0,
                    "complianceScore", calculateComplianceScore(endpoints)
                );
            });
    }

    private ApiEndpoint mapToApiEndpoint(Map.Entry<RequestMappingInfo, HandlerMethod> entry) {
        RequestMappingInfo mapping = entry.getKey();
        HandlerMethod method = entry.getValue();
        
        Set<String> patterns = mapping.getPatternValues();
        Set<String> methods = mapping.getMethodsCondition().getMethods()
            .stream()
            .map(Enum::name)
            .collect(java.util.stream.Collectors.toSet());

        String path = patterns.isEmpty() ? "unknown" : patterns.iterator().next();
        String httpMethod = methods.isEmpty() ? "unknown" : methods.iterator().next();
        
        // 🔍 Análisis de seguridad del endpoint
        boolean isInternal = isInternalEndpoint(path);
        boolean requiresAuth = requiresAuthentication(path);
        String riskLevel = calculateRiskLevel(path, httpMethod, isInternal);
        
        return ApiEndpoint.builder()
            .path(path)
            .method(httpMethod)
            .version(extractVersion(path))
            .description(generateDescription(path, httpMethod))
            .deprecated(isDeprecated(path))
            .requiresAuth(requiresAuth)
            .roles(determineRoles(path))
            .environment(environment)
            .lastModified(LocalDateTime.now())
            .owner("api9-demo")
            .status(determineStatus(path, isInternal))
            .controllerClass(method.getBeanType().getSimpleName())
            .methodName(method.getMethod().getName())
            .publicEndpoint(!requiresAuth)
            .riskLevel(riskLevel)
            .build();
    }

    private boolean isInternalEndpoint(String path) {
        return path.contains("/actuator") || 
               path.contains("/debug") || 
               path.contains("/internal") ||
               path.contains("/inventory");
    }

    private boolean requiresAuthentication(String path) {
        return !path.contains("/actuator/health") && 
               !path.contains("/actuator/info") &&
               !path.contains("/swagger-ui") &&
               !path.contains("/v3/api-docs");
    }

    private String calculateRiskLevel(String path, String method, boolean isInternal) {
        if (isInternal && "production".equals(environment)) {
            return "HIGH";
        }
        if (path.contains("/swagger-ui") || path.contains("/v3/api-docs")) {
            return "production".equals(environment) ? "HIGH" : "LOW";
        }
        if ("DELETE".equals(method)) {
            return "MEDIUM";
        }
        return "LOW";
    }

    private String extractVersion(String path) {
        if (path.contains("/v2/")) return "v2";
        if (path.contains("/v1/")) return "v1";
        return appVersion;
    }

    private String generateDescription(String path, String method) {
        if (path.contains("/actuator")) {
            return "Endpoint de monitoreo - Spring Actuator";
        }
        if (path.contains("/inventory")) {
            return "Endpoint de inventario de APIs (solo desarrollo)";
        }
        if (path.contains("/swagger-ui")) {
            return "Documentación Swagger UI (solo desarrollo)";
        }
        if (path.contains("/users")) {
            return switch (method) {
                case "GET" -> path.contains("{id}") ? 
                    "Obtener usuario específico" : "Listar usuarios";
                case "POST" -> "Crear nuevo usuario";
                case "PUT" -> "Actualizar usuario";
                case "DELETE" -> "Eliminar usuario";
                default -> "Operación sobre usuarios";
            };
        }
        if (path.contains("/qualitytests")) {
            return switch (method) {
                case "GET" -> path.contains("{id}") ? 
                    "Obtener prueba de calidad específica" : "Listar pruebas de calidad";
                case "POST" -> "Crear nueva prueba de calidad";
                case "PUT" -> "Actualizar prueba de calidad";
                case "DELETE" -> "Eliminar prueba de calidad";
                default -> "Operación sobre pruebas de calidad";
            };
        }
        return "Endpoint del microservicio de calidad de agua";
    }

    private boolean isDeprecated(String path) {
        return path.contains("/v0/") || 
               path.contains("/deprecated/") ||
               path.contains("/legacy/");
    }

    private List<String> determineRoles(String path) {
        if (path.contains("/actuator")) {
            return List.of("ADMIN", "MONITOR");
        }
        if (path.contains("/inventory")) {
            return List.of("ADMIN", "DEVELOPER");
        }
        if (path.contains("/users") || path.contains("/qualitytests")) {
            return List.of("USER", "ADMIN");
        }
        return List.of("USER");
    }

    private ApiEndpoint.ApiStatus determineStatus(String path, boolean isInternal) {
        if (isDeprecated(path)) {
            return ApiEndpoint.ApiStatus.DEPRECATED;
        }
        if (isInternal && "production".equals(environment)) {
            return ApiEndpoint.ApiStatus.DISABLED;
        }
        if (isInternal) {
            return ApiEndpoint.ApiStatus.INTERNAL;
        }
        return ApiEndpoint.ApiStatus.ACTIVE;
    }

    private double calculateComplianceScore(List<ApiEndpoint> endpoints) {
        if (endpoints.isEmpty()) return 100.0;
        
        long documented = endpoints.stream()
            .filter(e -> e.getDescription() != null && !e.getDescription().isEmpty())
            .count();
        long secured = endpoints.stream()
            .filter(e -> e.isRequiresAuth() || e.isPublicEndpoint())
            .count();
        long properStatus = endpoints.stream()
            .filter(e -> e.getStatus() != null)
            .count();
            
        return ((documented + secured + properStatus) * 100.0) / (endpoints.size() * 3);
    }
}