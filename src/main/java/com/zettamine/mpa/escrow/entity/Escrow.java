package com.zettamine.mpa.escrow.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "escrow_company")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Escrow extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "esco_id")
    @SequenceGenerator(name = "esco_id", initialValue = 101, allocationSize = 1)
	@Column(name = "esco_id")
	private Integer escoId;
	
    @Column(name = "esco_name", unique = true)
	private String escoName;
	private String address;
	private String city;
	private String state;
	private String zipcode;
	@Column(name = "phone", unique = true)
	private String phone;
	@Column(name = "email", unique = true)
	private String email;
	@Column(name = "in_escrow_ac_no", unique = true)
	private String inEscrowAcNo;
	@Column(name = "es_ac_bank_name")
	private String esAcBankName;
	@Column(name = "es_process_time")
	private Integer esProcessTime;
	
	
	@OneToMany(mappedBy = "escrow", cascade = CascadeType.ALL)
    private List<EscrowServiceArea> serviceArea;
	
	
	@OneToMany(mappedBy = "escrow", cascade = CascadeType.ALL)
    private List<EscrowAgent> escrowAgent;
}
