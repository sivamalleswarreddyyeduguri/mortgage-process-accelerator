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
import org.springframework.web.bind.annotation.RestController;

import com.zettamine.mpa.escrow.constants.EscrowCommonConstants;
import com.zettamine.mpa.escrow.constants.EscrowConstants;
import com.zettamine.mpa.escrow.constants.EscrowReqConstants;
import com.zettamine.mpa.escrow.dto.EscrowReqDto;
import com.zettamine.mpa.escrow.dto.ResponseDto;
import com.zettamine.mpa.escrow.service.EscrowReqService;
import com.zettamine.mpa.escrow.utils.StringUtil;
import com.zettamine.mpa.exception.RequirmentNotPresentException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;

/**
 * Controller class for managing Escrow Requirement operations.
 * This controller handles requests related to adding, retrieving, updating, and deleting Escrow Requirements.
 */
@RestController
@RequestMapping(path = "api/v1/escrow/requirements")
@AllArgsConstructor
@Validated
@Tag(name = "Escrow Requirment Controller to manage the requirements for loan product", 
description = "Requirements are those which the escrow company has to take care of during the holding period")
public class EscrowReqController {

    private final EscrowReqService escoReqService;
    
    /**
     * Adds new Escrow Requirements.
     * 
     * @param reqList The list of Escrow Requirement DTOs to be added.
     * @return ResponseEntity containing the response status and message.
     */
    @Operation(summary = "Add new Requirement",
               description = "Adding new requirement for escrow")
    @ApiResponses(
                  @ApiResponse(responseCode = "201",
                               description = "New Requirement Created")
                )
    @PostMapping(path = "/create")
    public ResponseEntity<ResponseDto> save(@RequestBody List<EscrowReqDto> reqList){
        escoReqService.save(reqList);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDto(EscrowCommonConstants.STATUS_201, EscrowReqConstants.SAVE_SUCCEESS));
    }

    /**
     * Retrieves all Escrow Requirements.
     * 
     * @return ResponseEntity containing the list of Escrow Requirement DTOs.
     */
    @GetMapping(path = "/fetch")
    public ResponseEntity<List<EscrowReqDto>> getAll() {
        List<EscrowReqDto> reqList = escoReqService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(reqList);
    }

    /**
     * Retrieves all Escrow Requirement names.
     * 
     * @return ResponseEntity containing the list of Escrow Requirement names.
     */
    @GetMapping(path = "/fetch-req")
    public ResponseEntity<List<String>> getAllReq() {
        return ResponseEntity.status(HttpStatus.OK).body(escoReqService.getAllReq());
    }

    /**
     * Retrieves an Escrow Requirement by name.
     * 
     * @param reqName The name of the Escrow Requirement to retrieve.
     * @return ResponseEntity containing the Escrow Requirement DTO.
     */
    @GetMapping("/fetch-by-name/{reqName}")
    public ResponseEntity<?> findByReqName(@PathVariable @NotBlank String reqName) {
        EscrowReqDto reqByName = escoReqService.findByReqName(reqName);
        return ResponseEntity.status(HttpStatus.OK).body(reqByName);
    }
    
    /**
     * Updates an existing Escrow Requirement.
     * 
     * @param reqId The ID of the Escrow Requirement to update.
     * @param escoReqDto The updated Escrow Requirement DTO.
     * @return ResponseEntity containing the response status and message.
     */
    @PutMapping(path = "/update/{reqId}")
    public ResponseEntity<ResponseDto> update(@PathVariable Integer reqId, @Valid @RequestBody EscrowReqDto escoReqDto) {
       
        escoReqService.update(escoReqDto, reqId);
        return ResponseEntity.status(HttpStatus.OK)
                             .body(new ResponseDto(EscrowCommonConstants.STATUS_201, EscrowReqConstants.UPDATE_SUCCESS));
    }
    
}
