package com.zettamine.mpa.escrow.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@IdClass(ReqLoanCompositePrimaryKey.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EscrowReqLoanProduct {
	
	
	@Id
	@Column(name = "req_id")
	private Integer reqId;
	@Id
	@Column(name = "prod_id")
	private Integer prodId;
	
	@Column(name = "active")
    private String active = "active";

    @PrePersist
    public void prePersist() {
        if (this.active == null) {
            this.active = "active"; 
        }
    }
}
