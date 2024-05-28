package peakspace.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import peakspace.exception.*;
import peakspace.exception.IllegalArgumentException;

import peakspace.exception.response.ExceptionResponse;

@RestControllerAdvice
@Slf4j
public class GlobalHandlerException {

    @ExceptionHandler(AccountIsBlock.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionResponse accountIsBlock(AccountIsBlock accountIsBlockException){
        log.error(accountIsBlockException.getMessage());
        return ExceptionResponse.builder()
                .httpStatus(HttpStatus.FORBIDDEN)
                .exceptionClassName(accountIsBlockException.getClass().getSimpleName())
                .message(accountIsBlockException.getMessage())
                .build();
    }

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
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse badRequest(IllegalArgumentException illegalArgumentException){
        log.error(illegalArgumentException.getMessage());
        return ExceptionResponse.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .exceptionClassName(illegalArgumentException.getClass().getSimpleName())
                .message(illegalArgumentException.getMessage())
                .build();
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse badReq(BadRequestException e){
        return ExceptionResponse.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
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
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public peakspace.exception.response.ExceptionResponse firebaseAuth(FirebaseAuthException notActiveException){
        return peakspace.exception.response.ExceptionResponse.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .exceptionClassName(notActiveException.getClass().getSimpleName())
                .message(notActiveException.getMessage())
                .build();
    }

    @ExceptionHandler(InvalidConfirmationCode.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public peakspace.exception.response.ExceptionResponse BadRequest(InvalidConfirmationCode invalidConfirmationCode){
        return peakspace.exception.response.ExceptionResponse.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .exceptionClassName(invalidConfirmationCode.getClass().getSimpleName())
                .message(invalidConfirmationCode.getMessage() + "Неправильный код!")
                .build();
    }

    @ExceptionHandler(SmsSendingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public peakspace.exception.response.ExceptionResponse BadRequest(SmsSendingException smsSendingException){
        return peakspace.exception.response.ExceptionResponse.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .exceptionClassName(smsSendingException.getClass().getSimpleName())
                .message(smsSendingException.getMessage() + "Не удалось отправить СМС!")
                .build();
    }
}
