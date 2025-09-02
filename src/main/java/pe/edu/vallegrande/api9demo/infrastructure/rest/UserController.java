package pe.edu.vallegrande.api9demo.infrastructure.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.api9demo.domain.models.User;
import pe.edu.vallegrande.api9demo.infrastructure.dto.ErrorMessage;
import pe.edu.vallegrande.api9demo.infrastructure.dto.ResponseDto;
import pe.edu.vallegrande.api9demo.infrastructure.dto.request.UserCreateRequest;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 👥 CONTROLADOR DE USUARIOS
 * 
 * Endpoints para gestión de usuarios del sistema de calidad de agua.
 * Implementa documentación completa para cumplir con API9:2023.
 */
@Slf4j
@RestController
@RequestMapping("/api/v2/users")
@CrossOrigin("*")
@RequiredArgsConstructor
@Tag(name = "👥 Users", description = "Gestión de usuarios del sistema")
public class UserController {

    @GetMapping
    @Operation(
        summary = "📋 Listar todos los usuarios",
        description = """
            Retorna la lista completa de usuarios registrados en el sistema.
            
            **Funcionalidad:**
            - Lista todos los usuarios activos e inactivos
            - Incluye información básica de cada usuario
            - Requiere autenticación
            
            **Casos de uso:**
            - Administración de usuarios
            - Reportes del sistema
            """,
        security = @SecurityRequirement(name = "bearerAuth"),
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado - Token requerido"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        }
    )
    public Mono<ResponseDto<List<User>>> getAllUsers() {
        log.info("📋 Solicitando lista de todos los usuarios");
        
        // Simulación de datos para la demo
        List<User> users = List.of(
            new User("1", "Juan Pérez", "juan@example.com", "***", "ACTIVE", LocalDateTime.now(), null),
            new User("2", "María García", "maria@example.com", "***", "ACTIVE", LocalDateTime.now(), null),
            new User("3", "Carlos López", "carlos@example.com", "***", "INACTIVE", LocalDateTime.now(), null)
        );
        
        return Mono.just(new ResponseDto<>(true, users))
            .doOnSuccess(result -> log.info("✅ Lista de usuarios obtenida: {} usuarios", users.size()));
    }

    @GetMapping("/active")
    @Operation(
        summary = "✅ Listar usuarios activos",
        description = """
            Retorna solo los usuarios con estado ACTIVE.
            
            **Filtros aplicados:**
            - status = 'ACTIVE'
            - Ordenados por fecha de creación
            """,
        security = @SecurityRequirement(name = "bearerAuth"),
        responses = {
            @ApiResponse(responseCode = "200", description = "Usuarios activos obtenidos")
        }
    )
    public Mono<ResponseDto<List<User>>> getActiveUsers() {
        log.info("✅ Solicitando usuarios activos");
        
        List<User> activeUsers = List.of(
            new User("1", "Juan Pérez", "juan@example.com", "***", "ACTIVE", LocalDateTime.now(), null),
            new User("2", "María García", "maria@example.com", "***", "ACTIVE", LocalDateTime.now(), null)
        );
        
        return Mono.just(new ResponseDto<>(true, activeUsers));
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "🔍 Obtener usuario por ID",
        description = """
            Retorna la información detallada de un usuario específico.
            
            **Parámetros:**
            - id: Identificador único del usuario
            
            **Validaciones:**
            - El usuario debe existir
            - El ID debe ser válido
            """,
        security = @SecurityRequirement(name = "bearerAuth"),
        responses = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
        }
    )
    public Mono<ResponseDto<User>> getUserById(
        @Parameter(description = "ID único del usuario", example = "1")
        @PathVariable String id
    ) {
        log.info("🔍 Buscando usuario con ID: {}", id);
        
        if ("999".equals(id)) {
            return Mono.just(new ResponseDto<>(false, 
                new ErrorMessage(HttpStatus.NOT_FOUND.value(), 
                    "Usuario no encontrado", 
                    "No existe un usuario con ID: " + id)));
        }
        
        User user = new User(id, "Usuario Demo", "demo@example.com", "***", "ACTIVE", LocalDateTime.now(), null);
        return Mono.just(new ResponseDto<>(true, user));
    }

    @PostMapping
    @Operation(
        summary = "➕ Crear nuevo usuario",
        description = """
            Crea un nuevo usuario en el sistema.
            
            **Validaciones:**
            - Email único en el sistema
            - Contraseña con mínimo 6 caracteres
            - Nombre obligatorio
            
            **Proceso:**
            1. Validar datos de entrada
            2. Verificar email único
            3. Encriptar contraseña
            4. Guardar en base de datos
            """,
        security = @SecurityRequirement(name = "bearerAuth"),
        responses = {
            @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "409", description = "Email ya existe")
        }
    )
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseDto<User>> createUser(@RequestBody UserCreateRequest request) {
        log.info("➕ Creando nuevo usuario: {}", request.getEmail());
        
        // Simulación de validación de email duplicado
        if ("duplicate@example.com".equals(request.getEmail())) {
            return Mono.just(new ResponseDto<>(false,
                new ErrorMessage(HttpStatus.CONFLICT.value(),
                    "Email ya existe",
                    "Ya existe un usuario con el email: " + request.getEmail())));
        }
        
        User newUser = new User(
            "new-id", 
            request.getName(), 
            request.getEmail(), 
            "***", 
            "ACTIVE", 
            LocalDateTime.now(), 
            null
        );
        
        return Mono.just(new ResponseDto<>(true, newUser))
            .doOnSuccess(result -> log.info("✅ Usuario creado exitosamente: {}", request.getEmail()));
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "✏️ Actualizar usuario",
        description = """
            Actualiza la información de un usuario existente.
            
            **Campos actualizables:**
            - Nombre
            - Email (debe ser único)
            - Estado
            
            **Restricciones:**
            - No se puede cambiar la contraseña por este endpoint
            - El usuario debe existir
            """,
        security = @SecurityRequirement(name = "bearerAuth"),
        responses = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
        }
    )
    public Mono<ResponseDto<User>> updateUser(
        @Parameter(description = "ID del usuario a actualizar")
        @PathVariable String id, 
        @RequestBody User user
    ) {
        log.info("✏️ Actualizando usuario ID: {}", id);
        
        user.setUserId(id);
        user.setUpdatedAt(LocalDateTime.now());
        
        return Mono.just(new ResponseDto<>(true, user))
            .doOnSuccess(result -> log.info("✅ Usuario actualizado: {}", id));
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "🗑️ Eliminar usuario",
        description = """
            Realiza eliminación lógica del usuario (cambia estado a INACTIVE).
            
            **Proceso:**
            - No elimina físicamente el registro
            - Cambia estado a INACTIVE
            - Mantiene historial para auditoría
            
            **Nota:** Para eliminación física usar endpoint específico de administrador.
            """,
        security = @SecurityRequirement(name = "bearerAuth"),
        responses = {
            @ApiResponse(responseCode = "204", description = "Usuario eliminado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
        }
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<ResponseDto<Object>> deleteUser(
        @Parameter(description = "ID del usuario a eliminar")
        @PathVariable String id
    ) {
        log.info("🗑️ Eliminando usuario ID: {}", id);
        
        return Mono.just(new ResponseDto<>(true, null))
            .doOnSuccess(result -> log.info("✅ Usuario eliminado (lógicamente): {}", id));
    }
}