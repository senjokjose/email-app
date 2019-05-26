package au.com.email.app.service;
import java.nio.charset.Charset;
import java.util.Base64;

import org.jsondoc.core.annotation.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import au.com.email.app.model.Message;
import au.com.email.app.rest.client.RestClient;
@Service
@Api(name = "MailGunEmailSender", description = "Methods for interacting  with MailGun email provider")
public class MailGunEmailSender implements EmailSender {
	private static final Logger logger = LoggerFactory.getLogger(MailGunEmailSender.class);
	@Autowired
	private RestClient restClient;

	@Value( "${mailgun.api.username}" )
	private String username;

	@Value( "${mailgun.api.password}" )
	private String password;

	@Value( "${mailgun.api.messages.url}" )
	private String resourceUrl;


	/**
	 * Internal implementation using rest client to post the request
	 */
	@Override
	public int sendEmail(Message message) throws Exception {
		logger.info("Start sending email through Mailgun");
		MultiValueMap<String, String> map = formatEmailContent(message);
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(map,
				createHeader(username,password));
		HttpStatus status =restClient.post(resourceUrl,requestEntity);
		logger.info("End sending email through Mailgun, status "+status.value());
		return status.value();
		
	}
	
	
	/**
	 * create header  using user name ands password
	 * @param username
	 * @param password
	 * @return
	 */
	private HttpHeaders createHeader(String username, String password) {
		return new HttpHeaders() {
			{
				String auth = username + ":" + password;
				String authHeader = "Basic " + new String(Base64.getEncoder().encode(auth.getBytes(Charset.defaultCharset())));
				set("Accept", MediaType.APPLICATION_JSON.toString());
				set("Content-Type", MediaType.APPLICATION_FORM_URLENCODED.toString());
				set("Authorization", authHeader);
			}
		};

	}
	/**
	 * Format message content acceptable for mail gun
	 * @param message
	 * @return
	 */
	private MultiValueMap<String, String> formatEmailContent(Message message) {
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		if(message.getFrom() !=null)
			map.add("from", message.getFrom());
		if(message.getTo() !=null)
			map.add("to", message.getTo());
		if(message.getSubject() !=null)
			map.add("subject", message.getSubject());
		if(message.getTextBody() !=null)
			map.add("text", message.getTextBody());
		if(message.getCc() !=null)
			map.add("cc", message.getCc());
		if(message.getBcc() !=null)
			map.add("bcc", message.getBcc());

		return map;
	}
}


