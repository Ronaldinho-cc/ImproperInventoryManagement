package pe.edu.vallegrande.api9demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.servers.Server;

@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(
        title = "API9:2023 Demo - Water Quality Management",
        version = "1.0.0",
        description = """
            🚨 DEMO: Implementación de protecciones contra API9:2023 Improper Inventory Management
            
            **¿Qué es API9:2023?**
            - Falta de inventario completo de APIs
            - Documentación expuesta en producción
            - Endpoints obsoletos sin control
            - APIs sin documentar
            
            **Protecciones Implementadas:**
            ✅ Inventario automático de endpoints
            ✅ Control por ambiente (dev/prod)
            ✅ Documentación restringida en producción
            ✅ Monitoreo de APIs activas/deprecadas
            ✅ Versionado consistente
            
            **Demo:**
            - Desarrollo: Swagger y inventario habilitados
            - Producción: Endpoints sensibles bloqueados
            
            **Verificar ambiente:** GET /api/v2/health
            """,
        contact = @Contact(
            name = "API Security Demo",
            email = "security-demo@vallegrande.edu.pe"
        )
    ),
    servers = {
        @Server(url = "http://localhost:8087", description = "Desarrollo - Todas las funciones habilitadas"),
        @Server(url = "https://api-prod.vallegrande.edu.pe", description = "Producción - Endpoints sensibles bloqueados")
    }
)
public class Api9DemoApplication {

    public static void main(String[] args) {
        System.out.println("🚀 Iniciando Demo API9:2023 - Improper Inventory Management");
        System.out.println("📋 Verificar inventario: GET /api/inventory (solo desarrollo)");
        System.out.println("📖 Documentación: GET /swagger-ui.html (solo desarrollo)");
        System.out.println("🏥 Estado: GET /api/v2/health");
        
        SpringApplication.run(Api9DemoApplication.class, args);
    }
}