package im.proxy.handler;

import im.proxy.db.SubscriberDAO;
import im.proxy.umg.Client;
import im.proxy.umg.util.NumberUtil;
import im.proxy.umg.util.ResponseBuilder;

import java.util.Map;
import java.util.ResourceBundle;

public class SendIntroMessageCmdHandler extends CommandHandler {
	
	private static String mNoIntroMessageFoundAlert = "Aap nay Intro Me set nahi kia huwa, Intro message set kernay k liay *6060# mila k manpasand message ya greeting set kijiye aur champion users set intro <intro message> e.g. set intro Hey, This is Naureen likh kar 6064 par send karain.";
	
	static {
		try {
			ResourceBundle myResources = ResourceBundle.getBundle("improxy");
			mNoIntroMessageFoundAlert = myResources.getString("sms.no_intro");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public SendIntroMessageCmdHandler(Map<String, String> requestParameters) {
		super(requestParameters);
	}

	@Override
	public void execute() {
		String aMsisdn = this.requestParameters.get("aMsisdn"); 
		String bMsisdn = this.requestParameters.get("bMsisdn");
		String refId = this.requestParameters.get("refId");
		
		if(!validateParam(aMsisdn)) {
			setResultCode(ResponseBuilder.RESULTCODE_APARTY_MSISDN_MISSING);
			addResponse(ResponseBuilder.build(ResponseBuilder.RESULT_FAILED, 
					ResponseBuilder.RESULTCODE_APARTY_MSISDN_MISSING, ""));
			
			return;
		}
		
		if(!validateParam(bMsisdn)) {
			setResultCode(ResponseBuilder.RESULTCODE_BPARTY_MSISDN_MISSING);
			addResponse(ResponseBuilder.build(ResponseBuilder.RESULT_FAILED, 
					ResponseBuilder.RESULTCODE_BPARTY_MSISDN_MISSING, ""));
			
			return;
		}
		
		String introMessage = new SubscriberDAO().getIntroductionMessage(NumberUtil.normalize(aMsisdn), 
				NumberUtil.normalize(bMsisdn));
		
		if(!validateParam(introMessage)) {
			setResultCode(ResponseBuilder.RESULTCODE_INTRO_MESSAGE_NOT_FOUND);
			addResponse(ResponseBuilder.build(ResponseBuilder.RESULT_FAILED, 
					ResponseBuilder.RESULTCODE_INTRO_MESSAGE_NOT_FOUND, ""));
			
			
			String xmlRequest = "<methodCall><shortcode>6060</shortcode><function>sendMessage</function>"
					+ "<channel>SMS</channel><apartyMSISDN>" + NumberUtil.normalize(aMsisdn) + "</apartyMSISDN><refID>"
							+ refId + "</refID><mtMessage>" + mNoIntroMessageFoundAlert + "</mtMessage></methodCall>";			
			new im.proxy.provgw.Client().sendRequest(xmlRequest, refId);
			
			return;
		}
		
		String umgResponse = new Client().sendRequest(refId, 
				NumberUtil.normalize(aMsisdn), NumberUtil.normalize(bMsisdn), introMessage);
		
		if(!validateParam(umgResponse)) {
			setResultCode(ResponseBuilder.RESULTCODE_ERROR);
			addResponse(ResponseBuilder.build(ResponseBuilder.RESULT_FAILED, 
					ResponseBuilder.RESULTCODE_ERROR, ""));		
		} else {
			setResultCode(ResponseBuilder.RESULT_SUCCESS);
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
