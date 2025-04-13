package com.zettamine.mpa.escrow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(
	    name = "SearchCriteria",
	    description = "Schema to perform search operations"
	)
public class SearchCriteria {
	
	@Schema(
	        description = "search based on escrow company name",
	        example = "ALPHA ESCROW SERVICES"
	    )
	private String name;
	@Schema(
	        description = "search based on service area state",
	        example = "CALIFORNIA"
	    )
	private String state;
	@Schema(
	        description = "search based on service area city",
	        example = "LOS ANGELES"
	    )
	private String city;
	@Schema(
	        description = "search based on service area zipcode",
	        example = "90001"
	    )
	private String zipcode;
	
}
