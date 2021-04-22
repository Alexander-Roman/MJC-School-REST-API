package com.epam.esm.web.specification.certificate;

import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.persistence.entity.Certificate_;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.In;
import net.kaczmarzyk.spring.data.jpa.domain.LikeIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.domain.NotNull;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Conjunction;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Join;
import net.kaczmarzyk.spring.data.jpa.web.annotation.JoinFetch;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Or;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.JoinType;


//@Join(path = Certificate_.TAGS, alias = "t", type = JoinType.LEFT)
//@Conjunction(
//        value = @Or({
//                @Spec(path = Certificate_.NAME, params = "search", spec = LikeIgnoreCase.class),
//                @Spec(path = Certificate_.DESCRIPTION, params = "search", spec = LikeIgnoreCase.class)
//        }),
//        and = @Spec(path = "t.name", params = "tag", spec = Equal.class)
//)
//@Or({
//        @Spec(path = Certificate_.NAME, params = "search", spec = LikeIgnoreCase.class),
//        @Spec(path = Certificate_.DESCRIPTION, params = "search", spec = LikeIgnoreCase.class)
//})
//@And({
//        @Spec(path = "t.name", params = "tag", spec = In.class)
//})

@And({
        @Spec(path = Certificate_.ID, spec = NotNull.class)
})
public interface FilterRequestSpecification extends Specification<Certificate> {

}
