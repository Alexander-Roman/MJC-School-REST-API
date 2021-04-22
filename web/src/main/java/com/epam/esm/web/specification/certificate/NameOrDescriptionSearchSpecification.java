package com.epam.esm.web.specification.certificate;

import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.persistence.entity.Certificate_;
import net.kaczmarzyk.spring.data.jpa.domain.LikeIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Or;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

@Or({
        @Spec(path = Certificate_.NAME, params = "search", spec = LikeIgnoreCase.class),
        @Spec(path = Certificate_.DESCRIPTION, params = "search", spec = LikeIgnoreCase.class)
})
public interface NameOrDescriptionSearchSpecification extends Specification<Certificate> {

}
