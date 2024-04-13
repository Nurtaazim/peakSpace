package peakspace.exceptions.handler;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import peakspace.exceptions.FirebaseAuthException;
import peakspace.exceptions.response.ExceptionResponse;
import peakspace.exceptions.NotActiveException;

public class HandlerException {

    @ExceptionHandler(NotActiveException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse notFound(NotActiveException notActiveException){
        return ExceptionResponse.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .exceptionClassName(notActiveException.getClass().getSimpleName())
                .message(notActiveException.getMessage())
                .build();
    }
    @ExceptionHandler(FirebaseAuthException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse notFound(FirebaseAuthException notActiveException){
        return ExceptionResponse.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .exceptionClassName(notActiveException.getClass().getSimpleName())
                .message(notActiveException.getMessage())
                .build();
    }
    
}
