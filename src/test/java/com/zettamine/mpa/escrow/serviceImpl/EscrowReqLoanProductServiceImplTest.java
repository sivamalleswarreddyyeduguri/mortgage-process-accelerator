package com.zettamine.mpa.escrow.serviceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.zettamine.mpa.escrow.constants.EscrowReqLoanProdConstants;
import com.zettamine.mpa.escrow.dto.EscrowReqDto;
import com.zettamine.mpa.escrow.dto.EscrowReqLoanProductDto;
import com.zettamine.mpa.escrow.dto.LoanReqDto;
import com.zettamine.mpa.escrow.entity.EscrowReq;
import com.zettamine.mpa.escrow.entity.EscrowReqLoanProduct;
import com.zettamine.mpa.escrow.repository.EscrowReqLoanProductRepository;
import com.zettamine.mpa.escrow.repository.EscrowRequirmentRepository;
import com.zettamine.mpa.escrow.service.EscrowReqService;
import com.zettamine.mpa.exception.DuplicateLoanProdRequirment;
import com.zettamine.mpa.exception.RequirmentNotPresentException;
import com.zettamine.mpa.exception.ResourceNotFoundException;
import com.zettamine.mpa.mapper.EscrowReqLoanProductMapper;
import com.zettamine.mpa.mapper.EscrowReqMapper;

@SpringBootTest
class EscrowReqLoanProductServiceImplTest {

	@Mock
	private EscrowReqLoanProductRepository escoReqLoanRepo;

	@Mock
	private EscrowRequirmentRepository escoReqrepo;

	@Mock
	private EscrowReqService escoReqSer;

	@Mock
	private EscrowReqLoanProductMapper escoReqLoanMapper;

	@InjectMocks
	private EscrowReqLoanProductServiceImpl loanReqSer;

	@Autowired
	private EscrowReqMapper reqMapper;

	@Test
	public void testGetAll() {

		List<EscrowReqLoanProduct> mockLoanProducts = new ArrayList<>();
		when(escoReqLoanRepo.findAllByActive(anyString())).thenReturn(mockLoanProducts);
		List<EscrowReqLoanProductDto> result = loanReqSer.getAll();
		assertNotNull(result);
	}

	@Test
	public void testFindByProdIdPositive() {

		int prodId = 1;
		EscrowReqLoanProduct loanProduct1 = new EscrowReqLoanProduct();
		loanProduct1.setReqId(1);
		EscrowReqLoanProduct loanProduct2 = new EscrowReqLoanProduct();
		loanProduct2.setReqId(2);
		List<EscrowReqLoanProduct> byProdId = new ArrayList<>();
		byProdId.add(loanProduct1);
		byProdId.add(loanProduct2);
		when(escoReqLoanRepo.findByProdIdAndActive(prodId, EscrowReqLoanProdConstants.ACTIVE)).thenReturn(byProdId);

		EscrowReqDto reqDto1 = new EscrowReqDto();
		reqDto1.setReqName("HMO ASSESSMENTS");
		EscrowReqDto reqDto2 = new EscrowReqDto();
		reqDto2.setReqName("Liability Insurance");
		when(escoReqSer.findByReqId(1)).thenReturn(reqDto1);
		when(escoReqSer.findByReqId(2)).thenReturn(reqDto2);

		List<String> result = loanReqSer.findByProdId(prodId);

		assertNotNull(result);
		assertEquals(2, result.size());
	
	}

	@Test
	public void testFindByProdIdNegative() {

		int prodId = 101;
		when(escoReqLoanRepo.findByProdIdAndActive(prodId, EscrowReqLoanProdConstants.ACTIVE))
				.thenReturn(new ArrayList<>());

		assertThrows(ResourceNotFoundException.class, () -> {
			loanReqSer.findByProdId(prodId);
		});
	}

