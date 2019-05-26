package au.com.email.app.rest.client;


import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
@Api(name = "RestClient", description = "Methods supporting http post operation, can be extended")
@Service
public class RestClient {

	private RestTemplate restTemplate;

	@Autowired
	public RestClient(RestTemplateBuilder builder) {
		this.restTemplate =  builder.build();
	}

    /**
     * Post a request
     * @param resourceUrl
     * @param requestEntity
     * @return
     */
	public HttpStatus post(String resourceUrl, HttpEntity requestEntity) {

		ResponseEntity<String> response = this.restTemplate.exchange(resourceUrl, HttpMethod.POST, requestEntity, String.class);
		//Object postObject = response.getBody();
		return response.getStatusCode();
	}
	
	
}