package com.example.FrontDesk_BE.service;

import com.example.FrontDesk_BE.constants.ApplicationConstants;
import com.example.FrontDesk_BE.constants.ErrorCode;
import com.example.FrontDesk_BE.constants.ErrorMessages;
import com.example.FrontDesk_BE.dto.IdCardDto;
import com.example.FrontDesk_BE.entity.IDCard;
import com.example.FrontDesk_BE.entity.TempIDCard;
import com.example.FrontDesk_BE.exception.CustomException;
import com.example.FrontDesk_BE.model.excelModel;
import com.example.FrontDesk_BE.repository.IdCardSignatureRepository;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.example.FrontDesk_BE.entity.IdCardSignature;
import com.example.FrontDesk_BE.repository.IdCardRepository;
import com.example.FrontDesk_BE.repository.TempIDCardRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class IdCardService {
    private final IdCardRepository idCardRepository;
    private final TempIDCardRepository tempIDCardRepository;
    private final IdCardSignatureRepository idCardSignatureRepository;

    public IdCardDto getIdCard(Long id) {
        Optional<IDCard> idCardOptional = idCardRepository.findById(id);
        if (idCardOptional.isPresent()) {
            IDCard idCard = idCardOptional.get();
            return convertToDto(idCard);
        } else {
            return null;// Handle exception here
        }
    }

    @Transactional
    public Page<IdCardDto> getIdCardDtoList(String searchParam,Pageable pageable) {

        Page<IDCard> returnStatusFalsePage = idCardRepository.findByReturnStatus(false, pageable);
        Page<IDCard> returnStatusTruePage = idCardRepository.findByReturnStatus(true, pageable);

        List<IdCardDto> idCardDtoList = Stream.concat(
                returnStatusFalsePage.getContent().stream().map(this::listDto),
                returnStatusTruePage.getContent().stream().map(this::listDto)
        ).collect(Collectors.toList());

        return new PageImpl<>(idCardDtoList, pageable,
                returnStatusFalsePage.getTotalElements() + returnStatusTruePage.getTotalElements());
    }

    @Transactional
    public Page<IdCardDto>  filterByIDorNameAndReturnStatus(String searchParam, Boolean returnStatus,
            Pageable pageable) {
        Long empId = null;
        Page<IDCard> idCardPage = null;
        String searchString = searchParam.trim();
        try {
            empId = Long.parseLong(searchString);
            String empIdPattern = "%" + empId + "%";
            idCardPage = idCardRepository.findByPartialEmpIdAndReturnStatus(empIdPattern, returnStatus, pageable);
        } catch (NumberFormatException ex) {
            idCardPage = idCardRepository.findByEmpNameContainingIgnoreCaseAndReturnStatus(searchParam, returnStatus,
                    pageable);
        }
        List<IdCardDto> idCardDtoList = idCardPage.stream().map(this::listDto).collect(Collectors.toList());
        return new PageImpl<>(idCardDtoList, pageable, idCardPage.getTotalElements());
    }

    @Transactional
    public Page<IdCardDto> filterByReturnStatus(Boolean returnStatus, Pageable pageable) {
        Page<IDCard> idCardPage = idCardRepository.findByReturnStatus(returnStatus, pageable);
        List<IdCardDto> idCardDtoList = idCardPage.stream().map(this::listDto).collect(Collectors.toList());
        return new PageImpl<>(idCardDtoList, pageable, idCardPage.getTotalElements());

    }

    @Transactional
    public Page<IdCardDto> filterByDate(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Page<IDCard> idCardPage = idCardRepository.findAllByIssueDateBetween(startDate, endDate, pageable);
        List<IdCardDto> idCardDtoList = idCardPage.stream().map(this::listDto).collect(Collectors.toList());
        return new PageImpl<>(idCardDtoList, pageable, idCardPage.getTotalElements());
    }

    @Transactional
    public Page<IdCardDto> filterByIDorName(String searchParam, Pageable pageable) {
        Long empId = null;
        Page<IDCard> idCardPage = null;
        String searchString = searchParam.trim();
        try {
            empId = Long.parseLong(searchString);
            String empIdPattern = "%" + empId + "%";
            idCardPage = idCardRepository.findByPartialEmpId(empIdPattern, pageable);
        } catch (NumberFormatException ex) {
            idCardPage = idCardRepository.findByEmpNameContainingIgnoreCase(searchParam, pageable);
        }
        List<IdCardDto> idCardDtoList = idCardPage.stream().map(this::listDto).collect(Collectors.toList());
        return new PageImpl<>(idCardDtoList, pageable, idCardPage.getTotalElements());
    }

    private IdCardDto convertToDto(IDCard idCard) {
        IdCardDto idCardDto = new IdCardDto();
        idCardDto.setId(idCard.getId());
        idCardDto.setDisplayId(ApplicationConstants.ID_CARD_ID + idCard.getId());
        idCardDto.setIssueDate(idCard.getIssueDate());
        idCardDto.setInTime(idCard.getInTime());
        idCardDto.setEmpName(idCard.getEmpName());
        idCardDto.setEmpId(idCard.getEmpId());
        idCardDto.setOutTime(idCard.getOutTime());
        idCardDto.setReturnDate(idCard.getReturnDate());
        idCardDto.setIdIssuer(idCard.getIdIssuer());
        idCardDto.setTempId(idCard.getTempIdCard().getId());
        idCardDto.setTempIdName(idCard.getTempIdCard().getIdName());
        idCardDto.setReturnStatus(idCard.getReturnStatus());
        String issuerSignBase64 = Base64.encodeBase64String(idCard.getIdCardSignature().getIssuerSign());
        String receiverSignBase64 = Base64.encodeBase64String(idCard.getIdCardSignature().getReceiverSign());
        String imgCaptureBase64 = Base64.encodeBase64String(idCard.getIdCardSignature().getImgCapture());
        idCardDto.setIssuerSign(idCard.getIdCardSignature().getIssuerFileType() + "," + issuerSignBase64);
        idCardDto.setReceiverSign(idCard.getIdCardSignature().getReceiverFileType() + "," + receiverSignBase64);
        idCardDto.setImgCapture(idCard.getIdCardSignature().getImgFileType() + "," + imgCaptureBase64);
        return idCardDto;
    }

    private IdCardDto listDto(IDCard idCard) {
        IdCardDto idCardDto = new IdCardDto();
        idCardDto.setId(idCard.getId());
        idCardDto.setDisplayId(ApplicationConstants.ID_CARD_ID + idCard.getId());
        idCardDto.setIssueDate(idCard.getIssueDate());
        idCardDto.setInTime(idCard.getInTime());
        idCardDto.setEmpName(idCard.getEmpName());
        // idCardDto.setEmpId(idCard.getEmpId());
        idCardDto.setOutTime(idCard.getOutTime());
        idCardDto.setReturnDate(idCard.getReturnDate());
        idCardDto.setReturnStatus(idCard.getReturnStatus());
        idCardDto.setLastUpdatedDate(idCard.getLastUpdatedDate());
        // idCardDto.setIdIssuer(idCard.getIdIssuer());
        idCardDto.setTempIdName(idCard.getTempIdCard().getIdName());
        return idCardDto;
    }

    @Transactional
    public ResponseEntity<String> saveIdCard(IdCardDto idCardDto) {
        try {
            if (idCardDto.getTempId() != null) {
                Long tempId = idCardDto.getTempId();
                Optional<TempIDCard> tempOpt = tempIDCardRepository.findById(tempId);
                if (tempOpt.isPresent()) {
                    TempIDCard tempIDCard = tempOpt.get();
                    IDCard idCard = new IDCard();
                    idCard.setEmpId(idCardDto.getEmpId());
                    idCard.setEmpName(idCardDto.getEmpName());
                    idCard.setIdIssuer(idCardDto.getIdIssuer());
                    idCard.setInTime(idCardDto.getInTime());
                    /* idCard.setOutTime(idCardDto.getOutTime()); */
                    idCard.setIssueDate(idCardDto.getIssueDate());
                    /* idCard.setReturnDate(idCardDto.getReturnDate()); */
                    idCard.setReturnStatus(false);
                    idCard.setTempIdCard(tempIDCard);
                    String[] receiverSignArray = idCardDto.getReceiverSign().split(","); // Get the IdcardDto Sign
                                                                                         // String and split
                    String[] issuerSignArray = idCardDto.getIssuerSign().split(",");
                    String[] imgCaptureArray = idCardDto.getImgCapture().split(",");
                    IdCardSignature idCardSignature = new IdCardSignature();
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
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failure: Internal Server Error");
            throw new CustomException(ErrorCode.ID_CARD_INTERNAL_ERROR, ErrorMessages.ID_CARD_INTERNAL_ERROR);
        }
    }

    public ResponseEntity<String> returnIdCard(IdCardDto idCardDto) {
      try
      {
        Long id = idCardDto.getId();
        LocalDate returnDate = idCardDto.getReturnDate();
        LocalTime outTime = idCardDto.getOutTime();
        if (returnDate == null || outTime == null) {
            return ResponseEntity.badRequest().body("Return date and Out time must not be empty.");
        }

        Optional<IDCard> optIdCard = idCardRepository.findById(id);
        if (optIdCard.isPresent()) {
            IDCard idCard = optIdCard.get();
            idCard.setReturnDate(idCardDto.getReturnDate());
            idCard.setOutTime(idCardDto.getOutTime());
            idCard.setReturnStatus(true);
            TempIDCard tempIDCard = idCard.getTempIdCard();
            tempIDCard.setInUse(false);
            tempIDCardRepository.save(tempIDCard);
            idCardRepository.save(idCard);

            return ResponseEntity.ok("Success");
        }
        else {
            return ResponseEntity.ok("Failure: ID Card not found");
        }
      }
          catch(Exception e){
              throw new CustomException(ErrorCode.ID_CARD_RETURN_ERROR,ErrorMessages.ID_CARD_RETURN_ERROR);
          }
    }

    public ResponseEntity<String> editIdCard(IdCardDto idCardDto) {
     try {
         Long id = idCardDto.getId();
         if (id == null) {
             return ResponseEntity.badRequest().body("IDCard-Number should not be left as empty.");
         }
         Optional<IDCard> optIdCard = idCardRepository.findById(id);
         if (!optIdCard.isPresent()) {
             return ResponseEntity.ok("IDCard not found, check the ID number");
         }
         IDCard idCard = optIdCard.get();
         boolean isUpdated = false;

         // Check and update issueDate if it's not null
         if (idCardDto.getIssueDate() != null) {
             idCard.setIssueDate(idCardDto.getIssueDate());
             isUpdated = true;
         }

         // Check and update inTime if it's not null
         if (idCardDto.getInTime() != null) {
             idCard.setInTime(idCardDto.getInTime());
             isUpdated = true;
         }

         // Check and update TempId if it's not null
         if (idCardDto.getTempId() != null) {
             Optional<TempIDCard> temp = tempIDCardRepository.findById(idCardDto.getTempId());
             if (!temp.isPresent()) {
                 return ResponseEntity.ok("Failure Temp Id not found");
             }
             TempIDCard tempCard = temp.get();
             // Set the previous TempIDCard inUse to false if it's different
             if (!idCard.getTempIdCard().getId().equals(tempCard.getId())) {
                 idCard.getTempIdCard().setInUse(false);
                 tempCard.setInUse(true);
                 tempIDCardRepository.save(idCard.getTempIdCard());
             }
             idCard.setTempIdCard(tempCard);
             isUpdated = true;
         }

         if (isUpdated) {
             idCardRepository.save(idCard);
             return ResponseEntity.ok("Success");
         } else {
             return ResponseEntity.ok("No changes detected to update.");
         }
     }
     catch (Exception e)
     {
         throw new CustomException(ErrorCode.ID_CARD_EDIT_ERROR,ErrorMessages.ID_CARD_EDIT_ERROR);
     }
    }

    @Transactional
    public List<excelModel> getDataForExcel(LocalDate startDate, LocalDate endDate) {
        List<IDCard> idCards = idCardRepository.findAllByIssueDateBetween(startDate, endDate);
        List<excelModel> excelModelList = new ArrayList<>();
        for (IDCard idCard : idCards) {
            excelModel model = new excelModel();
            model.setDisplayId(ApplicationConstants.ID_CARD_ID + idCard.getId());
            model.setEmpName(idCard.getEmpName());
            model.setIdIssuer(idCard.getIdIssuer());
            model.setIssueDate(idCard.getIssueDate());
            model.setReturnDate(idCard.getReturnDate());
            model.setInTime(idCard.getInTime());
            model.setOutTime(idCard.getOutTime());
            model.setTempIdName(idCard.getTempIdCard().getIdName());
            excelModelList.add(model);
        }
        return excelModelList;
    }

}
