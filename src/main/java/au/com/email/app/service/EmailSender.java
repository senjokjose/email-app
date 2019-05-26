package au.com.email.app.service;
import org.jsondoc.core.annotation.Api;

import au.com.email.app.model.Message;

@Api(name = "EmailSender Inteface", description = "Methods for interacting with email providers")
public interface EmailSender {
  int sendEmail(Message message) throws Exception;
  
}
