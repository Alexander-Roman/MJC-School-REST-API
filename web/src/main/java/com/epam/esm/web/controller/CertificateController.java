package com.epam.esm.web.controller;

import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.service.CertificateService;
import com.epam.esm.web.assember.CertificateDtoAssembler;
import com.epam.esm.web.mapper.CertificateMapper;
import com.epam.esm.web.model.CertificateDto;
import com.epam.esm.web.specification.certificate.FilterRequestSpecification;
import com.epam.esm.web.validator.CertificateDtoCreateValidator;
import com.epam.esm.web.validator.CertificateDtoUpdateValidator;
import com.epam.esm.web.validator.CertificatePageableValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/certificates")
@Validated
public class CertificateController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CertificateController.class);

    private static final String MSG_CODE_ID_INVALID = "controller.id.invalid";
    private static final long MIN_ID = 1L;

    private final CertificateService certificateService;
    private final CertificateDtoUpdateValidator certificateDtoUpdateValidator;
    private final CertificateDtoCreateValidator certificateDtoCreateValidator;
    private final CertificatePageableValidator certificatePageableValidator;
    private final CertificateMapper certificateMapper;
    private final PagedResourcesAssembler<Certificate> certificatePagedResourcesAssembler;
    private final CertificateDtoAssembler certificateDtoAssembler;

    @Autowired
    public CertificateController(CertificateService certificateService,
                                 CertificateDtoUpdateValidator certificateDtoUpdateValidator,
                                 CertificateDtoCreateValidator certificateDtoCreateValidator,
                                 CertificatePageableValidator certificatePageableValidator,
                                 CertificateMapper certificateMapper,
                                 PagedResourcesAssembler<Certificate> certificatePagedResourcesAssembler,
                                 CertificateDtoAssembler certificateDtoAssembler) {
        this.certificateService = certificateService;
        this.certificateDtoUpdateValidator = certificateDtoUpdateValidator;
        this.certificateDtoCreateValidator = certificateDtoCreateValidator;
        this.certificatePageableValidator = certificatePageableValidator;
        this.certificateMapper = certificateMapper;
        this.certificatePagedResourcesAssembler = certificatePagedResourcesAssembler;
        this.certificateDtoAssembler = certificateDtoAssembler;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CertificateDto> getCertificateById(
            @PathVariable("id")
            @Min(value = MIN_ID, message = MSG_CODE_ID_INVALID) Long id) {
        Certificate certificate = certificateService.findById(id);
        CertificateDto certificateDto = certificateMapper.map(certificate);

        this.addLinks(certificateDto);
        return new ResponseEntity<>(certificateDto, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<PagedModel<CertificateDto>> getCertificatePage(Pageable pageable,
//                                                                         BindingResult bindingResult,
                                                                         FilterRequestSpecification filterRequestSpecification) throws BindException {
//        certificatePageableValidator.validate(pageable, bindingResult);
//        if (bindingResult.hasErrors()) {
//            throw new BindException(bindingResult);
//        }
        Page<Certificate> certificatePage = certificateService.findPage(pageable, filterRequestSpecification);
        PagedModel<CertificateDto> certificateDtoPageModel = certificatePagedResourcesAssembler.toModel(certificatePage, certificateDtoAssembler);
        return new ResponseEntity<>(certificateDtoPageModel, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CertificateDto> createCertificate(@RequestBody CertificateDto certificateDto,
                                                            BindingResult bindingResult) throws BindException {
        certificateDtoCreateValidator.validate(certificateDto, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        Certificate certificate = certificateMapper.map(certificateDto);
        Certificate created = certificateService.create(certificate);
        CertificateDto createdDto = certificateMapper.map(created);

        this.addLinks(certificateDto);
        return new ResponseEntity<>(createdDto, HttpStatus.CREATED);
    }

    @PatchMapping
    public ResponseEntity<CertificateDto> updateCertificate(@RequestBody CertificateDto certificateDto,
                                                            BindingResult bindingResult) throws BindException {
        certificateDtoUpdateValidator.validate(certificateDto, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        Certificate certificate = certificateMapper.map(certificateDto);
        Certificate updated = certificateService.selectiveUpdate(certificate);
        CertificateDto updatedDto = certificateMapper.map(updated);

        this.addLinks(certificateDto);
        return new ResponseEntity<>(updatedDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CertificateDto> deleteById(
            @PathVariable("id")
            @Min(value = MIN_ID, message = MSG_CODE_ID_INVALID) Long id) {
        Certificate deleted = certificateService.deleteById(id);
        CertificateDto deletedDto = certificateMapper.map(deleted);
        return new ResponseEntity<>(deletedDto, HttpStatus.OK);
    }

    private void addLinks(CertificateDto certificateDto) {
        Long id = certificateDto.getId();
        certificateDto.add(linkTo(methodOn(CertificateController.class).getCertificateById(id)).withSelfRel());
    }

    @GetMapping("/test")
    public ResponseEntity<?> test(@RequestParam Map<String, String> parameters) {
        LOGGER.trace("TRACE");
        LOGGER.debug("DEBUG");
        LOGGER.info("INFO");
        LOGGER.warn("WARN");
        LOGGER.error("ERROR");
        return new ResponseEntity<>(parameters, HttpStatus.OK);
    }

}
