package com.epam.esm.web.model;

import com.epam.esm.persistence.entity.Account;
import com.epam.esm.persistence.entity.Purchase;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

public final class PurchaseDto extends RepresentationModel<PurchaseDto> implements Serializable {

//    private static final long serialVersionUID = 1L;
//    @JsonCreator
//    @JsonProperty("name")
//
//
//
//    private Long id;
//    private AccountDto accountDto;
//    private Set<PurchaseDto.Item> items;
//    private BigDecimal cost;
//    private LocalDateTime date;
//
//
//
//
//    public final class




}
