/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paymenteditor;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author ShaerulH
 */
public class PaymentAdviceSendThroughEmail {

    public static void sendEmailWithAttachments(String host, String port,
            final String userName, final String password, String toAddress, String ccAddress,
            String subject, String message, String[] attachFiles)
            throws AddressException, MessagingException {

        // sets SMTP server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.user", userName);
        properties.put("mail.password", password);

        // creates a new session with an authenticator
        Authenticator auth = new Authenticator() {
            @Override
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        };

        Session session = Session.getInstance(properties, auth);

        // creates a new e-mail message
        Message msg = new MimeMessage(session);

        msg.setFrom(new InternetAddress(userName));
        InternetAddress[] toAddresses = {new InternetAddress(toAddress)};
        msg.setRecipients(Message.RecipientType.TO, toAddresses);

        InternetAddress[] ccAddresses = {new InternetAddress(ccAddress)};
        msg.setRecipients(Message.RecipientType.CC, ccAddresses);
        msg.setSubject(subject);
        msg.setSentDate(new Date());

        // creates message part
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(message, "text/html");

        // creates multi-part
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);

        // adds attachments
        if (attachFiles != null && attachFiles.length > 0) {
            for (String filePath : attachFiles) {
                MimeBodyPart attachPart = new MimeBodyPart();

                try {
                    attachPart.attachFile(filePath);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                multipart.addBodyPart(attachPart);
            }
        }

        // sets the multi-part as e-mail's content
        msg.setContent(multipart);

        // sends the e-mail
        Transport.send(msg);

    }

    /**
     * Test sending e-mail with attachments
     */
    public static void main(String[] args) {

        // SMTP info
        String host = "smtp.epicstudiouk.com";
        String port = "25";
        String mailFrom = "mis@epicstudiouk.com";
        String password = "epuk2005";

        // message info
        String mailTo = "shaerul@gmail.com";
        String mailCC = "shaerul.haque@epicbd.com";
        String subject = "Payment Advice #23232323";
        /*
        String message = "Dear Sir,\n\nYour Payment has been Made and Advice has been attached herewith this email.\n"
                + "Thanks.\n\nBest Regards,\nEPIC Finance Team";
         */

        // message contains HTML markups
        String message = "Dear Supplier,<br><br>";
        message += "Your payment has been transferred successfully to your nominated Bank Account.<br>"
                + "Your <b>\"PAYMENT ADVICE\"</b> has been attached herewith this email.<br>";
        message += "Thanks for being with EPIC.<br><br>";
        message += "Best Regards,<br>";
        message += "EPIC Finance Team";

        //message += "<font color=red>Duke</font>";
        // attachments
        String[] attachFiles = new String[1];
        attachFiles[0] = "e:\\iTextImageExample.pdf";
        //attachFiles[1] = "e:/Test/Music.mp3";
        //attachFiles[2] = "e:/Test/Video.mp4";

        try {

            sendEmailWithAttachments(host, port, mailFrom, password, mailTo, mailCC,
                    subject, message, attachFiles);
            System.out.println("Email sent.");

        } catch (Exception ex) {
            System.out.println("Could not send email.");
            ex.printStackTrace();
        }
    }
}
