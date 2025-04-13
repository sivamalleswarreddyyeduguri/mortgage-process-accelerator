package com.zettamine.mpa.escrow.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "escrow_requirements")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EscrowReq extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "req_id_generator")
	@SequenceGenerator(name = "req_id_generator", sequenceName = "escrow_requirements_req_id_seq", allocationSize = 1, initialValue = 101)
	private Integer reqId;

	@Column(name = "req_name", unique = true)
	private String reqName;

	private String description;

}
