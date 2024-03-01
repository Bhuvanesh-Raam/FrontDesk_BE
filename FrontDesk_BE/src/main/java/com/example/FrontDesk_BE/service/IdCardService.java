package com.example.FrontDesk_BE.service;

import com.example.FrontDesk_BE.constants.ApplicationConstants;
import com.example.FrontDesk_BE.dto.IdCardDto;
import com.example.FrontDesk_BE.entity.IDCard;
import com.example.FrontDesk_BE.repository.IdCardRepository;
import com.example.FrontDesk_BE.repository.TempIDCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IdCardService {
    private final IdCardRepository idCardRepository;
    private final TempIDCardRepository tempIDCardRepository;

    public IDCard getById(Long id) {
        IDCard idCard = idCardRepository.findById(id).orElseThrow();
        return idCard;
    }


    public Page<IDCard> getIdCardList(Pageable pageable) {
        if (pageable.getSort().isUnsorted()) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, ApplicationConstants.ID_CARD_ID));
        }
        return idCardRepository.findAll(pageable);
    }

    public ResponseEntity<String> saveIdCard(IdCardDto idCardDto)
    {

       try{
            IDCard idCard= new IDCard();
            idCard.setEmpId(idCardDto.getEmpId());
            idCard.setEmpName(idCardDto.getEmpName());
            idCard.setIdIssuer(idCardDto.getIdIssuer());
            idCard.setInTime(idCardDto.getInTime());
            idCard.setOutTime(idCardDto.getOutTime());
            idCard.setIssueDate(idCardDto.getIssueDate());
            idCard.setReturnDate(idCardDto.getReturnDate());
            idCard.setTempIdCard(idCardDto.getTempId());
           /*idCard.setTempIdCard(tempIDCardRepository.findById(1L).get());*/
            idCard=idCardRepository.save(idCard);
            return ResponseEntity.ok("Success");
        }catch (Exception e){
            return ResponseEntity.ok("Failure");
        }
    }


    public List<IDCard> getAll() {
        return idCardRepository.findAll();
    }
}
