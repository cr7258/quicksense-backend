package pro.quicksense.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import pro.quicksense.exception.CustomExceptions.LoginException;
import pro.quicksense.exception.CustomExceptions.UserNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pro.quicksense.common.ApiResponseBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        log.error("Method or argument not valid: {}", ex.getMessage());
        List<Map<String, Object>> errors = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
            Map<String, Object> fieldErrorInfo = Map.of(
                    "object", fieldError.getObjectName(),
                    "field", fieldError.getField(),
                    "rejectedValue", fieldError.getRejectedValue(),
                    "errorMessage", fieldError.getDefaultMessage()
            );
            errors.add(fieldErrorInfo);
        });
        return ApiResponseBuilder.error(HttpStatus.UNPROCESSABLE_ENTITY, "Method or Argument not valid", errors);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolation(
            DataIntegrityViolationException ex,
            WebRequest request) {

        log.error("Data integrity violation occurred: {}", ex.getMessage());
        return ApiResponseBuilder.error(HttpStatus.CONFLICT, "Data integrity violation", ex.getMessage());
    }

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<Object> handleLoginException(LoginException ex) {
        log.error("Login exception occurred: {}", ex.getMessage());
        return ApiResponseBuilder.error(HttpStatus.UNAUTHORIZED, ex.getMessage(), null);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex) {
        log.error("User not found exception occurred: {}", ex.getMessage());
        return ApiResponseBuilder.error(HttpStatus.NOT_FOUND, ex.getMessage(), null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception ex) {
        log.error("An error occurred: {}", ex.getMessage());
        return ApiResponseBuilder.error(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), null);
    }
}