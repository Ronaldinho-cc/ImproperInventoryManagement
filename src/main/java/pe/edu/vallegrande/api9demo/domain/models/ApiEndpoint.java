package pe.edu.vallegrande.api9demo.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 🚨 MODELO PARA INVENTARIO DE APIs - MITIGA API9:2023
 * 
 * Representa cada endpoint de la API con metadatos completos
 * para mantener un inventario actualizado y seguro.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiEndpoint {
    
    private String path;                    // "/api/v2/users"
    private String method;                  // "GET", "POST", etc.
    private String version;                 // "v2"
    private String description;             // Descripción del endpoint
    private boolean deprecated;             // Si está deprecado
    private boolean requiresAuth;           // Si requiere autenticación
    private List<String> roles;            // Roles necesarios
    private String environment;             // "development", "production"
    private LocalDateTime lastModified;     // Última modificación
    private String owner;                   // Propietario del endpoint
    private ApiStatus status;               // Estado actual
    private String controllerClass;         // Clase del controlador
    private String methodName;              // Nombre del método
    private boolean publicEndpoint;         // Si es público
    private String riskLevel;              // "LOW", "MEDIUM", "HIGH"
    
    public enum ApiStatus {
        ACTIVE("Endpoint activo y disponible"),
        DEPRECATED("Endpoint deprecado, será removido próximamente"),
        BETA("Endpoint en fase beta, puede cambiar"),
        INTERNAL("Endpoint interno, no público"),
        DISABLED("Endpoint deshabilitado por seguridad");
        
        private final String description;
        
        ApiStatus(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
}