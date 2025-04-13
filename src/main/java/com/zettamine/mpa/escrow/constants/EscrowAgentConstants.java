package com.zettamine.mpa.escrow.constants;

public interface EscrowAgentConstants {
	
	// validation messages 
	public static final String LICENCE_VALIDATION = "Licence id should not be empty";
	public static final String FIRST_NAME_VALIDATION = "First name should not be empty";
	public static final String LAST_NAME_VALIDATION = "Last name should not be empty";
	public static final String AVGTXV_VALIDATION = "Average transcation volume should not be null";
	public static final String TX_SUCCESS_RATE = "Transaction success rate should not be null";
	public static final String ESCROW_SW = "Escrow software should not be empty";
	// response messages for escrow agent
	public static final	String SAVE_AGENT_SUCCEESS = "Escrow agent saved successfully";
	public static final	String UPDATE_AGENT_SUCCESS = "Escrow agent details updated successfully";
	public static final	String ESCROW_AGENT__NOT_FOUND = "Resource not found with this id ";
	public static final String SAVE_CONFLICT_AGENT = "Escrow agent already exists";

}
