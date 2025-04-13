package com.zettamine.mpa.escrow.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.zettamine.mpa.escrow.constants.EscrowReqConstants;
import com.zettamine.mpa.escrow.dto.EscrowReqDto;
import com.zettamine.mpa.escrow.entity.EscrowReq;
import com.zettamine.mpa.escrow.repository.EscrowRequirmentRepository;
import com.zettamine.mpa.escrow.serviceImpl.EscrowReqServiceImpl;
import com.zettamine.mpa.escrow.utils.StringUtil;
import com.zettamine.mpa.exception.RequirmentNotPresentException;
import com.zettamine.mpa.mapper.EscrowReqMapper;

@SpringBootTest
public class EscrowReqServiceTest {

    @Mock
    private EscrowRequirmentRepository escrowReqRepo;

    @Mock
    private EscrowReqMapper escrowReqMapper;

    @InjectMocks
    private EscrowReqServiceImpl escrowReqService;
    
    private StringUtil stringUtil;

    @Test
    void testGetAll() {
        List<EscrowReq> escrowReqs = Collections.singletonList(new EscrowReq());
        List<EscrowReqDto> expected = Collections.singletonList(new EscrowReqDto());

        when(escrowReqRepo.findAll()).thenReturn(escrowReqs);
        when(escrowReqMapper.toDto(escrowReqs)).thenReturn(expected);

        List<EscrowReqDto> result = escrowReqService.getAll();
        assertEquals(expected, result);
    }

    @Test
    void testFindById() {
        int reqId = 1;
        EscrowReq escrowReq = new EscrowReq();
        escrowReq.setReqId(reqId);
        EscrowReqDto expected = new EscrowReqDto();

        
        when(escrowReqRepo.findById(reqId)).thenReturn(Optional.of(escrowReq));
        when(escrowReqMapper.toDto(escrowReq)).thenReturn(expected);

        
        EscrowReqDto result = escrowReqService.findById(reqId);
        assertEquals(expected, result);
    }

    @Test
    void testFindByIdNegative() {
        int reqId = 1;

        when(escrowReqRepo.findById(reqId)).thenReturn(Optional.empty());

        EscrowReqDto result = escrowReqService.findById(reqId);
        assertNull(result);
    }

    @Test
    void testUpdatePositive() {
        int reqId = 187;
        EscrowReqDto escrowReqDto = new EscrowReqDto();
        EscrowReq escrowReq = new EscrowReq();

        when(escrowReqRepo.findById(reqId)).thenReturn(Optional.of(escrowReq));

        escrowReqService.update(escrowReqDto, reqId);

        verify(escrowReqRepo, times(1)).save(any());
    }

    @Test
    void testUpdateNegative() {
        
        Integer id = 152;
        EscrowReqDto escReqDto = new EscrowReqDto("Property Taxes", "Escrow account must cover property taxes");

        when(escrowReqRepo.findById(id)).thenReturn(Optional.empty());
        
        RequirmentNotPresentException exception = assertThrows(RequirmentNotPresentException.class,
                () -> escrowReqService.update(escReqDto, id));

        verify(escrowReqRepo, times(1)).findById(id);

        assertEquals(escReqDto.getReqName() + " " + EscrowReqConstants.NOT_AVAILABLE, exception.getMessage());
    }

    @Test
    void testSave() {

        List<EscrowReqDto> reqList = Arrays.asList(
            new EscrowReqDto("TITLE INSURANCE PREMIUMS", "Escrow account must cover premiums for title insurance to protect against title defects"),
            new EscrowReqDto("NATURAL DIASTER INSURANCE", "Escrow account must cover PREMIUMS for insurance")
        );
        assertDoesNotThrow(() -> escrowReqService.save(reqList));
    }
    
