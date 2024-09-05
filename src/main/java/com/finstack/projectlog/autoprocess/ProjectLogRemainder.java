package com.finstack.projectlog.autoprocess;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.finstack.projectlog.EmailUtil.EmailTemplateUtil;
import com.finstack.projectlog.entity.Resource;
import com.finstack.projectlog.service.WorkLogService;
import com.finstack.projectlog.service.WorkLogServiceImpl;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProjectLogRemainder {

	 @Value("${spring.mail.username}")
	    private String fromAddress;

	    @Autowired
	    private JavaMailSender javaMailSender;

	    @Autowired
	    private WorkLogService workLogService;
	    
	    @Autowired
	    private EmailTemplateUtil emailTemplateUtil;

	    @Scheduled(cron = "0 17 13 * * MON-FRI", zone = "Asia/Kolkata")
	    public void checkAndSendRemainders() {
	        List<Resource> resourcesWithoutWorkLog = workLogService.resourcesWithoutWorkLog();
	        String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

	        for (Resource resource : resourcesWithoutWorkLog) {
	            try {
	                sendReminderEmail(resource, currentDate);
	            } catch (MessagingException e) {
	                log.error("Failed to send reminder email to: {}", resource.getEmailAddress(), e);
	            }
	        }
	    }

	    private void sendReminderEmail(Resource resource, String currentDate) throws MessagingException {
	        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
	        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

	        mimeMessageHelper.setFrom(fromAddress);
	        mimeMessageHelper.setTo(resource.getEmailAddress());
	        mimeMessageHelper.setCc("worklogadmin@finstack-tech.com");
	        mimeMessageHelper.setSubject("Reminder: Worklog Submission Reminder for " + currentDate);

	        String fullName = resource.getFirstName() + " " + resource.getLastName();
	        String emailContent = emailTemplateUtil.getProcessedEmailContent("WorklogRemainder.xml", fullName, currentDate);
	        mimeMessageHelper.setText(emailContent, true);

	        try {
	            FileSystemResource logoResource = new FileSystemResource(new ClassPathResource("logo.finstack.png").getFile());
	            mimeMessageHelper.addInline("assistLogo", logoResource);
	        } catch (IOException e) {
	            log.error("Failed to load logo for email. Skipping inline attachment.", e);
	        }

	        javaMailSender.send(mimeMessage);
	        log.info("Sent reminder email to: {}", resource.getEmailAddress());
	    }

	    // Helper method to validate email addresses if needed
	    public boolean isValidEmail(String email) {
	        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
	        return email.matches(emailRegex);
	    }
}
