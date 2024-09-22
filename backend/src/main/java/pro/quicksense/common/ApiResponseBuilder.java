package pro.quicksense.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ApiResponseBuilder {

    public static ResponseEntity<Object> success(HttpStatus status, String message, Object data) {
        return ResponseEntity.status(status)
                .body(new ApiResponse<>(status, message, data));
    }

    public static ResponseEntity<Object> error(HttpStatus status, String message, Object data) {
        return ResponseEntity.status(status)
                .body(new ApiResponse<>(status, message, data));
    }
}