    @Test
	void testFindByReq() throws Exception {

		String reqName = "Naturial Disaster Insurance";
		String desc = "description of req";
		String normalizedReqName = StringUtil.normalizeString(reqName);
		EscrowReq escrowReq = new EscrowReq(101,reqName,desc);
		EscrowReqDto expectedDto = new EscrowReqDto(reqName, desc);
		when(escrowReqRepo.findByReqName(normalizedReqName)).thenReturn(Optional.of(escrowReq));
		when(escrowReqMapper.toDto(escrowReq)).thenReturn(expectedDto);

		
		EscrowReqDto actualDto = escrowReqService.findByReqName(reqName);

		assertEquals(expectedDto, actualDto);
		verify(escrowReqRepo).findByReqName(normalizedReqName);
		verify(escrowReqMapper).toDto(escrowReq);
	}
    
    @Test
    public void testFindByReqNameNegative() {
        
        String reqName = "Non-existent Requirement";
        String normalizedReqName = StringUtil.normalizeString(reqName);
        when(escrowReqRepo.findByReqName(normalizedReqName)).thenReturn(Optional.empty());

        assertThrows(RequirmentNotPresentException.class, () -> {
            escrowReqService.findByReqName(reqName);
        });
        verify(escrowReqRepo).findByReqName(normalizedReqName);
        verifyNoInteractions(escrowReqMapper); 
    }
    
    @Test
    public void testFindByReqNamePositive() throws Exception {
      
        List<String> reqNames = List.of("Proerty Taxes", "Liability Insurance", "Property Penality Taxes","HMO ASSESSMENTS");
        List<String> normalizedReqNames = StringUtil.formatEscrowReq(reqNames);
        List<EscrowReq> mockReqList = List.of(
            new EscrowReq(101,"Proerty Taxes","Description of property taxes"),
            new EscrowReq(102, "Liability Insurance","Description"),
            new EscrowReq(103, "Property Penality Taxes","Description"),
            new EscrowReq(104, "HMO ASSESSMENTS","Description")
        );
        List<EscrowReqDto> expectedDtoList = List.of(
            new EscrowReqDto("Proerty Taxes","Description of property taxes"),
            new EscrowReqDto( "Liability Insurance","Description"),
            new EscrowReqDto("Property Penality Taxes","Description"),
            new EscrowReqDto("HMO ASSESSMENTS","Description")
            
        );
        when(escrowReqRepo.findByReqName(normalizedReqNames)).thenReturn(mockReqList);
        when(escrowReqMapper.toDto(mockReqList)).thenReturn(expectedDtoList);


        List<EscrowReqDto> actualDtoList = escrowReqService.findByReqName(reqNames);

        assertEquals(expectedDtoList, actualDtoList);
        verify(escrowReqRepo).findByReqName(normalizedReqNames);
        verify(escrowReqMapper).toDto(mockReqList);
    }

    @Test
    public void testFindByReqNameFail() {
     
        List<String> reqNames = List.of("Non-existentReq1", "Non-existentReq2");
        List<String> normalizedReqNames = StringUtil.formatEscrowReq(reqNames);
        when(escrowReqRepo.findByReqName(normalizedReqNames)).thenReturn(Collections.emptyList());

        List<EscrowReqDto> actualDtoList = escrowReqService.findByReqName(reqNames);

        assertTrue(actualDtoList.isEmpty());
        verify(escrowReqRepo).findByReqName(normalizedReqNames);
    }
    
    @Test
    public void testGetAllReq() {
    	
        List<String> expectedReqNames = List.of("Proerty Taxes", "Liability Insurance", "Property Penality Taxes","HMO ASSESSMENTS");
        when(escrowReqRepo.findAllReqName()).thenReturn(expectedReqNames);

        List<String> actualReqNames = escrowReqService.getAllReq();
       
        assertEquals(expectedReqNames, actualReqNames);
        verify(escrowReqRepo).findAllReqName();
    }
    
    @Test
    public void testFindByReqId() {
       
        Integer reqId = 123;
        EscrowReqDto expectedDto = new EscrowReqDto("Property Penality Taxes","Description");
        when(escrowReqRepo.findByReqId(reqId)).thenReturn(expectedDto);

        EscrowReqDto actualDto = escrowReqService.findByReqId(reqId);
        assertEquals(expectedDto, actualDto);
        verify(escrowReqRepo).findByReqId(reqId);
    }

}
