package org.example.dam.util;
import java.io.*;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;


public class PdfGenerator {

    public void generateFromHtml(String htmlContent, String outputPath) throws IOException {
        try (OutputStream os = new FileOutputStream(outputPath)) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(htmlContent, null);
            builder.toStream(os);
            builder.run();
        }
    }
}

