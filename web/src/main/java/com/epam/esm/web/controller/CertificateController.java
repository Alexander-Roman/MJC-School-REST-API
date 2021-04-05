package com.epam.esm.web.controller;

import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.persistence.model.SortRequest;
import com.epam.esm.service.logic.CertificateService;
import com.epam.esm.persistence.model.FilterRequest;
import com.epam.esm.web.model.CertificateDto;
import com.epam.esm.web.model.FilterRequestDto;
import com.epam.esm.web.model.SortRequestDto;
import com.epam.esm.web.validator.CertificateDtoValidator;
import com.epam.esm.web.validator.SortRequestDtoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/certificate")
public class CertificateController {

    private final CertificateService certificateService;
    private final CertificateDtoValidator certificateDtoValidator;
    private final SortRequestDtoValidator sortRequestDtoValidator;
    private final Converter<CertificateDto, Certificate> certificateDtoToEntityConverter;
    private final Converter<SortRequestDto, SortRequest> sortRequestDtoToModelConverter;
    private final Converter<FilterRequestDto, FilterRequest> filterRequestDtoToModelConverter;

    @Autowired
    public CertificateController(CertificateService certificateService,
                                 CertificateDtoValidator certificateDtoValidator,
                                 SortRequestDtoValidator sortRequestDtoValidator,
                                 Converter<CertificateDto, Certificate> certificateDtoToEntityConverter,
                                 Converter<SortRequestDto, SortRequest> sortRequestDtoToModelConverter,
                                 Converter<FilterRequestDto, FilterRequest> filterRequestDtoToModelConverter) {
        this.certificateService = certificateService;
        this.certificateDtoValidator = certificateDtoValidator;
        this.sortRequestDtoValidator = sortRequestDtoValidator;
        this.certificateDtoToEntityConverter = certificateDtoToEntityConverter;
        this.sortRequestDtoToModelConverter = sortRequestDtoToModelConverter;
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
    public ResponseEntity<?> getCertificateList(SortRequestDto sortRequestDto,
                                                BindingResult bindingResult,
                                                FilterRequestDto filterRequestDto) throws BindException {
        sortRequestDtoValidator.validate(sortRequestDto, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        SortRequest sortRequest = sortRequestDtoToModelConverter.convert(sortRequestDto);
        FilterRequest filterRequest = filterRequestDtoToModelConverter.convert(filterRequestDto);
        List<Certificate> result = certificateService.findAll(sortRequest, filterRequest);
        List<CertificateDto> resultDto = result
                .stream()
                .map(CertificateDto::fromEntity)
                .collect(Collectors.toList());
        return new ResponseEntity<>(resultDto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CertificateDto> createCertificate(@RequestBody CertificateDto certificateDto,
                                                            BindingResult bindingResult) throws BindException {
        certificateDtoValidator.validate(certificateDto, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        Certificate certificate = certificateDtoToEntityConverter.convert(certificateDto);
        Certificate created = certificateService.create(certificate);
        CertificateDto createdDto = CertificateDto.fromEntity(created);
        return new ResponseEntity<>(createdDto, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<CertificateDto> updateCertificate(@RequestBody CertificateDto certificateDto,
                                                            BindingResult bindingResult) throws BindException {
        certificateDtoValidator.validate(certificateDto, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        Long id = certificateDto.getId();
        Optional<Certificate> found = certificateService.findById(id);
        if (!found.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Certificate does not exists! ID: " + id);
        }
        Certificate target = found.get();
        Certificate source = certificateDtoToEntityConverter.convert(certificateDto);
        Certificate updated = certificateService.selectiveUpdate(source, target);
        CertificateDto updatedDto = CertificateDto.fromEntity(updated);
        return new ResponseEntity<>(updatedDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CertificateDto> deleteById(@PathVariable("id") Long id) {
        Optional<Certificate> found = certificateService.findById(id);
        if (!found.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Certificate does not exists! ID: " + id);
        }
        certificateService.deleteById(id);
        Certificate certificate = found.get();
        CertificateDto certificateDto = CertificateDto.fromEntity(certificate);
        return new ResponseEntity<>(certificateDto, HttpStatus.OK);
    }

}
