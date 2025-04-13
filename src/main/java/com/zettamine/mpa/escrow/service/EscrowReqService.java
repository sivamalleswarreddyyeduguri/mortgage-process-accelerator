package com.zettamine.mpa.escrow.service;

import java.util.List;
import java.util.Optional;

import com.zettamine.mpa.escrow.dto.EscrowReqDto;
import com.zettamine.mpa.escrow.entity.EscrowReq;

public interface EscrowReqService {
	
	List<EscrowReqDto> getAll();
	
	EscrowReqDto findById(Integer escId);
	
	void update(EscrowReqDto escReqDto, Integer reqId);
	
	void save(List<EscrowReqDto> reqList);
	
	EscrowReqDto findByReqName(String reqName);
	
	List<EscrowReqDto> findByReqName(List<String> reqName);
	
	List<String> getAllReq();

	EscrowReqDto findByReqId(Integer reqId);


}
