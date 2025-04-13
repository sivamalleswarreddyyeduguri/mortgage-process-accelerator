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
import com.zettamine.mpa.escrow.constants.EscrowCommonConstants;
import com.zettamine.mpa.escrow.dto.EscrowSaveDto;
import com.zettamine.mpa.escrow.dto.EscrowServiceAreaDto;
import com.zettamine.mpa.escrow.service.EscrowServiceAreaService;

@AutoConfigureMockMvc
@SpringBootTest
class EscrowServiceAreaControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Mock
	private EscrowServiceAreaService serviceAreaService;

	@InjectMocks
	private EscrowServiceAreaController serviceAreaController;

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp() throws Exception {
		mockMvc = MockMvcBuilders.standaloneSetup(serviceAreaController).build();
	}

	@Test
	void testAddServiceArea() throws Exception {
		EscrowServiceAreaDto serviceAreaDto = new EscrowServiceAreaDto();
		serviceAreaDto.setCounty("LOS ANGELES");
		serviceAreaDto.setCity("LOS ANGELES");
		serviceAreaDto.setState("CALIFORNIA");
		serviceAreaDto.setZipcode("90001");

		List<EscrowServiceAreaDto> serviceAreaList = new ArrayList<>();
		serviceAreaList.add(serviceAreaDto);

		EscrowSaveDto escrowSaveDto = new EscrowSaveDto();
		escrowSaveDto.setEscoName("ALPHA ESCROW SERVICES");
		escrowSaveDto.setServiceAreas(serviceAreaList);

		doNothing().when(serviceAreaService).save(escrowSaveDto);

		mockMvc.perform(post("/api/v1/escrow/service-area/create").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(escrowSaveDto))).andExpect(status().isCreated())
				.andExpect(jsonPath("$.statusCode").value(EscrowCommonConstants.STATUS_201));
	}

	@Test
	void testFetchAllServiceAreas() throws Exception {
		EscrowServiceAreaDto serviceAreaDto1 = new EscrowServiceAreaDto();
		serviceAreaDto1.setCounty("LOS ANGELES");
		serviceAreaDto1.setCity("LOS ANGELES");
		serviceAreaDto1.setState("CALIFORNIA");
		serviceAreaDto1.setZipcode("90001");

		EscrowServiceAreaDto serviceAreaDto2 = new EscrowServiceAreaDto();
		serviceAreaDto2.setCounty("ORANGE");
		serviceAreaDto2.setCity("IRVINE");
		serviceAreaDto2.setState("CALIFORNIA");
		serviceAreaDto2.setZipcode("92618");

		when(serviceAreaService.getAll()).thenReturn(List.of(serviceAreaDto1, serviceAreaDto2));

		mockMvc.perform(get("/api/v1/escrow/service-area/fetch").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$[0].county").value("LOS ANGELES"))
				.andExpect(jsonPath("$[0].city").value("LOS ANGELES"))
				.andExpect(jsonPath("$[0].state").value("CALIFORNIA"))
				.andExpect(jsonPath("$[0].zipcode").value("90001")).andExpect(jsonPath("$[1].county").value("ORANGE"))
				.andExpect(jsonPath("$[1].city").value("IRVINE")).andExpect(jsonPath("$[1].state").value("CALIFORNIA"))
				.andExpect(jsonPath("$[1].zipcode").value("92618"));
	}

	@Test
	void testFetchServiceAreaById() throws Exception {
		EscrowServiceAreaDto serviceAreaDto = new EscrowServiceAreaDto();
		serviceAreaDto.setCounty("LOS ANGELES");
		serviceAreaDto.setCity("LOS ANGELES");
		serviceAreaDto.setState("CALIFORNIA");
		serviceAreaDto.setZipcode("90001");

		when(serviceAreaService.findById(101)).thenReturn(serviceAreaDto);

		mockMvc.perform(
				get("/api/v1/escrow/service-area/fetch/{serviceAreaId}", 101).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.county").value("LOS ANGELES"))
				.andExpect(jsonPath("$.city").value("LOS ANGELES")).andExpect(jsonPath("$.state").value("CALIFORNIA"))
				.andExpect(jsonPath("$.zipcode").value("90001"));
	}

	@Test
	void testUpdateEscrowServiceArea() throws Exception {
		EscrowServiceAreaDto serviceAreaDto = new EscrowServiceAreaDto();
		serviceAreaDto.setCounty("LOS ANGELES");
		serviceAreaDto.setCity("LOS ANGELES");
		serviceAreaDto.setState("CALIFORNIA");
		serviceAreaDto.setZipcode("90001");

		when(serviceAreaService.update(serviceAreaDto, 101)).thenReturn(true);

		mockMvc.perform(put("/api/v1/escrow/service-area/update/{serviceAreaId}", 101)
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(serviceAreaDto)))
				.andExpect(status().isOk());
	}
	
	@Test
	void testUpdateEscrowServiceArea_Negative() throws Exception {
		EscrowServiceAreaDto serviceAreaDto = new EscrowServiceAreaDto();
		serviceAreaDto.setCounty("LOS ANGELES");
		serviceAreaDto.setCity("LOS ANGELES");
		serviceAreaDto.setState("CALIFORNIA");
		serviceAreaDto.setZipcode("90001");
		
		when(serviceAreaService.update(serviceAreaDto, 101)).thenReturn(false);
		
		mockMvc.perform(put("/api/v1/escrow/service-area/update/{serviceAreaId}", 101)
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(serviceAreaDto)))
		.andExpect(status().isNotFound());
	}

	@Test
	void testFetchServiceAreasByEscrowName() throws Exception {
		String escoName = "ALPHA ESCROW SERVICES";
		Map<String, Object> serviceAreasMap = new HashMap<>();
		serviceAreasMap.put("area1", "LOS ANGELES");
		serviceAreasMap.put("area2", "IRVINE");

		when(serviceAreaService.findByEscrowName(escoName)).thenReturn(serviceAreasMap);

		mockMvc.perform(get("/api/v1/escrow/service-area/fetch-by-escrow-name/{escoName}", escoName)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.area1").value("LOS ANGELES")).andExpect(jsonPath("$.area2").value("IRVINE"));
	}
	
}
