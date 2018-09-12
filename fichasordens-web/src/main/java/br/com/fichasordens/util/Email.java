package br.com.fichasordens.util;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Email {

	public void enviarMail() {

		try {
			Session s = this.criarSessaoEmail();
			Message message = new MimeMessage(s);
			message.setFrom(new InternetAddress("paulhatake@gmail.com")); // Remetente

			Address[] toUser = InternetAddress // Destinatário(s)
					.parse("carreira_p@yahoo.com.br");

			message.setRecipients(Message.RecipientType.TO, toUser);
			message.setSubject("Enviando email com JavaMail");// Assunto
			message.setText("Enviei este email utilizando JavaMail com minha conta GMail!");
			/** Método para enviar a mensagem criada */
			
			Transport tr = s.getTransport("smtp");
			tr.connect("smtp.gmail.com", 465, "paulhatake", "#3V3l2n$");
			message.saveChanges();
			
			tr.send(message);
			tr.close();

			System.out.println("Feito!!!");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	private Session criarSessaoEmail() {
		final Properties props = new Properties();
		/** Parâmetros de conexão com servidor Gmail */
		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.host", "smtp.gmail.com");
		props.put("mail.smtp.host", "smtp.gmail.com");
		 props.put("mail.smtp.starttls.enable","true");  
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
		props.put("mail.smtp.debug", "true");
		props.put("mail.debug", "true");    
		
		 SimpleAuth auth = null;    
	        auth = new SimpleAuth ("paulhatake","#3V3l2n$");

		final Session session = Session.getDefaultInstance(props, auth);
		

		/** Ativa Debug para sessão */
		session.setDebug(true);

		return session;
	}
	
	class SimpleAuth extends Authenticator {    
	    public String username = null;    
	    public String password = null;    
	    public SimpleAuth(String user, String pwd) {    
	        username = user;    
	        password = pwd;    
	    }    
	    protected PasswordAuthentication getPasswordAuthentication() {    
	        return new PasswordAuthentication (username,password);    
	    }    
	}

}
