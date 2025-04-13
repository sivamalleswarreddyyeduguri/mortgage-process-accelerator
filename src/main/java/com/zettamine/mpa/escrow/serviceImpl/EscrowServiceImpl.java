package com.zettamine.mpa.escrow.serviceImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.zettamine.mpa.escrow.constants.EscrowConstants;
import com.zettamine.mpa.escrow.dto.EscrowAgentDto;
import com.zettamine.mpa.escrow.dto.EscrowDto;
import com.zettamine.mpa.escrow.dto.EscrowFetchDto;
import com.zettamine.mpa.escrow.dto.EscrowServiceAreaDto;
import com.zettamine.mpa.escrow.dto.SearchCriteria;
import com.zettamine.mpa.escrow.entity.Escrow;
import com.zettamine.mpa.escrow.entity.EscrowAgent;
import com.zettamine.mpa.escrow.entity.EscrowServiceArea;
import com.zettamine.mpa.escrow.repository.EscrowRepository;
import com.zettamine.mpa.escrow.service.EscrowService;
import com.zettamine.mpa.escrow.utils.StringUtil;
import com.zettamine.mpa.exception.ResourceNotFoundException;
import com.zettamine.mpa.mapper.EscrowAgentMapper;
import com.zettamine.mpa.mapper.EscrowMapper;
import com.zettamine.mpa.mapper.EscrowServiceAreaMapper;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EscrowServiceImpl implements EscrowService {

	private EscrowRepository escrowRepository;
	private EscrowMapper escrowMapper;
	private EscrowServiceAreaMapper serviceAreaMapper;
	private EscrowAgentMapper agentMapper;
	@PersistenceContext
	private EntityManager entityManager;

	private JdbcTemplate jdbcTemplate;

	/**
	 * Creates a new Escrow entity and saves it to the database.
	 *
	 * @param escrowDto The DTO containing the data for the new Escrow entity.
	 */

	@Override
	@Transactional
	public void save(EscrowDto escrowDto) {

		Escrow escrow = escrowMapper.toEntity(escrowDto);
		StringUtil.escrowRemoveSpaces(escrow);
		List<EscrowServiceArea> serviceAreaList = new ArrayList<>();
		List<EscrowAgent> agentList = new ArrayList<>();

		Set<EscrowServiceArea> distinctServiceAreas = new HashSet<>();
		for (EscrowServiceAreaDto serviceArea : escrowDto.getServiceArea()) {
			EscrowServiceArea escrowService = serviceAreaMapper.toEntity(serviceArea);
			StringUtil.escrowServiceAreaRemoveSpaces(escrowService);
			escrowService.setEscrow(escrow);
			distinctServiceAreas.add(escrowService);
		}
		serviceAreaList.addAll(distinctServiceAreas);

		Set<EscrowAgent> distinctAgents = new HashSet<>();
		for (EscrowAgentDto agent : escrowDto.getEscrowAgent()) {
			EscrowAgent escrowAgent = agentMapper.toEntity(agent);
			StringUtil.escrowAgentRemoveSpaces(escrowAgent);
			escrowAgent.setEscrow(escrow);
			distinctAgents.add(escrowAgent);
		}
		agentList.addAll(distinctAgents);

		escrow.setServiceArea(serviceAreaList);
		escrow.setEscrowAgent(agentList);
		escrowRepository.save(escrow);
	}

	/**
	 * Retrieves an Escrow entity by its ID.
	 *
	 * @param escrowId The ID of the Escrow entity to retrieve.
	 * @return The DTO representing the retrieved Escrow entity.
	 * @throws ResourceNotFoundException if no Escrow entity with the given ID is
	 *                                   found.
	 */
	@Override
	public EscrowDto findById(Integer escrowId) {
		Optional<Escrow> escrow = escrowRepository.findById(escrowId);
		if (escrow.isEmpty()) {
			throw new ResourceNotFoundException(EscrowConstants.ESCROW_NOT_FOUND + escrowId);
		}
		return escrowMapper.toDto(escrow.get());
	}

	/**
	 * Retrieves an Escrow entity by its name.
	 *
	 * @param escoName The name of the Escrow entity to retrieve.
	 * @return The DTO representing the retrieved Escrow entity.
	 * @throws ResourceNotFoundException if no Escrow entity with the given name is
	 *                                   found.
	 */
	@Override
	public EscrowDto findByEscrowName(String escoName) {
		Optional<Escrow> escrow = escrowRepository.findByEscoName(StringUtil.removeExtraSpaces(escoName).toUpperCase());
		if (escrow.isEmpty()) {
			throw new ResourceNotFoundException(EscrowConstants.ESCROW_NOT_FOUND_BY_NAME + escoName);
		}

		return escrowMapper.toDto(escrow.get());
	}

	/**
	 * Retrieves all Escrow entities from the database.
	 *
	 * @return A list of DTOs representing all Escrow entities.
	 */
	@Override
	public List<EscrowDto> getAll() {
		return escrowMapper.toDtoList(escrowRepository.findAll());
	}

	/**
	 * Updates an existing Escrow entity in the database.
	 *
	 * @param escrowDto The DTO containing the updated data for the Escrow entity.
	 * @param escoId    The ID of the Escrow entity to update.
	 * @return True if the update was successful, false otherwise.
	 */
	@Transactional
	@Override
	public boolean update(EscrowDto escrowDto, Integer escoId) {
		Optional<Escrow> escrowObj = escrowRepository.findById(escoId);
		if (escrowObj.isPresent()) {
			Escrow escrow = escrowMapper.toEntity(escrowDto);
			escrow.setEscoId(escoId);
			StringUtil.escrowRemoveSpaces(escrow);
			escrowRepository.save(escrow);
			return true;
		}
		return false;
	}

	/**
	 * Searches for Escrow entities based on the provided search criteria.
	 *
	 * @param criteria The criteria to search for Escrow entities.
	 * @return A list of DTOs representing the search results.
	 */

	@Override
	@Transactional
	public List<EscrowFetchDto> search(SearchCriteria criteria) {
	    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
	    CriteriaQuery<EscrowFetchDto> cq = cb.createQuery(EscrowFetchDto.class);
	    Root<Escrow> escrowCompanyRoot = cq.from(Escrow.class);
	    Join<Escrow, EscrowServiceArea> escrowServiceAreaJoin = escrowCompanyRoot.join("serviceArea", JoinType.INNER);

	    cq.select(cb.construct(
	        EscrowFetchDto.class,
	        escrowCompanyRoot.get("escoName"),
	        escrowCompanyRoot.get("state"),
	        escrowCompanyRoot.get("city"),
	        escrowCompanyRoot.get("zipcode"),
	        escrowCompanyRoot.get("email"),
	        escrowCompanyRoot.get("phone")
	    )).distinct(true);

	    List<Predicate> predicates = new ArrayList<>();

	    if (criteria.getName() != null) {
	        predicates.add(cb.like(cb.upper(escrowCompanyRoot.get("escoName")), "%" + criteria.getName().trim().toUpperCase() + "%"));
	    }

	    if (criteria.getState() != null) {
	        predicates.add(cb.equal(cb.upper(escrowServiceAreaJoin.get("state")), criteria.getState().trim().toUpperCase()));
	    }

	    if (criteria.getCity() != null) {
	        predicates.add(cb.equal(cb.upper(escrowServiceAreaJoin.get("city")), criteria.getCity().trim().toUpperCase()));
	    }

	    if (criteria.getZipcode() != null) {
	        predicates.add(cb.equal(cb.upper(escrowServiceAreaJoin.get("zipcode")), criteria.getZipcode().trim().toUpperCase()));
	    }

	    cq.where(predicates.toArray(new Predicate[0]));

	    return entityManager.createQuery(cq).getResultList();
	}
}
