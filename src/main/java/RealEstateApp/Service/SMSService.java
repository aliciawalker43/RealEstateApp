package RealEstateApp.Service;

import org.springframework.stereotype.Service;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;

import org.springframework.beans.factory.annotation.Value;


@Service
public class SMSService {
	
	@Value("${twilio.account_sid}")
    private  String ACCOUNT_SID;
	
	@Value("${twilio.auth_token}")
    private String AUTH_TOKEN;
	
	@Value("${twilio.from_number}")
    private  String FROM_NUMBER;

    
  

    public void sendSms(String to, String body) {
    	Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message.creator(
            new com.twilio.type.PhoneNumber(to),//to
            new com.twilio.type.PhoneNumber(FROM_NUMBER),//from
             body
        ).create();
    }

}
