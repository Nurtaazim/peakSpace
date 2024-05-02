package peakspace.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import peakspace.validation.validator.UniqueEmailValidator;

import java.lang.annotation.*;

@Documented
@Constraint(
        validatedBy = UniqueEmailValidator.class
)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@RestControllerAdvice
public @interface  UniqueEmailValidation {
    String message() default "{User with this email already exist! }";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};}
