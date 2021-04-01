package com.epam.esm.web.controller;

import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.service.logic.CertificateService;
import com.epam.esm.web.model.CertificateDto;
import com.epam.esm.web.validator.CertificateDtoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/certificate")
public class CertificateController {

    private final CertificateService certificateService;
    private final Converter<CertificateDto, Certificate> certificateDtoToEntityConverter;
    private final CertificateDtoValidator certificateDtoValidator;

    @Autowired
    public CertificateController(CertificateService certificateService,
                                 Converter<CertificateDto, Certificate> certificateDtoToEntityConverter,
                                 CertificateDtoValidator certificateDtoValidator) {
        this.certificateService = certificateService;
        this.certificateDtoToEntityConverter = certificateDtoToEntityConverter;
        this.certificateDtoValidator = certificateDtoValidator;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CertificateDto> getCertificateById(@PathVariable("id") Long id) {
        Optional<Certificate> found = certificateService.findById(id);
        if (!found.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Certificate does not exists! ID: " + id);
        }
        Certificate certificate = found.get();
        CertificateDto certificateDto = CertificateDto.fromEntity(certificate);
        return new ResponseEntity<>(certificateDto, HttpStatus.OK);
    }

}
