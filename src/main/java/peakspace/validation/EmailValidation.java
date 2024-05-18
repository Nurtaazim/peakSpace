package peakspace.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import peakspace.validation.validator.EmailValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Documented
@Constraint(
        validatedBy = EmailValidator.class
)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@RestControllerAdvice
public @interface  EmailValidation {
    String message() default "{Не правильный формат емайл!}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};}