package com.example.FrontDesk_BE.service;

import com.example.FrontDesk_BE.constants.ApplicationConstants;
import com.example.FrontDesk_BE.dto.IdCardDto;
import com.example.FrontDesk_BE.entity.IDCard;
import com.example.FrontDesk_BE.entity.IdSignature;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IdCardService {
    private final IdCardRepository idCardRepository;
    private final TempIDCardRepository tempIDCardRepository;

    /*public IDCard getById(Long id) {
        IDCard idCard = idCardRepository.findById(id).orElseThrow();
        return idCard;
    }*/


    public Page<IDCard> getIdCardList(Pageable pageable) {
        if (pageable.getSort().isUnsorted()) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, ApplicationConstants.LAST_UPDATED_DATE));
        }
        return idCardRepository.findAll(pageable);
    }

    public ResponseEntity<String> saveIdCard(IdCardDto idCardDto)
    {
       try{
           /* idCard.setTempIdCard(tempIDCardRepository.findById(1L).get());
           TempIDCard tempIDCard1 = tempIDCardRepository.findById(1L).orElseThrow(() -> new RuntimeException());*/
               Long tempId=idCardDto.getTempId();
               Optional<TempIDCard> temp=tempIDCardRepository.findById(tempId);
               if(temp.isPresent())
               {
                   TempIDCard tempIDCard=temp.get();
                   if(!tempIDCard.getInUse())
                   {
                       IDCard idCard= new IDCard();
                       idCard.setEmpId(idCardDto.getEmpId());
                       idCard.setEmpName(idCardDto.getEmpName());
                       idCard.setIdIssuer(idCardDto.getIdIssuer());
                       idCard.setInTime(idCardDto.getInTime());
                       idCard.setOutTime(idCardDto.getOutTime());
                       idCard.setIssueDate(idCardDto.getIssueDate());
                       idCard.setReturnDate(idCardDto.getReturnDate());
                       idCard.setTempIdCard(tempIDCard);
                       saveSignature(idCard,idCardDto);
                       idCard=idCardRepository.save(idCard);
                       tempIDCard.setInUse(true);
                       tempIDCardRepository.save(tempIDCard);
                       return ResponseEntity.ok("Success");
                   }
                   else {
                    return ResponseEntity.ok("Failure: Temp ID Card is already in use");
                    }
               }
               else{
                   return ResponseEntity.ok("Temp ID Card Not found");
               }
        }
       catch (Exception e){
            return ResponseEntity.ok("Failure");
        }
    }

    public List<IDCard> getAll() {
        return idCardRepository.findAll();
    }

   /* public List<IdCardDto> getIdCardList1() {
        return idCardRepository.findAll().stream().map(id -> {
            IdCardDto idCardDto = new IdCardDto();
            idCardDto.setId(id.getId());
            idCardDto.setIssueDate(id.getIssueDate());
            return idCardDto;
        }).collect(Collectors.toList());
    }*/

    private void saveSignature(IDCard idCard,IdCardDto idCardDto)
    {
        String [] receiverSign=idCardDto.getReceiverSign().split(",");
        String [] issuerSign=idCardDto.getIssuerSign().split(",");
        String [] imgCapture=idCardDto.getImgCapture().split(",");
        IdSignature idSignature=new IdSignature().builder().id(idCard.getId())
                .imgCapture(Base64.decodeBase64(imgCapture[1]))
                .issuerSign(Base64.decodeBase64(issuerSign[1]))
                .receiverSign(Base64.decodeBase64(receiverSign[1]))
                .fileType(receiverSign[0])
                .build();
        idCard.setIdSignature(idSignature);
        idCard.setImgCapture(idCardDto.getImgCapture());
        idCardRepository.save(idCard);

    }
}
