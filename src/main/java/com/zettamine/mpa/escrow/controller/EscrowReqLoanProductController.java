package com.zettamine.mpa.escrow.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zettamine.mpa.escrow.constants.EscrowCommonConstants;
import com.zettamine.mpa.escrow.constants.EscrowConstants;
import com.zettamine.mpa.escrow.constants.EscrowReqLoanProdConstants;
import com.zettamine.mpa.escrow.dto.EscrowReqLoanProductDto;
import com.zettamine.mpa.escrow.dto.LoanReqDto;
import com.zettamine.mpa.escrow.dto.ResponseDto;
import com.zettamine.mpa.escrow.dto.SearchByReqDto;
import com.zettamine.mpa.escrow.service.EscrowReqLoanProductService;
import com.zettamine.mpa.escrow.utils.StringUtil;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

/**
 * Controller class for managing Escrow Requirement Loan Product operations.
 * This controller handles requests related to adding, retrieving, updating, and deleting Escrow Requirement to the Loan Products.
 */
@RestController
@RequestMapping(path = "api/v1/escrow/loan-requirment")
@AllArgsConstructor
@Validated
public class EscrowReqLoanProductController {

    private final EscrowReqLoanProductService escoReqLoanSer;
    
    /**
     * Adds a new Escrow Requirement to the Loan Product.
     * 
     * @param reqLoanDto The LoanReqDto containing information about the Escrow Requirement Loan Product to be added.
     * @return ResponseEntity containing the response status and message.
     */
    @PostMapping("/add-requirment")
    public ResponseEntity<ResponseDto> save(@Valid @RequestBody LoanReqDto  reqLoanDto){
        escoReqLoanSer.save(reqLoanDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDto(EscrowCommonConstants.STATUS_201, EscrowReqLoanProdConstants.SAVE_SUCCEESS));
    }
    
    /**
     * Retrieves Escrow Requirement Loan Products by Product ID.
     * 
     * @param prodId The ID of the Product to retrieve Escrow Requirement Loan Products for.
     * @return ResponseEntity containing the list of Escrow Requirement Loan Product DTOs.
     */
    @GetMapping("/find-by-prodid/{prodId}")
    public ResponseEntity<List<String>> findByProdId(@PathVariable Integer prodId) {
    	
        List<String> byProdId = escoReqLoanSer.findByProdId(prodId);
        return ResponseEntity.status(HttpStatus.OK).body(byProdId);
    }

    /**
     * Retrieves Escrow Requirement Loan Products by Requirement ID and Product ID.
     * 
     * @param reqNameList The list of Requirement names to search for.
     * @return ResponseEntity containing the list of Escrow Requirement Loan Product DTOs.
     */
    @GetMapping("/find-by-requirment")
    public ResponseEntity<List<EscrowReqLoanProductDto>> findByReq(@RequestParam List<String> reqNameList) {
        List<String> norReqNameList = StringUtil.formatEscrowReq(reqNameList);
        List<EscrowReqLoanProductDto> byReqName = escoReqLoanSer.findByReqName(norReqNameList);
        return ResponseEntity.status(HttpStatus.OK).body(byReqName);
    }

    /**
     * Searches for Escrow Requirement Loan Products by a list of Requirements.
     * 
     * @param searchDto The SearchByReqDto containing the list of Requirements to search for.
     * @return ResponseEntity containing the list of Product IDs matching the search criteria.
     */
    @PostMapping("/search-by-requirments")
    public ResponseEntity<List<Integer>> searchByReq(@Valid @RequestBody SearchByReqDto searchDto) {
        List<String> norReqNameList = StringUtil.formatEscrowReq(searchDto.getRequirements());
        List<Integer> searchByReq = escoReqLoanSer.searchByReq(norReqNameList);
        return ResponseEntity.status(HttpStatus.OK).body(searchByReq);
    }
    
    /**
     * Deletes an Escrow Requirement Loan Product.
     * 
     * @param loanReqDto The LoanReqDto containing information about the Escrow Requirement Loan Product to be deleted.
     * @return ResponseEntity containing the response status and message.
     */
    @PutMapping("/delete")
    public ResponseEntity<ResponseDto> deleteLoanReq(@Valid @RequestBody LoanReqDto loanReqDto){
        escoReqLoanSer.delete(loanReqDto);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(new ResponseDto(EscrowCommonConstants.STATUS_202, EscrowReqLoanProdConstants.DELETED));
    }
}
