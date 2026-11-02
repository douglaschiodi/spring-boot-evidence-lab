package dev.douglaschiodi.evidence;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.http.converter.HttpMessageNotReadableException;
@RestControllerAdvice
public class ApiErrors {
    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<ProblemDetail> workspace(IllegalArgumentException error) {
        var problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,"Unknown demo workspace");
        return ResponseEntity.status(404).body(problem);
    }
    @ExceptionHandler({MethodArgumentNotValidException.class, HttpMessageNotReadableException.class})
    ResponseEntity<ProblemDetail> invalid(Exception error) {
        var problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,"Use a query of 3–400 characters and a limit from 1 to 3. Unknown fields are rejected.");
        return ResponseEntity.badRequest().body(problem);
    }
}
