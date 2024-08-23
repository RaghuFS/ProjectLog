package com.finstack.projectlog.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finstack.projectlog.entity.Resource;
import com.finstack.projectlog.entity.WorkLog;
import com.finstack.projectlog.exception.UnauthorizedException;
import com.finstack.projectlog.jwtauth.configuration.service.JWTUtil;
import com.finstack.projectlog.mapper.WorkLogMapper;
import com.finstack.projectlog.model.WorkLogDTO;
import com.finstack.projectlog.repository.ProjectRepository;
import com.finstack.projectlog.repository.WorkLogRepository;


@Service
public class WorkLogServiceImpl implements WorkLogService{
	
//	private final WorkLogRepository workLogRepository;
//	private final WorkLogMapper workLogMapper;
//	private final JWTUtil jwtUtil;
	@Autowired
	private  WorkLogRepository workLogRepository;
	@Autowired
	private  WorkLogMapper workLogMapper;
	@Autowired
	private  JWTUtil jwtUtil;
	
	@Autowired
	private ProjectRepository projectRepository;
	
//	@Autowired
//	public WorkLogServiceImpl(WorkLogRepository workLogRepository,WorkLogMapper workLogMapper, JWTUtil jwtUtil) {
//		this.workLogRepository = workLogRepository;
//		this.workLogMapper = workLogMapper;
//		this.jwtUtil = jwtUtil;
//	}

	@Override
	@Transactional
	public WorkLogDTO addWorkLog(WorkLogDTO workLogDTO) {
		String resourceId = jwtUtil.extractUsernameFromRequest();
		
		if(!resourceId.equals(workLogDTO.getResourceId())) {
			//throw new RuntimeException("you are not authorized to make this request");
			throw new UnauthorizedException("you are not authorized to make this request");
		}
		if(!isStartTimeBeforeEndTime(workLogDTO.getStartTime(),workLogDTO.getEndTime())) {
			//throw new RuntimeException("Start time should not greater than End time");
			throw new UnauthorizedException("Start time should not greater than End time");
		}
		if(!projectRepository.existsByProjectName(workLogDTO.getProject())) {
			throw new UnauthorizedException("Project does not exist");
		}
		 if (isWorkDateInFuture(workLogDTO.getWorkDate())) {
	            throw new UnauthorizedException("Work log date cannot be in the future");
	        }
		
		WorkLog workLog = workLogMapper.workLogDTOToWorkLog(workLogDTO);
		workLog.setTotalTime(totalTimeCalculation(workLogDTO.getStartTime(), workLogDTO.getEndTime()));
		//workLog.setResourceId(resourceId);
		 WorkLog save = workLogRepository.save(workLog);
		return workLogMapper.workLogToWorkLogDTO(save);
	}

	private boolean isWorkDateInFuture(Date workDate) {
		  LocalDate today = LocalDate.now();
	        LocalDate workLocalDate = workDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	        return workLocalDate.isAfter(today);
}

	@Override
	@Transactional(readOnly =true)
	public WorkLogDTO getWorkLog(int workLogId) {
		WorkLog workLog = workLogRepository.findById(workLogId).orElseThrow(() -> new ResourceNotFoundException("WorkLog not found with ID - "+ workLogId));
		return workLogMapper.workLogToWorkLogDTO(workLog);
	}
	@Override
	@Transactional(readOnly = true)
	public List<WorkLogDTO> getAllWorkLogs() {
		String resourceId = jwtUtil.extractRolesFromRequest();
		List<WorkLog> all = workLogRepository.findAll();
		return workLogMapper.workLogsToWorkLogDTOs(all);
	}

	@Override
	@Transactional
	public WorkLogDTO updateWorkLog(int workLogId,WorkLogDTO workLogDTO) {
		WorkLog existWorkLog = workLogRepository.findById(workLogId).orElseThrow(() -> new ResourceNotFoundException("WorkLog not found with ID - "+ workLogId));
		
		String resourceId = jwtUtil.extractUsernameFromRequest();
		if(!resourceId.equals(existWorkLog.getResourceId()) || !resourceId.equals(workLogDTO.getResourceId())) {
			//throw new RuntimeException("UnAuthorized Exception");
			throw new UnauthorizedException("Un Authorized Exception");
		}
		if(!isStartTimeBeforeEndTime(workLogDTO.getStartTime(),workLogDTO.getEndTime())) {
			//throw new RuntimeException("Start time should not greater than End time");
			throw new UnauthorizedException("Start time should not greater than End time");
		}
		if(!isWorkDateToday(workLogDTO.getWorkDate())) {
			//throw new RuntimeException("work log date is not todays date");	
			throw new UnauthorizedException("Only today's work logs can be updated");
		}
		
		
		existWorkLog.setProject(workLogDTO.getProject());
		existWorkLog.setModule(workLogDTO.getModule());
		existWorkLog.setDescription(workLogDTO.getDescription());
		existWorkLog.setWorkDate(workLogDTO.getWorkDate());
		existWorkLog.setStartTime(workLogDTO.getStartTime());
		existWorkLog.setEndTime(workLogDTO.getEndTime());
		existWorkLog.setTotalTime(totalTimeCalculation(workLogDTO.getStartTime(), workLogDTO.getEndTime()));
		//existWorkLog.setResourceId(resourceId);
		existWorkLog.setResourceId(workLogDTO.getResourceId());
		return workLogMapper.workLogToWorkLogDTO( workLogRepository.save(existWorkLog));
	}

