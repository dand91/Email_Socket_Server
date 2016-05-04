import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


public class SendHTMLEmail {
	
	
	private String text;
	private String subject; 
	private String email;
	
    private static final String FROM = "admin@murkla.se";
    private static final String HOST = "localhost";
    private static final String HTMLFILE = "index.html";
    private static final String PATH = "images/";
    private static final String[] FILELIST = {"header.jpg","image_1.jpg"};


	public SendHTMLEmail(String text,String subject, String email){
	      
	   this.text = text;
	   this.subject = subject;
	   this.email = email;
	   
	}
	
	public void send(){
		
		      String to = email;

		      Properties properties = System.getProperties();      
		      properties.setProperty("mail.smtp.host", HOST);
		      //props.put("mail.smtp.auth", "true");
		      //props.put("mail.smtp.starttls.enable", "true");
		      Session session = Session.getDefaultInstance(properties);

		      System.out.println("<debug> Email processing </debug>");

		      StringBuilder sb = null;
		      
		      try(BufferedReader br = new BufferedReader(new FileReader(HTMLFILE))) {
		    	    sb = new StringBuilder();
		    	    String line = br.readLine();

		    	    while (line != null) {
		    	        sb.append(line);
		    	        sb.append(System.lineSeparator());
		    	        line = br.readLine();
		    	    }
		    	} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
		      try{
		    	  
		    	   MimeMessage message = new MimeMessage(session);
		           message.setFrom(new InternetAddress(FROM));
		           message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
		           message.setSubject(subject);
		           
		    	  MimeMultipart multipart = new MimeMultipart("related");

		            BodyPart messageBodyPart = new MimeBodyPart();
		            
		          String htmlText = editText(sb.toString(),text);
		        
		          messageBodyPart.setContent(htmlText, "text/html; charset=utf-8");
		          
		          multipart.addBodyPart(messageBodyPart);
		          		          
		          for(int i = 0 ; i < FILELIST.length ; i++){
		        	  
		          MimeBodyPart imagePart = new MimeBodyPart();
		          
		          imagePart.attachFile(PATH + 
		                  FILELIST[i]);
		          

		          imagePart.setHeader("Content-ID", "<" + FILELIST[i] + ">");
		          imagePart.setDisposition(MimeBodyPart.INLINE);


		          multipart.addBodyPart(imagePart);
		          
		          }
		          
		          message.setContent(multipart);

		         Transport.send(message);
		         
		         System.out.println("<debug> Email sent </debug>");
		         
		      }catch (MessagingException mex) {
		         mex.printStackTrace();
		      } catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	private String editText(String htmlText, String text){
		
		 Calendar now = GregorianCalendar.getInstance();
		   
  	   	  String month = "";
  	   	  
  	   	  switch(now.get(Calendar.MONTH)){
  	   	  
  	   	  	case 0: month = "JANUARY";break;
  	   	  	case 1: month = "FEBRUARY";break;
  	   	  	case 2: month = "MARCH";break;
  	   	  	case 3: month = "APRIL";break;
  	   	  	case 4: month = "MAY";break;
  	   	  	case 5: month = "JUNE";break;
  	   	  	case 6: month = "JULY";break;
  	   	  	case 7: month = "AUGUST";break;
  	   	  	case 8: month = "SEPTEMBER";break;
  	   	  	case 9: month = "OCTOBER";break;
  	   	  	case 10: month = "NOVEMBER";break;
  	   	  	case 11: month = "DECEMBER";break;

  	   	  }
  	   	  
  	   	  System.out.println("Date: " + now.get(Calendar.YEAR) + " : " + month);
  	   	  System.out.println("Text: " + text);

  	   	  htmlText = htmlText.replace("[date]", now.get(Calendar.YEAR) + " : " + month)
  	   			  .replace("[text]", text)
  	   			  .replace("[endtext]", "Vill du veta mer om mig så besök gärna min CV-hemsida.")
  	   			  .replace("[greeting]","Hej!")
  	   			  .replace("[greetingtext]","Med vänlig hälsningar,")
  	   			  .replace("[name]","David.");
		
  	   	  return htmlText;
		
	}

}
