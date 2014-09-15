package im.proxy.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class CommandHandler {
	Map<String, String> requestParameters;
	List<String> responses = new ArrayList<String>();
	private String resultCode = "";
	
	public CommandHandler(Map<String, String> requestParameters) {
		this.requestParameters = requestParameters;
	}
	
	public void addResponse(String response) {
		this.responses.add(response);
	}
	
	public List<String> getResponses() {
		return this.responses;
	}
	
	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public abstract void execute();
}
