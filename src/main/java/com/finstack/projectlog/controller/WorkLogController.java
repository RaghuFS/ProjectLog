package com.finstack.projectlog.controller;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.finstack.projectlog.model.WorkLogDTO;
import com.finstack.projectlog.service.WorkLogService;

@RestController
@RequestMapping("/worklogs")
public class WorkLogController {

	public final WorkLogService workLogService;

	@Autowired
	public WorkLogController(WorkLogService workLogService) {
		this.workLogService = workLogService;
	}

	@PostMapping
	public ResponseEntity<WorkLogDTO> addWorkLog(@Valid @RequestBody WorkLogDTO workLogDTO) {
		WorkLogDTO savedDTO = workLogService.addWorkLog(workLogDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedDTO);
	}

	@GetMapping
	public ResponseEntity<List<WorkLogDTO>> getAllWorkLogs() {
		List<WorkLogDTO> allWorkLogs = workLogService.getAllWorkLogs();
		return ResponseEntity.status(HttpStatus.OK).body(allWorkLogs);
	}

	@GetMapping("/{workLogId}")
	public ResponseEntity<WorkLogDTO> getWorkLogById(@PathVariable(name = "workLogId") int workLogId) {
		WorkLogDTO workLogDTO = workLogService.getWorkLog(workLogId);
		return ResponseEntity.status(HttpStatus.OK).body(workLogDTO);
	}

	@PutMapping("/{workLogId}")
	public ResponseEntity<WorkLogDTO> updateWorkLog(@Valid @PathVariable(name = "workLogId") int workLogId,
			@ Valid @RequestBody WorkLogDTO workLogDTO) {
		WorkLogDTO updateWorkLog = workLogService.updateWorkLog(workLogId, workLogDTO);
		return ResponseEntity.ok(updateWorkLog);
	}

	@DeleteMapping("/{workLogId}")
	public ResponseEntity<Void> deleteWorkLog(@PathVariable(name = "workLogId") int workLogId) {
		workLogService.deleteWorkLog(workLogId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
	@GetMapping("/all")
	public ResponseEntity<Page<WorkLogDTO>> getAllWorkLogsByPaging(@RequestParam(name = "workDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date workDate,
			@RequestParam(name = "resourceId", required = false) String resourceId,
			Pageable pageable) {
		Page<WorkLogDTO> allWorkLogs = workLogService.getAllWorkLogsByPaging(workDate,resourceId,pageable);
		return ResponseEntity.status(HttpStatus.OK).body(allWorkLogs);
	}
}
