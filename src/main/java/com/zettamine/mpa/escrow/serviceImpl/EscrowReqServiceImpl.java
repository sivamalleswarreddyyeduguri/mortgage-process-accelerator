package com.zettamine.mpa.escrow.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.zettamine.mpa.escrow.constants.EscrowReqConstants;
import com.zettamine.mpa.escrow.dto.EscrowReqDto;
import com.zettamine.mpa.escrow.entity.EscrowReq;
import com.zettamine.mpa.escrow.repository.EscrowRequirmentRepository;
import com.zettamine.mpa.escrow.service.EscrowReqService;
import com.zettamine.mpa.escrow.utils.StringUtil;
import com.zettamine.mpa.exception.RequirmentNotPresentException;
import com.zettamine.mpa.mapper.EscrowReqMapper;

import lombok.AllArgsConstructor;

/**
 * Service implementation class for managing Escrow Requirement operations.
 * This service provides methods for retrieving, updating, and saving Escrow Requirements.
 */
@Service
@AllArgsConstructor
public class EscrowReqServiceImpl implements EscrowReqService{
    
    private final EscrowRequirmentRepository escroReqRepo;
    private final EscrowReqMapper reqMapper;
    
    /**
     * Retrieves all Escrow Requirements.
     * 
     * @return List of EscrowReqDto containing all Escrow Requirements.
     */
    @Override
    public List<EscrowReqDto> getAll() {
        List<EscrowReq> escoReqList = escroReqRepo.findAll();
        List<EscrowReqDto> dtoList = reqMapper.toDto(escoReqList);
        return dtoList;
    }

    /**
     * Retrieves an Escrow Requirement by its ID.
     * 
     * @param escId The ID of the Escrow Requirement to retrieve.
     * @return The EscrowReqDto representing the Escrow Requirement, or null if not found.
     */
    @Override
    public EscrowReqDto findById(Integer escId) {
        Optional<EscrowReq> byId = escroReqRepo.findById(escId);
        if(byId.isPresent()) {
            return reqMapper.toDto(byId.get());
        }
        return null;
    }

    /**
     * Updates an existing Escrow Requirement.
     * 
     * @param escReqDto The EscrowReqDto containing updated information about the Escrow Requirement.
     * @param id The ID of the Escrow Requirement to be updated.
     */
	@Override
	public void update(EscrowReqDto escReqDto, Integer id) {
		Optional<EscrowReq> byId = escroReqRepo.findById(id);
		if (byId.isPresent()) {
			EscrowReq escrowReq = byId.get();
			escrowReq.setReqName(escReqDto.getReqName());
			escrowReq.setDescription(escReqDto.getDescription());
			escroReqRepo.save(escrowReq);
		} else {

			throw new RequirmentNotPresentException(
					String.format("%s %s", escReqDto.getReqName(), EscrowReqConstants.NOT_AVAILABLE));
		}
    }
    
    /**
     * Saves a list of new Escrow Requirements.
     * 
     * @param reqList The list of EscrowReqDto containing information about the new Escrow Requirements.
     */
    @Override
    public void save(List<EscrowReqDto> reqList) {
        for(EscrowReqDto requirment : reqList) {
            requirment = StringUtil.formatEscrowReq(requirment);
        }   
        List<EscrowReq> escrowReq = reqMapper.toEntity(reqList);
        escroReqRepo.saveAll(escrowReq);
    }

    /**
     * Retrieves an Escrow Requirement by its name.
     * 
     * @param reqName The name of the Escrow Requirement to retrieve.
     * @return The EscrowReqDto representing the Escrow Requirement.
     * @throws RequirmentNotPresentException if the Escrow Requirement is not found.
     */
    @Override
    public EscrowReqDto findByReqName(String reqName) {
        reqName =  StringUtil.normalizeString(reqName);
        Optional<EscrowReq> reqOpt = escroReqRepo.findByReqName(reqName);
        if(reqOpt.isPresent()) {
            return reqMapper.toDto(reqOpt.get());
        }
        else {
            throw new RequirmentNotPresentException(String.format("%s %s", reqName, EscrowReqConstants.NOT_AVAILABLE));
        }
    }

    /**
     * Retrieves a list of Escrow Requirements by their names.
     * 
     * @param reqName The list of names of the Escrow Requirements to retrieve.
     * @return List of EscrowReqDto representing the Escrow Requirements.
     */
    @Override
    public List<EscrowReqDto> findByReqName(List<String> reqName) {
        List<EscrowReq> reqList = escroReqRepo.findByReqName(StringUtil.formatEscrowReq(reqName));
        List<EscrowReqDto> dto = reqMapper.toDto(reqList);
        return dto;
    }

    /**
     * Retrieves the names of all Escrow Requirements.
     * 
     * @return List of Strings containing the names of all Escrow Requirements.
     */
    @Override
    public List<String> getAllReq() {
        return escroReqRepo.findAllReqName();
    }

    /**
     * Retrieves an Escrow Requirement by its ID.
     * 
     * @param reqId The ID of the Escrow Requirement to retrieve.
     * @return The EscrowReqDto representing the Escrow Requirement.
     */
    @Override
    public EscrowReqDto findByReqId(Integer reqId) {
        return escroReqRepo.findByReqId(reqId);
    }
}


