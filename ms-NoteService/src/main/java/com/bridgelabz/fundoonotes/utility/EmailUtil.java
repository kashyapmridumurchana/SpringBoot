package com.bridgelabz.fundoonotes.utility;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

@Component
public class EmailUtil {

	@Autowired
	private MailSender mailSender;

	public void sendEmail(String toEmail, String subject, String activationUrl) {
		try {
			SimpleMailMessage msg = new SimpleMailMessage();
			msg.setFrom("mridumurchanakashyap49@gmail.com");
			msg.setTo("mridumurchanakashyap49@gmail.com");
			String message = "Note has been shared with you.\n kindly click the url to login and check for the shared note\n"
					+ "http://localhost:4200/login";
			msg.setText(message);
			msg.setSentDate(new Date());
			mailSender.send(msg);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
