package pe.edu.vallegrande.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO para creación de usuarios
 */
@Schema(description = "User creation data transfer object")
public class UserCreateDto {
    
    @Schema(description = "Username", example = "john_doe", required = true)
    private String username;
    
    @Schema(description = "Email address", example = "john@example.com", required = true)
    private String email;
    
    @Schema(description = "Password", example = "securePassword123", required = true)
    private String password;

    public UserCreateDto() {}

    public UserCreateDto(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}