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
import pe.edu.vallegrande.api9demo.domain.models.QualityTest;
import pe.edu.vallegrande.api9demo.infrastructure.dto.ErrorMessage;
import pe.edu.vallegrande.api9demo.infrastructure.dto.ResponseDto;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 🧪 CONTROLADOR DE PRUEBAS DE CALIDAD
 * 
 * Endpoints para gestión de pruebas de calidad del agua.
 * Documentación completa para cumplimiento de API9:2023.
 */
@Slf4j
@RestController
@RequestMapping("/api/v2/qualitytests")
@CrossOrigin("*")
@RequiredArgsConstructor
@Tag(name = "🧪 Quality Tests", description = "Gestión de pruebas de calidad del agua")
public class QualityTestController {

    @GetMapping
    @Operation(
        summary = "📋 Listar pruebas de calidad",
        description = """
            Retorna todas las pruebas de calidad registradas en el sistema.
            
            **Información incluida:**
            - Datos básicos de la prueba
            - Resultados de parámetros medidos
            - Estado de la prueba
            - Información del punto de muestreo
            
            **Filtros disponibles:**
            - Por fecha de prueba
            - Por estado
            - Por punto de muestreo
            """,
        security = @SecurityRequirement(name = "bearerAuth"),
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista de pruebas obtenida"),
            @ApiResponse(responseCode = "401", description = "No autorizado")
        }
    )
    public Mono<ResponseDto<List<QualityTest>>> getAllQualityTests() {
        log.info("📋 Solicitando lista de pruebas de calidad");
        
        // Datos de demo
        List<QualityTest> tests = List.of(
            createSampleTest("1", "TEST-001", "COMPLETED"),
            createSampleTest("2", "TEST-002", "IN_PROGRESS"),
            createSampleTest("3", "TEST-003", "COMPLETED")
        );
        
        return Mono.just(new ResponseDto<>(true, tests))
            .doOnSuccess(result -> log.info("✅ Pruebas de calidad obtenidas: {}", tests.size()));
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "🔍 Obtener prueba específica",
        description = """
            Retorna los detalles completos de una prueba de calidad específica.
            
            **Información detallada:**
            - Metadatos de la prueba
            - Resultados de todos los parámetros
            - Condiciones ambientales
            - Observaciones del técnico
            - Historial de cambios
            """,
        security = @SecurityRequirement(name = "bearerAuth"),
        responses = {
            @ApiResponse(responseCode = "200", description = "Prueba encontrada"),
            @ApiResponse(responseCode = "404", description = "Prueba no encontrada")
        }
    )
    public Mono<ResponseDto<QualityTest>> getQualityTestById(
        @Parameter(description = "ID único de la prueba", example = "1")
        @PathVariable String id
    ) {
        log.info("🔍 Buscando prueba de calidad ID: {}", id);
        
        if ("999".equals(id)) {
            return Mono.just(new ResponseDto<>(false,
                new ErrorMessage(HttpStatus.NOT_FOUND.value(),
                    "Prueba no encontrada",
                    "No existe una prueba con ID: " + id)));
        }
        
        QualityTest test = createSampleTest(id, "TEST-" + id, "COMPLETED");
        return Mono.just(new ResponseDto<>(true, test));
    }

    @PostMapping
    @Operation(
        summary = "➕ Crear nueva prueba de calidad",
        description = """
            Registra una nueva prueba de calidad en el sistema.
            
            **Proceso de creación:**
            1. Validar datos de entrada
            2. Generar código único de prueba
            3. Registrar condiciones iniciales
            4. Crear estructura para resultados
            5. Asignar técnico responsable
            
            **Validaciones:**
            - Punto de muestreo debe existir
            - Técnico debe estar activo
            - Fecha no puede ser futura
            """,
        security = @SecurityRequirement(name = "bearerAuth"),
        responses = {
            @ApiResponse(responseCode = "201", description = "Prueba creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
        }
    )
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseDto<QualityTest>> createQualityTest(@RequestBody QualityTest request) {
        log.info("➕ Creando nueva prueba de calidad");
        
        QualityTest newTest = createSampleTest("new-id", "TEST-NEW", "IN_PROGRESS");
        newTest.setCreatedAt(LocalDateTime.now());
        
        return Mono.just(new ResponseDto<>(true, newTest))
            .doOnSuccess(result -> log.info("✅ Prueba de calidad creada: {}", newTest.getTestCode()));
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "✏️ Actualizar prueba de calidad",
        description = """
            Actualiza los datos de una prueba de calidad existente.
            
            **Campos actualizables:**
            - Resultados de parámetros
            - Observaciones
            - Estado de la prueba
            - Condiciones ambientales
            
            **Restricciones:**
            - No se puede cambiar el código de prueba
            - Pruebas finalizadas tienen restricciones
            """,
        security = @SecurityRequirement(name = "bearerAuth"),
        responses = {
            @ApiResponse(responseCode = "200", description = "Prueba actualizada"),
            @ApiResponse(responseCode = "404", description = "Prueba no encontrada")
        }
    )
    public Mono<ResponseDto<QualityTest>> updateQualityTest(
        @Parameter(description = "ID de la prueba a actualizar")
        @PathVariable String id,
        @RequestBody QualityTest request
    ) {
        log.info("✏️ Actualizando prueba de calidad ID: {}", id);
        
        request.setId(id);
        return Mono.just(new ResponseDto<>(true, request))
            .doOnSuccess(result -> log.info("✅ Prueba actualizada: {}", id));
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "🗑️ Eliminar prueba de calidad",
        description = """
            Realiza eliminación lógica de la prueba de calidad.
            
            **Proceso:**
            - Marca la prueba como eliminada
            - Mantiene datos para auditoría
            - Actualiza fecha de eliminación
            
            **Nota:** Los datos se conservan para trazabilidad regulatoria.
            """,
        security = @SecurityRequirement(name = "bearerAuth"),
        responses = {
            @ApiResponse(responseCode = "204", description = "Prueba eliminada"),
            @ApiResponse(responseCode = "404", description = "Prueba no encontrada")
        }
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<ResponseDto<Object>> deleteQualityTest(
        @Parameter(description = "ID de la prueba a eliminar")
        @PathVariable String id
    ) {
        log.info("🗑️ Eliminando prueba de calidad ID: {}", id);
        
        return Mono.just(new ResponseDto<>(true, null))
            .doOnSuccess(result -> log.info("✅ Prueba eliminada (lógicamente): {}", id));
    }

    @PutMapping("/{id}/restore")
    @Operation(
        summary = "♻️ Restaurar prueba eliminada",
        description = """
            Restaura una prueba que fue eliminada lógicamente.
            
            **Funcionalidad:**
            - Reactiva la prueba eliminada
            - Limpia fecha de eliminación
            - Restaura estado anterior
            
            **Uso típico:** Recuperación de datos eliminados por error.
            """,
        security = @SecurityRequirement(name = "bearerAuth"),
        responses = {
            @ApiResponse(responseCode = "200", description = "Prueba restaurada"),
            @ApiResponse(responseCode = "404", description = "Prueba no encontrada")
        }
    )
    public Mono<ResponseDto<QualityTest>> restoreQualityTest(
        @Parameter(description = "ID de la prueba a restaurar")
        @PathVariable String id
    ) {
        log.info("♻️ Restaurando prueba de calidad ID: {}", id);
        
        QualityTest restoredTest = createSampleTest(id, "TEST-" + id, "ACTIVE");
        restoredTest.setDeletedAt(null);
        
        return Mono.just(new ResponseDto<>(true, restoredTest))
            .doOnSuccess(result -> log.info("✅ Prueba restaurada: {}", id));
    }

    private QualityTest createSampleTest(String id, String testCode, String status) {
        QualityTest test = new QualityTest();
        test.setId(id);
        test.setTestCode(testCode);
        test.setOrganizationId("ORG-001");
        test.setTestingPointId("POINT-001");
        test.setTestDate(LocalDateTime.now().minusDays(1));
        test.setTestType("ROUTINE");
        test.setTestedByUserId("USER-001");
        test.setWeatherConditions("Soleado, 22°C");
        test.setWaterTemperature(18.5);
        test.setGeneralObservations("Muestra tomada según protocolo estándar");
        test.setStatus(status);
        test.setCreatedAt(LocalDateTime.now().minusDays(1));
        
        // Resultados de ejemplo
        List<QualityTest.TestResult> results = List.of(
            new QualityTest.TestResult("PH-001", "PH", 7.2, "pH", "NORMAL", "Dentro del rango"),
            new QualityTest.TestResult("CL-001", "CLORO", 0.8, "mg/L", "NORMAL", "Nivel adecuado")
        );
        test.setResults(results);
        
        return test;
    }
}