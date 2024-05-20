package peakspace.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import peakspace.validation.validator.UserNameValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Documented
@Constraint(
        validatedBy = UserNameValidator.class
)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@RestControllerAdvice
public @interface UserNameValidation {
    String message() default "{Ник должен быть не меньше 1 и не больше 15 символов. \nНик должен содержать только латинских букв и цифр}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};}