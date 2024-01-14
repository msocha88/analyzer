package pl.msocha.analyzer.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AnalyzerApiExceptionHandler {

    @ExceptionHandler(value = InvalidInputException.class)
    protected ResponseEntity<Object> handleInvalidInputException(InvalidInputException exception) {
        return ResponseEntity.badRequest().body(
                String.format("Exception occurred while processing task with id [%s] with errorMessage [%s]",
                        exception.getId(),
                        exception.getMessage()));

    }
}
