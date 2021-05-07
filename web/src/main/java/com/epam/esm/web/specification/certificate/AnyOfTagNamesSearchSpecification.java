package com.epam.esm.web.specification.certificate;

import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.persistence.entity.Certificate_;
import net.kaczmarzyk.spring.data.jpa.domain.In;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Join;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.JoinType;

@Join(path = Certificate_.TAGS, alias = "t", type = JoinType.LEFT)
@Spec(path = "t.name", params = "tag", spec = In.class)
public interface AnyOfTagNamesSearchSpecification extends Specification<Certificate> {

}
