package com.zettamine.mpa.escrow.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.MethodArgumentNotValidException;

import com.zettamine.mpa.escrow.dto.EscrowSaveDto;
import com.zettamine.mpa.escrow.dto.EscrowServiceAreaDto;

import jakarta.validation.Valid;
public interface EscrowServiceAreaService {


	void save(EscrowSaveDto nameDtos);
	
	EscrowServiceAreaDto findById(Integer srvcId);
	
	List<EscrowServiceAreaDto> getAll();
	
	Map<String, Object> findByEscrowName(String escoName);
	
	boolean update(EscrowServiceAreaDto escSrvcDto, Integer serviceAreaId);

	/**
	 * Retrieves Escrow Service Area entities associated with a specific Escrow entity.
	 *
	 * @param escoId The ID of the associated Escrow entity.
	 * @return A map containing the Escrow company name and a list of Escrow Service Area DTOs associated with the given Escrow.
	 * @throws ResourceNotFoundException if no Escrow entity with the given ID is found.
	 */

	


}
