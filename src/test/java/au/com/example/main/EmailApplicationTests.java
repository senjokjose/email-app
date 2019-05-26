package au.com.example.main;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import au.com.email.app.model.Message;

@RunWith(SpringRunner.class)
@SpringBootTest(classes={EmailApplicationTests.class})
@ComponentScan(basePackages={"au.com.email.app"})
@AutoConfigureWebClient
public class EmailApplicationTests {
	
	protected MockMvc mockMvc;
	//protected static EmailService emailService;
	@Autowired protected WebApplicationContext context;
	

	
	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}
	
	@Test
	public void sendemailHappyCase() throws Exception {
		/*String json = getJsonString("sampleemail@somedoamin.com", "xxxxxxxx@gmail.com", "Test Email", "Sample Email body");
		mockMvc.perform(post("http://localhost:5000/email/send/")
		        .contentType(MediaType.APPLICATION_JSON).content(json))
		        .andDo(print())
		        .andExpect(status().isOk());
		        */
	}
	//@Test
	public void sendemailHappyCaseMultipleToAddress() throws Exception {
		String json = getJsonString("sampleemail@somedoamin.com", "xxxxxxxx@gmail.com,jose.k.@gmail.com", "Test Email", "Sample Email body");
		mockMvc.perform(post("http://localhost:5000/email/send")
		        .contentType(MediaType.APPLICATION_JSON).content(json))
		        .andExpect(status().isOk());
	}
	//@Test
	public void sendemailInvalidEmailAddress() throws Exception {
		String json = getJsonString("sampleemail@somedoamin.com", "xxxxxxxxx@gmail.com", "Test Email", "Sample Email body");
		mockMvc.perform(post("http://localhost:5000/email/send")
		        .contentType(MediaType.APPLICATION_JSON).content(json))
		        .andExpect(status().isOk());
		      //  .andExpect("");
	}
	private String getJsonString(String from, String to, String subject,String body) throws JsonProcessingException {
		Message message = new Message(subject,from,to);
		message.setTextBody(body);
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(message);
	}

}
