package com.zettamine.mpa.escrow.serviceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.zettamine.mpa.escrow.constants.EscrowConstants;
import com.zettamine.mpa.escrow.constants.EscrowServiceAreaConstants;
import com.zettamine.mpa.escrow.dto.EscrowSaveDto;
import com.zettamine.mpa.escrow.dto.EscrowServiceAreaDto;
import com.zettamine.mpa.escrow.entity.Escrow;
import com.zettamine.mpa.escrow.entity.EscrowServiceArea;
import com.zettamine.mpa.escrow.repository.EscrowRepository;
import com.zettamine.mpa.escrow.repository.EscrowServiceAreaRepository;
import com.zettamine.mpa.escrow.service.EscrowServiceAreaService;
import com.zettamine.mpa.escrow.utils.StringUtil;
import com.zettamine.mpa.exception.EscrowServiceAreaAlreadyExistsException;
import com.zettamine.mpa.exception.ResourceNotFoundException;
import com.zettamine.mpa.mapper.EscrowServiceAreaMapper;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

/**
 * Service implementation for managing Escrow Service Areas.
 */
@Service
@AllArgsConstructor
public class EscrowServiceAreaServiceImpl implements EscrowServiceAreaService {

	private EscrowServiceAreaMapper serviceAreaMapper;
	private EscrowServiceAreaRepository serviceAreaRepository;
	private EscrowRepository escrowRepository;

	/**
     * Saves new Escrow Service Areas to the database.
     *
     * @param nameDtos The DTO containing the data for the Escrow Service Areas to save.
     * @throws ResourceNotFoundException              if the associated Escrow entity is not found.
     * @throws EscrowServiceAreaAlreadyExistsException if any of the provided Escrow Service Areas already exist.
     */
	@Override
	@Transactional
	public void save(EscrowSaveDto nameDtos) {
	     
	        List<EscrowServiceArea> entityList = serviceAreaMapper.toEntityList(nameDtos.getServiceAreas());
	        for(EscrowServiceArea serviceArea: entityList) {
		        StringUtil.escrowServiceAreaRemoveSpaces(serviceArea);
		        
		        Escrow escrow = escrowRepository.findByEscoName(StringUtil.removeExtraSpaces(nameDtos.getEscoName().toUpperCase()))
		                .orElseThrow(() -> new ResourceNotFoundException(
		                        EscrowConstants.ESCROW_NOT_FOUND_BY_NAME + StringUtil.removeExtraSpaces(nameDtos.getEscoName().toUpperCase())));

		        serviceArea.setEscrow(escrow);
		        List<EscrowServiceArea> existingServiceAreas = serviceAreaRepository.findByCountyStateCityZipcode(
		                serviceArea.getCounty(), serviceArea.getState(), serviceArea.getCity(), serviceArea.getZipcode(),
		                escrow.getEscoId());

		        if (!existingServiceAreas.isEmpty()) {
		            throw new EscrowServiceAreaAlreadyExistsException(EscrowServiceAreaConstants.SAVE_CONFLICT_SERVICE_AREA);
		        }
	        }
	        
	        serviceAreaRepository.saveAll(entityList);
	    
	}
	
	/**
     * Retrieves an Escrow Service Area entity by its ID.
     *
     * @param srvcId The ID of the Escrow Service Area entity to retrieve.
     * @return The DTO representing the retrieved Escrow Service Area entity.
     * @throws ResourceNotFoundException if no Escrow Service Area entity with the given ID is found.
     */
	@Override
	public EscrowServiceAreaDto findById(Integer srvcId) {
		Optional<EscrowServiceArea> escrowServiceArea = serviceAreaRepository.findById(srvcId);
		if (escrowServiceArea.isEmpty()) {
			throw new ResourceNotFoundException(EscrowServiceAreaConstants.ESCROW_SERVICE_AREA_NOT_FOUND + srvcId);
		}
		return serviceAreaMapper.toDto(escrowServiceArea.get());
	}

	/**
     * Retrieves all Escrow Service Area entities from the database.
     *
     * @return A list of DTOs representing all Escrow Service Area entities.
     */
	@Override
	public List<EscrowServiceAreaDto> getAll() {
		return serviceAreaMapper.toDTOList(serviceAreaRepository.findAll());
	}

	/**
     * Retrieves Escrow Service Area entities associated with a specific Escrow entity.
     *
     * @param escoId The ID of the associated Escrow entity.
     * @return A map containing the Escrow company name and a list of Escrow Service Area DTOs associated with the given Escrow.
     * @throws ResourceNotFoundException if no Escrow entity with the given ID is found.
     */
	@Override
	public Map<String, Object> findByEscrowName(String escoName) {
		Optional<Escrow> escrow = escrowRepository.findByEscoName(StringUtil.removeExtraSpaces(escoName).toUpperCase());
		if(escrow.isEmpty()) {
			throw new ResourceNotFoundException(EscrowConstants.ESCROW_NOT_FOUND_BY_NAME + StringUtil.removeExtraSpaces(escoName).toUpperCase());
		}
		List<EscrowServiceArea> serviceAreaList = serviceAreaRepository.findByEscrowEscoId(escrow.get().getEscoId());
		List<EscrowServiceAreaDto> list =  serviceAreaMapper.toDTOList(serviceAreaList);
		Map<String, Object> response = new HashMap<>();
		response.put("company ", escrow.get().getEscoName());
		response.put("service Areas", list);
		return response;
	}

	/**
     * Updates an existing Escrow Service Area entity in the database.
     *
     * @param escSrvcDto   The DTO containing the updated data for the Escrow Service Area entity.
     * @param serviceAreaId The ID of the Escrow Service Area entity to update.
     * @return True if the update was successful, false otherwise.
     */
	@Override
	@Transactional
	public boolean update(EscrowServiceAreaDto escSrvcDto, Integer serviceAreaId) {
		Optional<EscrowServiceArea> ServiceAreaObj = serviceAreaRepository.findById(serviceAreaId);
		if (ServiceAreaObj.isPresent()) {
			EscrowServiceArea escrowServiceArea = serviceAreaMapper.toEntity(escSrvcDto);
			escrowServiceArea.setServiceAreaId(serviceAreaId);
			StringUtil.escrowServiceAreaRemoveSpaces(escrowServiceArea);
			serviceAreaRepository.save(escrowServiceArea);
			return true;
		}
		return false;
	}

}
