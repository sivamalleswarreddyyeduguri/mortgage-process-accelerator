package com.zettamine.mpa.escrow.constants;

public interface EscrowCommonConstants {
	
	// Regular Expression Constants
	public static final String PHONE_REGEX = "^\\d{3}-\\d{3}-\\d{4}$";
	public static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
	public static final String NAMES_REGEX = "^[a-zA-Z ]+$";
	public static final String ZIPCODE_REGEX = "^\\d{5}(?:-\\d{4})?$";
	public static final String ACCOUNT_NUMBER_REGEX = "^\\d{12,16}$";
	public static final String ESC_LINCENCE_ID_REGEX = "^[A-Za-z0-9]{10}$";
	public static final	String ONLY_ALPHA_REGEX = "[^a-z A-Z]";
		
	// status code constants
	public static final String STATUS_201 = "201";
	public static final String STATUS_200 = "200";
	public static final String STATUS_404 = "404";
	public static final String STATUS_400 = "400";
	public static final String STATUS_409 = "409";
	public static final String STATUS_202 = "202";
	
	// validation messages constants
	public static final String CITY_VALIDATION = "City should not be a null or empty";
	public static final String STATE_VALIDATION = "State should not be a null or empty";
	public static final String ZIPCODE_VALIDATION = "Zipcode should not be a null or empty";
	public static final String PHONE_VALIDATION = "Phone number should not be a empty";
	public static final String EMAIL_VALIDATION = "Email should not be a empty";
	
	public static final String INVALID_PHONE_NUMBER = "Please provide valid phone number";
	public static final String INVALID_EMAIL = "Please provide valid email id";
	public static final String INVALID_ZIPCODE = "Please enter a valid zipcode";
	public static final String INVALID_NAME = "Please enter a valid name";

}
