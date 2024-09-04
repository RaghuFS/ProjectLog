package com.finstack.projectlog.autoprocess;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.finstack.projectlog.entity.FrequencyEnum;
import com.finstack.projectlog.entity.Resource;
import com.finstack.projectlog.entity.Role;
import com.finstack.projectlog.entity.WorkLog;
import com.finstack.projectlog.exception.UnauthorizedException;
import com.finstack.projectlog.jwtauth.configuration.service.JWTUtil;
import com.finstack.projectlog.repository.ResourceRepository;
import com.finstack.projectlog.repository.WorkLogRepository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class WorkLogReport {

    private FrequencyEnum frequency = FrequencyEnum.NONE; // Set the default frequency
    
    
    private WorkLogRepository workLogRepository;
    private JWTUtil jwtUtil;
    private ResourceRepository resourceRepository;
    private JavaMailSender javaMailSender;

    @Autowired
    public WorkLogReport(WorkLogRepository workLogRepository, JWTUtil jwtUtil, ResourceRepository resourceRepository, JavaMailSender javaMailSender){
    	this.workLogRepository = workLogRepository;
    	this.jwtUtil = jwtUtil;
    	this.resourceRepository = resourceRepository;
    	this.javaMailSender = javaMailSender;
    }
    public void setFrequency(FrequencyEnum frequency) {
        this.frequency = frequency;
    }
    // check the resource has worklog_admin role
    
    
    
    

    @Scheduled(cron = "0 11 16 * * ?") // Runs every day at 12 PM
    public void executeTask() {
    	
		/*
		 * String rolesFromRequest = jwtUtil.extractRolesFromRequest(); List<String>
		 * rolesList = Arrays.asList(rolesFromRequest.split(","));
		 * if(!rolesList.contains("WORKLOG_ADMIN")) { setFrequency(FrequencyEnum.NONE);
		 * throw new UnauthorizedException(); } String usernameFromRequest =
		 * jwtUtil.extractUsernameFromRequest(); Resource resource =
		 * resourceRepository.findByEmailAddress(usernameFromRequest).orElseThrow(()->
		 * new ResourceNotFoundException("Resource not found")); frequency=
		 * resource.getWorkFrequency(); String to_address = usernameFromRequest;
		 */
    	
    	
    	List<Resource> resources = resourceRepository.findAll();
    	List<Resource> worklogAdmins = new ArrayList<>();
    	for (Resource resource : resources) {
			List<Role> roles = resource.getRoles();
			for(Role role : roles) {
				if(role.getName().equals("WORKLOGADMIN")) {
					System.out.println("worklog admin "+resource);
					worklogAdmins.add(resource);
				}
			}

		}
    	worklogAdmins.forEach(System.out::println);
        LocalDate today = LocalDate.now();
        
        for (Resource resource : worklogAdmins) {
        	if(resource.getWorkFrequency() == null ) {
        		setFrequency(FrequencyEnum.NONE);
        	}else {
        		
        		 setFrequency(resource.getWorkFrequency());
        	}
	        switch (frequency) {
	            case DAILY:
	                //runTask(); // Run daily at 12 PM
	            	//todaddress
	            	runTaskDaily();
	                break;
	            case WEEKLY:
	                if (isLastWorkingDayOfWeek(today)) {
	                    //runTask(); // Run on the last working day of the week at 12 PM
	                	runTaskWeekly();
	                }
	                break;
	            case MONTHLY:
	                if (isLastWorkingDayOfMonth(today)) {
	                    //runTask(); // Run on the last working day of the month at 12 PM
	                	runTaskMonthly();
	                }
	                break;
	            case YEARLY:
	                if (isLastWorkingDayOfYear(today)) {
	                    //runTask(); // Run on the last working day of the year at 12 PM
	                	runTaskYearly();
	                }
	                break;
	            case NONE:
	                // No task to run
	                break;
	        }
        }
    }



	private boolean isLastWorkingDayOfWeek(LocalDate date) {
        DayOfWeek lastWorkingDay = DayOfWeek.FRIDAY;
        return date.equals(date.with(TemporalAdjusters.nextOrSame(lastWorkingDay)));
    }

    private boolean isLastWorkingDayOfMonth(LocalDate date) {
        return date.equals(getLastWorkingDayOfMonth(date));
    }

    private boolean isLastWorkingDayOfYear(LocalDate date) {
        return date.equals(getLastWorkingDayOfYear(date));
    }

    private LocalDate getLastWorkingDayOfMonth(LocalDate date) {
        LocalDate lastDayOfMonth = date.with(TemporalAdjusters.lastDayOfMonth());
        return adjustForWeekend(lastDayOfMonth);
    }

    private LocalDate getLastWorkingDayOfYear(LocalDate date) {
        LocalDate lastDayOfYear = date.with(TemporalAdjusters.lastDayOfYear());
        return adjustForWeekend(lastDayOfYear);
    }

    private LocalDate adjustForWeekend(LocalDate date) {
        if (date.getDayOfWeek() == DayOfWeek.SATURDAY) {
            return date.minusDays(1); // Move to Friday
        } else if (date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            return date.minusDays(2); // Move to Friday
        } else {
            return date;
        }
    }
    
    private void runTaskYearly() {
		List<WorkLog> yearlyWorkLogs = workLogRepository.findYearlyWorkLogs();
	    String sheetName= "Yearly Work Log Report";
	    String fileName= "YearlyWorkLogReport.xlsx";
	    sendEmail(yearlyWorkLogs,sheetName,fileName);
	
	}

	private void runTaskMonthly() {
		List<WorkLog> monthlyWorkLogs = workLogRepository.findMonthlyWorkLogs();
	    String sheetName= "Monthly Work Log Report";
	    String fileName= "MonthlyWorkLogReport.xlsx";
	    sendEmail(monthlyWorkLogs,sheetName,fileName);
	
	}

	private void runTaskWeekly() {
		List<WorkLog> weeklyWorkLogs = workLogRepository.findWeeklyWorkLogs();
	    String sheetName= "Weekly Work Log Report";
	    String fileName= "WeeklyWorkLogReport.xlsx";
	    sendEmail(weeklyWorkLogs,sheetName,fileName);	
	}

	//private
	public void runTaskDaily() {

		//List<Object[]> totalTimeGroupedByResourceIdForToday = workLogRepository.findLogsForDaily();
		//Map<String, String> totalTimeWithResouceIds = totalTimeWithResouceIds(totalTimeGroupedByResourceIdForToday);
//	    Map<String, Integer> totalTimeByResourceId = new HashMap<>();
//
//	    totalTimeGroupedByResourceIdForToday.forEach(record -> {
//	        String resourceId = (String) record[0];
//	        String totalTime = (String) record[1];
//	        int totalMinutes = convertTimeToMinutes(totalTime);
//	        totalTimeByResourceId.merge(resourceId, totalMinutes, Integer::sum);
//	    });
//	    
//	    Map<String, String> dailyMap = new HashMap<>();
//	    totalTimeByResourceId.forEach((resourceId, totalMinutes) -> {
//	    	//String formattedTotalTime = convertMinutesToTime(totalMinutes);
//	    	//dailyMap.put(resourceId, formattedTotalTime);
//	    	dailyMap.put(resourceId, convertMinutesToTime(totalMinutes));
//	    });	 
		
		List<WorkLog> dailyWorkLogs = workLogRepository.findDailyWorkLogs();
	    String sheetName= "Daily Work Log Report";
	    String fileName= "DailyWorkLogReport.xlsx";
	    sendEmail(dailyWorkLogs,sheetName,fileName);
	}
	
	private Map<String, String> totalTimeWithResouceIds(List<Object[]> totalTimeGroupedByResourceIdForToday) {
	    Map<String, Integer> totalTimeByResourceId = new HashMap<>();

	    totalTimeGroupedByResourceIdForToday.forEach(record -> {
	        String resourceId = (String) record[0];
	        String totalTime = (String) record[1];
	        int totalMinutes = convertTimeToMinutes(totalTime);
	        totalTimeByResourceId.merge(resourceId, totalMinutes, Integer::sum);
	    });
	    
	    Map<String, String> dailyMap = new HashMap<>();
	    totalTimeByResourceId.forEach((resourceId, totalMinutes) -> {
	    	//String formattedTotalTime = convertMinutesToTime(totalMinutes);
	    	//dailyMap.put(resourceId, formattedTotalTime);
	    	dailyMap.put(resourceId, convertMinutesToTime(totalMinutes));
	    });	 
	    System.out.println("@@@@ The daily map = "+dailyMap);
	    return dailyMap;
	}
	
	private int convertTimeToMinutes(String time) {
	    String[] parts = time.split(":");
	    int hours = Integer.parseInt(parts[0]);
	    int minutes = Integer.parseInt(parts[1]);
	    return (hours * 60) + minutes;
	}

	private String convertMinutesToTime(int totalMinutes) {
	    int hours = totalMinutes / 60;
	    int minutes = totalMinutes % 60;
	    return String.format("%02d:%02d", hours, minutes);
	}
	
	private void sendEmail(List<WorkLog> dailyWorkLogs, String sheetName,String fileName) {
		
		try {
			ByteArrayResource excelAttachment = createExcel(dailyWorkLogs,sheetName);
			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
			helper.setTo("sandeepvallepu2@gmail.com");
			helper.setSubject("daily worklog report");
			helper.setText("This is daily report with attached excel");
			helper.addAttachment(fileName, excelAttachment);
			javaMailSender.send(mimeMessage);
		} catch (MessagingException | IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private ByteArrayResource createExcel(List<WorkLog> dailyWorkLogs, String sheetName) throws IOException {
        // Create a new Excel workbook and sheet
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(sheetName);

        // Create header row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Resource ID");
        headerRow.createCell(1).setCellValue("Task Description");
        headerRow.createCell(2).setCellValue("Project Name");
        headerRow.createCell(3).setCellValue("Module");
        headerRow.createCell(4).setCellValue("Work Date");
        headerRow.createCell(5).setCellValue("Start Time");
        headerRow.createCell(6).setCellValue("End Time");
        headerRow.createCell(7).setCellValue("Total Time");

        // Populate the data rows
        int rowNum = 1;
        for (WorkLog workLog : dailyWorkLogs) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(workLog.getResourceId());
            row.createCell(1).setCellValue(workLog.getDescription());
            row.createCell(2).setCellValue(workLog.getProject());
            row.createCell(3).setCellValue(workLog.getModule());
            row.createCell(4).setCellValue(workLog.getWorkDate().toString());
            row.createCell(5).setCellValue(workLog.getStartTime());
            row.createCell(6).setCellValue(workLog.getEndTime());
            row.createCell(7).setCellValue(workLog.getTotalTime());
        }

        // Write the data to a ByteArrayOutputStream
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        // Convert to ByteArrayResource for email attachment
        return new ByteArrayResource(out.toByteArray());
    }
	
	
}
