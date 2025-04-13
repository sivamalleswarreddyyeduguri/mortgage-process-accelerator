package com.zettamine.mpa.escrow.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;

import com.zettamine.mpa.escrow.dto.LoanReqDto;
import com.zettamine.mpa.escrow.dto.EscrowReqDto;
import com.zettamine.mpa.escrow.dto.EscrowReqLoanProductDto;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

public interface EscrowReqLoanProductService {

	List<EscrowReqLoanProductDto> getAll();

	List<String> findByProdId(Integer loanId);

	List<EscrowReqLoanProductDto> findByReqId(Integer reqId);

	List<EscrowReqLoanProductDto> findByReqName(List<String> reqName);

	void save(EscrowReqLoanProductDto escoReqLoanDto);

	void save(List<EscrowReqLoanProductDto> loanReqList);

	EscrowReqLoanProductDto findByReqIdAndProdId(Integer reqId, Integer prodId);

	List<Integer> searchByReq(List<String> reqNameList);

	void save(LoanReqDto reqLoanDto);

	void delete(@Valid LoanReqDto loanReqDto);

	
	List<Integer> getReqIdByName(List<String> reqNames);
}