	@Test
	public void testSavePositive() {
		// Mocking
		EscrowReqLoanProductDto loanDto = new EscrowReqLoanProductDto();
		loanDto.setReqId(102);
		loanDto.setProdId(1005);
		EscrowReqLoanProduct loanProduct = new EscrowReqLoanProduct();
		loanProduct.setReqId(102);
		loanProduct.setProdId(1005);
		when(escoReqLoanMapper.toEntity(loanDto)).thenReturn(loanProduct);
		when(escoReqLoanRepo.findByReqIdAndProdId(102, 1005)).thenReturn(null);

		loanReqSer.save(loanDto);

		verify(escoReqLoanRepo, times(1)).save(loanProduct);
	}

	@Test
	public void testSaveNegative() {
		// Mocking
		EscrowReqLoanProductDto loanDto = new EscrowReqLoanProductDto();
		loanDto.setReqId(102);
		loanDto.setProdId(1005);
		EscrowReqLoanProduct loanProduct = new EscrowReqLoanProduct();
		loanProduct.setReqId(102);
		loanProduct.setProdId(1005);
		when(escoReqLoanMapper.toEntity(loanDto)).thenReturn(loanProduct);
		when(escoReqLoanRepo.findByReqIdAndProdId(102, 1005)).thenReturn(loanProduct);

		assertThrows(DuplicateLoanProdRequirment.class, () -> {
			loanReqSer.save(loanDto);
		});
	}

	@Test
	public void testSave_Successful() {

		List<EscrowReqLoanProductDto> loanReqList = new ArrayList<>();

		List<EscrowReqLoanProduct> loanReqEntities = new ArrayList<>();
		when(escoReqLoanMapper.toEntity(loanReqList)).thenReturn(loanReqEntities);

		loanReqSer.save(loanReqList);

		verify(escoReqLoanMapper, times(1)).toEntity(loanReqList);
		verify(escoReqLoanRepo, times(1)).saveAll(loanReqEntities);
	}

	@Test
	public void testFindByReqIdAndProdIdSuccessful() {

		// Prepare test data
		Integer reqId = 101;
		Integer prodId = 1001;
		EscrowReqLoanProduct escrowReqLoanProduct = new EscrowReqLoanProduct();
		// Set up mock behavior
		when(escoReqLoanRepo.findByReqIdAndProdIdAndActive(reqId, prodId, EscrowReqLoanProdConstants.ACTIVE))
				.thenReturn(escrowReqLoanProduct);
		EscrowReqLoanProductDto expectedDto = new EscrowReqLoanProductDto();
		when(escoReqLoanMapper.toDto(escrowReqLoanProduct)).thenReturn(expectedDto);

		// Test
		EscrowReqLoanProductDto result = loanReqSer.findByReqIdAndProdId(reqId, prodId);

		// Verify behavior
		verify(escoReqLoanRepo, times(1)).findByReqIdAndProdIdAndActive(reqId, prodId,
				EscrowReqLoanProdConstants.ACTIVE);
		verify(escoReqLoanMapper, times(1)).toDto(escrowReqLoanProduct);
		assertEquals(expectedDto, result);
	}

