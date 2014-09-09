package im.proxy.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class CommandHandler {
	Map<String, String> requestParameters;
	List<String> responses = new ArrayList<String>();
	
	public CommandHandler(Map<String, String> requestParameters) {
		this.requestParameters = requestParameters;
	}
	
	public void addResponse(String response) {
		this.responses.add(response);
	}
	
	public List<String> getResponses() {
		return this.responses;
	}
	
	public abstract void execute();
}
