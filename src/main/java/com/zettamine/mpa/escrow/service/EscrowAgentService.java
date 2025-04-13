package com.zettamine.mpa.escrow.service;

import java.util.List;
import java.util.Map;

import com.zettamine.mpa.escrow.dto.EscrowAgentDto;
import com.zettamine.mpa.escrow.dto.EscrowSaveDto;

import jakarta.validation.Valid;

public interface EscrowAgentService {
   void save(EscrowSaveDto escrowNameDto);
	
	EscrowAgentDto findById(Integer escrowAgentId);
	
	List<EscrowAgentDto> getAll();
	
	Map<String, Object> findByEscrowName(String escoName);
	
	boolean update(EscrowAgentDto escrowAgentDto, Integer escrowAgentId);

}
