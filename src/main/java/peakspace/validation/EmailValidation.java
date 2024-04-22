package peakspace.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import peakspace.validation.validator.EmailValidator;

import java.lang.annotation.*;

@Documented
@Constraint(
        validatedBy = EmailValidator.class
)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface  EmailValidation {
    String message() default "{Wrong format! Email must end with to @gmail.com!}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};}
