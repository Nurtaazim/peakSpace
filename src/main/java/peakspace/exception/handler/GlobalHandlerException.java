package peakspace.exception.handler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import peakspace.exception.ForbiddenException;
import peakspace.exception.MessagingException;
import peakspace.exception.NotFoundException;
import peakspace.exception.response.ExceptionResponse;
import peakspace.exception.FirebaseAuthException;
import peakspace.exception.NotActiveException;

@RestControllerAdvice
@Slf4j
public class GlobalHandlerException {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse notFound(NotFoundException notFoundException){
        log.error(notFoundException.getMessage());
        return ExceptionResponse.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .exceptionClassName(notFoundException.getClass().getSimpleName())
                .message(notFoundException.getMessage())
                .build();
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionResponse badRequest(ForbiddenException forbiddenException){
        log.error(forbiddenException.getMessage());
        return ExceptionResponse.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .exceptionClassName(forbiddenException.getClass().getSimpleName())
                .message(forbiddenException.getMessage())
                .build();
    }

    @ExceptionHandler(HttpClientErrorException.BadRequest.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse badReq(HttpClientErrorException.BadRequest e){
        return ExceptionResponse.builder()
                .httpStatus(HttpStatus.FORBIDDEN)
                .exceptionClassName(e.getClass().getSimpleName())
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse argumentNotValid(MethodArgumentNotValidException e){
        return ExceptionResponse.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .exceptionClassName(e.getClass().getSimpleName())
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(MessagingException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    public ExceptionResponse messageException(MessagingException e){
        return ExceptionResponse.builder()
                .httpStatus(HttpStatus.EXPECTATION_FAILED)
                .exceptionClassName(e.getClass().getSimpleName())
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionResponse accessDenied(AccessDeniedException accessDeniedException){
        log.error(accessDeniedException.getMessage());
        return ExceptionResponse.builder()
                .httpStatus(HttpStatus.FORBIDDEN)
                .exceptionClassName(accessDeniedException.getClass().getSimpleName())
                .message("Доступ запрещен: " + accessDeniedException.getMessage())
                .build();
    }

    @ExceptionHandler(NotActiveException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public peakspace.exception.response.ExceptionResponse notFound(NotActiveException notActiveException){
        return peakspace.exception.response.ExceptionResponse.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .exceptionClassName(notActiveException.getClass().getSimpleName())
                .message(notActiveException.getMessage())
                .build();
    }
    @ExceptionHandler(FirebaseAuthException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public peakspace.exception.response.ExceptionResponse notFound(FirebaseAuthException notActiveException){
        return peakspace.exception.response.ExceptionResponse.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .exceptionClassName(notActiveException.getClass().getSimpleName())
                .message(notActiveException.getMessage())
                .build();
    }

}
