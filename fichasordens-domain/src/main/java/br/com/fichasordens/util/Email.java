package br.com.fichasordens.util;

import java.io.ByteArrayOutputStream;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Email {
	
	@Value("${email.conta}")
	private String conta;
	
	@Value("${email.label}")
	private String label;
	
	

	public void enviarMail(final String destino, final String conteudo, final String titulo) {

		try {
			Session session = this.criarSessaoEmail();
			Message message = this.criarEmail(session);

			Address[] toUser = InternetAddress // Destinatário(s)
					.parse(destino);

			message.setRecipients(Message.RecipientType.TO, toUser);
			message.setSubject(titulo);// Assunto
			message.setText(conteudo);

			
			enviarMensagem(session, message);

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void enviarEmailComAnexo(final String titulo, final String conteudo, ByteArrayOutputStream pdf, String nameFile) throws Exception {
		Session session = this.criarSessaoEmail();
		Message message = criarEmail(session);
		message.setSubject(titulo);
		
        BodyPart messageBodyPart = new MimeBodyPart();

        messageBodyPart.setText(conteudo);

        Multipart multipart = new MimeMultipart();

        multipart.addBodyPart(messageBodyPart);

        messageBodyPart = new MimeBodyPart();
        ByteArrayDataSource bds = new ByteArrayDataSource(pdf.toByteArray(), nameFile); 
        messageBodyPart.setDataHandler(new DataHandler(bds)); 
        messageBodyPart.setFileName(bds.getName()); 
        
        multipart.addBodyPart(messageBodyPart);

        message.setContent(multipart);

        enviarMensagem(session, message);
	}

	private void enviarMensagem(Session session, Message message) throws NoSuchProviderException, MessagingException {
		Transport tr = session.getTransport("smtp");
		tr.connect("smtp.gmail.com", 465, "paulhatake", label);
		message.saveChanges();
		
		tr.send(message);
		tr.close();

		System.out.println("Feito!!!");
	}
	
	private Message criarEmail(final Session session) throws AddressException, MessagingException {
		
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(conta)); // Remetente
		
		return message;
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
