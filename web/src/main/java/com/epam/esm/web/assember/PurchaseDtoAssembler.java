package com.epam.esm.web.assember;

import com.epam.esm.persistence.entity.Purchase;
import com.epam.esm.web.model.PurchaseDto;
import org.springframework.hateoas.server.RepresentationModelAssembler;

/**
 * Interface for components that convert Purchase type into PurchaseDto as RepresentationModel
 */
public interface PurchaseDtoAssembler extends RepresentationModelAssembler<Purchase, PurchaseDto> {

}
