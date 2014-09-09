package im.proxy.handler;

import java.util.Map;

public class SendIntroMessageCommandHandler extends CommandHandler {
	
	public SendIntroMessageCommandHandler(Map<String, String> requestParameters) {
		super(requestParameters);
	}

	@Override
	public String execute() {
		String msisdn = this.requestParameters.get("msisdn"); 
		String skypeId = this.requestParameters.get("skypeid");
		
		return "";
	}
}
