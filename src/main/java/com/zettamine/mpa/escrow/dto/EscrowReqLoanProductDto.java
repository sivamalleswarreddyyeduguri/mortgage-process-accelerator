package com.zettamine.mpa.escrow.dto;


import com.zettamine.mpa.escrow.constants.EscrowReqLoanProdConstants;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name="Escrow Loan Product Requirment" , description = "DTO representing an Escrow Requirement Loan Product")
public class EscrowReqLoanProductDto {
    
    @NotNull(message = EscrowReqLoanProdConstants.REQ_ID_REQUIRED)
    @Schema(description = "The ID of the escrow requirement", example = "12345")
    private Integer reqId;

    @NotNull(message = EscrowReqLoanProdConstants.LOAN_ID_REQUIRED)
    @Schema(description = "The ID of the loan product", example = "54321")
    private Integer prodId;
}

