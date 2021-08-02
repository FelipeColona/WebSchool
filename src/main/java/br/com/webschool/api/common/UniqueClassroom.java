package br.com.webschool.api.common;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
 
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = UniqueClassroomValidator.class)
public @interface UniqueClassroom {
    String message() default "Classroom must be unique";
 
    Class<?>[] groups() default {};
 
    Class<? extends Payload>[] payload() default {};
 
    String value() default "";
}
