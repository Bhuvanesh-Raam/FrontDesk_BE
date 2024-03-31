package com.example.FrontDesk_BE.service;

import com.example.FrontDesk_BE.constants.ApplicationConstants;
import com.example.FrontDesk_BE.dto.AccessoryDto;
import com.example.FrontDesk_BE.dto.VisitorDto;
import com.example.FrontDesk_BE.entity.*;
import com.example.FrontDesk_BE.model.visitorExcelModel;
import com.example.FrontDesk_BE.repository.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VisitorService {

    private final VisitorRepository visitorRepository;
    private final TempIDCardRepository tempIDCardRepository;
    private final VisitorSignatureRepository visitorSignatureRepository;
    private final AccessoriesRepository accessoriesRepository;

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
        visitorDto.setDisplayId(ApplicationConstants.VIS_ID + visitor.getId());
        visitorDto.setIssueDate(visitor.getIssueDate());
        visitorDto.setInTime(visitor.getInTime());
        visitorDto.setVisitorName(visitor.getVisitorName());
        visitorDto.setEmpName(visitor.getEmpName());
        visitorDto.setOutTime(visitor.getOutTime());
        visitorDto.setReturnDate(visitor.getReturnDate());
        visitorDto.setClockedOutStatus(visitor.getClockedOutStatus());
        if (visitor.getTempIdCard() != null && visitor.getTempIdCard().getIdName() != null) {
            visitorDto.setTempIdName(visitor.getTempIdCard().getIdName());
        } else {
            visitorDto.setTempIdName(null);
        }
        return visitorDto;
    }

    @Transactional
    public ResponseEntity<String> saveVisitor(VisitorDto visitorDto) {
        try {
            Visitor visitor = new Visitor();
            visitor.setVisitorName(visitorDto.getVisitorName());
            visitor.setVisitorType(visitorDto.getVisitorType());
            visitor.setIssueDate(visitorDto.getIssueDate());
            visitor.setInTime(visitorDto.getInTime());
            visitor.setIdReturnStatus(false);
            visitor.setClockedOutStatus(false);
            visitor.setContactNumber(visitorDto.getContactNumber());

            if (visitorDto.getPurposeOfVisit() != null) {
                visitor.setPurposeOfVisit(visitorDto.getPurposeOfVisit());
            }

            if (visitorDto.getVisitEmployee() != null && visitorDto.getVisitEmployee()) {
                visitor.setEmpName(visitorDto.getEmpName());
                visitor.setEmpId(visitorDto.getEmpId());
            }

            visitor.setIdIssuer(visitorDto.getIdIssuer());

            if (visitorDto.getTempIdIssued() != null && visitorDto.getTempIdIssued()) {
                Optional<TempIDCard> tempOpt = tempIDCardRepository.findById(visitorDto.getTempId());
                if (tempOpt.isPresent() && !tempOpt.get().getInUse()) {
                    TempIDCard tempIDCard = tempOpt.get();
                    visitor.setTempIdCard(tempIDCard);
                    tempIDCard.setInUse(true);
                    tempIDCardRepository.save(tempIDCard);
                } else {
                    return ResponseEntity.ok("Failure: Temp ID Card not found");
                }
            }

            visitor = visitorRepository.save(visitor);

            if (visitorDto.getHasAccessories() && visitorDto.getAccessories() != null
                    && !visitorDto.getAccessories().isEmpty()) {
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

                accessoriesRepository.saveAll(accessories);
                visitor.setAccessories(accessories);
            }

            String[] visitorSignArray = visitorDto.getVisitorSign().split(",");
            String[] issuerSignArray = visitorDto.getIssuerSign().split(",");
            String[] imgCaptureArray = visitorDto.getImgCapture().split(",");

            VisitorSignature visitorSignature = new VisitorSignature();
            visitorSignature.setVisitorFileType(visitorSignArray[0]);
            visitorSignature.setIssuerFileType(issuerSignArray[0]);
            visitorSignature.setImgFileType(imgCaptureArray[0]);
            visitorSignature.setVisitorSign(Base64.decodeBase64(visitorSignArray[1]));
            visitorSignature.setIssuerSign(Base64.decodeBase64(issuerSignArray[1]));
            visitorSignature.setImgCapture(Base64.decodeBase64(imgCaptureArray[1]));
            visitorSignature.setVisitor(visitor);

            visitorSignatureRepository.save(visitorSignature);

            return ResponseEntity.ok("Success");
        } catch (

        Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failure: Internal Server Error");
        }
    }

    public ResponseEntity<String> editVisitor(VisitorDto visitorDto) {
        Long id = visitorDto.getId();

        if (id == null) {
            return ResponseEntity.badRequest().body("Visitor ID is required.");
        }

        Optional<Visitor> optionalVisitor = visitorRepository.findById(id);
        if (optionalVisitor.isPresent()) {
            Visitor visitor = optionalVisitor.get();

            if (visitorDto.getVisitorName() != null) {
                visitor.setVisitorName(visitorDto.getVisitorName());
            }

            if (visitorDto.getVisitorType() != null) {
                visitor.setVisitorType(visitorDto.getVisitorType());
            }

            if (visitorDto.getIssueDate() != null) {
                visitor.setIssueDate(visitorDto.getIssueDate());
            }

            if (visitorDto.getInTime() != null) {
                visitor.setInTime(visitorDto.getInTime());
            }

            if (visitorDto.getContactNumber() != null) {
                visitor.setContactNumber(visitorDto.getContactNumber());
            }

            if (visitorDto.getPurposeOfVisit() != null) {
                visitor.setPurposeOfVisit(visitorDto.getPurposeOfVisit());
            }

            if (visitorDto.getVisitEmployee() != null) {
                boolean visitEmployee = visitorDto.getVisitEmployee();
                if (visitEmployee) {
                    if (visitorDto.getEmpName() != null && !visitorDto.getEmpName().equals(visitor.getEmpName())) {
                        visitor.setEmpName(visitorDto.getEmpName());
                    }
                    if (visitorDto.getEmpId() != null && !visitorDto.getEmpId().equals(visitor.getEmpId())) {
                        visitor.setEmpId(visitorDto.getEmpId());
                    }
                }
            }

            if (visitorDto.getTempIdIssued() != null && visitorDto.getTempIdIssued()) {
                if (visitorDto.getTempId() != null) {
                    Optional<TempIDCard> tempOpt = tempIDCardRepository.findById(visitorDto.getTempId());
                    if (tempOpt.isPresent()) {
                        TempIDCard tempIDCard = tempOpt.get();
                        if (!tempIDCard.equals(visitor.getTempIdCard())) {
                            if (!tempIDCard.getInUse()) {
                                visitor.setTempIdCard(tempIDCard);
                                tempIDCard.setInUse(true);
                                tempIDCardRepository.save(tempIDCard);
                            } else {
                                return ResponseEntity.ok("Failure: Temp ID Card is already in use");
                            }
                        } else {
                            // If the tempIdCard is the same, skip the update
                            // but continue with the rest of the updates
                        }
                    } else {
                        return ResponseEntity.ok("Failure: Temp ID Card not found");
                    }
                } else {
                    return ResponseEntity.ok("Failure: Temp ID Card not provided/null");
                }
            } else if (visitorDto.getTempIdIssued() != null && !visitorDto.getTempIdIssued()
                    && visitor.getTempIdCard() != null) {
                visitor.getTempIdCard().setInUse(false);
                visitor.setTempIdCard(null);
            }

            if (visitorDto.getIdIssuer() != null) {
                visitor.setIdIssuer(visitorDto.getIdIssuer());
            }

            if (visitorDto.getHasAccessories() != null && visitorDto.getHasAccessories()) {
                visitor.getAccessories().clear();

                // Re-add accessories based on the incoming VisitorDto
                List<Accessory> accessories = visitorDto.getAccessories().stream()
                        .map(dto -> {
                            Accessory accessory = new Accessory();
                            accessory.setType(dto.getType());
                            accessory.setName(dto.getName());
                            accessory.setSerialNumber(dto.getSerialNo());
                            accessory.setQuantity(dto.getQuantity());
                            accessory.setVisitor(visitor); // Set the visitor to maintain the relationship
                            return accessory;
                        }).collect(Collectors.toList());

                // Save the new set of accessories
                accessoriesRepository.saveAll(accessories);
                visitor.setAccessories(accessories);
            } else if (visitorDto.getHasAccessories() != null && !visitorDto.getHasAccessories()) {
                visitor.getAccessories().clear();
            }

            visitorRepository.save(visitor);
            return ResponseEntity.ok("Visitor updated successfully");
        } else {
            return ResponseEntity.ok("Visitor not found, please check the ID number");
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
            visitorDto.setPurposeOfVisit(visitor.get().getPurposeOfVisit());
            visitorDto.setVisitEmployee(visitor.get().getEmpId() != null);
            visitorDto.setHasAccessories(visitor.get().getAccessories() != null);
            visitorDto.setTempIdIssued(visitor.get().getTempIdCard() != null);
            visitorDto.setEmpName(visitor.get().getEmpName());
            visitorDto.setEmpId(visitor.get().getEmpId());
            visitorDto.setOutTime(visitor.get().getOutTime());
            visitorDto.setReturnDate(visitor.get().getReturnDate());
            visitorDto.setIdIssuer(visitor.get().getIdIssuer());
            visitorDto.setEmployeeID(visitor.get().getEmpId() != null ? visitor.get().getEmpId().toString() : null);
            if (visitor.get().getTempIdCard() != null) {
                visitorDto.setTempId(visitor.get().getTempIdCard().getId());
                visitorDto.setTempIdName(visitor.get().getTempIdCard().getIdName());
            } else {
                visitorDto.setTempId(null);
                visitorDto.setTempIdName(null);
            }
            String issuerSignBase64 = Base64.encodeBase64String(visitor.get().getVisitorSignature().getIssuerSign());
            String visitorSignBase64 = Base64.encodeBase64String(visitor.get().getVisitorSignature().getVisitorSign());
            String imgCaptureBase64 = Base64.encodeBase64String(visitor.get().getVisitorSignature().getImgCapture());

            visitorDto
                    .setVisitorSign(visitor.get().getVisitorSignature().getVisitorFileType() + "," + visitorSignBase64);
            visitorDto.setIssuerSign(visitor.get().getVisitorSignature().getIssuerFileType() + "," + issuerSignBase64);
            visitorDto.setImgCapture(visitor.get().getVisitorSignature().getImgFileType() + "," + imgCaptureBase64);
            visitorDto.setClockedOutStatus(visitor.get().getClockedOutStatus());
            visitorDto.setVisitEmployee(visitor.get().getEmpId() != null);
            visitorDto.setAccessories(
                    visitor.get().getAccessories().stream().map(this::accessoryDto).collect(Collectors.toList()));
            return visitorDto;
        } else {
            return null;
        }
    }

    public ResponseEntity<String> clockoutVisitor(VisitorDto visitorDto) {
        Long id = visitorDto.getId();
        LocalTime outTime = visitorDto.getOutTime();
        LocalDate returnDate = visitorDto.getReturnDate();

        if (id == null || outTime == null || returnDate == null) {
            return ResponseEntity.badRequest().body("Out time and Return date must not be empty.");
        }

        Optional<Visitor> optionalVisitor = visitorRepository.findById(id);
        if (optionalVisitor.isPresent()) {
            Visitor visitor = optionalVisitor.get();
            if (visitor.getTempIdCard() != null) {
                visitor.setOutTime(outTime);
                visitor.setReturnDate(returnDate);
                TempIDCard tempIDCard = visitor.getTempIdCard();
                tempIDCard.setInUse(false);
                tempIDCardRepository.save(tempIDCard);
            } else {
                visitor.setOutTime(outTime);
                visitor.setReturnDate(returnDate);
            }
            visitor.setIdReturnStatus(true);
            visitor.setClockedOutStatus(true);
            visitorRepository.save(visitor);
            return ResponseEntity.ok("Success");
        } else {
            return ResponseEntity.ok("Failure: Visitor not found");
        }
    }

    @Transactional
    public List<visitorExcelModel> getVisitorDataForExcel(LocalDate startDate,
            LocalDate endDate) {
        List<Visitor> visitors = visitorRepository.findAllByIssueDateBetween(startDate, endDate);
        List<visitorExcelModel> visitorExcelModelList = new ArrayList<>();
        for (Visitor visitor : visitors) {
            visitorExcelModel visitorModel = new visitorExcelModel();
            visitorModel.setDisplayId(ApplicationConstants.VIS_ID + visitor.getId());
            visitorModel.setIssueDate(visitor.getIssueDate());
            visitorModel.setVisitorName(visitor.getVisitorName());
            visitorModel.setVisitorType(visitor.getVisitorType());
            visitorModel.setContactNo(visitor.getContactNumber());
            visitorModel.setIdIssuer(visitor.getIdIssuer());
            visitorModel.setEmployeeVisited(visitor.getEmpName());
            visitorModel.setClockoutDate(visitor.getReturnDate());
            visitorModel.setInTime(visitor.getInTime());
            visitorModel.setOutTime(visitor.getOutTime());
            visitorModel.setTempIdIssued(visitor.getTempIdCard().getIdName());
            visitorExcelModelList.add(visitorModel);
        }
        return visitorExcelModelList;
    }

    @Transactional
    public Page<VisitorDto> filterByIDorNameAndClockedOutStatus(String searchParam, Boolean clockedOutStatusParam,
            Pageable pageable) {
        Long visitorId = null;
        Page<Visitor> visitorPage = null;
        String searchString = searchParam.trim();
        try {
            visitorId = Long.parseLong(searchString);
            String visitorIdPattern = "%" + visitorId + "%";
            visitorPage = visitorRepository.findByPartialVisitorIdAndClockedOutStatus(visitorIdPattern,
                    clockedOutStatusParam,
                    pageable);
        } catch (NumberFormatException e) {
            visitorPage = visitorRepository.findByVisitorNameContainingIgnoreCaseAndClockedOutStatus(searchString,
                    clockedOutStatusParam, pageable);
        }
        List<VisitorDto> visitorDtoList = visitorPage.getContent().stream().map(this::listDto)
                .collect(Collectors.toList());
        return new PageImpl<>(visitorDtoList, pageable, visitorPage.getTotalElements());
    }

    @Transactional
    public Page<VisitorDto> filterByIDorName(String searchParam, Pageable pageable) {
        Long visitorId = null;
        Page<Visitor> visitorPage = null;
        String searchString = searchParam.trim();
        try {
            visitorId = Long.parseLong(searchString);
            String visitorIdPattern = "%" + visitorId + "%";
            visitorPage = visitorRepository.findByPartialVisitorId(visitorIdPattern, pageable);
        } catch (NumberFormatException e) {
            visitorPage = visitorRepository.findByVisitorNameContainingIgnoreCase(searchString, pageable);
        }
        List<VisitorDto> visitorDtoList = visitorPage.getContent().stream().map(this::listDto)
                .collect(Collectors.toList());
        return new PageImpl<>(visitorDtoList, pageable, visitorPage.getTotalElements());
    }

    @Transactional
    public Page<VisitorDto> filterByClockedOutStatus(Boolean clockedOutStatusParam, Pageable pageable) {
        Page<Visitor> visitorPage = visitorRepository.findByClockedOutStatus(clockedOutStatusParam, pageable);
        List<VisitorDto> visitorDtoList = visitorPage.getContent().stream().map(this::listDto)
                .collect(Collectors.toList());
        return new PageImpl<>(visitorDtoList, pageable, visitorPage.getTotalElements());
    }
}
