package com.zettamine.mpa.escrow.dto;

import java.util.List;

import com.zettamine.mpa.escrow.constants.EscrowReqLoanProdConstants;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(name = "Search by Requirments", description = "Schema to hold requirments to search")
public class SearchByReqDto {
    
  
    @NotNull(message = EscrowReqLoanProdConstants.REQ_REQUIRED)
    @Schema(description = "The list of requirements to search by.", example = "[\"PROPERTY TAX PENALTY PAYMENTS\", \"PROPERTY TAXES\"]")
    private List<String> requirements;

}

