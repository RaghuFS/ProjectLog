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

		            return content;
		        }
		    } catch (Exception e) {
		        throw new RuntimeException("Failed to process email template: " + e.getMessage(), e);
		    }

	  }
}
