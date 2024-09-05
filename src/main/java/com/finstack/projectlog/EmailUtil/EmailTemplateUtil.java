package com.finstack.projectlog.EmailUtil;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

@Component
public class EmailTemplateUtil {
	/**
     * Processes the email template by replacing placeholders with actual values.
     *
     * @param templateFileName the name of the XML template file
     * @param resourceName the name of the resource to replace in the template
     * @param currentDate the current date to replace in the template
     * @return the processed email content with placeholders replaced
     */
	  public String getProcessedEmailContent(String templateFileName, String resourceName, String currentDate) {
		  try {
		        ClassPathResource resource = new ClassPathResource("EmailTemplate/" + templateFileName);

		        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		        DocumentBuilder builder = factory.newDocumentBuilder();
		        
		        try (Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
		            Document doc = builder.parse(new InputSource(reader));
		            String content = doc.getElementsByTagName("mailContent").item(0).getTextContent();
		            content = content.replace("<%resourceName%>", resourceName)
		                             .replace("<%currentDate%>", currentDate);
		       //     String sub = doc.getElementsByTagName("mailSubject").item(0).getTextContent();
		        //    sub = sub.replace("<%currentDate%>", currentDate);
		            
		            return content;
		        }
		    } catch (Exception e) {
		        throw new RuntimeException("Failed to process email template: " + e.getMessage(), e);
		    }

	  }
}
