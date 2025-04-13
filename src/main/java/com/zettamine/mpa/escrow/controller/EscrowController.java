package com.zettamine.mpa.escrow.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zettamine.mpa.escrow.constants.EscrowCommonConstants;
import com.zettamine.mpa.escrow.constants.EscrowConstants;
import com.zettamine.mpa.escrow.dto.ErrorResponseDto;
import com.zettamine.mpa.escrow.dto.EscrowDto;
import com.zettamine.mpa.escrow.dto.EscrowFetchDto;
import com.zettamine.mpa.escrow.dto.ResponseDto;
import com.zettamine.mpa.escrow.dto.SearchCriteria;
import com.zettamine.mpa.escrow.service.EscrowService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;

@Tag(
	    name = "CRUD REST APIs for Escrow",
	    description = "CRUD REST APIs for managing Escrow details"
	)

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/escrow")
public class EscrowController {
	  
	 EscrowService escrowService;
	 
	    @Operation(
	            summary = "Create Escrow Company",
	            description = "Create a new Escrow Company with the provided details"
	    )
	    @ApiResponses({
	            @ApiResponse(
	                    responseCode = "201",
	                    description = "Escrow Company created successfully"
	            ),
	            @ApiResponse(
	                    responseCode = "500",
	                    description = "Internal Server Error",
	                    content = @Content(
	                            schema = @Schema(implementation = ErrorResponseDto.class)
	                    )
	            )
	    })
	 @PostMapping("/create")
	    public ResponseEntity<ResponseDto> addEscrow(@Valid @RequestBody EscrowDto escrowDto) {
	            System.err.println(escrowDto.getInEscrowAcNo());
	            escrowService.save(escrowDto);
	            return ResponseEntity
	                    .status(HttpStatus.CREATED)
	                    .body(new ResponseDto(EscrowCommonConstants.STATUS_201, EscrowConstants.ESCROW_SAVE_SUCCEESS));
	        
	    }
	 
	    @Operation(
	            summary = "Fetch All Escrow Companies",
	            description = "Fetch details of all Escrow Companies"
	    )
	    @ApiResponses({
	            @ApiResponse(
	                    responseCode = "200",
	                    description = "List of Escrow Companies retrieved successfully"
	            ),
	            @ApiResponse(
	                    responseCode = "500",
	                    description = "Internal Server Error",
	                    content = @Content(
	                            schema = @Schema(implementation = ErrorResponseDto.class)
	                    )
	            )
	    })
	 
	 @GetMapping("/fetch")
	 public ResponseEntity<List<EscrowDto>> fetchEscrowCompanies(){
		 
		     List<EscrowDto> allEscrowDtos = escrowService.getAll();
		     return ResponseEntity
		    		 .status(HttpStatus.OK)
		    		 .body(allEscrowDtos);
		    		 
	 }
	 
	    @Operation(
	            summary = "Fetch Escrow Company by ID",
	            description = "Fetch details of an Escrow Company by its ID"
	    )
	    @ApiResponses({
	            @ApiResponse(
	                    responseCode = "200",
	                    description = "Escrow Company retrieved successfully"
	            ),
	            @ApiResponse(
	                    responseCode = "404",
	                    description = "Escrow Company not found",
	                    content = @Content(
	                            schema = @Schema(implementation = ErrorResponseDto.class)
	                    )
	            )
	    })
	 @GetMapping("/fetch/{escoId}")
	 public ResponseEntity<EscrowDto> fetchEscrowById(@PathVariable Integer escoId){
		         EscrowDto escrowDto = escrowService.findById(escoId);
		         return ResponseEntity
		        		 .status(HttpStatus.OK)
		        		 .body(escrowDto);
	 }
	    @Operation(
	            summary = "Fetch Escrow Company by Name",
	            description = "Fetch details of an Escrow Company by its name"
	    )
	    @ApiResponses({
	            @ApiResponse(
	                    responseCode = "200",
	                    description = "Escrow Company retrieved successfully"
	            ),
	            @ApiResponse(
	                    responseCode = "404",
	                    description = "Escrow Company not found",
	                    content = @Content(
	                            schema = @Schema(implementation = ErrorResponseDto.class)
	                    )
	            )
	    })
	 
	 @GetMapping("/fetch/name/{escoName}")
	 public ResponseEntity<EscrowDto> fetchEscrowByName(@PathVariable @NotBlank(message = "Escrow name should be null or empty") String escoName){
		   	    EscrowDto escrowDto = escrowService.findByEscrowName(escoName);
		   	    return ResponseEntity
		   	    		.status(HttpStatus.OK)
		   	    		.body(escrowDto);
	 }
	 
	 
	    @Operation(
	            summary = "Update Escrow Company",
	            description = "Update details of an existing Escrow Company"
	    )
	    @ApiResponses({
	            @ApiResponse(
	                    responseCode = "200",
	                    description = "Escrow Company updated successfully"
	            ),
	            @ApiResponse(
	                    responseCode = "404",
	                    description = "Escrow Company not found",
	                    content = @Content(
	                            schema = @Schema(implementation = ErrorResponseDto.class)
	                    )
	            )
	    })
	 @PutMapping("/update/{escoId}")
	 public ResponseEntity<ResponseDto> updateEscrow(@Valid @RequestBody EscrowDto escrowDto, @PathVariable Integer escoId){
		         boolean isUpdated = escrowService.update(escrowDto, escoId);
		         if(isUpdated) {
		             return ResponseEntity
		                     .status(HttpStatus.OK)
		                     .body(new ResponseDto(EscrowCommonConstants.STATUS_200, EscrowConstants.ESCROW_UPDATE_SUCCESS));
		         }else{
		             return ResponseEntity
		                     .status(HttpStatus.NOT_FOUND)
		                     .body(new ResponseDto(EscrowCommonConstants.STATUS_404, EscrowConstants.NOT_FOUND + escoId));
		         }
	 }
	 
	    @Operation(
	            summary = "Search Escrow Companies",
	            description = "Search for Escrow Companies based on the provided criteria"
	    )
	    @ApiResponses({
	            @ApiResponse(
	                    responseCode = "200",
	                    description = "List of Escrow Companies retrieved successfully"
	            ),
	            @ApiResponse(
	                    responseCode = "500",
	                    description = "Internal Server Error",
	                    content = @Content(
	                            schema = @Schema(implementation = ErrorResponseDto.class)
	                    )
	            )
	    })
	    
	   @GetMapping("/search")
	    public ResponseEntity<List<EscrowFetchDto>> searchEscrows(@RequestBody  SearchCriteria criteria) {
	        List<EscrowFetchDto> escrows = escrowService.search(criteria);
	        return ResponseEntity.ok(escrows);
	    }
	 
}
