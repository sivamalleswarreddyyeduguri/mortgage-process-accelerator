package com.zettamine.mpa.escrow.utils;

import java.util.List;
import java.util.regex.Pattern;

import com.zettamine.mpa.escrow.constants.EscrowCommonConstants;
import com.zettamine.mpa.escrow.constants.EscrowConstants;
import com.zettamine.mpa.escrow.dto.EscrowReqDto;
import com.zettamine.mpa.escrow.entity.Escrow;
import com.zettamine.mpa.escrow.entity.EscrowAgent;
import com.zettamine.mpa.escrow.entity.EscrowServiceArea;

public class StringUtil {
		
	private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\s+");

    public static String removeExtraSpaces(String str) {
        if (str != null && !str.isEmpty()) {
            return WHITESPACE_PATTERN.matcher(str).replaceAll(" ").trim();
//            return str = str.replaceAll("^\\s+|\\s+$|\\s+(?=\\s)", "");

        }
        return str;
    }

    public static String removeAllSpaces(String str) {
        if (str != null && !str.isEmpty()) {
            return WHITESPACE_PATTERN.matcher(str).replaceAll("").trim();
        }
        return str;
    }
    
    public static Escrow escrowRemoveSpaces(Escrow escrow) {
    	    
    	    if(escrow != null) {
    	    	 escrow.setAddress(removeExtraSpaces(escrow.getAddress().toUpperCase()));
    	    	 escrow.setEsAcBankName(removeExtraSpaces(escrow.getEsAcBankName().toUpperCase()));
    	    	 escrow.setState(removeExtraSpaces(escrow.getState().toUpperCase()));
    	    	 escrow.setCity(removeExtraSpaces(escrow.getCity().toUpperCase()));
    	    	 escrow.setEscoName(removeExtraSpaces(escrow.getEscoName().toUpperCase()));
    	    	  
    	    	 return escrow;
    	    }
    	    return escrow;
    }
    
    public static EscrowServiceArea escrowServiceAreaRemoveSpaces(EscrowServiceArea serviceArea) {
    	            if(serviceArea != null) {
    	       serviceArea.setCity(removeExtraSpaces(serviceArea.getCity().toUpperCase()));
    	       serviceArea.setState(removeExtraSpaces(serviceArea.getState().toUpperCase()));
    	       serviceArea.setCounty(removeExtraSpaces(serviceArea.getCounty().toUpperCase()));
               
    	       return serviceArea;
    	  }
    	            return serviceArea;
    }
    
    public static EscrowAgent escrowAgentRemoveSpaces(EscrowAgent escrowAgent) {
    	       
    	         if(escrowAgent != null) {
    		   escrowAgent.setLastName(removeAllSpaces(escrowAgent.getLastName().toUpperCase()));
    		   escrowAgent.setFirstName(removeAllSpaces(escrowAgent.getFirstName().toUpperCase()));
    		   escrowAgent.setEscrowSw(removeAllSpaces(escrowAgent.getEscrowSw().toUpperCase()));
    		   escrowAgent.setEscrowAgentLicenceId(removeAllSpaces(escrowAgent.getEscrowAgentLicenceId().toUpperCase()));
    		   
    		   return escrowAgent;
    		   
    	   }
    	         return escrowAgent;
    		   
    }
   
    
    public static EscrowReqDto formatEscrowReq(EscrowReqDto escoReqDto) {
    	escoReqDto.setReqName(normalizeString(escoReqDto.getReqName()));
		return escoReqDto;
    }
    public static List<String> formatEscrowReq(List<String> reqNames) {
		return reqNames.stream().map(req->normalizeString(req)).toList();
    }
    
    public static String normalizeString(String str) {
    	str = str.replaceAll(EscrowCommonConstants.ONLY_ALPHA_REGEX, "").trim().toUpperCase();
    	 return StringUtil.removeExtraSpaces(removeExtraSpaces(str));
    }
    
    
}
