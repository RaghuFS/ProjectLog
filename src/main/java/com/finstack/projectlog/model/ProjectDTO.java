package com.finstack.projectlog.model;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDTO {

	private int projectId;

	@NotBlank(message = "Project name should not be blank")
	private String projectName;

	@NotBlank(message = "Projecgt description should not be empty")
	private String description;
}
