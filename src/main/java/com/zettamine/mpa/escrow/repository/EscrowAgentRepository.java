package com.zettamine.mpa.escrow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.zettamine.mpa.escrow.entity.EscrowAgent;

public interface EscrowAgentRepository extends JpaRepository<EscrowAgent, Integer> {

	@Query("SELECT ea FROM EscrowAgent ea WHERE ea.escrowAgentLicenceId = :escrowLicenceId AND ea.escrow.escoId = :escoId")
	List<EscrowAgent> findByLicenceIdEscoId(String escrowLicenceId, Integer escoId);
	
	 List<EscrowAgent> findByEscrowEscoId(Integer escoId);

}
