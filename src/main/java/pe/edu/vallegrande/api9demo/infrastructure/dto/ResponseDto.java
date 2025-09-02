package pe.edu.vallegrande.api9demo.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDto<T> {
    private boolean success;
    private T data;
    private ErrorMessage error;

    public ResponseDto(boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    public ResponseDto(boolean success, ErrorMessage error) {
        this.success = success;
        this.error = error;
    }
}