 package com.zettamine.mpa.escrow.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.zettamine.mpa.escrow.constants.EscrowCommonConstants;
import com.zettamine.mpa.escrow.constants.EscrowConstants;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(
	    name = "Escrow",
	    description = "Schema to hold Escrow information"
	)
public class EscrowDto {
	
//	private Integer id;
	
	@NotBlank(message = EscrowConstants.NAME_VALIDATION)
    @Pattern(regexp= EscrowCommonConstants.NAMES_REGEX, message = EscrowCommonConstants.INVALID_NAME)
	@Schema(
	        description = "Name of the escrow company",
	        example = "ALPHA ESCROW SERVICES"
	    )
	private String escoName;
	
	@NotBlank(message = EscrowConstants.ADDRESS_VALIDATION)
	 @Schema(
		        description = "Address of the escrow company",
		        example = "123 Main St"
		    )
	private String address;
	
	@NotBlank(message = EscrowCommonConstants.CITY_VALIDATION)
    @Pattern(regexp= EscrowCommonConstants.NAMES_REGEX, message = EscrowCommonConstants.INVALID_NAME)
	@Schema(
	        description = "City where the escrow company is located",
	        example = "LOS ANGELES"
	    )
	private String city;
	
	@NotBlank(message = EscrowCommonConstants.STATE_VALIDATION)
    @Pattern(regexp= EscrowCommonConstants.NAMES_REGEX, message = EscrowCommonConstants.INVALID_NAME)
	 @Schema(
		        description = "State where the escrow company is located",
		        example = "WASHINGTON"
		    )
	private String state;
	
	@NotBlank(message = EscrowCommonConstants.CITY_VALIDATION)
    @Pattern(regexp= EscrowCommonConstants.ZIPCODE_REGEX, message = EscrowCommonConstants.INVALID_ZIPCODE)
	 @Schema(
		        description = "Zipcode of the city where the escrow company is located",
		        example = "90001"
		    )
	@NotNull(message = EscrowCommonConstants.ZIPCODE_VALIDATION)
	private String zipcode;
	
	@NotEmpty(message = EscrowCommonConstants.PHONE_VALIDATION)
    @Pattern(regexp= EscrowCommonConstants.PHONE_REGEX, message = EscrowCommonConstants.INVALID_PHONE_NUMBER)
	@Schema(
	        description = "Phone number of the escrow company",
	        example = "123-456-7890"
	    )
	private String phone;
	
	@NotEmpty(message = EscrowCommonConstants.EMAIL_VALIDATION)
	@Pattern(regexp = EscrowCommonConstants.EMAIL_REGEX, message = EscrowCommonConstants.INVALID_EMAIL)
	@Schema(
	        description = "Email address of the escrow company",
	        example = "example@example.com"
	    )
	private String email;
	
	@NotEmpty(message = EscrowConstants.ESCROW_ACCOUNT_VALIDAATION)
	@Pattern(regexp = EscrowCommonConstants.ACCOUNT_NUMBER_REGEX, message = EscrowConstants.INVALID_ACCOUNT_NUMBER)
	 @Schema(
		        description = "Account number of the escrow company",
		        example = "748939805490"
		    )
	private String inEscrowAcNo;
	
	@NotEmpty(message = EscrowConstants.ESCROW_BANK_NAME_VALIDATION)
	@Pattern(regexp = EscrowCommonConstants.NAMES_REGEX, message = EscrowCommonConstants.INVALID_NAME)
	 @Schema(
		        description = "Bank name of the escrow company",
		        example = "BANK OF WESTON"
		    )
	private String esAcBankName;
	
	@Schema(
	        description = "Estimated processing time of the escrow company in days",
	        example = "30"
	    )
	private Integer esProcessTime;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	@Valid
    private List<EscrowServiceAreaDto> serviceArea = new ArrayList<>();
	
	@JsonProperty(access = Access.WRITE_ONLY)
	@Valid
    private List<EscrowAgentDto> escrowAgent = new ArrayList<>();
       
              
}
