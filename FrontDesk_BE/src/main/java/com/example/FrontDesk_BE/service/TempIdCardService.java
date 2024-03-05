package com.example.FrontDesk_BE.service;

import com.example.FrontDesk_BE.dto.TempIDCardDto;
import com.example.FrontDesk_BE.entity.IDCard;
import com.example.FrontDesk_BE.entity.TempIDCard;
import com.example.FrontDesk_BE.repository.IdCardRepository;
import com.example.FrontDesk_BE.repository.TempIDCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TempIdCardService {
    private final TempIDCardRepository tempIDCardRepository;
    private final IdCardRepository idCardRepository;
    public List<TempIDCard> getActiveTempIdCard(){
        /*return tempIDCardRepository.findByInUseFalse();*/
        return tempIDCardRepository.findAll();
    }

   /* public void assignTempIdCard(Long tempIdCardId, Long empId)
    {
        TempIDCard tempId=tempIDCardRepository.findById(tempIdCardId).orElseThrow();
        if(!tempId.getInUse())
        {
            Optional<IDCard> id= idCardRepository.findById(empId);
                if(id.isPresent())
                {
                    IDCard idCard=id.get();
                    idCard.setTempIdCard(tempId);
                    idCardRepository.save(idCard);
                    tempId.setInUse(true);
                    tempIDCardRepository.save(tempId);
                }
        }
    }*/

    public ResponseEntity<String> saveTempIdCard(TempIDCardDto tempIDCardDto)
    {
        try
        {
            TempIDCard tempIDCard=new TempIDCard();
            tempIDCard.setId(tempIDCardDto.getId());
            tempIDCard.setOdcAccessCardNumber(tempIDCardDto.getOdcAccessCardNumber());
            tempIDCard.setSezAccessCardNumber(tempIDCardDto.getSezAccessCardNumber());
            tempIDCard.setIdName(tempIDCardDto.getIdName());
            tempIDCard.setInUse(tempIDCardDto.getInUse());
            tempIDCard=tempIDCardRepository.save(tempIDCard);
            return ResponseEntity.ok("Success");
        }catch (Exception e){
            return ResponseEntity.ok("Failure");
        }

    }

}
