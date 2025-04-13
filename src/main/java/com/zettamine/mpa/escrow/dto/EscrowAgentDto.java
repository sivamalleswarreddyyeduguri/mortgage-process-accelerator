package com.zettamine.mpa.escrow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.zettamine.mpa.escrow.constants.EscrowAgentConstants;
import com.zettamine.mpa.escrow.constants.EscrowCommonConstants;
import com.zettamine.mpa.escrow.constants.EscrowConstants;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(
	    name = "Escrow Agent",
	    description = "Schema to hold Escrow Agent information"
	)
public class EscrowAgentDto {
	
	@NotBlank(message = EscrowAgentConstants.LICENCE_VALIDATION)
    @Pattern(regexp= EscrowCommonConstants.ESC_LINCENCE_ID_REGEX, message = EscrowConstants.INVALID_ESC_LIENCE_ID)
	@Schema(
	        description = "Licence ID of the escrow agent",
	        example = "EL00123478"
	    )
	private String escrowAgentLicenceId;
	
	@NotBlank(message = EscrowAgentConstants.FIRST_NAME_VALIDATION)
    @Pattern(regexp= EscrowCommonConstants.NAMES_REGEX, message = EscrowCommonConstants.INVALID_NAME)
	@Schema(
	        description = "First name of the escrow agent",
	        example = "John"
	    )
	private String firstName;
	
	@NotBlank(message = EscrowAgentConstants.LAST_NAME_VALIDATION)
    @Pattern(regexp= EscrowCommonConstants.NAMES_REGEX, message = EscrowCommonConstants.INVALID_NAME)
	 @Schema(
		        description = "Last name of the escrow agent",
		        example = "Doe"
		    )
	private String lastName;
	
	@NotBlank(message = EscrowCommonConstants.EMAIL_VALIDATION)
    @Pattern(regexp= EscrowCommonConstants.EMAIL_REGEX, message = EscrowCommonConstants.INVALID_EMAIL)
	@Schema(
	        description = "Email address of the escrow agent",
	        example = "john.doe@example.com"
	    )
	private String email;
	
	@NotBlank(message = EscrowCommonConstants.PHONE_VALIDATION)
    @Pattern(regexp= EscrowCommonConstants.PHONE_REGEX, message = EscrowCommonConstants.INVALID_PHONE_NUMBER)
	 @Schema(
		        description = "Phone number of the escrow agent",
		        example = "123-456-7890"
		    )
	private String phone;
	
	@NotNull(message = EscrowAgentConstants.AVGTXV_VALIDATION)
	 @Schema(
		        description = "Average transaction volume of the escrow agent",
		        example = "20"
		    )
	private Integer avgTxVol;
	
	@NotNull(message = EscrowAgentConstants.TX_SUCCESS_RATE)
	@Schema(
	        description = "Transaction success rate of the escrow agent",
	        example = "0.95"
	    )
	private Float txSuccessRate;
	
	@NotBlank(message = EscrowAgentConstants.ESCROW_SW)
    @Pattern(regexp= EscrowCommonConstants.NAMES_REGEX, message = EscrowCommonConstants.INVALID_NAME)
	 @Schema(
		        description = "Escrow software used by the escrow agent",
		        example = "QUALIA"
		    )
	private String escrowSw;
}
