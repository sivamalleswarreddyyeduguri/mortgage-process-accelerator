
package com.zettamine.mpa.escrow.controller;

import java.util.List;
import java.util.Map;

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
import com.zettamine.mpa.escrow.constants.EscrowServiceAreaConstants;
import com.zettamine.mpa.escrow.dto.EscrowSaveDto;
import com.zettamine.mpa.escrow.dto.EscrowServiceAreaDto;
import com.zettamine.mpa.escrow.dto.ResponseDto;
import com.zettamine.mpa.escrow.service.EscrowServiceAreaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Tag(
	    name = "CRUD REST APIs for Escrow Service Areas",
	    description = "CRUD REST APIs for managing Escrow Service Areas"
	)

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/escrow")
public class EscrowServiceAreaController {

	private EscrowServiceAreaService serviceAreaService;

	
	@Operation(
	        summary = "Create Escrow Service Area REST API",
	        description = "REST API to create a new Escrow Service Area"
	    )
	    @ApiResponses({
	        @ApiResponse(
	            responseCode = "201",
	            description = "Escrow Service Area created successfully"
	        ),
	        @ApiResponse(
	            responseCode = "500",
	            description = "Internal Server Error",
	            content = @Content(
	                schema = @Schema(implementation = ResponseDto.class)
	            )
	        )
	    })
	@PostMapping("/service-area/create")
	public ResponseEntity<ResponseDto> addServiceArea(@Valid @RequestBody EscrowSaveDto nameDtos) {
		serviceAreaService.save(nameDtos);
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(new ResponseDto(EscrowCommonConstants.STATUS_201, EscrowServiceAreaConstants.SAVE_SERVICE_AREA_SUCCEESS));
	}

	@Operation(
	        summary = "Fetch All Escrow Service Areas REST API",
	        description = "REST API to fetch all Escrow Service Areas"
	    )
	    @ApiResponses({
	        @ApiResponse(
	            responseCode = "200",
	            description = "OK"
	        ),
	        @ApiResponse(
	            responseCode = "500",
	            description = "Internal Server Error",
	            content = @Content(
	                schema = @Schema(implementation = ResponseDto.class)
	            )
	        )
	    })
	@GetMapping("/service-area/fetch")
	public ResponseEntity<List<EscrowServiceAreaDto>> fetchServiceAreaDetails() {

		List<EscrowServiceAreaDto> allServiceAreaDtos = serviceAreaService.getAll();
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(allServiceAreaDtos);
	}

	 @Operation(
		        summary = "Fetch Escrow Service Area by ID REST API",
		        description = "REST API to fetch an Escrow Service Area by its ID"
		    )
		    @ApiResponses({
		        @ApiResponse(
		            responseCode = "200",
		            description = "OK"
		        ),
		        @ApiResponse(
		            responseCode = "404",
		            description = "Not Found",
		            content = @Content(
		                schema = @Schema(implementation = ResponseDto.class)
		            )
		        )
		    })
	@GetMapping("/service-area/fetch/{serviceAreaId}")
	public ResponseEntity<EscrowServiceAreaDto> fetchServiceAreaById(@PathVariable Integer serviceAreaId) {
		EscrowServiceAreaDto serviceAreaDto = serviceAreaService.findById(serviceAreaId);
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(serviceAreaDto);
	}
	
	 @Operation(
		        summary = "Update Escrow Service Area REST API",
		        description = "REST API to update an existing Escrow Service Area by its ID"
		    )
		    @ApiResponses({
		        @ApiResponse(
		            responseCode = "200",
		            description = "Escrow Service Area updated successfully"
		        ),
		        @ApiResponse(
		            responseCode = "404",
		            description = "Not Found",
		            content = @Content(
		                schema = @Schema(implementation = ResponseDto.class)
		            )
		        ),
		        @ApiResponse(
		            responseCode = "500",
		            description = "Internal Server Error",
		            content = @Content(
		                schema = @Schema(implementation = ResponseDto.class)
		            )
		        )
		    })
	@PutMapping("/service-area/update/{serviceAreaId}")
	public ResponseEntity<ResponseDto> updateEscrowServiceArea(@RequestBody EscrowServiceAreaDto serviceAreaDto, @PathVariable Integer serviceAreaId){
		       
		boolean isUpdated = serviceAreaService.update(serviceAreaDto, serviceAreaId);
		if(isUpdated) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(EscrowCommonConstants.STATUS_200, EscrowServiceAreaConstants.UPDATE_SERVICE_AREA_SUCCESS));
        }else{
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDto(EscrowCommonConstants.STATUS_404, EscrowServiceAreaConstants.ESCROW_SERVICE_AREA_NOT_FOUND + serviceAreaId));
        } 
	}
	
	 @Operation(
		        summary = "Fetch Escrow Service Areas by Escrow Name REST API",
		        description = "REST API to fetch all Escrow Service Areas associated with an Escrow by its ID"
		    )
		    @ApiResponses({
		        @ApiResponse(
		            responseCode = "200",
		            description = "OK"
		        )
		    })
	@GetMapping("/service-area/fetch-by-escrow-name/{escoName}")
	public ResponseEntity<Map<String, Object>> fetchServiceAreasByEscrowName(@PathVariable String escoName){
		
		         return ResponseEntity
		        		 .status(HttpStatus.OK)
		        		 .body(serviceAreaService.findByEscrowName(escoName));
	}
}
