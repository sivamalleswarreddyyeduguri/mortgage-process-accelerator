package com.zettamine.mpa.escrow.controller;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zettamine.mpa.escrow.constants.EscrowCommonConstants;
import com.zettamine.mpa.escrow.constants.EscrowReqLoanProdConstants;
import com.zettamine.mpa.escrow.dto.EscrowReqLoanProductDto;
import com.zettamine.mpa.escrow.dto.LoanReqDto;
import com.zettamine.mpa.escrow.dto.SearchByReqDto;
import com.zettamine.mpa.escrow.service.EscrowReqLoanProductService;

@AutoConfigureMockMvc
@SpringBootTest
class EscrowReqLoanProductControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Mock
	private EscrowReqLoanProductService escoReqLoanSer;

	@InjectMocks
	private EscrowReqLoanProductController escoReqLoanController;

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(escoReqLoanController).build();
	}

	@AfterEach
	void tearDown() throws Exception {
		mockMvc = null;
	}

	@Test
	void testSave() throws Exception {
		LoanReqDto loanReqDto = new LoanReqDto();
		loanReqDto.setRequirements(new ArrayList<>());
		loanReqDto.getRequirements().add("LMO Requirments");
		loanReqDto.getRequirements().add("HMO ASSESSMENTS");
		loanReqDto.setProdId(1001);

		doNothing().when(escoReqLoanSer).save(loanReqDto);

		mockMvc.perform(post("/api/v1/escrow/loan-requirment/add-requirment").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loanReqDto))).andExpect(status().isCreated())
				.andExpect(jsonPath("$.statusCode").value(EscrowCommonConstants.STATUS_201))
				.andExpect(jsonPath("$.statusMsg").value(EscrowReqLoanProdConstants.SAVE_SUCCEESS));
	}

	@Test
	void testFindByProdId() throws Exception {
		Integer prodId = 123;
		List<String> reqList = new ArrayList<>();
		reqList.add("LMO Requirments");
		reqList.add("HMO ASSESSMENTS");

		when(escoReqLoanSer.findByProdId(prodId)).thenReturn(reqList);

		mockMvc.perform(get("/api/v1/escrow/loan-requirment/find-by-prodid/{prodId}", prodId)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$[0]").value(reqList.get(0))).andExpect(jsonPath("$[1]").value(reqList.get(1)));
	}

	 @Test
	    void testFindByReq() throws Exception {
		 
		 List<String> reqNameList = Arrays.asList("req1", "req2");
	        List<EscrowReqLoanProductDto> mockDtoList = Arrays.asList(
	            new EscrowReqLoanProductDto(101, 1001),
	            new EscrowReqLoanProductDto(204, 1012)
	        );

	         
	        when(escoReqLoanSer.findByReqName(anyList())).thenReturn(mockDtoList);

	         
	        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/escrow/loan-requirment/find-by-requirment")
	                .param("reqNameList", "req1", "req2")
	                .contentType(MediaType.APPLICATION_JSON))
	                
	                .andExpect(status().isOk())
	              
	              
	                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

	                .andExpect(jsonPath("$[0].reqId").value(101))
	                .andExpect(jsonPath("$[0].prodId").value(1001))
	                .andExpect(jsonPath("$[1].reqId").value(204))
	                .andExpect(jsonPath("$[1].prodId").value(1012));
	    }



	 @Test
	 void testSearchByReq() throws Exception {
		 
		 SearchByReqDto requestDto = new SearchByReqDto();
			requestDto.setRequirements(List.of("Property Taxes", "Property Penalty Taxes"));

			when(escoReqLoanSer.searchByReq(anyList())).thenReturn(List.of(123, 456));

			mockMvc.perform(post("/api/v1/escrow/loan-requirment/search-by-requirments").contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(requestDto)))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$").isArray())
					.andExpect(jsonPath("$").isNotEmpty())
					.andExpect(jsonPath("$").value(hasItem(123)))
					.andExpect(jsonPath("$").value(hasItem(456)));
	 }


	@Test
	void testDeleteLoanReq() throws Exception {
		List<String> reqNameList = List.of("Property Taxes", "Property Penalty Taxes");
		 LoanReqDto loanReqDto = new LoanReqDto(1001,reqNameList);

	        // Mocking the service behavior
	        doNothing().when(escoReqLoanSer).delete(loanReqDto);

	        // Prepare HTTP put request
	        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/escrow/loan-requirment/delete")
	                .contentType(MediaType.APPLICATION_JSON)
	                .content(objectMapper.writeValueAsString(loanReqDto)))
	                // Validate status
	                .andExpect(status().isAccepted())
	                // Validate content type
	                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
	                // Validate response body
	                .andExpect(jsonPath("$.statusCode").value("202"))
	                .andExpect(jsonPath("$.statusMsg").value(EscrowReqLoanProdConstants.DELETED));
	}

}
