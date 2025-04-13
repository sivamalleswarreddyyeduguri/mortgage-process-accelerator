package com.zettamine.mpa.escrow.serviceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;

import com.zettamine.mpa.escrow.constants.EscrowAgentConstants;
import com.zettamine.mpa.escrow.constants.EscrowConstants;
import com.zettamine.mpa.escrow.dto.EscrowAgentDto;
import com.zettamine.mpa.escrow.dto.EscrowSaveDto;
import com.zettamine.mpa.escrow.entity.Escrow;
import com.zettamine.mpa.escrow.entity.EscrowAgent;
import com.zettamine.mpa.escrow.repository.EscrowAgentRepository;
import com.zettamine.mpa.escrow.repository.EscrowRepository;
import com.zettamine.mpa.escrow.service.EscrowAgentService;
import com.zettamine.mpa.escrow.utils.StringUtil;
import com.zettamine.mpa.exception.EscrowAgentAlreadyExistsException;
import com.zettamine.mpa.exception.ResourceNotFoundException;
import com.zettamine.mpa.mapper.EscrowAgentMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

/**
 * Service implementation for managing Escrow Agents.
 */
@Service
@AllArgsConstructor
public class EscrowAgentServiceImpl implements EscrowAgentService {

	private EscrowAgentMapper agentMapper;
    private EscrowAgentRepository agentRepository;
    private EscrowRepository escrowRepository;	

    /**
     * Saves new Escrow Agents to the database.
     *
     * @param nameDtos The DTO containing the data for the Escrow Agents to save.
     * @throws ResourceNotFoundException              if the associated Escrow entity is not found.
     * @throws EscrowAgentAlreadyExistsException       if any of the provided Escrow Agents already exist.
     */
	@Override
	@Transactional
	public void save(EscrowSaveDto nameDtos) {
		if (nameDtos.getEscoName() == null) {
            throw new ResourceNotFoundException(EscrowConstants.ESCROW_Name_VALIDAATION);
        }
        List<EscrowAgent> entityList = agentMapper.toEntityList(nameDtos.getEscrowAgent());
        for(EscrowAgent escrowAgent: entityList) {
	        StringUtil.escrowAgentRemoveSpaces(escrowAgent);
	        
	        Escrow escrow = escrowRepository.findByEscoName( StringUtil.removeExtraSpaces(nameDtos.getEscoName().toUpperCase()))
	                .orElseThrow(() -> new ResourceNotFoundException(
	                        EscrowConstants.ESCROW_NOT_FOUND_BY_NAME + nameDtos.getEscoName()));

	        escrowAgent.setEscrow(escrow);
	        List<EscrowAgent> existingAgents = agentRepository.findByLicenceIdEscoId(
	            escrowAgent.getEscrowAgentLicenceId(), escrow.getEscoId());
	        if (!existingAgents.isEmpty()) {
	            throw new EscrowAgentAlreadyExistsException(EscrowAgentConstants.SAVE_CONFLICT_AGENT);
	        }
        }
        agentRepository.saveAll(entityList);
	    
	}		

	/**
     * Retrieves an Escrow Agent entity by its ID.
     *
     * @param escrowAgentId The ID of the Escrow Agent entity to retrieve.
     * @return The DTO representing the retrieved Escrow Agent entity.
     * @throws ResourceNotFoundException if no Escrow Agent entity with the given ID is found.
     */
	@Override
	public EscrowAgentDto findById(Integer escrowAgentId) {
		Optional<EscrowAgent> escrowAgent = agentRepository.findById(escrowAgentId);
		  if(escrowAgent.isEmpty()) {
			  throw new ResourceNotFoundException(EscrowAgentConstants.ESCROW_AGENT__NOT_FOUND + escrowAgentId);
		  }

		   return agentMapper.toDto(escrowAgent.get());

	}

	/**
     * Retrieves all Escrow Agent entities from the database.
     *
     * @return A list of DTOs representing all Escrow Agent entities.
     */
	@Override
	public List<EscrowAgentDto> getAll() {
		 return agentMapper.toDTOList(agentRepository.findAll());	
	}

	/**
     * Retrieves Escrow Agent entities associated with a specific Escrow entity.
     *
     * @param escoId The ID of the associated Escrow entity.
     * @return A map containing the Escrow company name and a list of Escrow Agent DTOs associated with the given Escrow.
     * @throws ResourceNotFoundException if no Escrow entity with the given ID is found.
     */
	@Override
	public Map<String, Object> findByEscrowName(String escoName) {
		Optional<Escrow> escrow = escrowRepository.findByEscoName(StringUtil.removeExtraSpaces(escoName).toUpperCase());
		if(escrow.isEmpty()) {
			throw new ResourceNotFoundException(EscrowConstants.ESCROW_NOT_FOUND_BY_NAME + StringUtil.removeExtraSpaces(escoName).toUpperCase());
		}
		List<EscrowAgent> escrowAgentList = agentRepository.findByEscrowEscoId(escrow.get().getEscoId());
		List<EscrowAgentDto> list =  agentMapper.toDTOList(escrowAgentList);
		Map<String, Object> response = new HashMap<>();
		response.put("company ", escrow.get().getEscoName());
		response.put("escrow agents", list);
		return response;
	}

	 /**
     * Updates an existing Escrow Agent entity in the database.
     *
     * @param escrowAgentDto  The DTO containing the updated data for the Escrow Agent entity.
     * @param escrowAgentId The ID of the Escrow Agent entity to update.
     * @return True if the update was successful, false otherwise.
     */
	@Override
	@Transactional
	public boolean update(EscrowAgentDto escrowAgentDto, Integer escrowAgentId) {
		Optional<EscrowAgent> escrowAgentObj = agentRepository.findById(escrowAgentId);
		   if(escrowAgentObj.isPresent()) {
			    EscrowAgent escrowAgent = agentMapper.toEntity(escrowAgentDto);
			    escrowAgent.setEscrowAgentId(escrowAgentId);
			    StringUtil.escrowAgentRemoveSpaces(escrowAgent);
			    agentRepository.save(escrowAgent);
			    return true;
		   }
			return false;
	}
      
}
