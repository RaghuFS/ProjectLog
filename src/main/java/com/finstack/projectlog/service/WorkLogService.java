package com.finstack.projectlog.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.finstack.projectlog.model.WorkLogDTO;

public interface WorkLogService {
	
	WorkLogDTO addWorkLog(WorkLogDTO workLogDTO);
	WorkLogDTO getWorkLog(int workLogId);
	List<WorkLogDTO> getAllWorkLogs();
	WorkLogDTO updateWorkLog(int workLogId, WorkLogDTO workLogDTO);
	void deleteWorkLog(int workLogId);
	
	Page<WorkLogDTO> getAllWorkLogsByPaging(Date workDate, String resourceId, Pageable pageable);

}
