package utils;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
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
import javax.mail.util.ByteArrayDataSource;

public class EnvioEmail {

public static void enviar(String titulo,byte[] bytesEmail) {
    final String username = "versiontotalcorner@gmail.com";
    final String emailPara = "versiontotalcorner@gmail.com";
    final String password = "Corsita2.0";

    Properties props = new Properties();
    props.put("mail.smtp.auth", true);
    props.put("mail.smtp.host", "smtp.gmail.com");
    props.put("mail.smtp.port", "465");
    props.setProperty("mail.smtp.ssl.enable", "true");


    // Session session = Session.getDefaultInstance(props, null);
    Session session = Session.getInstance(props,
            new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });


    Message msg = new MimeMessage(session);
    try {
        msg.setFrom(new InternetAddress(username));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(emailPara));
        msg.setSubject("Consejos generados  - " + titulo  );

        Multipart multipart = new MimeMultipart();

        MimeBodyPart textBodyPart = new MimeBodyPart();
        textBodyPart.setText("Adjunto en este email está el pdf: " + titulo + " del día "+ getFechaActual());

        MimeBodyPart attachmentBodyPart= new MimeBodyPart();
        DataSource dataSource = new ByteArrayDataSource(bytesEmail, "application/pdf");
        attachmentBodyPart.setDataHandler(new DataHandler(dataSource));
        attachmentBodyPart.setFileName(titulo+".pdf");

        multipart.addBodyPart(textBodyPart);  // add the text part
        multipart.addBodyPart(attachmentBodyPart); // add the attachement part

        msg.setContent(multipart);


        Transport.send(msg);
    } catch (MessagingException e) {
        System.out.println("Error al enviar email");
    }
  }

	public static String getFechaActual() { 
    Date objDate = new Date(); // Sistema actual La fecha y la hora se asignan a objDate 
    String strDateFormat = "dd-MMM-aaaa hh: mm: ss"; // El formato de fecha está especificado  
    SimpleDateFormat objSDF = new SimpleDateFormat(strDateFormat); // La cadena de formato de fecha se pasa como un argumento al objeto 
     return objSDF.format(objDate);
	}
	
	public static void enviarFallidosTelegram(String urlTelegram,String idPartido) {
	    final String username = "versiontotalcorner@gmail.com";
	    final String emailPara = "versiontotalcorner@gmail.com";
	    final String password = "Corsita2.0";

	    Properties props = new Properties();
	    props.put("mail.smtp.auth", true);
	    props.put("mail.smtp.host", "smtp.gmail.com");
	    props.put("mail.smtp.port", "465");
	    props.setProperty("mail.smtp.ssl.enable", "true");


	    // Session session = Session.getDefaultInstance(props, null);
	    Session session = Session.getInstance(props,
	            new javax.mail.Authenticator() {
	                protected PasswordAuthentication getPasswordAuthentication() {
	                    return new PasswordAuthentication(username, password);
	                }
	            });


	    Message msg = new MimeMessage(session);
	    try {
	        msg.setFrom(new InternetAddress(username));
	        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(emailPara));
	        msg.setSubject("Partido Telegram Fallido - " + idPartido  );

	        Multipart multipart = new MimeMultipart();

	        MimeBodyPart textBodyPart = new MimeBodyPart();
	        textBodyPart.setText("Visite esta url para enviar el partido al grupo: " + urlTelegram);
	        multipart.addBodyPart(textBodyPart);  // add the text part

	        msg.setContent(multipart);


	        Transport.send(msg);
	    } catch (MessagingException e) {
	        System.out.println("Error al enviar email " + e.getMessage());
	    }
	  }

}
