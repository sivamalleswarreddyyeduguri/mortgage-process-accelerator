package com.zettamine.mpa.escrow.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.zettamine.mpa.escrow.entity.EscrowReqLoanProduct;

import jakarta.transaction.Transactional;

public interface EscrowReqLoanProductRepository extends JpaRepository<EscrowReqLoanProduct, Serializable> {

	public List<EscrowReqLoanProduct> findByProdIdAndActive(Integer prodId, String active);

	public EscrowReqLoanProduct findByReqIdAndProdIdAndActive(Integer reqId, Integer prodId, String active	);

	public List<EscrowReqLoanProduct> findByReqId(Integer reqId);

	@Query(value = "SELECT lr.prod_id " + "FROM escrow_req_loan_product lr " + "WHERE lr.req_id IN :reqIds "
			+ "GROUP BY lr.prod_id " + "HAVING COUNT(DISTINCT lr.req_id) = :reqCount", nativeQuery = true)
	List<Integer> searchByReq(@Param("reqIds") Set<Integer> reqIds, @Param("reqCount") int reqCount);
	
	@Transactional
	@Modifying
	@Query("UPDATE EscrowReqLoanProduct e SET e.active = :active WHERE e.reqId IN :reqIds AND e.prodId = :prodId")
	void updateActiveByReqIdInAndProdId(@Param("reqIds") List<Integer> reqIds, @Param("prodId") Integer prodId,  @Param("active") String active);

	public List<EscrowReqLoanProduct> findAllByActive(String active);

	public EscrowReqLoanProduct findByReqIdAndProdId(Integer reqId, Integer loanId);
}
