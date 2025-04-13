package com.zettamine.mpa.escrow.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zettamine.mpa.escrow.constants.EscrowAgentConstants;
import com.zettamine.mpa.escrow.constants.EscrowCommonConstants;
import com.zettamine.mpa.escrow.dto.EscrowAgentDto;
import com.zettamine.mpa.escrow.dto.EscrowSaveDto;
import com.zettamine.mpa.escrow.service.EscrowAgentService;


@AutoConfigureMockMvc
@SpringBootTest
class EscrowAgentControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Mock
	private EscrowAgentService escrowAgentService;

	@InjectMocks
	private EscrowAgentController escrowAgentController;

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp() throws Exception {
		mockMvc = MockMvcBuilders.standaloneSetup(escrowAgentController).build();

	}

	@Test
	void testAddEscrowAgent() throws Exception {
	    EscrowAgentDto escrowAgentDto = new EscrowAgentDto();
	    escrowAgentDto.setEscrowAgentLicenceId("EL00123478");
	    escrowAgentDto.setFirstName("John");
	    escrowAgentDto.setLastName("Doe");
	    escrowAgentDto.setEmail("john.doe@example.com");
	    escrowAgentDto.setPhone("123-456-7890");
	    escrowAgentDto.setAvgTxVol(20);
	    escrowAgentDto.setTxSuccessRate(0.95f);
	    escrowAgentDto.setEscrowSw("QUALIA");
	    
	    EscrowSaveDto escrowSaveDto = new EscrowSaveDto();
	    escrowSaveDto.setEscoName("ALPHA ESCROW SERVICES");
	    
	    List<EscrowAgentDto> escrowAgentList = new ArrayList<>();
	    escrowAgentList.add(escrowAgentDto);
	    escrowSaveDto.setEscrowAgent(escrowAgentList);

	    doNothing().when(escrowAgentService).save(escrowSaveDto);

	    mockMvc.perform(post("/api/v1/escrow/agent/create")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(objectMapper.writeValueAsString(escrowSaveDto)))
	            .andExpect(status().isCreated())
	            .andExpect(jsonPath("$.statusCode").value(EscrowCommonConstants.STATUS_201)) 
	            .andExpect(jsonPath("$.statusMsg").value(EscrowAgentConstants.SAVE_AGENT_SUCCEESS)); 
	}
	
	@Test
    void testFetchAllEscrowAgents() throws Exception {
        List<EscrowAgentDto> agentDtoList = new ArrayList<>();
        agentDtoList.add(new EscrowAgentDto("EL00123478", "John", "Doe", "john.doe@example.com", "123-456-7890", 20, 0.95f, "QUALIA"));
        agentDtoList.add(new EscrowAgentDto("EL00123479", "Jane", "Smith", "jane.smith@example.com", "987-654-3210", 30, 0.85f, "QUALIA"));
        when(escrowAgentService.getAll()).thenReturn(agentDtoList);

        mockMvc.perform(get("/api/v1/escrow/agent/fetch")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].escrowAgentLicenceId").value(agentDtoList.get(0).getEscrowAgentLicenceId()))
                .andExpect(jsonPath("$[0].firstName").value(agentDtoList.get(0).getFirstName()))
                .andExpect(jsonPath("$[1].escrowAgentLicenceId").value(agentDtoList.get(1).getEscrowAgentLicenceId()))
                .andExpect(jsonPath("$[1].firstName").value(agentDtoList.get(1).getFirstName()));
    }
	
	@Test
	void testfetchEscrowAgentById() throws Exception {
		EscrowAgentDto expectedDto = new EscrowAgentDto("EL00123478", "John", "Doe", "john.doe@example.com", "123-456-7890", 20, 0.95f, "QUALIA");
        when(escrowAgentService.findById(101)).thenReturn(expectedDto);
        mockMvc.perform(get("/api/v1/escrow/agent/fetch/{escrowAgentId}", 101)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.escrowAgentLicenceId").value("EL00123478"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.phone").value("123-456-7890"))
                .andExpect(jsonPath("$.avgTxVol").value(20))
                .andExpect(jsonPath("$.txSuccessRate").value(0.95))
                .andExpect(jsonPath("$.escrowSw").value("QUALIA"));
    }

	@Test
    void testUpdateEscrowAgentById() throws Exception {
        EscrowAgentDto escrowAgentDto = new EscrowAgentDto("EL00123478", "John", "Doe", "john.doe@example.com", "123-456-7890", 20, 0.95f, "QUALIA");

        when(escrowAgentService.update(escrowAgentDto, 101)).thenReturn(true);

        mockMvc.perform(put("/api/v1/escrow/agent/update/{escrowAgentId}", 101)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(escrowAgentDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(EscrowCommonConstants.STATUS_200))
                .andExpect(jsonPath("$.statusMsg").value(EscrowAgentConstants.UPDATE_AGENT_SUCCESS));
    }
	
	@Test
	void testUpdateEscrowAgentById_NotFound() throws Exception {
		EscrowAgentDto escrowAgentDto = new EscrowAgentDto("EL00123478", "John", "Doe", "john.doe@example.com", "123-456-7890", 20, 0.95f, "QUALIA");
		
		when(escrowAgentService.update(escrowAgentDto, 101)).thenReturn(false);
		
		mockMvc.perform(put("/api/v1/escrow/agent/update/{escrowAgentId}", 101)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(escrowAgentDto)))
		.andExpect(status().isNotFound())
		.andExpect(jsonPath("$.statusCode").value(EscrowCommonConstants.STATUS_404))
		.andExpect(jsonPath("$.statusMsg").value(EscrowAgentConstants.ESCROW_AGENT__NOT_FOUND + 101));
	}
	
	@Test
	void testFetchEscrowAgentsByEscrowName() throws Exception {
		String escoName = "ALPHA ESCROW SERVICES";
        Map<String, Object> agentsMap = new HashMap<>();
        agentsMap.put("agent1", "John Doe");
        agentsMap.put("agent2", "Jane Smith");
        when(escrowAgentService.findByEscrowName(escoName)).thenReturn(agentsMap);
        mockMvc.perform(get("/api/v1/escrow/agent/fetch-by-escrow-name/{escoName}", escoName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.agent1").value("John Doe"))
                .andExpect(jsonPath("$.agent2").value("Jane Smith"));
	}

}
