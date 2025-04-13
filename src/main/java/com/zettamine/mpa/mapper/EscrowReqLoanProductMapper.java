package com.zettamine.mpa.mapper;

import java.util.List;
import java.util.Optional;

import org.mapstruct.Mapper;

import com.zettamine.mpa.escrow.dto.EscrowReqLoanProductDto;
import com.zettamine.mpa.escrow.entity.EscrowReqLoanProduct;

@Mapper(componentModel = "spring")
public interface EscrowReqLoanProductMapper {
	
	public EscrowReqLoanProduct toEntity(EscrowReqLoanProductDto escoReqLoanProdDto);

	public EscrowReqLoanProductDto toDto(EscrowReqLoanProduct byId);

	public List<EscrowReqLoanProduct> toEntity(List<EscrowReqLoanProductDto> loanReqList);

	public List<EscrowReqLoanProductDto> toDto(List<EscrowReqLoanProduct> reqLoan);

}
