package pe.edu.vallegrande.controller.vulnerable;

import org.springframework.web.bind.annotation.*;

/**
 * VULNERABLE: API Legacy sin inventario adecuado
 * Esta API está obsoleta pero sigue activa sin advertencias
 */
@RestController
@RequestMapping("/legacy") // API obsoleta sin documentar
public class LegacyApiController {

    // Endpoint obsoleto que debería estar deshabilitado
    @GetMapping("/old-endpoint")
    public String oldEndpoint() {
        return "This is an old endpoint that should be deprecated";
    }

    // Endpoint de prueba que se olvidó remover
    @GetMapping("/test")
    public String testEndpoint() {
        return "Test endpoint - should not be in production";
    }

    // Endpoint interno expuesto por error
    @GetMapping("/internal/config")
    public String getInternalConfig() {
        return "Internal config: database=prod, debug=true";
    }
}