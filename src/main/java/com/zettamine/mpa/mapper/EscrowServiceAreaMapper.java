package com.zettamine.mpa.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.zettamine.mpa.escrow.dto.EscrowServiceAreaDto;
import com.zettamine.mpa.escrow.entity.EscrowServiceArea;

@Mapper(componentModel = "spring")
public interface EscrowServiceAreaMapper {
	
	EscrowServiceArea toEntity(EscrowServiceAreaDto serviceAreaDto);
	
//    @Mapping(target = "escrow", ignore = true)
	EscrowServiceAreaDto toDto(EscrowServiceArea serviceArea);
	
	List<EscrowServiceAreaDto> toDTOList(List<EscrowServiceArea> serviceAreas);
	
	List<EscrowServiceArea> toEntityList(List<EscrowServiceAreaDto> serviceAreas);
}
