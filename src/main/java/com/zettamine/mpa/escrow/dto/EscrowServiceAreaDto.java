package com.zettamine.mpa.escrow.dto;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.zettamine.mpa.escrow.constants.EscrowCommonConstants;
import com.zettamine.mpa.escrow.constants.EscrowServiceAreaConstants;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
@Schema(
	    name = "Escrow service area",
	    description = "Schema to hold Escrow service area information"
	)
public class EscrowServiceAreaDto {
		
	@NotBlank(message = EscrowServiceAreaConstants.COUNTY_VALIDATION)
    @Pattern(regexp= EscrowCommonConstants.NAMES_REGEX, message = EscrowCommonConstants.INVALID_NAME)
	 @Schema(
		        description = "County of the service area",
		        example = "LOS ANGELES"
		    )
	 private String county;
	
	@NotBlank(message = EscrowCommonConstants.CITY_VALIDATION)
    @Pattern(regexp= EscrowCommonConstants.NAMES_REGEX, message = EscrowCommonConstants.INVALID_NAME)
	@Schema(
	        description = "City of the service area",
	        example = "LOS ANGELES"
	    )
	 private String city;
	
	@NotBlank(message = EscrowCommonConstants.STATE_VALIDATION)
    @Pattern(regexp= EscrowCommonConstants.NAMES_REGEX, message = EscrowCommonConstants.INVALID_NAME)
	@Schema(
	        description = "State of the service area",
	        example = "CALIFORNIA"
	    )
	 private String state;
	
	 @NotBlank(message = EscrowCommonConstants.ZIPCODE_VALIDATION)
     @Pattern(regexp= EscrowCommonConstants.ZIPCODE_REGEX, message = EscrowCommonConstants.INVALID_ZIPCODE)
	 @Schema(
		        description = "Zipcode of the service area",
		        example = "90001"
		    )
	 private String zipcode;
	 
//	 @JsonProperty(access = Access.WRITE_ONLY)
//	 private EscrowDto escrow;
	 
//	 @JsonProperty(access = Access.WRITE_ONLY)
//	 @NotNull(message = EscrowConstants.ESCROW_ID_VALIDAATION)
//	 private String escoName;
	 
	 @JsonProperty(access = Access.WRITE_ONLY)
	 private EscrowSaveDto escrowNameDto;
}
