package peakspace.exceptions.handler;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import peakspace.exceptions.FirebaseAuthException;
import peakspace.exceptions.response.ExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import peakspace.exceptions.NotActiveException;

@Slf4j
public class HandlerException {
    @ExceptionHandler(NotActiveException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse notFound(NotActiveException notActiveException){
        log.error(notActiveException.getMessage());
        return ExceptionResponse.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .exceptionClassName(notActiveException.getClass().getSimpleName())
                .message(notActiveException.getMessage())
                .build();
    }
    @ExceptionHandler(FirebaseAuthException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse notFound(FirebaseAuthException notActiveException){
        log.error(notActiveException.getMessage());
        return ExceptionResponse.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .exceptionClassName(notActiveException.getClass().getSimpleName())
                .message(notActiveException.getMessage())
                .build();
    }
}
