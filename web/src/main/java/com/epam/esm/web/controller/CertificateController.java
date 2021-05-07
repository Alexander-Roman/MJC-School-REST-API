package com.epam.esm.web.controller;

import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.persistence.entity.Certificate_;
import com.epam.esm.service.CertificateService;
import com.epam.esm.web.assember.CertificateDtoAssembler;
import com.epam.esm.web.mapper.CertificateMapper;
import com.epam.esm.web.model.CertificateDto;
import com.epam.esm.web.specification.certificate.FilterRequestSpecification;
import com.epam.esm.web.validator.constraint.AllowedOrderProperties;
import com.epam.esm.web.validator.group.CertificateCreate;
import com.epam.esm.web.validator.group.CertificateUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import javax.validation.groups.Default;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/certificates")
@Validated
public class CertificateController {


    private static final String MSG_CODE_ID_INVALID = "{validation.constraints.id.min}";
    private static final long MIN_ID = 1L;
    private static final String REL_ALL_CERTIFICATES = "certificates";

    private final CertificateService certificateService;
    private final CertificateMapper certificateMapper;
    private final PagedResourcesAssembler<Certificate> certificatePagedResourcesAssembler;
    private final CertificateDtoAssembler certificateDtoAssembler;

    @Autowired
    public CertificateController(CertificateService certificateService,
                                 CertificateMapper certificateMapper,
                                 PagedResourcesAssembler<Certificate> certificatePagedResourcesAssembler,
                                 CertificateDtoAssembler certificateDtoAssembler) {
        this.certificateService = certificateService;
        this.certificateMapper = certificateMapper;
        this.certificatePagedResourcesAssembler = certificatePagedResourcesAssembler;
        this.certificateDtoAssembler = certificateDtoAssembler;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CertificateDto> getCertificateById(
            @PathVariable("id")
            @Min(value = MIN_ID, message = MSG_CODE_ID_INVALID) Long id) {
        Certificate certificate = certificateService.findById(id);
        CertificateDto certificateDto = certificateDtoAssembler.toModel(certificate);

        certificateDto.add(linkTo(methodOn(CertificateController.class).getCertificatePage(null, null)).withRel(REL_ALL_CERTIFICATES));
        return new ResponseEntity<>(certificateDto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<PagedModel<CertificateDto>> getCertificatePage(
            @AllowedOrderProperties({
                    Certificate_.NAME,
                    Certificate_.DESCRIPTION,
                    Certificate_.PRICE,
                    Certificate_.DURATION,
                    Certificate_.CREATE_DATE,
                    Certificate_.LAST_UPDATE_DATE
            }) Pageable pageable, FilterRequestSpecification filterRequestSpecification) {

        Page<Certificate> certificatePage = certificateService.findPage(pageable, filterRequestSpecification);
        PagedModel<CertificateDto> certificateDtoPageModel = certificatePagedResourcesAssembler.toModel(certificatePage, certificateDtoAssembler);
        return new ResponseEntity<>(certificateDtoPageModel, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CertificateDto> createCertificate(@RequestBody
                                                            @Validated({
                                                                    Default.class,
                                                                    CertificateCreate.class
                                                            }) CertificateDto certificateDto) {
        Certificate certificate = certificateMapper.map(certificateDto);
        Certificate created = certificateService.create(certificate);
        CertificateDto createdDto = certificateDtoAssembler.toModel(created);

        createdDto.add(linkTo(methodOn(CertificateController.class).getCertificatePage(null, null)).withRel(REL_ALL_CERTIFICATES));
        return new ResponseEntity<>(createdDto, HttpStatus.CREATED);
    }

    @PatchMapping
    public ResponseEntity<CertificateDto> updateCertificate(@RequestBody
                                                            @Validated({
                                                                    Default.class,
                                                                    CertificateUpdate.class
                                                            }) CertificateDto certificateDto) {
        Long id = certificateDto.getId();
        Certificate found = certificateService.findById(id);
        Certificate certificate = certificateMapper.mapMerge(certificateDto, found);
        Certificate updated = certificateService.update(certificate);
        CertificateDto updatedDto = certificateDtoAssembler.toModel(updated);

        updatedDto.add(linkTo(methodOn(CertificateController.class).getCertificatePage(null, null)).withRel(REL_ALL_CERTIFICATES));
        return new ResponseEntity<>(updatedDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CertificateDto> deleteById(
            @PathVariable("id")
            @Min(value = MIN_ID, message = MSG_CODE_ID_INVALID) Long id) {
        Certificate deleted = certificateService.deleteById(id);
        CertificateDto deletedDto = certificateMapper.map(deleted);

        deletedDto.add(linkTo(methodOn(CertificateController.class).getCertificatePage(null, null)).withRel(REL_ALL_CERTIFICATES));
        return new ResponseEntity<>(deletedDto, HttpStatus.OK);
    }

}
