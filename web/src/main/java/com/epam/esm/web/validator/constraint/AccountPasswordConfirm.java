package com.epam.esm.web.validator.constraint;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({
        ElementType.TYPE,
        ElementType.ANNOTATION_TYPE
})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AccountPasswordConfirmValidator.class)
public @interface AccountPasswordConfirm {

    String message() default "{validation.constraints.password.confirm}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
