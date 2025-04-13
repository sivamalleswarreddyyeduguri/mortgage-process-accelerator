package com.zettamine.mpa.escrow.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.zettamine.mpa.escrow.dto.EscrowReqDto;
import com.zettamine.mpa.escrow.entity.EscrowReq;

public interface EscrowRequirmentRepository extends JpaRepository<EscrowReq, Serializable> {

	Optional<EscrowReq> findByReqName(String reqName);

	 @Query("SELECT e FROM EscrowReq e WHERE e.reqName IN :reqName")
	    List<EscrowReq> findByReqName(List<String> reqName);

	 @Query("SELECT reqName FROM EscrowReq ")
	  List<String> findAllReqName();

	 EscrowReqDto findByReqId(Integer reqId);

}
