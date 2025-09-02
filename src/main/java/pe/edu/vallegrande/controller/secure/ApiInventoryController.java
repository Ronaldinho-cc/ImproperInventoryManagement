package pe.edu.vallegrande.controller.secure;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * SECURE: API Inventory Management
 * Proporciona información sobre las APIs disponibles y su estado
 */
@RestController
@RequestMapping("/api/v2/inventory")
@Tag(name = "API Inventory", description = "API inventory and version management")
public class ApiInventoryController {

    @Operation(
        summary = "Get API inventory",
        description = "Returns information about all available API versions and their status"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "API inventory retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<Map<String, Object>> getApiInventory() {
        Map<String, Object> inventory = new HashMap<>();
        
        // Información de versiones
        Map<String, Object> versions = new HashMap<>();
        
        Map<String, Object> v1Info = new HashMap<>();
        v1Info.put("status", "DEPRECATED");
        v1Info.put("deprecationDate", "2024-01-01");
        v1Info.put("sunsetDate", "2024-06-01");
        v1Info.put("description", "Legacy version - use v2 instead");
        v1Info.put("securityIssues", "Exposes sensitive data");
        versions.put("v1", v1Info);
        
        Map<String, Object> v2Info = new HashMap<>();
        v2Info.put("status", "CURRENT");
        v2Info.put("releaseDate", "2024-01-15");
        v2Info.put("description", "Current stable version");
        v2Info.put("features", "Secure DTOs, proper documentation, versioned endpoints");
        versions.put("v2", v2Info);
        
        inventory.put("versions", versions);
        inventory.put("currentVersion", "v2");
        inventory.put("documentationUrl", "/swagger-ui.html");
        inventory.put("lastUpdated", "2024-02-01");
        
        return ResponseEntity.ok(inventory);
    }

    @Operation(
        summary = "Get deprecated endpoints",
        description = "Returns list of deprecated endpoints that should not be used"
    )
    @GetMapping("/deprecated")
    public ResponseEntity<Map<String, Object>> getDeprecatedEndpoints() {
        Map<String, Object> deprecated = new HashMap<>();
        
        deprecated.put("endpoints", new String[]{
            "/api/users (v1) - Use /api/v2/users instead",
            "/legacy/old-endpoint - No replacement, functionality removed",
            "/legacy/test - Test endpoint, should not be in production",
            "/legacy/internal/config - Internal endpoint, not for public use"
        });
        
        deprecated.put("removalDate", "2024-06-01");
        deprecated.put("migrationGuide", "/docs/migration-guide");
        
        return ResponseEntity.ok(deprecated);
    }
}