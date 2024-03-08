package com.example.FrontDesk_BE.service;

import com.example.FrontDesk_BE.constants.ApplicationConstants;
import com.example.FrontDesk_BE.dto.IdCardDto;
import com.example.FrontDesk_BE.entity.IDCard;
import com.example.FrontDesk_BE.entity.IdSignature;
import com.example.FrontDesk_BE.entity.TempIDCard;
import com.example.FrontDesk_BE.repository.IdCardRepository;
import com.example.FrontDesk_BE.repository.IdCardSignRepository;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IdCardService {
    private final IdCardRepository idCardRepository;
    private final TempIDCardRepository tempIDCardRepository;
    private final IdCardSignRepository idCardSignRepository;

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
        String issuerSignBase64=Base64.encodeBase64String(idCard.getIssuerSign());
        String receiverSignBase64=Base64.encodeBase64String(idCard.getReceiverSign());
        String imgCaptureBase64=Base64.encodeBase64String(idCard.getImgCapture());
        idCardDto.setIssuerSign(idCard.getIssuerFileType()+","+issuerSignBase64);
        idCardDto.setReceiverSign(idCard.getReceiverFileType()+","+receiverSignBase64);
        idCardDto.setImgCapture(idCard.getImgFileType()+","+imgCaptureBase64);
        return idCardDto;
    }


    public ResponseEntity<String> saveIdCard(IdCardDto idCardDto) {
        try {
            IDCard idCard = new IDCard();
            idCard.setEmpId(idCardDto.getEmpId());
            idCard.setEmpName(idCardDto.getEmpName());
            idCard.setIdIssuer(idCardDto.getIdIssuer());
            idCard.setInTime(idCardDto.getInTime());
            idCard.setOutTime(idCardDto.getOutTime());
            idCard.setIssueDate(idCardDto.getIssueDate());
            idCard.setReturnDate(idCardDto.getReturnDate());

            // Check for TempID
            if (idCardDto.getTempId() != null) {
                Long tempId = idCardDto.getTempId();
                Optional<TempIDCard> tempOptional = tempIDCardRepository.findById(tempId);
                if (tempOptional.isPresent()) {
                    TempIDCard tempIDCard = tempOptional.get();
                    if (!tempIDCard.getInUse()) {
                        // Set TempIDCard for IDCard entity
                        idCard.setTempIdCard(tempIDCard);
                        saveSignature(idCard,idCardDto);

                        // Save IDCard entity
                        idCard = idCardRepository.save(idCard);

                        // Update TempIDCard
                        tempIDCard.setInUse(true);
                        tempIDCardRepository.save(tempIDCard);

                        return ResponseEntity.ok("Success");
                    } else {
                        return ResponseEntity.ok("Failure: Temp ID Card is already in use");
                    }
                } else {
                    return ResponseEntity.ok("Failure: Temp ID Card not found");
                }
            } else {
                return ResponseEntity.ok("Failure: Temp ID Card not provided");
            }
        } catch (Exception e) {
            // Log the exception for debugging
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failure: Internal Server Error");
        }
    }

    public List<IDCard> getAll() {
        return idCardRepository.findAll();
    }

    private void saveSignature(IDCard idCard,IdCardDto idCardDto)
    {
        String [] receiverSignArray=idCardDto.getReceiverSign().split(",");
        String [] issuerSignArray=idCardDto.getIssuerSign().split(",");
        String [] imgCaptureArray=idCardDto.getImgCapture().split(",");
        idCard.setReceiverFileType(receiverSignArray[0]);
        idCard.setIssuerFileType(issuerSignArray[0]);
        idCard.setImgFileType(imgCaptureArray[0]);
        idCard.setReceiverSign(Base64.decodeBase64(receiverSignArray[1]));
        idCard.setIssuerSign(Base64.decodeBase64(issuerSignArray[1]));
        idCard.setImgCapture(Base64.decodeBase64(imgCaptureArray[1]));
    }
}
