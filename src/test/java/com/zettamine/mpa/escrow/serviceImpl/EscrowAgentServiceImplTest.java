package com.zettamine.mpa.escrow.serviceImpl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.zettamine.mpa.escrow.constants.EscrowAgentConstants;
import com.zettamine.mpa.escrow.dto.EscrowAgentDto;
import com.zettamine.mpa.escrow.dto.EscrowSaveDto;
import com.zettamine.mpa.escrow.dto.EscrowServiceAreaDto;
import com.zettamine.mpa.escrow.entity.Escrow;
import com.zettamine.mpa.escrow.entity.EscrowAgent;
import com.zettamine.mpa.escrow.entity.EscrowServiceArea;
import com.zettamine.mpa.escrow.repository.EscrowAgentRepository;
import com.zettamine.mpa.escrow.repository.EscrowRepository;
import com.zettamine.mpa.exception.ResourceNotFoundException;
import com.zettamine.mpa.mapper.EscrowAgentMapper;

@SpringBootTest
class EscrowAgentServiceImplTest {

    @Mock
    private EscrowAgentRepository agentRepository;

    @Mock
    private EscrowRepository escrowRepository;

    @Mock
    private EscrowAgentMapper agentMapper;

    @InjectMocks
    private EscrowAgentServiceImpl agentService;
    
    

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave_Success() {
        EscrowSaveDto saveDto = new EscrowSaveDto();
        saveDto.setEscoName("Test Escrow");
        List<EscrowAgentDto> agents = new ArrayList<>();
        EscrowAgentDto agentDto = new EscrowAgentDto();
        agentDto.setEscrowAgentLicenceId("EL123456");
        agentDto.setFirstName("John");
        agentDto.setLastName("Doe");
        agentDto.setEmail("john.doe@example.com");
        agentDto.setPhone("123-456-7890");
        agentDto.setAvgTxVol(20);
        agentDto.setTxSuccessRate(0.95f);
        agentDto.setEscrowSw("QUALIA");
        agents.add(agentDto);
        saveDto.setEscrowAgent(agents);

        Escrow escrow = new Escrow();
        when(escrowRepository.findByEscoName(anyString())).thenReturn(Optional.of(escrow));
        when(agentMapper.toEntityList(agents)).thenReturn(new ArrayList<>());

        assertDoesNotThrow(() -> agentService.save(saveDto));
    }


    @Test
    void testFindById_Positive() {
        int agentId = 1;
        EscrowAgent escroAgent = new EscrowAgent();
        EscrowAgentDto expectedDto = new EscrowAgentDto();
        when(agentRepository.findById(agentId)).thenReturn(Optional.of(escroAgent));
        when(agentMapper.toDto(escroAgent)).thenReturn(expectedDto);
        EscrowAgentDto actualDto = agentService.findById(agentId);
        assertEquals(expectedDto, actualDto);
    }

    
    @Test
    void testFindById_Negative() {
        int invalidAgentId = 100;
        when(agentRepository.findById(invalidAgentId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            agentService.findById(invalidAgentId);
        });

        String expectedMessage = EscrowAgentConstants.ESCROW_AGENT__NOT_FOUND + invalidAgentId;
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void testGetAll() {
        List<EscrowAgent> agents = new ArrayList<>();
        agents.add(new EscrowAgent());
        when(agentRepository.findAll()).thenReturn(agents);

        assertNotNull(agentService.getAll());
    }

    @Test
    void testFindByEscrowName_Success() {
        Escrow escrow = new Escrow();
        escrow.setEscoName("Test Escrow");
        when(escrowRepository.findByEscoName(anyString())).thenReturn(Optional.of(escrow));

        assertNotNull(agentService.findByEscrowName("Test Escrow"));
    }
    
    @Test
    void testFindByEscrowName_EscrowNotFound() {
        when(escrowRepository.findByEscoName(anyString())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> agentService.findByEscrowName("Test Escrow"));
    }
    
    @Test
    void testUpdate_Success() {
        EscrowAgentDto agentDto = new EscrowAgentDto();
        agentDto.setFirstName("John");
        agentDto.setLastName("Doe");
        agentDto.setEmail("john.doe@example.com");

        EscrowAgent existingAgent = new EscrowAgent();
        existingAgent.setFirstName("John");
        existingAgent.setLastName("Doe");
        existingAgent.setEmail("john.doe@example.com");
        existingAgent.setPhone("123-456-7890");
        existingAgent.setAvgTxVol(20);
        existingAgent.setTxSuccessRate(0.95f);
        existingAgent.setEscrowSw("QUALIA");
        existingAgent.setEscrowAgentLicenceId("el12345678");

        when(agentRepository.findById(10)).thenReturn(Optional.of(existingAgent));
        when(agentMapper.toEntity(any())).thenReturn(existingAgent); 
        boolean result = agentService.update(agentDto, 10);
        assertTrue(result);
        
    }
    
    @Test
    void testUpdate_NotFound() {
        EscrowAgentDto agentDto = new EscrowAgentDto();
        agentDto.setFirstName("John");
        agentDto.setLastName("Doe");
        agentDto.setEmail("john.doe@example.com");
        when(agentRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertFalse(agentService.update(agentDto, 1));
    }
    
    
}


