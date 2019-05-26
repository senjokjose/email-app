package au.com.email.app.service;

import org.apache.commons.validator.routines.EmailValidator;
import org.jsondoc.core.annotation.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import au.com.email.app.model.Message;

@Api(name = "EmailService", description = "Methods for assisting email services")
@Service
public class EmailService {
	public static final String EMAIL_DELIMITER =",";
	private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
	
	@Autowired MailGunEmailSender mailGunEmailSender;
	@Autowired SendGridEmailSender sendGridEmailSender;
	
	/**
	 * Send email, Strategy: system will use a secondary provider in case primary is down
	 * @param message
	 * @return
	 */
	public boolean sendEmail(Message message)throws IllegalAccessException {
		validateEmail(message);
		boolean success = sendEmail(message, mailGunEmailSender);
		if(success) {
			return success;
		} //fallback to next provider, Can configure multiple providers
		return sendEmail(message, sendGridEmailSender);
	}
	
	/**
	 * Send email using a specific provider
	 * @param message
	 * @param emailSender
	 * @return
	 */
	private boolean sendEmail(Message message, EmailSender emailSender) {
		try {
		 int status = emailSender.sendEmail(message);
		 if(status != HttpStatus.OK.value()) {
			 logger.warn(emailSender.getClass().getName()+" returned status code:"+status);  // Logging error code 202 etc, Email is not delivered but in queue
		 }
		}catch(Exception e) {
			logger.warn("Failed to send email through "+emailSender.getClass().getName()+ ", "+e);
			return false;
		}
		return true;

	}
	
	/**
	 * validate the message parameters
	 * @param message
	 * @throws IllegalAccessException
	 */
	private void validateEmail(Message message) throws IllegalArgumentException {
		if(StringUtils.isEmpty(message.getTo())){
			throw new IllegalArgumentException("To address is empty");
		}
		
		if(message.getTo().contains(EMAIL_DELIMITER)) {  //validate each address in the list if any of them is found invalid reject all ? or skip that and proceed
			for(String to:message.getTo().split(EMAIL_DELIMITER)) {
				if(!EmailValidator.getInstance().isValid(to)){
					throw new IllegalArgumentException("Invalid To address:"+to);  // if anything found invalid reject all
				}
			}
			
		}else if(!EmailValidator.getInstance().isValid(message.getTo())){
			throw new IllegalArgumentException("Invalid To address");
		}
		
		
		if(StringUtils.isEmpty(message.getFrom())){
			throw new IllegalArgumentException("From address is empty");
		}
		if(StringUtils.isEmpty(message.getSubject())){
			throw new IllegalArgumentException("Subject is empty");
		}
		
		// Skipping cc, bcc, and subject validation for now
		
		
	}
	
	 
}
