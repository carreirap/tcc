package br.com.fichasordens.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import br.com.fichasordens.exception.ExcecaoRetorno;

@Component
public class Email {
	
	private static final Logger LOGGER = LogManager.getLogger(Email.class);
	
	@Value("${email.conta}")
	private String conta;
	
	@Value("${email.label}")
	private String label;
	
	@Value("${email.server.smtp}")
	private String smtpServer;
	
	

	public void enviarMail(final String destino, final String conteudo, final String titulo) throws ExcecaoRetorno {

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
			throw new ExcecaoRetorno(e.getMessage());
		}
	}
	
	public void enviarEmailComAnexo(final String destino, 
			final String titulo, final String conteudo, ByteArrayOutputStream pdf, String nameFile) throws MessagingException {
		Session session = this.criarSessaoEmail();
		Message message = criarEmail(session);
		message.setSubject(titulo);
		
		
		Address[] toUser = InternetAddress // Destinatário(s)
				.parse(destino);
		message.setRecipients(Message.RecipientType.TO, toUser);

        Multipart multipart = new MimeMultipart();
        MimeBodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent( conteudo, "text/html; charset=utf-8" );
        
        
        multipart.addBodyPart(htmlPart);

        MimeBodyPart anexo = new MimeBodyPart();
        BufferedDataSource  bds = new BufferedDataSource(pdf.toByteArray(), nameFile); 
        anexo.setDataHandler(new DataHandler(bds)); 
        anexo.setFileName(bds.getName()); 
        
        multipart.addBodyPart(anexo);

        message.setContent(multipart);

        enviarMensagem(session, message);
	}

	private void enviarMensagem(Session session, Message message) throws MessagingException {
		final Transport tr = session.getTransport("smtp");
		tr.connect(smtpServer, 465, conta, label);
		message.saveChanges();
		
		Transport.send(message);
		tr.close();

		LOGGER.info("Email Enviado");
	}
	
	private Message criarEmail(final Session session) throws MessagingException {
		
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(conta)); // Remetente
		
		return message;
	}

	private Session criarSessaoEmail() {
		final Properties props = new Properties();
		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.host", smtpServer);
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
		props.put("mail.smtp.debug", "true");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.debug", "true");
		props.put("mail.smtp.ssl", "true");

		SimpleAuth auth = null;
		auth = new SimpleAuth(conta, label);

		final Session session = Session.getInstance(props, auth);

		/** Ativa Debug para sessão */
		session.setDebug(true);

		return session;
	}
	
	class SimpleAuth extends Authenticator {    
	    private String username = null;    
	    private String password = null;    
	    public SimpleAuth(String user, String pwd) {    
	        username = user;    
	        password = pwd;    
	    }
	    @Override
	    protected PasswordAuthentication getPasswordAuthentication() {    
	        return new PasswordAuthentication (username,password);    
	    }    
	}
	
	class BufferedDataSource implements DataSource { 

		private byte[] _data; 
		private java.lang.String _name; 

		public BufferedDataSource(byte[] data, String name) { 
		_data = data; 
		_name = name;
	}

		public String getContentType() { return "application/octet-stream";} 
		public InputStream getInputStream() throws IOException { return new ByteArrayInputStream(_data);} 
		public String getName() { return _name;}

		@Override
		public OutputStream getOutputStream() throws IOException {
			// TODO Auto-generated method stub
			return null;
		} 

	}
}