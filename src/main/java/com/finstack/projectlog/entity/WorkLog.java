package com.finstack.projectlog.entity;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "WORKLOG_TABLE", schema = "WORKDETAILS")
public class WorkLog {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "worklog_seq")
	@SequenceGenerator(name = "worklog_seq", sequenceName = "WORKLOG_SEQ", allocationSize = 1)
	@Column(name = "WORKLOG_ID")
	private int workLogId;

	@Column(name = "TASK_DESCRIPTION")
	private String description;

	@Column(name = "PROJECT_NAME")
	private String project;

	@Column(name = "MODULE")
	private String module;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "WORK_DATE")
	private Date workDate;
	
	@Column(name = "START_TIME")
	private String startTime;
	
	@Column(name = "END_TIME")
	private String endTime;
	
	@Column(name = "TOTAL_TIME")
	private String totalTime;
	
	//@Column(name = "ASSIGNEE")
	//private String assignee;
	//@Column(name = "ASSIGNEE_ID")
	//private String  assigneeId;

	@Column(name = "RESOURCE_ID")
	private String resourceId;
}