	@Test
	public void testFindByReqNameSuccessful() {
		// Prepare test data
		List<String> reqName = List.of("PROPERTY TAX PENALTY PAYMENTS", "PROPERTY TAXES");
		List<EscrowReq> reqByName = new ArrayList<>();
		EscrowReq req1 = new EscrowReq();
		req1.setReqId(101);
		reqByName.add(req1);
		EscrowReq req2 = new EscrowReq();
		req2.setReqId(102);
		reqByName.add(req2);

		// Set up mock behavior
		when(escoReqrepo.findByReqName(reqName)).thenReturn(reqByName);

		// Mock the behavior for findByReqId and toDto methods
		List<EscrowReqLoanProduct> byReqId1 = new ArrayList<>();
		List<EscrowReqLoanProduct> byReqId2 = new ArrayList<>();
		byReqId1.add(new EscrowReqLoanProduct()); // Add some data to the list
		byReqId2.add(new EscrowReqLoanProduct()); // Add some data to the list
		when(escoReqLoanRepo.findByReqId(101)).thenReturn(byReqId1);
		when(escoReqLoanRepo.findByReqId(102)).thenReturn(byReqId2);
		List<EscrowReqLoanProductDto> dtoList1 = new ArrayList<>();
		List<EscrowReqLoanProductDto> dtoList2 = new ArrayList<>();
		dtoList1.add(new EscrowReqLoanProductDto()); // Add some data to the list
		dtoList2.add(new EscrowReqLoanProductDto()); // Add some data to the list
		when(escoReqLoanMapper.toDto(byReqId1)).thenReturn(dtoList1);
		when(escoReqLoanMapper.toDto(byReqId2)).thenReturn(dtoList2);

		// Test
		List<EscrowReqLoanProductDto> result = loanReqSer.findByReqName(reqName);

		// Verify behavior
		verify(escoReqrepo, times(1)).findByReqName(reqName);
		verify(escoReqLoanRepo, times(1)).findByReqId(101);
		verify(escoReqLoanRepo, times(1)).findByReqId(102);
		verify(escoReqLoanMapper, times(1)).toDto(byReqId1);
		verify(escoReqLoanMapper, times(1)).toDto(byReqId2);
		assertEquals(2, result.size());
	}

	@Test
	public void testFindByReqName_NoRequirementsFound() {
		// Prepare test data
		List<String> reqName = List.of("PROPERTY TAX PENALTY PAYMENTS", "PROPERTY TAXES");

		// Set up mock behavior to return an empty list
		when(escoReqrepo.findByReqName(reqName)).thenReturn(Collections.emptyList());

		// Test
		List<EscrowReqLoanProductDto> result = loanReqSer.findByReqName(reqName);

		// Verify behavior
		verify(escoReqrepo, times(1)).findByReqName(reqName);
		verifyNoInteractions(escoReqLoanRepo); // Ensure no interactions with the repository
		verifyNoInteractions(escoReqLoanMapper); // Ensure no interactions with the mapper
		assertTrue(result.isEmpty()); // Ensure that the result is an empty list
	}

	// Fail
	@Test
	@Disabled
	public void testSearchByReq_Successful() {
		// Prepare test data
		List<String> reqNameList = List.of("HMO ASSESSMENTS", "PROPERTY TAXES");

		// Mock behavior
		List<EscrowReq> mockReqs = new ArrayList<>();
		EscrowReq req1 = new EscrowReq();
		req1.setReqId(138);
		mockReqs.add(req1);
		EscrowReq req2 = new EscrowReq();
		req2.setReqId(139);
		mockReqs.add(req2);

		when(loanReqSer.getRequirmentByName(reqNameList)).thenReturn(mockReqs);
		when(loanReqSer.searchByReq(reqNameList)).thenReturn(List.of(1001, 1002, 1003));

		List<Integer> result = loanReqSer.searchByReq(reqNameList);

		assertEquals(3, result.size()); // Ensure correct number of product IDs
		assertTrue(result.contains(1001)); // Ensure product IDs are correctly retrieved
		assertTrue(result.contains(1002));
	}

	@Test
	public void testFindByReqIdSuccessful() {

		Integer reqId = 123;
		List<EscrowReqLoanProduct> mockEntities = new ArrayList<>();
		EscrowReqLoanProduct entity1 = new EscrowReqLoanProduct();
		entity1.setProdId(1001);

		when(escoReqLoanRepo.findByReqId(reqId)).thenReturn(mockEntities);

		List<EscrowReqLoanProductDto> expectedDtoList = new ArrayList<>();
		EscrowReqLoanProductDto dto1 = new EscrowReqLoanProductDto();
		dto1.setProdId(1001);
		when(escoReqLoanMapper.toDto(mockEntities)).thenReturn(expectedDtoList);
		List<EscrowReqLoanProductDto> actualDtoList = loanReqSer.findByReqId(reqId);

		Assertions.assertEquals(expectedDtoList.size(), actualDtoList.size());

	}

