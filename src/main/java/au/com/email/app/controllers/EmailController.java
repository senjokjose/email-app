package au.com.email.app.controllers;

import javax.servlet.http.HttpServletRequest;

import org.jsondoc.core.annotation.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import au.com.email.app.model.Message;
import au.com.email.app.service.EmailService;

@Api(name = "EmailController", description = "Methods for assisting email services")
@Controller
@RequestMapping(value = "/email")
public class EmailController {
	private static final Logger logger = LoggerFactory.getLogger(EmailController.class);
	
	@Autowired EmailService emailHandlerService;
	
	@RequestMapping(value = "/send", method = RequestMethod.POST )
    public ResponseEntity<String> sendEmail(@RequestBody Message message)  throws Exception{
        if(!emailHandlerService.sendEmail(message) ) {
        	throw new Exception("Error in sending  email. All the email providers are failing");
        }
        return new ResponseEntity<String>("Email sent successfully", HttpStatus.OK);
    }
	
	@ExceptionHandler(HttpMediaTypeNotSupportedException.class) 
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ResponseEntity<String> handleHttpMediaTypeNotAcceptableException(HttpMediaTypeNotSupportedException ex,HttpServletRequest request) {
	    logger.error("InvalidInputFormat - on request: " + request.getRequestURI());
		return new ResponseEntity<String>("acceptable MIME type:" + MediaType.APPLICATION_JSON_VALUE , HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ResponseEntity<String> handlleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request) {
		logger.error("InvalidArguments - on request: " + request.getRequestURI());
		return new ResponseEntity<String>("Unable to send Email:"+ex.getMessage() , HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<String> handleSystemError(Exception ex, HttpServletRequest request) {
		logger.error("Error occured - on request: " + ex);
		return new ResponseEntity<String>("Unable to send Email, Contact technical team" , HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}
