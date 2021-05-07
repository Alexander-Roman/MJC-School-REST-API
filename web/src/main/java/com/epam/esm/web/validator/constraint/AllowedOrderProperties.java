package com.epam.esm.web.validator.constraint;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = AllowedOrderPropertiesPageableValidator.class)
@Target({
        ElementType.ANNOTATION_TYPE,
        ElementType.CONSTRUCTOR,
        ElementType.PARAMETER
})
@Retention(RetentionPolicy.RUNTIME)
public @interface AllowedOrderProperties {

    String message() default "{validation.constraints.pageable.sort.order.property}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String[] value();

    @Target({
            ElementType.ANNOTATION_TYPE,
            ElementType.CONSTRUCTOR,
            ElementType.PARAMETER
    })
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        AllowedOrderProperties[] value();
    }

}
