package com.zettamine.mpa.escrow.dto;

import java.util.List;

import com.zettamine.mpa.escrow.constants.EscrowReqLoanProdConstants;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Loan Requirments", description = "Schema to hold the data for add and delete the requirments for the loan product")
public class LoanReqDto {
    
    @NotNull(message = EscrowReqLoanProdConstants.LOAN_ID_REQUIRED)
    @Schema(description = "The ID of the loan product.", example = "12345")
    private Integer prodId;
    
    @NotNull(message = EscrowReqLoanProdConstants.REQ_ID_REQUIRED)
    @Schema(description = "The list of requirements for the loan product.", example = "[\"PROPERTY TAX PENALTY PAYMENTS\", \"PROPERTY TAXES\"]")
    private List<String> requirements;
    
}

