package model.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class ApiErrorResponse {
    private List<ErrorDetail> error;

    @Data
    @AllArgsConstructor
    public static class ErrorDetail {
        private LocalDateTime timestamp;
        private int codigo;
        private String detail;
    }
}
