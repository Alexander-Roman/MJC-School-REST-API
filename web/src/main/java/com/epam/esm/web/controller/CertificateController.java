package com.epam.esm.web.controller;

import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.persistence.model.FilterRequest;
import com.epam.esm.persistence.model.SortRequest;
import com.epam.esm.service.logic.CertificateService;
import com.epam.esm.web.model.CertificateDto;
import com.epam.esm.web.model.FilterRequestDto;
import com.epam.esm.web.model.SortRequestDto;
import com.epam.esm.web.validator.CertificateDtoValidator;
import com.epam.esm.web.validator.SortRequestDtoValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/certificate")
public class CertificateController {

    private final CertificateService certificateService;
    private final CertificateDtoValidator certificateDtoValidator;
    private final SortRequestDtoValidator sortRequestDtoValidator;
    private final ModelMapper modelMapper;

    @Autowired
    public CertificateController(CertificateService certificateService,
                                 CertificateDtoValidator certificateDtoValidator,
                                 SortRequestDtoValidator sortRequestDtoValidator,
                                 ModelMapper modelMapper) {
        this.certificateService = certificateService;
        this.certificateDtoValidator = certificateDtoValidator;
        this.sortRequestDtoValidator = sortRequestDtoValidator;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CertificateDto> getCertificateById(@PathVariable("id") Long id) {
        Certificate certificate = certificateService.findById(id);
        CertificateDto certificateDto = modelMapper.map(certificate, CertificateDto.class);
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
        SortRequest sortRequest = modelMapper.map(sortRequestDto, SortRequest.class);
        FilterRequest filterRequest = modelMapper.map(filterRequestDto, FilterRequest.class);
        List<Certificate> result = certificateService.findAll(sortRequest, filterRequest);
        List<CertificateDto> resultDto = result
                .stream()
                .map(entity -> modelMapper.map(entity, CertificateDto.class))
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
        Certificate certificate = modelMapper.map(certificateDto, Certificate.class);
        Certificate created = certificateService.create(certificate);
        CertificateDto createdDto = modelMapper.map(created, CertificateDto.class);
        return new ResponseEntity<>(createdDto, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<CertificateDto> updateCertificate(@RequestBody CertificateDto certificateDto,
                                                            BindingResult bindingResult) throws BindException {
        certificateDtoValidator.validate(certificateDto, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        Certificate certificate = modelMapper.map(certificateDto, Certificate.class);
        Certificate updated = certificateService.selectiveUpdate(certificate);
        CertificateDto updatedDto = modelMapper.map(updated, CertificateDto.class);
        return new ResponseEntity<>(updatedDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CertificateDto> deleteById(@PathVariable("id") Long id) {
        Certificate deleted = certificateService.deleteById(id);
        CertificateDto deletedDto = modelMapper.map(deleted, CertificateDto.class);
        return new ResponseEntity<>(deletedDto, HttpStatus.OK);
    }

}
