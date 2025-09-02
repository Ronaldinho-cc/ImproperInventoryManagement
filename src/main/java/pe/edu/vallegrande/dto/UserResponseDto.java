package pe.edu.vallegrande.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO para respuestas de usuario - NO expone información sensible
 */
@Schema(description = "User response data transfer object")
public class UserResponseDto {
    
    @Schema(description = "User ID", example = "1")
    private Long id;
    
    @Schema(description = "Username", example = "john_doe")
    private String username;
    
    @Schema(description = "Email address", example = "john@example.com")
    private String email;
    
    // NO incluye password por seguridad

    public UserResponseDto() {}

    public UserResponseDto(Long id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}