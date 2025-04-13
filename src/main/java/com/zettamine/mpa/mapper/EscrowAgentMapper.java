package com.zettamine.mpa.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.zettamine.mpa.escrow.dto.EscrowAgentDto;
import com.zettamine.mpa.escrow.dto.EscrowServiceAreaDto;
import com.zettamine.mpa.escrow.entity.EscrowAgent;

@Mapper(componentModel = "spring")
public interface EscrowAgentMapper {
      EscrowAgent toEntity(EscrowAgentDto agentDto);
      
//      @Mapping(target = "escrow", ignore = true)
      EscrowAgentDto toDto(EscrowAgent agent);
    
     List<EscrowAgentDto> toDTOList(List<EscrowAgent> escrowAgent);

	List<EscrowAgent> toEntityList(List<EscrowAgentDto> list);
 
}
