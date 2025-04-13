package com.zettamine.mpa.escrow.serviceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

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
import com.zettamine.mpa.exception.ResourceNotFoundException;
import com.zettamine.mpa.mapper.EscrowAgentMapper;
import com.zettamine.mpa.mapper.EscrowMapper;
import com.zettamine.mpa.mapper.EscrowServiceAreaMapper;

class EscrowServiceImplTest{
	
	 @Mock
	 private JdbcTemplate jdbcTemplate;

	@Mock
	private EscrowMapper escrowMapper;

	@Mock
	private EscrowServiceAreaMapper serviceAreaMapper;

	@Mock
	private EscrowAgentMapper agentMapper;

	@Mock
	private EscrowRepository escrowRepository;

	@InjectMocks
	private EscrowServiceImpl escrowService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testSave() {
		EscrowDto escrowDto = new EscrowDto();
		escrowDto.setEscoName("ALPHA ESCROW SERVICES");
		escrowDto.setAddress("123 Main St");
		escrowDto.setCity("LOS ANGELES");
		escrowDto.setState("WASHINGTON");
		escrowDto.setZipcode("90001");
		escrowDto.setPhone("123-456-7890");
		escrowDto.setEmail("example@example.com");
		escrowDto.setInEscrowAcNo("748939805490");
		escrowDto.setEsAcBankName("BANK OF WESTON");
		escrowDto.setEsProcessTime(30);

		EscrowServiceAreaDto serviceAreaDto = new EscrowServiceAreaDto();
		serviceAreaDto.setCounty("Los Angeles");
		serviceAreaDto.setCity("Los Angeles");
		serviceAreaDto.setState("California");
		serviceAreaDto.setZipcode("90001");
		List<EscrowServiceAreaDto> serviceAreas = new ArrayList<>();
		serviceAreas.add(serviceAreaDto);

		EscrowAgentDto agentDto = new EscrowAgentDto();
		agentDto.setFirstName("John");
		agentDto.setLastName("Doe");
		agentDto.setEmail("john.doe@example.com");
		agentDto.setPhone("123-456-7890");
		agentDto.setAvgTxVol(20);
		agentDto.setTxSuccessRate(0.95f);
		agentDto.setEscrowSw("QUALIA");
		agentDto.setEscrowAgentLicenceId("el12345678");

		List<EscrowAgentDto> agents = new ArrayList<>();
		agents.add(agentDto);

		escrowDto.setServiceArea(serviceAreas);
		escrowDto.setEscrowAgent(agents);

		Escrow escrowEntity = new Escrow();
		escrowEntity.setEscoName("ALPHA ESCROW SERVICES");
		escrowEntity.setAddress("123 Main St");
		escrowEntity.setCity("LOS ANGELES");
		escrowEntity.setState("WASHINGTON");
		escrowEntity.setZipcode("90001");
		escrowEntity.setPhone("123-456-7890");
		escrowEntity.setEmail("example@example.com");
		escrowEntity.setInEscrowAcNo("748939805490");
		escrowEntity.setEsAcBankName("BANK OF WESTON");
		escrowEntity.setEsProcessTime(30);
		when(escrowMapper.toEntity(escrowDto)).thenReturn(escrowEntity);

		EscrowServiceArea serviceAreaEntity = new EscrowServiceArea();
		serviceAreaEntity.setCounty("Los Angeles");
		serviceAreaEntity.setCity("Los Angeles");
		serviceAreaEntity.setState("California");
		serviceAreaEntity.setZipcode("90001");
		when(serviceAreaMapper.toEntity(serviceAreaDto)).thenReturn(serviceAreaEntity);

		EscrowAgent agentEntity = new EscrowAgent();
		agentEntity.setFirstName("John");
		agentEntity.setLastName("Doe");
		agentEntity.setEmail("john.doe@example.com");
		agentEntity.setPhone("123-456-7890");
		agentEntity.setAvgTxVol(20);
		agentEntity.setTxSuccessRate(0.95f);
		agentEntity.setEscrowSw("QUALIA");
		agentEntity.setEscrowAgentLicenceId("el12345678");
		when(agentMapper.toEntity(agentDto)).thenReturn(agentEntity);

		escrowService.save(escrowDto);

	}

	@Test
	void testFindById_Positive() {
		int escrowId = 1;
		Escrow escrow = new Escrow();
		EscrowDto expectedDto = new EscrowDto();
		when(escrowRepository.findById(escrowId)).thenReturn(Optional.of(escrow));
		when(escrowMapper.toDto(escrow)).thenReturn(expectedDto);

		EscrowDto actualDto = escrowService.findById(escrowId);
		assertEquals(expectedDto, actualDto);
	}

