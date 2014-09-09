package im.proxy.handler;

import im.proxy.db.SubscriberDAO;
import im.proxy.umg.Client;
import im.proxy.umg.util.NumberUtil;
import im.proxy.umg.util.ResponseBuilder;

import java.util.Map;

public class SendIntroMessageCmdHandler extends CommandHandler {
	
	public SendIntroMessageCmdHandler(Map<String, String> requestParameters) {
		super(requestParameters);
	}

	@Override
	public void execute() {
		String aMsisdn = this.requestParameters.get("aMsisdn"); 
		String bMsisdn = this.requestParameters.get("bMsisdn");
		String refId = this.requestParameters.get("refId");
		
		if(!validateParam(aMsisdn)) {
			addResponse(ResponseBuilder.build(ResponseBuilder.RESULT_FAILED, 
					ResponseBuilder.RESULTCODE_APARTY_MSISDN_MISSING, ""));
			
			return;
		}
		
		if(!validateParam(bMsisdn)) {
			addResponse(ResponseBuilder.build(ResponseBuilder.RESULT_FAILED, 
					ResponseBuilder.RESULTCODE_BPARTY_MSISDN_MISSING, ""));
			
			return;
		}
		
		String introMessage = new SubscriberDAO().getIntroductionMessage(aMsisdn, bMsisdn);
		
		if(!validateParam(introMessage)) {
			addResponse(ResponseBuilder.build(ResponseBuilder.RESULT_FAILED, 
					ResponseBuilder.RESULTCODE_INTRO_MESSAGE_NOT_FOUND, ""));
			
			return;
		}
		
		String umgResponse = new Client().sendRequest(refId, 
				NumberUtil.normalize(aMsisdn), NumberUtil.normalize(bMsisdn), introMessage);
		
		if(!validateParam(umgResponse)) {
			addResponse(ResponseBuilder.build(ResponseBuilder.RESULT_FAILED, 
					ResponseBuilder.RESULTCODE_ERROR, ""));		
		} else {
			addResponse(ResponseBuilder.build(ResponseBuilder.RESULT_SUCCESS, 
					ResponseBuilder.RESULTCODE_SUCCESS, umgResponse));
		}
	}
	
	private boolean validateParam(String param) {
		if(param == null)
			return false;
		
		if(param.isEmpty())
			return false;
		
		return true;
	}
}
