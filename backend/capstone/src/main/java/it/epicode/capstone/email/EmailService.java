package it.epicode.capstone.email;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.util.Properties;


@Service
public class EmailService {
    @Autowired
    private JavaMailSender emailSender;

    public void sendWelcomeEmail(String recipientEmail) {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setTo(recipientEmail);
            helper.setSubject("Benvenuto in MusicEveryWhere!");
            helper.setText("Grazie per esserti registrato. Ti diamo il Benvenuto nella nostra applicazione MusicEveryWhere!");

            emailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace(); // gestisci l'errore in modo adeguato
            throw new RuntimeException("Failed to send email", e);
        }
    }


//EMAIL PER ACQUISTO CARRELLO
    public void sendPurchaseConfirmationEmail(String recipientEmail) {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setTo(recipientEmail);
            helper.setSubject("Conferma di Acquisto");
            helper.setText("Complimenti per l'ottima scelta! Il tuo ordine è stato ricevuto e verrà elaborato a breve.");

            emailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace(); // gestisci l'errore in modo adeguato
        }
    }

    //EMAIL PER ACQUISTO NOLEGGIO
    public void sendPurchaseConfirmationEmailRental(String recipientEmail) {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setTo(recipientEmail);
            helper.setSubject("Conferma di Noleggio");
            helper.setText("Complimenti per l'ottima scelta! Il tuo ordine è stato ricevuto e verrà elaborato a breve.");

            emailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace(); // gestisci l'errore in modo adeguato
        }
    }


}
