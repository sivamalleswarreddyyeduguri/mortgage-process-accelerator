package com.zettamine.mpa.escrow.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.zettamine.mpa.escrow.entity.EscrowServiceArea;

public interface EscrowServiceAreaRepository extends JpaRepository<EscrowServiceArea, Integer>{
//	  List<EscrowServiceArea> findByCountyAndCityAndStateAndZipcode(String county, String city, String state, String zipcode);
	  
	  @Query("SELECT esa FROM EscrowServiceArea esa WHERE esa.county = :county AND esa.state = :state AND esa.city = :city AND esa.zipcode = :zipcode AND esa.escrow.escoId = :escrowId")
	  List<EscrowServiceArea> findByCountyStateCityZipcode(@Param("county") String county, @Param("state") String state, @Param("city") String city, @Param("zipcode") String zipcode, @Param("escrowId") Integer escrowId);

	  List<EscrowServiceArea> findByEscrowEscoId(Integer escoId);

	List<EscrowServiceArea> findAll(Specification<EscrowServiceArea> areaSpec);
}
