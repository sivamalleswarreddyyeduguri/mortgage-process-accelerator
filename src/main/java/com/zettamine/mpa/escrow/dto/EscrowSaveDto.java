package com.zettamine.mpa.escrow.dto;

import java.util.List;

import com.zettamine.mpa.escrow.constants.EscrowCommonConstants;
import com.zettamine.mpa.escrow.constants.EscrowConstants;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EscrowSaveDto {
		
	@NotBlank(message = EscrowConstants.ESCROW_Name_VALIDAATION)
    @Pattern(regexp= EscrowCommonConstants.NAMES_REGEX, message = EscrowCommonConstants.INVALID_NAME)
	private String escoName;
	
	@Valid
	private List<EscrowServiceAreaDto> serviceAreas;
	
	@Valid
	private List<EscrowAgentDto> escrowAgent;
}
