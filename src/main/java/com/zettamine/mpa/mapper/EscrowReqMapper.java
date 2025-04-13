package com.zettamine.mpa.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.zettamine.mpa.escrow.dto.EscrowReqDto;
import com.zettamine.mpa.escrow.entity.EscrowReq;

@Mapper(componentModel = "spring")
public interface EscrowReqMapper {
	
	 public EscrowReq toEntity(EscrowReqDto escrowReqDto);
	 public EscrowReqDto toDto(EscrowReq escoReq);
	public List<EscrowReqDto> toDto(List<EscrowReq> escoReqList);
	public List<EscrowReq> toEntity(List<EscrowReqDto> reqList);

}
