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
        helper.setFrom("yvo.sobrero@gmail.com");
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

        // Replace body placeholder
        helper.setText(content, true);  // true = HTML

        // Add PDF attachment
       /* byte[] pdfBytes = generatePdf();  // Generate the PDF (this method will be explained)
        helper.addAttachment("ticket.pdf", new ByteArrayDataSource(pdfBytes, "application/pdf"));*/

        try {
            javaMailSender.send(message);
        } catch (MailException e) {
            throw new Exception("Failed to send email", e);
        }
    }

    private String getContentOfHTML(String filename) throws IOException {
        try (InputStream is = EmailsSender.class.getClassLoader().getResourceAsStream(filename)) {
            if (is == null) {
                return "";
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                return reader.lines().collect(Collectors.joining(System.lineSeparator()));
            }
        }
    }

   /* private byte[] generatePdf() throws IOException {
        // PDF generation logic (using iText)
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            com.itextpdf.kernel.pdf.PdfWriter writer = new com.itextpdf.kernel.pdf.PdfWriter(baos);
            com.itextpdf.kernel.pdf.PdfDocument pdfDoc = new com.itextpdf.kernel.pdf.PdfDocument(writer);
            com.itextpdf.layout.Document doc = new com.itextpdf.layout.Document(pdfDoc);

            doc.add(new com.itextpdf.layout.element.Paragraph("Your flight details:"));
            doc.add(new com.itextpdf.layout.element.Paragraph("This is your ticket."));  // Customize content here

            doc.close();
            return baos.toByteArray();  // Return PDF as byte array
        }
    }*/


}


