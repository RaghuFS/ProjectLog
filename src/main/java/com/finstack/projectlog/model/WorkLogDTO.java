package com.finstack.projectlog.model;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import org.springframework.data.web.ProjectedPayload;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WorkLogDTO {

	private int workLogId;

	@NotBlank(message = "Project should not be blank")
	private String project;

	@NotBlank(message = "module should not be blank")
	private String module;

	@NotBlank(message = "description should not be blank")
	private String description;
	
	@NotNull(message = "workDate should not be null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd" ,timezone = "Asia/Dubai")
	@PastOrPresent(message = "Work Date should not be future date")
	private Date workDate;
	
	@NotNull(message = "start time should not be null")
	private String startTime;
	
	@NotNull(message = "end time should not be null")
	private String endTime;
	
	//@NotNull(message = "Total time should not be null")
	private String totalTime;
	
	//private String assignee;
	//private UUID assigneeId;
	
	private String resourceId;
	private List<ProjectTempDTO> projects;
	
	  public void setProjects(List<ProjectTempDTO> projectTempDTOs){
		  System.out.println("@@@@@ project: "+project);
		  project=(projectTempDTOs == null || projectTempDTOs.isEmpty()) ? project : projectTempDTOs.get(0).getItemName()==null?project:projectTempDTOs.get(0).getItemName();
			  System.out.println("@@@@@ project: "+project);

	    }
}
