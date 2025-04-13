package com.zettamine.mpa.escrow.constants;

public interface EscrowConstants {

	// response message constants
	public static final String ESCROW_SAVE_SUCCEESS = "Escrow company saved successfully";
	public static final String ESCROW_SAVE_CONFLICT = "Escrow service area already exists";
	public static final String ESCROW_UPDATE_SUCCESS = "Escrow company details updated successfully";
	public static final String ESCROW_NOT_FOUND = "Escrow company not found with this id ";
	public static final String ESCROW_NOT_FOUND_BY_NAME = "Escrow company not found with this name ";
	public static final String NOT_FOUND = "Resource not found with this id ";
	public static final String INTERNAL_SERVER_ERROR = "Internal server error";
	
	// regular expression messages for escrow company
	public static final String INVALID_ACCOUNT_NUMBER = "Please provide a valid account number  12-16 characters required";
	public static final String INVALID_ESC_LIENCE_ID = "Please provide valid escrow lienece number";

	// validation messages for escrow company
	public static final String NAME_VALIDATION = "Company name should not be a null or empty";
	public static final String ADDRESS_VALIDATION = "Address should not be null or empty";
	public static final String ESCROW_BANK_NAME_VALIDATION = "Escrow bank name should not be null or empty";
	public static final String ESCROW_ACCOUNT_VALIDAATION = "Account number should not be empty";
	public static final String ESCROW_Name_VALIDAATION = "Please provide escrow name to map with escrow company";

	
}
