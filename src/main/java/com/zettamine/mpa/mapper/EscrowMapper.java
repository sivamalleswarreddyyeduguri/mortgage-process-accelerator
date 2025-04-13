package com.zettamine.mpa.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.zettamine.mpa.escrow.dto.EscrowDto;
import com.zettamine.mpa.escrow.dto.EscrowFetchDto;
import com.zettamine.mpa.escrow.entity.Escrow;

@Mapper(componentModel = "spring")
public interface EscrowMapper {
	
	@Mapping(target = "serviceArea", ignore = true)
	@Mapping(target = "escrowAgent", ignore = true)
	Escrow toEntity(EscrowDto dto);
	
	@Mapping(target = "serviceArea", ignore = true)
	@Mapping(target = "escrowAgent", ignore = true)
	EscrowDto toDto(Escrow escrow);
	
	List<EscrowDto> toDtoList(List<Escrow> entities);
	
	EscrowFetchDto toFetchDto(Escrow escrow);
	
}
