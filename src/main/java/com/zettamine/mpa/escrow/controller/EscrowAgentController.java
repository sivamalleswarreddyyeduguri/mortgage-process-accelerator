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

import com.zettamine.mpa.escrow.constants.EscrowAgentConstants;
import com.zettamine.mpa.escrow.constants.EscrowCommonConstants;
import com.zettamine.mpa.escrow.constants.EscrowConstants;
import com.zettamine.mpa.escrow.dto.ErrorResponseDto;
import com.zettamine.mpa.escrow.dto.EscrowAgentDto;
import com.zettamine.mpa.escrow.dto.EscrowNameDto;
import com.zettamine.mpa.escrow.dto.ResponseDto;
import com.zettamine.mpa.escrow.service.EscrowAgentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Tag(
	    name = "CRUD REST APIs for Escrow Agents",
	    description = "CRUD REST APIs for managing Escrow Agents"
	)

@RestController
@RequestMapping("/api/v1/escrow")
@AllArgsConstructor
public class EscrowAgentController {
	
	
	private EscrowAgentService escrowAgentService;

	
	@Operation(
		    summary = "Create Escrow Agent REST API",
		    description = "REST API to create a new Escrow Agent associated with an Escrow"
		)
		@ApiResponses({
		    @ApiResponse(
		        responseCode = "201",
		        description = "Escrow Agent created successfully"
		    ),
		    @ApiResponse(
		        responseCode = "500",
		        description = "Internal Server Error",
		        content = @Content(
		            schema = @Schema(implementation = ErrorResponseDto.class)
		        )
		    )
		})
	
	@PostMapping("/agent/create")
	public ResponseEntity<ResponseDto> addEscrowAgent(@Valid @RequestBody EscrowNameDto escrowNameDto) {
		escrowAgentService.save(escrowNameDto);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ResponseDto(EscrowCommonConstants.STATUS_201, EscrowAgentConstants.SAVE_AGENT_SUCCEESS));
	}

	
	@Operation(
		    summary = "Fetch All Escrow Agents REST API",
		    description = "REST API to fetch all Escrow Agents"
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
		            schema = @Schema(implementation = ErrorResponseDto.class)
		        )
		    )
		})
	
	@GetMapping("/agent/fetch")
	public ResponseEntity<List<EscrowAgentDto>> fetchAllEscrowAgents() {

		List<EscrowAgentDto> allAgentDtos = escrowAgentService.getAll();
		return ResponseEntity.status(HttpStatus.OK).body(allAgentDtos);
	}

	@Operation(
		    summary = "Fetch Escrow Agent by ID REST API",
		    description = "REST API to fetch an Escrow Agent by its ID"
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
		            schema = @Schema(implementation = ErrorResponseDto.class)
		        )
		    )
		})
	
	@GetMapping("/agent/fetch/{escrowAgentId}")
	public ResponseEntity<EscrowAgentDto> fetchEscrowAgentById(@PathVariable Integer escrowAgentId) {
		EscrowAgentDto agentDto = escrowAgentService.findById(escrowAgentId);
		return ResponseEntity.status(HttpStatus.OK).body(agentDto);
	}
	
	@Operation(
		    summary = "Update Escrow Agent REST API",
		    description = "REST API to update an existing Escrow Agent by its ID"
		)
		@ApiResponses({
		    @ApiResponse(
		        responseCode = "200",
		        description = "Escrow Agent updated successfully"
		    ),
		    @ApiResponse(
		        responseCode = "404",
		        description = "Not Found",
		        content = @Content(
		            schema = @Schema(implementation = ErrorResponseDto.class)
		        )
		    ),
		    @ApiResponse(
		        responseCode = "500",
		        description = "Internal Server Error",
		        content = @Content(
		            schema = @Schema(implementation = ErrorResponseDto.class)
		        )
		    )
		})
	
	@PutMapping("/agent/update/{escrowAgentId}")
	public ResponseEntity<ResponseDto> updateEscrowAgentById(@RequestBody EscrowAgentDto escrowAgentDto, @PathVariable Integer escrowAgentId){
		       
		boolean isUpdated = escrowAgentService.update(escrowAgentDto, escrowAgentId);
		if(isUpdated) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(EscrowCommonConstants.STATUS_200, EscrowAgentConstants.UPDATE_AGENT_SUCCESS));
        }else{
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDto(EscrowCommonConstants.STATUS_404, EscrowAgentConstants.ESCROW_AGENT__NOT_FOUND + escrowAgentId));
        } 
	}
	
	
	@Operation(
		    summary = "Fetch Escrow Agents by Escrow Name REST API",
		    description = "REST API to fetch all Escrow Agents associated with an Escrow by its ID"
		)
		@ApiResponses({
		    @ApiResponse(
		        responseCode = "200",
		        description = "OK"
		    )
		})
	
	@GetMapping("/agent/fetch-by-escrow-name/{escoName}")
	public ResponseEntity<Map<String, Object>> fetchEscrowAgentsByEscrowName(@PathVariable String escoName){
		         return ResponseEntity
		        		 .status(HttpStatus.OK)
		        		 .body(escrowAgentService.findByEscrowName(escoName));
	}
	
}