	@Test
	void testFindById_Negative() {
		int invalidEscrowId = 100;

		when(escrowRepository.findById(invalidEscrowId)).thenReturn(Optional.empty());

		ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
			escrowService.findById(invalidEscrowId);
		});

		String expectedMessage = EscrowConstants.ESCROW_NOT_FOUND + invalidEscrowId;
		assertEquals(expectedMessage, exception.getMessage());
	}

	@Test
	void testGetAll() {
		List<Escrow> escrows = new ArrayList<>();
		escrows.add(new Escrow());
		when(escrowRepository.findAll()).thenReturn(escrows);
		assertNotNull(escrowService.getAll());
	}

	void testFindByEscrowName_Success() {
		Escrow escrow = new Escrow();
		escrow.setEscoName("Test Escrow");
		when(escrowRepository.findByEscoName(anyString())).thenReturn(Optional.of(escrow));

		assertNotNull(escrowService.findByEscrowName("Test Escrow"));
	}

	@Test
	void testFindByEscrowName_EscrowNotFound() {
		when(escrowRepository.findByEscoName(anyString())).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> escrowService.findByEscrowName("Test Escrow"));
	}
	
	@Test
	void testUpdate_Success() {
	    EscrowDto escrowDto = new EscrowDto();
	    escrowDto.setEscoName("Alpha Escrow Services");
	    escrowDto.setAddress("123 Main St");
	    escrowDto.setCity("Los Angeles");
	    escrowDto.setState("California");
	    escrowDto.setZipcode("90001");
	    escrowDto.setPhone("123-456-7890");
		escrowDto.setEmail("example@example.com");
		escrowDto.setInEscrowAcNo("748939805490");
		escrowDto.setEsAcBankName("BANK OF WESTON");
		escrowDto.setEsProcessTime(30);
		
		
	    Escrow escrowEntity = new Escrow();
	    escrowEntity.setEscoName("Alpha Escrow Services");
	    escrowEntity.setAddress("123 Main St");
	    escrowEntity.setCity("Los Angeles");
	    escrowEntity.setState("California");
	    escrowEntity.setZipcode("90001");
	    escrowEntity.setEscoId(10); 
	    escrowEntity.setPhone("123-456-7890");
	    escrowEntity.setEmail("example@example.com");
	    escrowEntity.setInEscrowAcNo("748939805490");
	    escrowEntity.setEsAcBankName("BANK OF WESTON");
	    escrowEntity.setEsProcessTime(30);
	    
	    when(escrowRepository.findById(10)).thenReturn(Optional.of(escrowEntity));
	    when(escrowMapper.toEntity(any())).thenReturn(escrowEntity); // Mock the mapper

	    boolean result = escrowService.update(escrowDto, 10);
	    assertTrue(result);
	}
	
	@Test
	void testUpdate_NotFound() {
	    EscrowDto escrowDto = new EscrowDto();
	    escrowDto.setEscoName("Alpha Escrow Services");
	    escrowDto.setAddress("123 Main St");
	    escrowDto.setCity("Los Angeles");
	    escrowDto.setState("California");
	    escrowDto.setZipcode("90001");
	    escrowDto.setPhone("123-456-7890");
		escrowDto.setEmail("example@example.com");
		escrowDto.setInEscrowAcNo("748939805490");
		escrowDto.setEsAcBankName("BANK OF WESTON");
		escrowDto.setEsProcessTime(30);

	    when(escrowRepository.findById(anyInt())).thenReturn(Optional.empty());

	    assertFalse(escrowService.update(escrowDto, 1));
	}
	

	@Test
	@Disabled
    public void testSearch_AllCriteriaNull() {
        SearchCriteria criteria = new SearchCriteria();
        List<EscrowFetchDto> result = escrowService.search(criteria);
        assertEquals(0, result.size());
    }

    @Test
    @Disabled
    public void testSearch_NameNotNull() {
        SearchCriteria criteria = new SearchCriteria();
        criteria.setName("TestName");
        criteria.setCity("TestName");
        criteria.setState("TestName");
        criteria.setZipcode("TestName");
        ResultSet resultSet = mockResultSet();

        mockJdbcTemplateQuery(resultSet);

        List<EscrowFetchDto> result = escrowService.search(criteria);

        assertTrue(result.size() > 0);
    }

    private ResultSet mockResultSet() {
        ResultSet resultSet = mock(ResultSet.class);
        try {
            when(resultSet.next()).thenReturn(true).thenReturn(false); // Simulate one row of result
            when(resultSet.getString("esco_name")).thenReturn("TestName");
            when(resultSet.getString("state")).thenReturn("TestState");
            when(resultSet.getString("city")).thenReturn("TestCity");
            when(resultSet.getString("zipcode")).thenReturn("TestZipcode");
            when(resultSet.getString("email")).thenReturn("test@example.com");
            when(resultSet.getString("phone")).thenReturn("1234567890");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    private void mockJdbcTemplateQuery(ResultSet resultSet) {
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), any(Object[].class)))
                .thenAnswer(invocation -> {
                    RowMapper<EscrowFetchDto> rowMapper = invocation.getArgument(1);
                    EscrowFetchDto escrowFetchDto = rowMapper.mapRow(resultSet, 1);
                    List<EscrowFetchDto> resultList = new ArrayList<>();
                    resultList.add(escrowFetchDto);
                    return resultList;
                });
    }

	
	
}