	/*
	 * public boolean isWorkDateToday(Date workDate) { LocalDate today =
	 * LocalDate.now(); LocalDate workLocalDate =
	 * workDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(); return
	 * workLocalDate.isEqual(today); }
	 */

	@Override
	@Transactional
	public void deleteWorkLog(int workLogId) {
		
//		if (!workLogRepository.existsById(workLogId)) 
//			throw  new ResourceNotFoundException("Resource Not Found With ID - "+ workLogId + " to delete");
		WorkLog existedWorkLog =  workLogRepository.findById(workLogId).orElseThrow(()-> new ResourceNotFoundException("Resource Not Found With ID - "+ workLogId + " to delete"));
		String resourceId = jwtUtil.extractUsernameFromRequest();
		if(!resourceId.equals(existedWorkLog.getResourceId())) {
			//throw new RuntimeException("you are not authorized to make this request");
			throw new UnauthorizedException("UnAuthorized Exception");
		}
		workLogRepository.deleteById(workLogId);
	}

	@Override
	public Page<WorkLogDTO> getAllWorkLogsByPaging(Date workDate, String resourceId,Pageable pageable) {
		if(resourceId!=null && resourceId.isBlank()) {
			resourceId = null;
		}
		//resourceId = (resourceId!=null && resourceId.isBlank())?null:resourceId;
		
		String usernameFromRequest = jwtUtil.extractUsernameFromRequest();
		String rolesFromRequest = jwtUtil.extractRolesFromRequest();
		List<String> rolesList = Arrays.asList(rolesFromRequest.split(","));
		
		if(rolesList.contains("WORKLOG_ADMIN")) {
			Page<WorkLog> allWorkLogs = workLogRepository.findAllWorkLogs(resourceId,workDate,
					pageable);
			if(allWorkLogs == null || pageable.getPageNumber() >= allWorkLogs.getTotalPages()) {
				return new PageImpl<>(Collections.emptyList(), pageable, allWorkLogs.getTotalElements());
			}
			
			return workLogMapper.workLogsToWorkLogDTOsPaging(allWorkLogs);
		}else {
			if(resourceId!=null && !resourceId.equals(usernameFromRequest) ) {
				//throw new RuntimeException("UnAuthorized Exception");
				throw new UnauthorizedException("UnAuthorized Exception");
			}
			resourceId = usernameFromRequest;
			Page<WorkLog> allWorkLogs = workLogRepository.findAllWorkLogs(resourceId,workDate,
					pageable);
			if(allWorkLogs == null || pageable.getPageNumber() >= allWorkLogs.getTotalPages()) {
				return new PageImpl<>(Collections.emptyList(), pageable, allWorkLogs.getTotalElements());
			}
			
			return workLogMapper.workLogsToWorkLogDTOsPaging(allWorkLogs);
		}
		
//		Page<WorkLog> allWorkLogs = workLogRepository.findAllWorkLogs(resourceId,//workLogDate,
//				pageable);
//		if(allWorkLogs == null || pageable.getPageNumber() >= allWorkLogs.getTotalPages()) {
//			return new PageImpl<>(Collections.emptyList(), pageable, allWorkLogs.getTotalElements());
//		}
//		
//		return workLogMapper.workLogsToWorkLogDTOsPaging(allWorkLogs);
	}

	public String totalTimeCalculation(String startTime, String endTime) {
		
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");
		LocalTime start = LocalTime.parse(startTime, dateTimeFormatter);
		LocalTime end = LocalTime.parse(endTime, dateTimeFormatter);
		Duration duration = Duration.between(start, end);
		long hours = duration.toHours();
		long minutes = duration.toMinutes() % 60;
		
		return String.format("%02d:%02d", hours, minutes);
	}
	
	public boolean isStartTimeBeforeEndTime(String startTime, String endTime) {
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");
		LocalTime start = LocalTime.parse(startTime, dateTimeFormatter);
		LocalTime end = LocalTime.parse(endTime, dateTimeFormatter);
		
		return start.isBefore(end);
	}
	public boolean isWorkDateToday(Date workDate) {
	    LocalDate today = LocalDate.now();

	    LocalDate workLocalDate = workDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	    
	    System.out.println("%%% workDate -"+workDate+"   today = "+today+ "  worklocaldate = "+workLocalDate);

	    return workLocalDate.equals(today);
	}

	@Override
	public 	List<Resource> resourcesWithoutWorkLog(){
		List<Resource> resourcesWithoutWorkLog = workLogRepository.findResourcesWithoutWorkLogToday();
		return resourcesWithoutWorkLog;
	}
}
