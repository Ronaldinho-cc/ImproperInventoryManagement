package pe.edu.vallegrande.controller.secure;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.dto.UserCreateDto;
import pe.edu.vallegrande.dto.UserResponseDto;
import pe.edu.vallegrande.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * SECURE VERSION - API v2
 * Soluciones para API9:2023 - Improper Inventory Management:
 * 1. Versionado explícito en la URL
 * 2. Documentación completa con OpenAPI/Swagger
 * 3. DTOs que no exponen información sensible
 * 4. Endpoints claramente documentados
 */
@RestController
@RequestMapping("/api/v2/users") // Versionado explícito
@Tag(name = "Users V2", description = "User management API - Version 2 (Current)")
public class UserControllerV2 {

    private List<User> users = new ArrayList<>();

    public UserControllerV2() {
        // Datos de prueba
        users.add(new User(1L, "admin", "admin@example.com", "admin123"));
        users.add(new User(2L, "user1", "user1@example.com", "password123"));
    }

    @Operation(
        summary = "Get all users",
        description = "Retrieves a list of all users. Passwords are not included in the response for security."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved users"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> userDtos = users.stream()
                .map(user -> new UserResponseDto(user.getId(), user.getUsername(), user.getEmail()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDtos);
    }

    @Operation(
        summary = "Get user by ID",
        description = "Retrieves a specific user by their ID. Password is not included for security."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User found"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(
            @Parameter(description = "User ID", required = true) @PathVariable Long id) {
        
        User user = users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElse(null);
        
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        
        UserResponseDto userDto = new UserResponseDto(user.getId(), user.getUsername(), user.getEmail());
        return ResponseEntity.ok(userDto);
    }

    @Operation(
        summary = "Create new user",
        description = "Creates a new user. The password will be securely stored and not returned in the response."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserCreateDto userCreateDto) {
        User user = new User();
        user.setId((long) (users.size() + 1));
        user.setUsername(userCreateDto.getUsername());
        user.setEmail(userCreateDto.getEmail());
        user.setPassword(userCreateDto.getPassword()); // En producción, esto debería ser hasheado
        
        users.add(user);
        
        UserResponseDto responseDto = new UserResponseDto(user.getId(), user.getUsername(), user.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
}