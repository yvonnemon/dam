package org.example.dam.util;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.stream.Collectors;

@Component
public class EmailsSender {
    @Value("${spring.mail.username}")
    private String mail;

    private final JavaMailSender javaMailSender;

    public EmailsSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendEmailWithAttachment(String subject, String dataAsBody, String userEmail, String userName) throws Exception {
        MimeMessage message = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);  // true = multipart
        helper.setFrom(mail); //TODO hardcoded for development
        helper.setTo(userEmail);
        helper.setSubject(subject);

        // Get HTML content
        String content;
        try {
            content = getContentOfHTML("templates/email_template.html")
                    .replace("{{body}}", dataAsBody)
                    .replace("{{firstName}}", userEmail);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        helper.setText(content, true);  // true = HTML

        try {
            javaMailSender.send(message);
        } catch (MailException e) {
            throw new Exception("Failed to send email", e);
        }
    }

    public String getContentOfHTML(String filename) throws IOException {
        try (InputStream is = EmailsSender.class.getClassLoader().getResourceAsStream(filename)) {
            if (is == null) {
                return "";
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                return reader.lines().collect(Collectors.joining(System.lineSeparator()));
            }
        }
    }
}


