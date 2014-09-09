package im.proxy.handler;

import java.util.Map;

public abstract class CommandHandler {
	Map<String, String> requestParameters;
	List
	public CommandHandler(Map<String, String> requestParameters) {
		this.requestParameters = requestParameters;
	}
	
	public abstract String execute();
}
