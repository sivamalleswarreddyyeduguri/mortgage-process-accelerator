package com.zettamine.mpa.escrow.serviceImpl;

/**
 * This class provides the implementation of the {@link EscrowReqLoanProductService} interface.
 * It handles operations related to escrow requirements linked with loan products.
 */
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.zettamine.mpa.escrow.constants.EscrowConstants;
import com.zettamine.mpa.escrow.constants.EscrowReqConstants;
import com.zettamine.mpa.escrow.constants.EscrowReqLoanProdConstants;
import com.zettamine.mpa.escrow.dto.LoanReqDto;
import com.zettamine.mpa.escrow.dto.EscrowReqDto;
import com.zettamine.mpa.escrow.dto.EscrowReqLoanProductDto;
import com.zettamine.mpa.escrow.entity.EscrowReq;
import com.zettamine.mpa.escrow.entity.EscrowReqLoanProduct;
import com.zettamine.mpa.escrow.repository.EscrowReqLoanProductRepository;
import com.zettamine.mpa.escrow.repository.EscrowRequirmentRepository;
import com.zettamine.mpa.escrow.service.EscrowReqLoanProductService;
import com.zettamine.mpa.escrow.service.EscrowReqService;
import com.zettamine.mpa.escrow.utils.StringUtil;
import com.zettamine.mpa.exception.DuplicateLoanProdRequirment;
import com.zettamine.mpa.exception.RequirmentNotPresentException;
import com.zettamine.mpa.exception.ResourceNotFoundException;
import com.zettamine.mpa.mapper.EscrowReqLoanProductMapper;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EscrowReqLoanProductServiceImpl implements EscrowReqLoanProductService {

	private EscrowReqLoanProductRepository escoReqLoanRepo;
	private EscrowReqLoanProductMapper escoReqLoanMapper;
	private EscrowRequirmentRepository escoReqrepo;
	private EscrowReqService escoReqSer;

	 /**
     * Retrieves all active escrow requirement loan products.
     *
     * @return List of {@link EscrowReqLoanProductDto}
     */
	@Override
	public List<EscrowReqLoanProductDto> getAll() {
		List<EscrowReqLoanProduct> all = escoReqLoanRepo.findAllByActive(EscrowReqLoanProdConstants.ACTIVE);
		List<EscrowReqLoanProductDto> dtoList = escoReqLoanMapper.toDto(all);
		return dtoList;
	}
	
	 /**
     * Retrieves escrow requirement names associated with a given loan product ID.
     *
     * @param ProdId The loan product ID
     * @return List of escrow requirement names
     * @throws ResourceNotFoundException if no loan product is found with the given ID
     */

	@Override
	public List<String> findByProdId(Integer ProdId) {

		List<EscrowReqLoanProduct> byProdId = escoReqLoanRepo.findByProdIdAndActive(ProdId,
				EscrowReqLoanProdConstants.ACTIVE);
		if(byProdId.size() == 0) {
			throw new ResourceNotFoundException(String.format("%s %s", "No Requirements found for loan product with id", ProdId.toString()));
		}
		List<String> reqNames = new ArrayList<>();
		for(EscrowReqLoanProduct loanReq : byProdId) {
			EscrowReqDto byReqId = escoReqSer.findByReqId(loanReq.getReqId());
			reqNames.add(byReqId.getReqName());
		}
		return reqNames;
	}
	

    /**
     * Saves an escrow requirement loan product.
     *
     * @param escoReqLoanDto The DTO representing the escrow requirement loan product to be saved
     * @throws DuplicateLoanProdRequirment if a duplicate requirement loan product is found
     */

	@Override
	public void save(EscrowReqLoanProductDto escoReqLoanDto) {

		EscrowReqLoanProduct escoReqLoan = escoReqLoanMapper.toEntity(escoReqLoanDto);
		EscrowReqLoanProduct byReqIdAndProdId = escoReqLoanRepo.findByReqIdAndProdId(escoReqLoanDto.getReqId(),
				escoReqLoanDto.getProdId());
		if (byReqIdAndProdId != null && byReqIdAndProdId.getActive().equals(EscrowReqLoanProdConstants.ACTIVE)) {
			throw new DuplicateLoanProdRequirment(
					String.format("%s %s", byReqIdAndProdId.getReqId(), EscrowReqLoanProdConstants.SAVE_CONFLICT));
		}
		escoReqLoanRepo.save(escoReqLoan);
	}
	
	/**
    * Saves escrow requirement loan products associated with a loan.
    *
    * @param reqLoanDto The DTO representing the loan and its associated requirements
    * @throws DuplicateLoanProdRequirment if a duplicate requirement loan product is found
    */

	@Override
	@Transactional
	public void save(LoanReqDto reqLoanDto) {
		Integer loanId = reqLoanDto.getProdId();

		List<EscrowReq> reqByName = getRequirmentByName(reqLoanDto.getRequirements());

		for (EscrowReq req : reqByName) {
			EscrowReqLoanProduct byReqIdAndProdId = escoReqLoanRepo.findByReqIdAndProdId(req.getReqId(), loanId);

			if (byReqIdAndProdId != null && byReqIdAndProdId.getActive().equals(EscrowReqLoanProdConstants.ACTIVE)) {
				throw new DuplicateLoanProdRequirment(
						String.format("%s %s", req.getReqName(), EscrowReqLoanProdConstants.SAVE_CONFLICT));
			}

			if (byReqIdAndProdId != null && byReqIdAndProdId.getActive().equals(EscrowReqLoanProdConstants.INACTIVE)) {
				byReqIdAndProdId.setActive(EscrowReqLoanProdConstants.ACTIVE);
			}

			EscrowReqLoanProduct loanReq = new EscrowReqLoanProduct();
			loanReq.setProdId(loanId);
			loanReq.setReqId(req.getReqId());
			loanReq.setActive(EscrowReqLoanProdConstants.ACTIVE);
			escoReqLoanRepo.save(loanReq);
		}
	}
	

    /**
     * Saves a list of escrow requirement loan products.
     *
     * @param loanReqList The list of DTOs representing the escrow requirement loan products to be saved
     */

	@Override
	public void save(List<EscrowReqLoanProductDto> loanReqList) {
		List<EscrowReqLoanProduct> loanReq = escoReqLoanMapper.toEntity(loanReqList);
		escoReqLoanRepo.saveAll(loanReq);
	}

	/**
	 * Finds an escrow requirement loan product by the given requirement ID and product ID.
	 *
	 * @param reqId  The requirement ID
	 * @param prodId The product ID
	 * @return The DTO representing the escrow requirement loan product, or null if not found
	 */
	@Override
	public EscrowReqLoanProductDto findByReqIdAndProdId(Integer reqId, Integer prodId) {

		EscrowReqLoanProduct byReqIdAndProdId = escoReqLoanRepo.findByReqIdAndProdIdAndActive(reqId, prodId,
				EscrowReqLoanProdConstants.ACTIVE);
		return escoReqLoanMapper.toDto(byReqIdAndProdId);
	}
	
	/**
	 * Finds escrow requirement loan products by the given list of requirement names.
	 *
	 * @param reqName The list of requirement names
	 * @return The list of DTOs representing the escrow requirement loan products
	 */

	@Override
	public List<EscrowReqLoanProductDto> findByReqName(List<String> reqName) {
		List<List<EscrowReqLoanProductDto>> byReqName = new ArrayList<>();
		List<EscrowReq> reqByName = escoReqrepo.findByReqName(reqName);
		for (EscrowReq req : reqByName) {
			List<EscrowReqLoanProduct> byReqId = escoReqLoanRepo.findByReqId(req.getReqId());
			byReqName.add(escoReqLoanMapper.toDto(byReqId));
		}
		return byReqName.stream().flatMap(m -> m.stream()).collect(Collectors.toList());
	}

	/**
	 * Searches for escrow requirement loan products based on the given list of requirement names.
	 *
	 * @param reqNameList The list of requirement names
	 * @return The list of product IDs matching the requirements
	 */
	@Override
	public List<Integer> searchByReq(List<String> reqNameList) {

		List<EscrowReq> byReqName = getRequirmentByName(reqNameList);
		Set<Integer> reqIds = new HashSet<>();
		for (EscrowReq req : byReqName) {
			reqIds.add(req.getReqId());
		}
		List<Integer> searchByReq = escoReqLoanRepo.searchByReq(reqIds, reqIds.size());
		return searchByReq;
	}

	/**
	 * Finds escrow requirement loan products by the given requirement ID.
	 *
	 * @param reqId The requirement ID
	 * @return The list of DTOs representing the escrow requirement loan products
	 */
	@Override
	public List<EscrowReqLoanProductDto> findByReqId(Integer reqId) {
		return escoReqLoanMapper.toDto(escoReqLoanRepo.findByReqId(reqId));
	}
	
	/**
	 * Deletes escrow requirement loan products associated with the given loan request DTO.
	 *
	 * @param loanReqDto The loan request DTO containing the product ID and requirement names
	 * @throws RequirmentNotPresentException if a requirement is not present for the loan product
	 */

	@Override
	public void delete(@Valid LoanReqDto loanReqDto) {
		Integer prodId = loanReqDto.getProdId();
		List<String> reqNotAviForLoan = new ArrayList<>();
		List<String> loanReqs = StringUtil.formatEscrowReq(loanReqDto.getRequirements());
		List<Integer> reqIdByName = getReqIdByName(loanReqs);
		for (int reqId : reqIdByName) {
			EscrowReqLoanProduct loanReq = escoReqLoanRepo.findByReqIdAndProdIdAndActive(reqId, prodId,
					EscrowReqLoanProdConstants.ACTIVE);
			if (loanReq == null) {
				EscrowReqDto byReqId = escoReqSer.findByReqId(reqId);
				reqNotAviForLoan.add(byReqId.getReqName()); 
			}
		}
		if(reqNotAviForLoan.size() > 0) {
			throw new RequirmentNotPresentException(
					String.format("%s %s for loan product with ID: %s ",reqNotAviForLoan, EscrowReqLoanProdConstants.ENTITY_NOT_FOUND, prodId.toString()));
		}
		escoReqLoanRepo.updateActiveByReqIdInAndProdId(reqIdByName, prodId, EscrowReqLoanProdConstants.INACTIVE);
	}

	/**
	 * 
	 * @param reqNames names of requirements associated with the loan product
	 * @return List<Integer>  requirements id associated with the requirements provided
	 */
	@Override
	public List<Integer> getReqIdByName(List<String> reqNames) {
		List<EscrowReq> requirments = new ArrayList<>();
		List<String> noReq = new ArrayList<>();
		for (String req : reqNames) {
			Optional<EscrowReq> reqByName = escoReqrepo.findByReqName(req);
			if (reqByName.isEmpty()) {
				noReq.add(req);
			} else {
				requirments.add(reqByName.get());
			}
		}
		if (noReq.size() > 0) {
			throw new RequirmentNotPresentException(
					String.format("%s %s", noReq.toString(), EscrowReqConstants.NOT_AVAILABLE));
		}
		return requirments.stream().map(req -> req.getReqId()).toList();
	}

	/**
	 * 
	 * @param reqNames names of requirements associated with the loan product
	 * @return List<EscrowReq>  requirements object  associated with the requirement names provided
	 */
	List<EscrowReq> getRequirmentByName(List<String> reqNames) {
		reqNames = StringUtil.formatEscrowReq(reqNames);
		List<EscrowReq> requirments = new ArrayList<>();
		List<String> noReq = new ArrayList<>();
		for (String req : reqNames) {
			Optional<EscrowReq> reqByName = escoReqrepo.findByReqName(req);
			if (reqByName.isEmpty()) {
				noReq.add(req);
			} else {
				requirments.add(reqByName.get());
			}
		}
		if (noReq.size() > 0)
			throw new RequirmentNotPresentException(
					String.format("%s %s", noReq.toString(), EscrowReqConstants.NOT_AVAILABLE));
		return requirments;
	}
}
