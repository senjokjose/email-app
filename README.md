# Email Application

Service accepts json format and sends email. A valid input format can be found below. Multiple email "to" address can be separated with ","

{
"subject": "Email content from application",
"from": "from@domain.com",
"to": "to@domain.com",
"cc": "cc@domain.com",
"bcc": "bcc@domain.com",
"textBody": "Sample alert"
}

You may use postman or any other rest client application to post the request, If you use postman, please add header parameter "Content-Type:application/json" 


# Technology used
  1. Springboot2
  2. Apache Maven 3.6.0

# How to Build and Run

  mvn clean install
  
  To run the application, simply use "mvn clean spring-boot:run" in terminal. This will bring up the application in port 5000
 
# URL
  http://localhost:5000/email/send
  
 
# Scope and limitations

 1. Only default profile is created, we can extend this to any other environment by creating new property files
 2. Properties can be found at application.properties, I have not included Api Keys for mailgun and sendgrid. Please add those before starting the     application 
 3. Various validations applied on the code for example,
    a. InvalidInput type. Accepts only application/json
    b. Validating the essential To address format, subject etc. Currently not handling any validations for CC, BCC
 4. Supporting multiple To address. Constraint: If any of the address in To address input is invalid rejecting all, This can be changed
 5. Not supporting cc,bcc for SendGrid service, yet to do
 6. Not sending any user specific message in case of email is in queue. Just logging only
 7. TTL in service call?
 
    
    
# Test Cases

I have started test case with Mochito but could not finish it. I am ready to discuss on that
  