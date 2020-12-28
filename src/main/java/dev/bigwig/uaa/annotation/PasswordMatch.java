package dev.bigwig.uaa.annotation;

import dev.bigwig.uaa.validation.PasswordMathValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordMathValidator.class)
@Documented
public @interface PasswordMatch {

  String message() default "密码不匹配";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
