package com.zettamine.mpa.escrow.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.util.Streamable;

import com.zettamine.mpa.escrow.entity.Escrow;
import com.zettamine.mpa.escrow.entity.EscrowAgent;
import com.zettamine.mpa.escrow.entity.EscrowServiceArea;

public interface EscrowRepository extends JpaRepository<Escrow, Integer> {
			
	  public Optional<Escrow> findByEscoName(String escoName);

	public List<Escrow> findAll(Specification<Escrow> spec);




}
