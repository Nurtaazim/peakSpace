package peakspace.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import peakspace.validation.validator.PasswordValidator;

import java.lang.annotation.*;

@Documented
@Constraint(
        validatedBy = PasswordValidator.class
)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface  PasswordValidation {
    String message() default "{Длина пароля должна быть от 8 до 20 символов.\n" +
            "Пароль должен содержать хотя бы одну цифру.\n" +
            "Пароль должен содержать хотя бы одну букву в верхнем регистре.\n" +
            "Пароль должен содержать хотя бы одну букву в нижнем регистре.\n" +
            "Пароль может содержать специальные символы.}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};


}

