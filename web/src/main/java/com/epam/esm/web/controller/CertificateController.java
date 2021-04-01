package com.epam.esm.web.controller;

import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.service.logic.CertificateService;
import com.epam.esm.service.model.FilterRequest;
import com.epam.esm.web.model.CertificateDto;
import com.epam.esm.web.model.FilterRequestDto;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/certificate")
public class CertificateController {

    private final CertificateService certificateService;
    private final CertificateDtoValidator certificateDtoValidator;
    private final Converter<CertificateDto, Certificate> certificateDtoToEntityConverter;
    private final Converter<FilterRequestDto, FilterRequest> filterRequestDtoToModelConverter;

    @Autowired
    public CertificateController(CertificateService certificateService,
                                 CertificateDtoValidator certificateDtoValidator,
                                 Converter<CertificateDto, Certificate> certificateDtoToEntityConverter,
                                 Converter<FilterRequestDto, FilterRequest> filterRequestDtoToModelConverter) {
        this.certificateService = certificateService;
        this.certificateDtoValidator = certificateDtoValidator;
        this.certificateDtoToEntityConverter = certificateDtoToEntityConverter;
        this.filterRequestDtoToModelConverter = filterRequestDtoToModelConverter;
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

    @GetMapping
    public ResponseEntity<?> getCertificateList(FilterRequestDto filterRequestDto) {
        FilterRequest filterRequest = filterRequestDtoToModelConverter.convert(filterRequestDto);
        List<Certificate> result = certificateService.findAll(filterRequest);
        List<CertificateDto> resultDto = result
                .stream()
                .map(CertificateDto::fromEntity)
                .collect(Collectors.toList());
        return new ResponseEntity<>(resultDto, HttpStatus.OK);
    }

}
