package com.epam.esm.web.specification.certificate;

import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.persistence.entity.Certificate_;
import com.epam.esm.web.specification.ExclusivelyIn;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Join;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.JoinType;

@Join(path = Certificate_.TAGS, alias = "t", type = JoinType.LEFT)
@Spec(path = "t.name", params = "tag", spec = ExclusivelyIn.class)
public interface AllOfTagNamesSearchSpecification extends Specification<Certificate> {

}
