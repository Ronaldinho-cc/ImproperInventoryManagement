package pe.edu.vallegrande.api9demo.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "quality_tests")
public class QualityTest {

    @Id
    private String id;

    private String organizationId;
    private String testCode;
    private String testingPointId;
    private LocalDateTime testDate;
    private String testType;
    private String testedByUserId;
    private String weatherConditions;
    private Double waterTemperature;
    private String generalObservations;
    private String status;
    private List<TestResult> results;

    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TestResult {
        private String parameterId;
        private String parameterCode;
        private Double measuredValue;
        private String unit;
        private String status;
        private String observations;
    }
}