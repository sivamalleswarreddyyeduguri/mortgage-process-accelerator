package com.zettamine.mpa.exception;

import java.time.LocalDateTime;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.NestedExceptionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.orm.jpa.JpaSystemException;

import com.zettamine.mpa.escrow.constants.EscrowReqConstants;
import com.zettamine.mpa.escrow.constants.EscrowReqLoanProdConstants;
import com.zettamine.mpa.escrow.dto.ErrorResponseDto;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(EscrowAlreadyExistsException.class)
	public ResponseEntity<ErrorResponseDto> handleEscrowAlreadyExistsException(EscrowAlreadyExistsException exception,
			WebRequest webRequest) {

		ErrorResponseDto errorResponseDto = new ErrorResponseDto(webRequest.getDescription(false),
				HttpStatus.BAD_REQUEST, exception.getMessage(), LocalDateTime.now());
		return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponseDto> handleGlobalException(Exception exception, WebRequest webRequest) {
		ErrorResponseDto errorResponseDTO = new ErrorResponseDto(webRequest.getDescription(false),
				HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(), LocalDateTime.now());
		return new ResponseEntity<>(errorResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponseDto> handleResourceNotFoundException(ResourceNotFoundException exception,
			WebRequest webRequest) {
		ErrorResponseDto errorResponseDTO = new ErrorResponseDto(webRequest.getDescription(false), HttpStatus.NOT_FOUND,
				exception.getMessage(), LocalDateTime.now());
		return new ResponseEntity<>(errorResponseDTO, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
	public ResponseEntity<ErrorResponseDto> handleDataIntegrityViolationException(
			org.springframework.dao.DataIntegrityViolationException exception, WebRequest webRequest) {

		ErrorResponseDto errorResponseDto = new ErrorResponseDto(webRequest.getDescription(false), HttpStatus.CONFLICT,
				extractErrorMessage(exception), LocalDateTime.now());
		return new ResponseEntity<>(errorResponseDto, HttpStatus.CONFLICT);
	}

	private String extractErrorMessage(org.springframework.dao.DataIntegrityViolationException e) {
		Throwable rootCause = NestedExceptionUtils.getRootCause(e);
		return rootCause.getMessage();
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		Map<String, String> validationErrors = new HashMap<>();
		List<ObjectError> validationErrorList = ex.getBindingResult().getAllErrors();

		validationErrorList.forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String validationMsg = error.getDefaultMessage();
			validationErrors.put(fieldName, validationMsg);
		});
		return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(DuplicateRequirementException.class)
	protected ResponseEntity<ErrorResponseDto> handleDuplicateReqException(DuplicateRequirementException exp,
			WebRequest request) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponseDto(request.getDescription(false),
				HttpStatus.CONFLICT, EscrowReqConstants.SAVE_CONFLICT, LocalDateTime.now()));
	}

	@ExceptionHandler(DuplicateLoanProdRequirment.class)
	protected ResponseEntity<ErrorResponseDto> handleDuplicateLoanReqException(DuplicateLoanProdRequirment exp,
			WebRequest request) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponseDto(request.getDescription(false),
				HttpStatus.CONFLICT, EscrowReqLoanProdConstants.SAVE_CONFLICT, LocalDateTime.now()));
	}

	@ExceptionHandler(RequirmentNotPresentException.class)
	protected ResponseEntity<ErrorResponseDto> handleRequirmentNotPresentException(RequirmentNotPresentException exp,
			WebRequest request) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND.value())
							.body(new ErrorResponseDto(request.getDescription(false), 
							HttpStatus.NOT_FOUND, exp.getLocalizedMessage(), 
							LocalDateTime.now()));
	}
	
	@ExceptionHandler(org.springframework.orm.jpa.JpaSystemException.class)
	protected ResponseEntity<ErrorResponseDto> handleJpaSystemException(org.springframework.orm.jpa.JpaSystemException ex,
			WebRequest request) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND.value())
							.body(new ErrorResponseDto(request.getDescription(false), 
							HttpStatus.NOT_FOUND, "No match found for given loan id and requirements", 
							LocalDateTime.now()));
	}
	
}
