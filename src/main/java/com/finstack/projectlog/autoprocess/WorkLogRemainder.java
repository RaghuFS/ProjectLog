package com.finstack.projectlog.autoprocess;

import java.util.List;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.finstack.projectlog.entity.Resource;
import com.finstack.projectlog.service.WorkLogService;
import com.finstack.projectlog.service.WorkLogServiceImpl;

@Service
public class WorkLogRemainder {

	@Value("${spring.mail.username}")
	String fromAddress;
	@Autowired
	private JavaMailSender javaMailSender;
	
	@Autowired
	WorkLogService workLogService;
	
	@Scheduled(cron = "0 08 16 * * MON-FRI",zone = "Asia/Kolkata")
	public void checkAndSendRemainders() {
		List<Resource> resourcesWithoutWorkLog = workLogService.resourcesWithoutWorkLog();
		for (Resource resource : resourcesWithoutWorkLog) {
			
			sendRemainderEmail(resource.getEmailAddress());
		}
	}
	
	public void sendRemainderEmail(String to) {
		SimpleMailMessage message = new SimpleMailMessage();
		
		message.setTo(to);
		message.setSubject("Work Log remainder");
		message.setText("reaminder mail for worklog, do it asap");
		javaMailSender.send(message);
	}
	
	public void sendEmail( String to) {
		try {
			MimeMessage msg = javaMailSender.createMimeMessage();
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(msg, true);
			mimeMessageHelper.setFrom(fromAddress);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
