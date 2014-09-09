package im.proxy.umg.util;

import java.util.List;

public class ResponseBuilder {
	
	public final static String RESULTCODE_SUCCESS = "000";
	public final static String RESULTCODE_APARTY_MSISDN_MISSING = "101";
	public final static String RESULTCODE_BPARTY_MSISDN_MISSING = "102";
	public final static String RESULTCODE_INTRO_MESSAGE_NOT_FOUND = "201";
	public final static String RESULTCODE_TIME_OUT = "604";
	public final static String RESULTCODE_ERROR = "605";
	
	public final static String RESULT_SUCCESS = "0";
	public final static String RESULT_FAILED = "1";
	
	
	public static String build(String result, String resultCode, String resultMessage) {
		StringBuilder sb = new StringBuilder("<Response>");
		sb.append("<result>" + result + "</result>");
		sb.append("<resultCode>" + resultCode + "</resultCode>");
		sb.append("<resultMessage><message>" + resultMessage + "</message></resultMessage>");
		
		sb.append("</Response>");
		return sb.toString();
	}
	
	public static String build(String result, String resultCode, String resultMessage, String... nameValuePairs) {
		StringBuilder sb = new StringBuilder("<Response>");
		sb.append("<result>" + result + "</result>");
		sb.append("<resultCode>" + resultCode + "</resultCode>");
		sb.append("<resultMessage><message>" + resultMessage + "</message></resultMessage>");
		
		if(nameValuePairs.length > 0 && nameValuePairs.length % 2 == 0) {
			for(int i=0; i<nameValuePairs.length/2; i++) {
				sb.append("<");
				sb.append(nameValuePairs[i*2]);
				sb.append(">");
				
				sb.append(nameValuePairs[i*2 + 1]);
				
				sb.append("</");
				sb.append(nameValuePairs[i*2]);
				sb.append(">");
			}
		}
		
		sb.append("</Response>");
		return sb.toString();
	}
	
	public static String buildResponses(List<String> responses) {
		StringBuilder sb = new StringBuilder("<Responses>");
		
		for(String response : responses) {
			sb.append(response);
		}
		
		sb.append("</Responses>");
		return sb.toString();
	}
	
	public static String buildResponses(String[] responses) {
		StringBuilder sb = new StringBuilder("<Responses>");
		
		for(String response : responses) {
			sb.append(response);
		}
		
		sb.append("</Responses>");
		return sb.toString();
	}
}