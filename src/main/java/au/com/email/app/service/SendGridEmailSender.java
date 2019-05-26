package au.com.email.app.service;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsondoc.core.annotation.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import au.com.email.app.model.Message;
import au.com.email.app.rest.client.RestClient;
@Service
@Api(name = "SendGridEmailSender", description = "Methods for interacting  with SendGrid email provider")
public class SendGridEmailSender implements EmailSender {
	private static final Logger logger = LoggerFactory.getLogger(SendGridEmailSender.class);
	@Autowired
	private RestClient restClient;

	@Value( "${sendgrid.api.key}" )
	private String apiKey;

	@Value( "${sendgrid.api.messages.url}" )
	private String resourceUrl;

	
	/**
	 * Internal implementation using rest client to post the request
	 */
	@Override
	public int sendEmail(Message message) {
		logger.info("Start sending email through SendGrid");
		String input = formatEmailContent(message);
		HttpEntity<String> requestEntity = new HttpEntity<>(input,
				createHeader(apiKey));
		restClient.post(resourceUrl,requestEntity );
		HttpStatus status =restClient.post(resourceUrl,requestEntity);
		logger.info("End sending email through SendGrid, status "+status.value());
		return status.value();
	}
	
	private HttpHeaders createHeader(String apiKey) {
		return new HttpHeaders() {
			{
				String authHeader = "Bearer " + apiKey;
				set("Content-Type", "application/json");
				set("Authorization", authHeader);
			}
		};

	}
	
	
	/**
	 * Format message content acceptable for send grid
	 * @param message
	 * @return
	 */
	private String formatEmailContent(Message message) {
		JSONObject json = new JSONObject();
		JSONArray pesronalizationArray = new JSONArray();
		
		//To
		JSONArray toArray = new JSONArray();
		for(String email:message.getTo().split(",")) {
			JSONObject to = new JSONObject();
			to.put("email", email);
			toArray.put(to);
		}
		JSONObject personlization = new JSONObject();
		personlization.put("to", toArray);

        //Subject
		personlization.put("subject", message.getSubject());
		pesronalizationArray.put(personlization);
		json.put("personalizations", pesronalizationArray);

		//From
		JSONObject from = new JSONObject();
		from.put("email", message.getFrom());
		json.put("from",from);

        //Content
		JSONArray contentArray = new JSONArray();
		JSONObject content = new JSONObject();
		content.put("type", "text/plain");
		content.put("value", message.getTextBody());
		contentArray.put(content);
		json.put("content", contentArray);
		
		// To do bcc and cc

		return json.toString();

	}
}


