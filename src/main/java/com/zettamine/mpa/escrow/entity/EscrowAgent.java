package com.zettamine.mpa.escrow.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "escrow_agent")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EscrowAgent extends BaseEntity {
		
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "escrow_agent_id")
	private Integer escrowAgentId;
	 
	
	@Column(name = "escrow_licence_id", unique = true)
	private String escrowAgentLicenceId;
	@Column(name = "first_name")
	private String firstName;
	@Column(name = "last_name")
	private String lastName;
	@Column(unique = true)
	private String email;
	@Column(unique = true)
	private String phone;
	@Column(name = "avg_tx_vol")
	private Integer avgTxVol;
	@Column(name = "tx_success_rate")
	private Float txSuccessRate;
	@Column(name = "escrow_sw")
	private String escrowSw;
	
	 
	 @ManyToOne
	 @JoinColumn(name = "esco_id", updatable = false)
	 private Escrow escrow;
}
