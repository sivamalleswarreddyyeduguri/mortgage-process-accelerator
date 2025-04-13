package com.zettamine.mpa.escrow.controller;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zettamine.mpa.escrow.constants.EscrowCommonConstants;
import com.zettamine.mpa.escrow.constants.EscrowReqConstants;
import com.zettamine.mpa.escrow.constants.EscrowReqLoanProdConstants;
import com.zettamine.mpa.escrow.dto.EscrowReqDto;
import com.zettamine.mpa.escrow.dto.ResponseDto;
import com.zettamine.mpa.escrow.service.EscrowReqService;
import com.zettamine.mpa.exception.RequirmentNotPresentException;

@AutoConfigureMockMvc
@SpringBootTest
class EscrowReqControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Mock
	private EscrowReqService escoReqService;

	@InjectMocks
	private EscrowReqController escoReqController;

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(escoReqController).build();
	}

	@AfterEach
	void tearDown() throws Exception {
		mockMvc = null;
	}

	@Test
	void testSave() throws Exception {
		List<EscrowReqDto> reqDto = new ArrayList<>();
		reqDto.add(new EscrowReqDto("LMO Requirments", "Escrow company must cover lmo req"));
		reqDto.add(new EscrowReqDto("HMO ASSESSMENTS", "Escrow company must cover Hmo req"));

		doNothing().when(escoReqService).save(reqDto);

		mockMvc.perform(post("/api/v1/escrow/requirements/create").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(reqDto))).andExpect(status().isCreated())
				.andExpect(jsonPath("$.statusCode").value(EscrowCommonConstants.STATUS_201))
				.andExpect(jsonPath("$.statusMsg").value(EscrowReqConstants.SAVE_SUCCEESS));
	}

	@Test
	void testFetch() throws Exception {

		List<EscrowReqDto> reqDto = new ArrayList<>();
		reqDto.add(new EscrowReqDto("LMO Requirments", "Escrow company must cover lmo req"));
		reqDto.add(new EscrowReqDto("HMO ASSESSMENTS", "Escrow company must cover Hmo req"));

		when(escoReqService.getAll()).thenReturn(reqDto);

		mockMvc.perform(get("/api/v1/escrow/requirements/fetch").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$[0].reqName").value(reqDto.get(0).getReqName()))
				.andExpect(jsonPath("$[1].reqName").value(reqDto.get(1).getReqName()))
				.andExpect(jsonPath("$[0].description").value(reqDto.get(0).getDescription()))
				.andExpect(jsonPath("$[1].description").value(reqDto.get(1).getDescription()));

	}

	@Test
	void testGetAllReq() throws Exception {

		List<String> reqList = new ArrayList<>();
		reqList.add("LMO Requirments");
		reqList.add("HMO ASSESSMENTS");

		when(escoReqService.getAllReq()).thenReturn(reqList);

		mockMvc.perform(get("/api/v1/escrow/requirements/fetch-req").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$[0]").value("LMO Requirments"))
				.andExpect(jsonPath("$[1]").value("HMO ASSESSMENTS"));
	}

	@Test
	void testFindByReqName() throws Exception {
		String reqName = "LMO Requirments";
		EscrowReqDto reqDto = new EscrowReqDto(reqName, "Escrow company must cover LMO requirements");
		when(escoReqService.findByReqName(reqName)).thenReturn(reqDto);

		mockMvc.perform(get("/api/v1/escrow/requirements/fetch-by-name/{reqName}", reqName)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.reqName").value(reqName))
				.andExpect(jsonPath("$.description").value("Escrow company must cover LMO requirements"));
	}


	@Test
    void testUpdate() throws Exception {
       
        int reqId = 101;
        EscrowReqDto escrowReqDto = new EscrowReqDto("LMO Requirements", "Updated LMO requirements");
        
        when(escoReqService.findById(reqId)).thenReturn(new EscrowReqDto());
        doNothing().when(escoReqService).update(escrowReqDto, reqId);

        mockMvc.perform(put("/api/v1/escrow/requirements/update/{reqId}", reqId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(escrowReqDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(EscrowCommonConstants.STATUS_201))
                .andExpect(jsonPath("$.statusMsg").value(EscrowReqConstants.UPDATE_SUCCESS));
    }
}
