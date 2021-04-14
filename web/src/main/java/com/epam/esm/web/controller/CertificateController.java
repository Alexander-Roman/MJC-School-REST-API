package com.epam.esm.web.controller;

import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.persistence.model.FilterRequest;
import com.epam.esm.persistence.model.SortRequest;
import com.epam.esm.service.CertificateService;
import com.epam.esm.web.mapper.CertificateMapper;
import com.epam.esm.web.mapper.FilterRequestMapper;
import com.epam.esm.web.mapper.SortRequestMapper;
import com.epam.esm.web.model.CertificateDto;
import com.epam.esm.web.model.FilterRequestDto;
import com.epam.esm.web.model.SortRequestDto;
import com.epam.esm.web.validator.CertificateDtoCreateValidator;
import com.epam.esm.web.validator.CertificateDtoUpdateValidator;
import com.epam.esm.web.validator.CertificateSortRequestDtoValidator;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/certificates")
@Validated
public class CertificateController {

    private static final String MSG_CODE_ID_INVALID = "controller.id.invalid";
    private static final long MIN_ID = 1L;

    private final CertificateService certificateService;
    private final CertificateDtoUpdateValidator certificateDtoUpdateValidator;
    private final CertificateDtoCreateValidator certificateDtoCreateValidator;
    private final CertificateSortRequestDtoValidator certificateSortRequestDtoValidator;
    private final FilterRequestMapper filterRequestMapper;
    private final CertificateMapper certificateMapper;
    private final SortRequestMapper sortRequestMapper;

    @Autowired
    public CertificateController(CertificateService certificateService,
                                 CertificateDtoUpdateValidator certificateDtoUpdateValidator,
                                 CertificateDtoCreateValidator certificateDtoCreateValidator,
                                 CertificateSortRequestDtoValidator certificateSortRequestDtoValidator,
                                 FilterRequestMapper filterRequestMapper,
                                 CertificateMapper certificateMapper,
                                 SortRequestMapper sortRequestMapper) {
        this.certificateService = certificateService;
        this.certificateDtoUpdateValidator = certificateDtoUpdateValidator;
        this.certificateDtoCreateValidator = certificateDtoCreateValidator;
        this.certificateSortRequestDtoValidator = certificateSortRequestDtoValidator;
        this.filterRequestMapper = filterRequestMapper;
        this.certificateMapper = certificateMapper;
        this.sortRequestMapper = sortRequestMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CertificateDto> getCertificateById(
            @PathVariable("id")
            @Min(value = MIN_ID, message = MSG_CODE_ID_INVALID) Long id) {
        Certificate certificate = certificateService.findById(id);
        CertificateDto certificateDto = certificateMapper.map(certificate);
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
        SortRequest sortRequest = sortRequestMapper.map(sortRequestDto);
        FilterRequest filterRequest = filterRequestMapper.map(filterRequestDto);
        List<Certificate> result = certificateService.findAll(sortRequest, filterRequest);
        List<CertificateDto> resultDto = result
                .stream()
                .map(certificateMapper::map)
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
        Certificate certificate = certificateMapper.map(certificateDto);
        Certificate created = certificateService.create(certificate);
        CertificateDto createdDto = certificateMapper.map(created);
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

}
