package pe.edu.vallegrande.controller.vulnerable;

import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * VULNERABLE VERSION - API v1
 * Problemas de API9:2023 - Improper Inventory Management:
 * 1. Versión obsoleta sin deprecation warnings
 * 2. Expone información sensible (passwords)
 * 3. Sin documentación adecuada
 * 4. Sin versionado claro en la URL
 */
@RestController
@RequestMapping("/api/users") // Sin versionado explícito
public class UserControllerV1 {

    private List<User> users = new ArrayList<>();

    public UserControllerV1() {
        // Datos de prueba
        users.add(new User(1L, "admin", "admin@example.com", "admin123"));
        users.add(new User(2L, "user1", "user1@example.com", "password123"));
    }

    // Endpoint sin documentación adecuada
    @GetMapping
    public List<User> getAllUsers() {
        // VULNERABLE: Retorna passwords en texto plano
        return users;
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        // VULNERABLE: Expone password
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        user.setId((long) (users.size() + 1));
        users.add(user);
        return user; // VULNERABLE: Retorna password
    }

    // Endpoint "secreto" sin documentar
    @GetMapping("/admin/debug")
    public String getDebugInfo() {
        return "Debug info: Total users: " + users.size() + 
               ", Server version: 1.0-DEPRECATED";
    }
}