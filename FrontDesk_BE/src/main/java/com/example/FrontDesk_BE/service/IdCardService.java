package com.example.FrontDesk_BE.service;

import com.example.FrontDesk_BE.constants.ApplicationConstants;
import com.example.FrontDesk_BE.dto.IdCardDto;
import com.example.FrontDesk_BE.entity.IDCard;
import com.example.FrontDesk_BE.entity.TempIDCard;
import com.example.FrontDesk_BE.repository.IdCardSignatureRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import com.example.FrontDesk_BE.entity.IdCardSignature;
import com.example.FrontDesk_BE.entity.TempIDCard;
import com.example.FrontDesk_BE.repository.IdCardRepository;
import com.example.FrontDesk_BE.repository.TempIDCardRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IdCardService {
    private final IdCardRepository idCardRepository;
    private final TempIDCardRepository tempIDCardRepository;
    private final IdCardSignatureRepository idCardSignatureRepository;

    public Page<IDCard> getIdCardList(Pageable pageable) {
        if (pageable.getSort().isUnsorted()) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, ApplicationConstants.LAST_UPDATED_DATE));
        }
        return idCardRepository.findAll(pageable);
    }

    public Page<IdCardDto> getIdCardDtoList(Pageable pageable){
        if (pageable.getSort().isUnsorted()) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, ApplicationConstants.LAST_UPDATED_DATE));
        }
        return idCardRepository.findAll(pageable).map(this::convertToDto);
    }
    private IdCardDto convertToDto(IDCard idCard){
        IdCardDto idCardDto=new IdCardDto();
        idCardDto.setId(idCard.getId());
        idCardDto.setIssueDate(idCard.getIssueDate());
        idCardDto.setInTime(idCard.getInTime());
        idCardDto.setEmpName(idCard.getEmpName());
        idCardDto.setEmpId(idCard.getEmpId());
        idCardDto.setOutTime(idCard.getOutTime());
        idCardDto.setReturnDate(idCard.getReturnDate());
        idCardDto.setIdIssuer(idCard.getIdIssuer());
        idCardDto.setTempId(idCard.getTempIdCard().getId());

        String issuerSignBase64=Base64.encodeBase64String(idCard.getIdCardSignature().getImgCapture());
        String receiverSignBase64=Base64.encodeBase64String(idCard.getIdCardSignature().getImgCapture());
        String imgCaptureBase64=Base64.encodeBase64String(idCard.getIdCardSignature().getImgCapture());
        idCardDto.setIssuerSign(idCard.getIdCardSignature().getIssuerFileType()+","+issuerSignBase64);
        idCardDto.setReceiverSign(idCard.getIdCardSignature().getReceiverFileType()+","+receiverSignBase64);
        idCardDto.setImgCapture(idCard.getIdCardSignature().getImgFileType()+","+imgCaptureBase64);
        return idCardDto;
    }


    public ResponseEntity<String> saveIdCard(IdCardDto idCardDto) {
        try {
             if (idCardDto.getTempId() != null) {
                Long tempId = idCardDto.getTempId();
                Optional<TempIDCard> tempOpt= tempIDCardRepository.findById(tempId);
                if (tempOpt.isPresent()) {
                    TempIDCard tempIDCard = tempOpt.get();
                    IDCard idCard=new IDCard();
                    idCard.setEmpId(idCardDto.getEmpId());
                    idCard.setEmpName(idCardDto.getEmpName());
                    idCard.setIdIssuer(idCardDto.getIdIssuer());
                    idCard.setInTime(idCardDto.getInTime());
                    idCard.setOutTime(idCardDto.getOutTime());
                    idCard.setIssueDate(idCardDto.getIssueDate());
                    idCard.setReturnDate(idCardDto.getReturnDate());
                    idCard.setTempIdCard(tempIDCard);
                    String [] receiverSignArray=idCardDto.getReceiverSign().split(","); //Get the IdcardDto Sign String and split
                    String [] issuerSignArray=idCardDto.getIssuerSign().split(",");
                    String [] imgCaptureArray=idCardDto.getImgCapture().split(",");
                    IdCardSignature idCardSignature=new IdCardSignature();
                    idCardSignature.setReceiverFileType(receiverSignArray[0]);
                    idCardSignature.setIssuerFileType(issuerSignArray[0]);
                    idCardSignature.setImgFileType(imgCaptureArray[0]);
                    idCardSignature.setReceiverSign(Base64.decodeBase64(receiverSignArray[1]));
                    idCardSignature.setIssuerSign(Base64.decodeBase64(issuerSignArray[1]));
                    idCardSignature.setImgCapture(Base64.decodeBase64(imgCaptureArray[1]));
                    idCardSignature.setIdCard(idCard);
                    idCard = idCardRepository.save(idCard);
                    idCardSignatureRepository.save(idCardSignature);
                    tempIDCard.setInUse(true);
                    tempIDCardRepository.save(tempIDCard);
                    return ResponseEntity.ok("Success");
                } else {
                    return ResponseEntity.ok("Failure: Temp ID Card not found");
                }
            } else {
                return ResponseEntity.ok("Failure: Temp ID Card not provided");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failure: Internal Server Error");
        }
    }

    public List<IDCard> getAll() {
        return idCardRepository.findAll();
    }
}
