package pe.edu.vallegrande.api9demo.application.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 📖 CONFIGURACIÓN OPENAPI/SWAGGER - API9:2023 COMPLIANT
 * 
 * ⚠️ IMPORTANTE: Esta configuración solo se activa cuando Swagger está habilitado.
 * En producción, Swagger debe estar deshabilitado para cumplir con API9:2023.
 */
@Configuration
@ConditionalOnProperty(name = "springdoc.swagger-ui.enabled", havingValue = "true", matchIfMissing = true)
public class OpenApiConfig {

    @Value("${app.version:1.0.0}")
    private String appVersion;

    @Value("${app.environment:development}")
    private String environment;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("🚨 API9:2023 Demo - Water Quality Management")
                        .version(appVersion)
                        .description(generateApiDescription())
                        .contact(new Contact()
                                .name("API Security Demo Team")
                                .email("security-demo@vallegrande.edu.pe")
                                .url("https://vallegrande.edu.pe"))
                        .license(new License()
                                .name("Demo License")
                                .url("https://vallegrande.edu.pe/license")))
                .servers(getServers())
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("bearerAuth", 
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("JWT token obtenido del servidor OAuth2")));
    }

    private String generateApiDescription() {
        return String.format("""
                🚨 **DEMO: Implementación de protecciones contra API9:2023 Improper Inventory Management**
                
                **🎯 Objetivo de la Demo:**
                Mostrar cómo implementar un inventario completo y seguro de APIs para mitigar la vulnerabilidad API9:2023.
                
                **🛡️ Protecciones Implementadas:**
                ✅ Inventario automático de todos los endpoints
                ✅ Documentación completa y actualizada
                ✅ Control por ambiente (desarrollo vs producción)
                ✅ Endpoints sensibles bloqueados en producción
                ✅ Monitoreo de APIs deprecadas y de alto riesgo
                ✅ Versionado consistente y controlado
                
                **🔍 Endpoints de Inventario (Solo Desarrollo):**
                - `GET /api/inventory` - Inventario completo
                - `GET /api/inventory/active` - Endpoints activos
                - `GET /api/inventory/deprecated` - Endpoints deprecados
                - `GET /api/inventory/high-risk` - Endpoints de alto riesgo
                - `GET /api/inventory/health` - Estado del inventario
                
                **🏥 Endpoints de Salud:**
                - `GET /api/v2/health` - Estado general de la API
                - `GET /api/v2/health/version` - Información de versión
                - `GET /api/v2/health/security` - Estado de seguridad (solo dev)
                
                **⚠️ IMPORTANTE:**
                - **Ambiente actual:** %s
                - **Swagger habilitado:** %s
                - **Inventario disponible:** %s
                
                **🚨 En Producción:**
                - Esta documentación está DESHABILITADA
                - Endpoints de inventario están BLOQUEADOS
                - Solo endpoints de negocio están disponibles
                
                **📋 APIs de Negocio Disponibles:**
                - **Usuarios:** Gestión completa de usuarios del sistema
                - **Pruebas de Calidad:** Gestión de pruebas de calidad del agua
                
                **🔐 Autenticación:**
                Todos los endpoints de negocio requieren token JWT válido.
                """, 
                environment,
                !"production".equals(environment) ? "SÍ" : "NO",
                !"production".equals(environment) ? "SÍ" : "NO"
        );
    }

    private List<Server> getServers() {
        return switch (environment.toLowerCase()) {
            case "production" -> List.of(
                    new Server()
                            .url("https://api-prod.vallegrande.edu.pe")
                            .description("🚀 Producción - Endpoints sensibles bloqueados")
            );
            case "staging" -> List.of(
                    new Server()
                            .url("https://api-staging.vallegrande.edu.pe")
                            .description("🧪 Staging - Pruebas pre-producción")
            );
            default -> List.of(
                    new Server()
                            .url("http://localhost:8087")
                            .description("🔧 Desarrollo - Todas las funciones habilitadas")
            );
        };
    }
}