package com.zettamine.mpa.escrow.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zettamine.mpa.escrow.constants.EscrowCommonConstants;
import com.zettamine.mpa.escrow.constants.EscrowConstants;
import com.zettamine.mpa.escrow.dto.EscrowDto;
import com.zettamine.mpa.escrow.dto.EscrowFetchDto;
import com.zettamine.mpa.escrow.dto.SearchCriteria;
import com.zettamine.mpa.escrow.service.EscrowService;

@AutoConfigureMockMvc
@SpringBootTest
class EscrowControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Mock
	private EscrowService escrowService;

	@InjectMocks
	private EscrowController escrowController;

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp() throws Exception {
		mockMvc = MockMvcBuilders.standaloneSetup(escrowController).build();
	}

	@Test
	void testAddEscrow() throws Exception {
		EscrowDto escrowDto = new EscrowDto("ALPHA ESCROW SERVICES", "123 Main St", "LOS ANGELES", "WASHINGTON",
				"90001", "123-456-7890", "info@abcescrowabcd.com", "748939805490", "BANK OF WESTON", 28, null, null);

		doNothing().when(escrowService).save(escrowDto);
		System.out.println("------------");

		mockMvc.perform(post("/api/v1/escrow/create").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(escrowDto))).andExpect(status().isCreated())
				.andExpect(jsonPath("$.statusCode").value(EscrowCommonConstants.STATUS_201))
				.andExpect(jsonPath("$.statusMsg").value(EscrowConstants.ESCROW_SAVE_SUCCEESS));
		;

	}

	@Test
	void testFetchEscrowCompanies() throws Exception {
		List<EscrowDto> list = new ArrayList<EscrowDto>();
		EscrowDto escrowDto1 = new EscrowDto();
		escrowDto1.setCity("SEATTLE");
		EscrowDto escrowDto2 = new EscrowDto();
		escrowDto2.setCity("LOS ANGELES");
		list.add(escrowDto1);
		list.add(escrowDto2);
		System.err.println("------------------");

		when(escrowService.getAll()).thenReturn(list);

		mockMvc.perform(get("/api/v1/escrow/fetch").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$[1].city").value("LOS ANGELES"))
				.andExpect(jsonPath("$[0].city").value("SEATTLE"));

	}

	@Test
	void testUpdateEscrow() throws Exception {
		EscrowDto escrowDto = new EscrowDto("ALPHA ESCROW SERVICES PQR", "123 Main St", "LOS ANGELES", "WASHINGTON",
				"90001", "123-456-7890", "info@abcescrowabcd.com", "748939805490", "BANK OF WESTON", 28, null, null);

		when(escrowService.update(any(EscrowDto.class), anyInt())).thenReturn(true);

		mockMvc.perform(put("/api/v1/escrow/update/{escoId}", 101).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(escrowDto))).andExpect(status().isOk())
				.andExpect(jsonPath("$.statusCode").value(EscrowCommonConstants.STATUS_200))
				.andExpect(jsonPath("$.statusMsg").value(EscrowConstants.ESCROW_UPDATE_SUCCESS));
	}

	@Test
	void testUpdateEscrow_Negative() throws Exception {
		EscrowDto escrowDto = new EscrowDto("ALPHA ESCROW SERVICES PQR", "123 Main St", "LOS ANGELES", "WASHINGTON",
				"90001", "123-456-7890", "info@abcescrowabcd.com", "748939805490", "BANK OF WESTON", 28, null, null);

		when(escrowService.update(escrowDto, 101)).thenReturn(false);

		mockMvc.perform(put("/api/v1/escrow/update/{escoId}", 101).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(escrowDto))).andExpect(status().isNotFound())
				.andExpect(jsonPath("$.statusCode").value(EscrowCommonConstants.STATUS_404))
				.andExpect(jsonPath("$.statusMsg").value(EscrowConstants.NOT_FOUND + 101));

	}

	@Test
	void testfetchEscrowById() throws Exception {
		EscrowDto expectedDto = new EscrowDto();
		expectedDto.setEscoName("Alpha Escrow Services");
		when(escrowService.findById(101)).thenReturn(expectedDto);
		mockMvc.perform(get("/api/v1/escrow/fetch/{escoId}", 101)).andExpect(status().isOk())
				.andExpect(jsonPath("$.escoName").value("Alpha Escrow Services"));
	}

	@Test
	void testfetchEscrowByName() throws Exception {
		EscrowDto expectedDto = new EscrowDto();
		expectedDto.setEscoName("Alpha Escrow Services");

		when(escrowService.findByEscrowName("Alpha Escrow Services")).thenReturn(expectedDto);

		mockMvc.perform(get("/api/v1/escrow/fetch/name/{escoName}", "Alpha Escrow Services")).andExpect(status().isOk())
				.andExpect(jsonPath("$.escoName").value("Alpha Escrow Services"));
	}

	@Test
	@Disabled
	void testsearchEscrows() throws Exception {
		SearchCriteria searchCriteria = new SearchCriteria();
		searchCriteria.setName("ALPHA ESCROW SERVICES");
		searchCriteria.setState("CALIFORNIA");
		searchCriteria.setCity("LOS ANGELES");
		searchCriteria.setZipcode("90001");
		List<EscrowFetchDto> mockedEscrows = new ArrayList<>();
		mockedEscrows.add(new EscrowFetchDto("Sample ESCO Name", "Sample City", "Sample State", "Sample Zipcode",
				"sample@email.com", "123-456-7890"));

		when(escrowService.search(any(SearchCriteria.class))).thenReturn(mockedEscrows);

		mockMvc.perform(post("/api/v1/escrow/search").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(searchCriteria))).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].escoName", is("Sample ESCO Name")))
				.andExpect(jsonPath("$[0].city", is("Sample City")))
				.andExpect(jsonPath("$[0].state", is("Sample State")))
				.andExpect(jsonPath("$[0].zipcode", is("Sample Zipcode")))
				.andExpect(jsonPath("$[0].email", is("sample@email.com")))
				.andExpect(jsonPath("$[0].phone", is("123-456-7890")));
	}

}