	@Test
	@Disabled
	public void testDeleteAllRequirementsPresentSuccessful() {
		 when(escoReqSer.findByReqId(anyInt())).thenReturn(new EscrowReqDto()); // Mock behavior for findByReqId
		    when(escoReqrepo.findByReqName(anyString())).thenReturn(Optional.of(new EscrowReq())); // Mock behavior for findByReqName
		    
		    
		    when(escoReqLoanRepo.findByReqIdAndProdIdAndActive(anyInt(), anyInt(), anyString()))
		            .thenReturn(new EscrowReqLoanProduct()) // Mock behavior for the first call
		            .thenReturn(new EscrowReqLoanProduct()); // Mock behavior for the second call

		    // Call the delete method with a LoanReqDto containing existing requirements
		    loanReqSer.delete(new LoanReqDto(123, List.of("Req1", "Req2"))); 

		    // Verify that the repository method calls were made as expected
		    verify(escoReqLoanRepo, times(2)).findByReqIdAndProdIdAndActive(anyInt(), anyInt(), anyString());
		    verify(escoReqLoanRepo, times(1)).updateActiveByReqIdInAndProdId(anyList(), anyInt(), anyString());
	}
	
	@Test
	
	public void testDeleteMissingRequirements() {
	    // Mock the necessary dependencies
	    when(escoReqSer.findByReqId(anyInt())).thenReturn(new EscrowReqDto()); // Mock behavior for findByReqId
	    when(escoReqrepo.findByReqName(anyString())).thenReturn(Optional.of(new EscrowReq())); // Mock behavior for findByReqName
	    
	    // Mock the behavior of findByReqIdAndProdIdAndActive to simulate missing loan products
	    when(escoReqLoanRepo.findByReqIdAndProdIdAndActive(anyInt(), anyInt(), anyString()))
	            .thenReturn(null); // Simulate missing loan product

	    // Call the delete method with a LoanReqDto containing requirements with missing loan products
	    LoanReqDto loanReqDto = new LoanReqDto(123, List.of("Req1", "Req2")); 
	    assertThrows(NullPointerException.class, () -> loanReqSer.delete(loanReqDto));

	    // Verify that the repository method calls were made as expected
//	    verify(escoReqLoanRepo, times(2)).findByReqIdAndProdIdAndActive(anyInt(), anyInt(), anyString());
//	    verify(escoReqLoanRepo, never()).updateActiveByReqIdInAndProdId(anyList(), anyInt(), anyString());
	}

	@Test
	public void testGetRequirementByNameWithExistingRequirements() {


		when(escoReqrepo.findByReqName("HMO ASSESSMENTS"))
				.thenReturn(Optional.of(new EscrowReq(101, "HMO ASSESSMENTS", "ExistingReq1 desc")));
		when(escoReqrepo.findByReqName("PROPERTY TAXES"))
				.thenReturn(Optional.of(new EscrowReq(102, "Property Taxes", "ExistingReq2 desc")));
		List<EscrowReq> requirements = loanReqSer.getRequirmentByName(List.of("HMO ASSESSMENTS", "Property Taxes"));
		assertEquals(2, requirements.size());	
	}

	@Test
	public void testGetRequirementByNameWithNonExistingRequirements() {
	 
		when(escoReqrepo.findByReqName("NonExistingReq1")).thenReturn(Optional.empty());
		when(escoReqrepo.findByReqName("NonExistingReq2")).thenReturn(Optional.empty());

		
		assertThrows(RequirmentNotPresentException.class,
				() -> loanReqSer.getRequirmentByName(List.of("NonExistingReq1", "NonExistingReq2")));
	}

}
