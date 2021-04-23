package com.epam.esm.web.specification.certificate;

import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.persistence.entity.Certificate_;
import com.epam.esm.web.specification.All;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

@Spec(path = Certificate_.ID, spec = All.class)
public interface FindAllSpecification extends Specification<Certificate> {

}
