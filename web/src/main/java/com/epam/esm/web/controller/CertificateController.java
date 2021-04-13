package com.epam.esm.web.controller;

import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.persistence.model.FilterRequest;
import com.epam.esm.persistence.model.SortRequest;
import com.epam.esm.service.CertificateService;
import com.epam.esm.web.model.CertificateDto;
import com.epam.esm.web.model.FilterRequestDto;
import com.epam.esm.web.model.SortRequestDto;
import com.epam.esm.web.validator.CertificateDtoCreateValidator;
import com.epam.esm.web.validator.CertificateDtoUpdateValidator;
import com.epam.esm.web.validator.CertificateSortRequestDtoValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/certificate")
public class CertificateController {

    private final CertificateService certificateService;
    private final CertificateDtoUpdateValidator certificateDtoUpdateValidator;
    private final CertificateDtoCreateValidator certificateDtoCreateValidator;
    private final CertificateSortRequestDtoValidator certificateSortRequestDtoValidator;
    private final ModelMapper modelMapper;

    @Autowired
    public CertificateController(CertificateService certificateService,
                                 CertificateDtoUpdateValidator certificateDtoUpdateValidator,
                                 CertificateDtoCreateValidator certificateDtoCreateValidator,
                                 CertificateSortRequestDtoValidator certificateSortRequestDtoValidator,
                                 ModelMapper modelMapper) {
        this.certificateService = certificateService;
        this.certificateDtoUpdateValidator = certificateDtoUpdateValidator;
        this.certificateDtoCreateValidator = certificateDtoCreateValidator;
        this.certificateSortRequestDtoValidator = certificateSortRequestDtoValidator;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CertificateDto> getCertificateById(@PathVariable("id") Long id) {
        Certificate certificate = certificateService.findById(id);
        CertificateDto certificateDto = modelMapper.map(certificate, CertificateDto.class);
        return new ResponseEntity<>(certificateDto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<CertificateDto>> getCertificateList(SortRequestDto sortRequestDto,
                                                                   BindingResult bindingResult,
                                                                   FilterRequestDto filterRequestDto) throws BindException {
        certificateSortRequestDtoValidator.validate(sortRequestDto, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        SortRequest sortRequest = modelMapper.map(sortRequestDto, SortRequest.class);
        FilterRequest filterRequest = modelMapper.map(filterRequestDto, FilterRequest.class);
        List<Certificate> result = certificateService.findAll(sortRequest, filterRequest);
        List<CertificateDto> resultDto = result
                .stream()
                .map(certificate -> modelMapper.map(certificate, CertificateDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(resultDto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CertificateDto> createCertificate(@RequestBody CertificateDto certificateDto,
                                                            BindingResult bindingResult) throws BindException {
        certificateDtoCreateValidator.validate(certificateDto, bindingResult);
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
        certificateDtoUpdateValidator.validate(certificateDto, bindingResult);
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
