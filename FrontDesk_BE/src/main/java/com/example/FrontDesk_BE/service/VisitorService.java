package com.example.FrontDesk_BE.service;

import com.example.FrontDesk_BE.constants.ApplicationConstants;
import com.example.FrontDesk_BE.dto.AccessoryDto;
import com.example.FrontDesk_BE.dto.VisitorDto;
import com.example.FrontDesk_BE.entity.*;
import com.example.FrontDesk_BE.repository.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VisitorService {

    private final VisitorRepository visitorRepository;
    private final TempIDCardRepository tempIDCardRepository;
    private final VisitorSignatureRepository visitorSignatureRepository;

    private AccessoryDto accessoryDto(Accessory accessory) {
    AccessoryDto accessoryDto = new AccessoryDto();
    accessoryDto.setType(accessory.getType());
    accessoryDto.setName(accessory.getName());
    accessoryDto.setSerialNo(accessory.getSerialNumber());
    accessoryDto.setQuantity(accessory.getQuantity());
    return accessoryDto;
}

    public Page<VisitorDto> getVisitorDtoList(Pageable pageable) {
        return visitorRepository.findAll(pageable).map(this::listDto);
    }

    private VisitorDto listDto(Visitor visitor) {
        VisitorDto visitorDto = new VisitorDto();
        visitorDto.setId(visitor.getId());
        visitorDto.setDisplayId(ApplicationConstants.ID_CARD_ID + visitor.getId());
        visitorDto.setIssueDate(visitor.getIssueDate());
        visitorDto.setInTime(visitor.getInTime());
        visitorDto.setVisitorName(visitor.getVisitorName());
        visitorDto.setOutTime(visitor.getOutTime());
        visitorDto.setReturnDate(visitor.getReturnDate());
        visitorDto.setReturnStatus(visitor.getIdReturnStatus());
        visitorDto.setLastUpdatedDate(visitor.getLastUpdatedDate());
        visitorDto.setTempIdName(visitor.getTempIdCard().getIdName());
        return visitorDto;
    }

    @Transactional
    public ResponseEntity<String> saveVisitor(VisitorDto visitorDto) {
        try {
                    Visitor visitor=new Visitor();
                    visitor.setVisitorName(visitorDto.getVisitorName());
                    visitor.setVisitorType(visitorDto.getVisitorType());

                    visitor.setIssueDate(visitorDto.getIssueDate());
                    visitor.setInTime(visitorDto.getInTime());
                    visitor.setIdReturnStatus(false);
                    visitor.setClockedOutStatus(false);

                    if (visitorDto.getVisitEmployee() != null && visitorDto.getVisitEmployee()) {
                        visitor.setEmpName(visitorDto.getEmpName());
                        visitor.setEmpId(visitorDto.getEmpId());
                    }

                    visitor.setIdIssuer(visitorDto.getIdIssuer());

                    if (visitorDto.getTempId() != null) {
                        Optional<TempIDCard> tempOpt = tempIDCardRepository.findById(visitorDto.getTempId());
                        if (tempOpt.isPresent()) {
                            TempIDCard tempIDCard = tempOpt.get();
                            visitor.setTempIdCard(tempIDCard);
                            tempIDCard.setInUse(true);
                            tempIDCardRepository.save(tempIDCard);
                        } else {
                            return ResponseEntity.ok("Failure: Temp ID Card not found");
                        }
                    }

                    if (visitorDto.getAccessories() != null && !visitorDto.getAccessories().isEmpty()) {
                        Visitor finalVisitor = visitor;
                        List<Accessory> accessories = visitorDto.getAccessories().stream()
                                .map(dto -> {
                                    Accessory accessory = new Accessory();
                                    accessory.setType(dto.getType());
                                    accessory.setName(dto.getName());
                                    accessory.setSerialNumber(dto.getSerialNo());
                                    accessory.setQuantity(dto.getQuantity());
                                    accessory.setVisitor(finalVisitor);
                                    return accessory;
                                }).collect(Collectors.toList());
                        visitor.setAccessories(accessories);
                    }

                    String[] receiverSignArray = visitorDto.getReceiverSign().split(",");
                    String[] issuerSignArray = visitorDto.getIssuerSign().split(",");
                    String[] imgCaptureArray = visitorDto.getImgCapture().split(",");

                    VisitorSignature visitorSignature=new VisitorSignature();

                    visitorSignature.setReceiverFileType(receiverSignArray[0]);
                    visitorSignature.setIssuerFileType(issuerSignArray[0]);
                    visitorSignature.setImgFileType(imgCaptureArray[0]);
                    visitorSignature.setReceiverSign(Base64.decodeBase64(receiverSignArray[1]));
                    visitorSignature.setIssuerSign(Base64.decodeBase64(issuerSignArray[1]));
                    visitorSignature.setImgCapture(Base64.decodeBase64(imgCaptureArray[1]));
                    visitorSignature.setVisitor(visitor);

                    visitor = visitorRepository.save(visitor);
                    visitorSignatureRepository.save(visitorSignature);

                    return ResponseEntity.ok("Success");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failure: Internal Server Error");
        }
    }

    public ResponseEntity<String> editVisitor(VisitorDto visitorDto) {
        Long id = visitorDto.getId();
        LocalDate issueDate = visitorDto.getIssueDate();
        LocalTime inTime = visitorDto.getInTime();
        Long tempId = visitorDto.getTempId();

        if (id == null || issueDate == null || inTime == null || tempId == null) {
            return ResponseEntity.badRequest()
                    .body("IDCard-Number, Issue-Date, In-Time & Temp-Card should not be left as empty.");
        }
        Optional<Visitor> optionalVisitor = visitorRepository.findById(id);
        if (optionalVisitor.isPresent()) {
            Visitor visitor = optionalVisitor.get();
            visitor.setIssueDate(visitorDto.getIssueDate());
            visitor.setInTime(visitorDto.getInTime());
            TempIDCard tempIDCard = visitor.getTempIdCard();
            tempIDCard.setInUse(false);
            Optional<TempIDCard> temp = tempIDCardRepository.findById(tempId);
            if (temp.isPresent()) {
                TempIDCard tempCard = temp.get();
                visitor.setTempIdCard(tempCard);
                tempCard.setInUse(true);
                visitorRepository.save(visitor);
                tempIDCardRepository.save(tempIDCard);
                return ResponseEntity.ok("Success");
            } else {
                return ResponseEntity.ok("Failure Temp Id not found");
            }
        } else {
            return ResponseEntity.ok("IDCard not found, check the ID number");
        }
    }

    public VisitorDto getVisitor(Long id) {
        Optional<Visitor> visitor = visitorRepository.findById(id);
        if (visitor.isPresent()) {
            VisitorDto visitorDto = new VisitorDto();
            visitorDto.setId(visitor.get().getId());
            visitorDto.setVisitorName(visitor.get().getVisitorName());
            visitorDto.setVisitorType(visitor.get().getVisitorType());
            visitorDto.setIssueDate(visitor.get().getIssueDate());
            visitorDto.setInTime(visitor.get().getInTime());
            visitorDto.setContactNumber(visitor.get().getContactNumber());
            visitorDto.setEmpName(visitor.get().getEmpName());
            visitorDto.setEmpId(visitor.get().getEmpId());
            visitorDto.setOutTime(visitor.get().getOutTime());
            visitorDto.setReturnDate(visitor.get().getReturnDate());
            visitorDto.setIdIssuer(visitor.get().getIdIssuer());
            visitorDto.setTempId(visitor.get().getTempIdCard().getId());
            visitorDto.setTempIdName(visitor.get().getTempIdCard().getIdName());
            visitorDto.setReceiverSign(visitor.get().getVisitorSignature().getReceiverSign().toString());
            visitorDto.setIssuerSign(visitor.get().getVisitorSignature().getIssuerSign().toString());
            visitorDto.setImgCapture(visitor.get().getVisitorSignature().getImgCapture().toString());
            visitorDto.setReturnStatus(visitor.get().getIdReturnStatus());
            visitorDto.setClockedOutStatus(visitor.get().getClockedOutStatus());
            visitorDto.setVisitEmployee(visitor.get().getEmpId() != null);
            visitorDto.setAccessories(visitor.get().getAccessories().stream().map(this::accessoryDto).collect(Collectors.toList()));
            return visitorDto;
        } else {
            return null;
        }
    }
}
