package com.finstack.projectlog.service;

import java.util.List;

import com.finstack.projectlog.model.ProjectDTO;

public interface ProjectService {

	ProjectDTO addProject(ProjectDTO projectDTO);
	ProjectDTO getProject(int projectId);
	List<ProjectDTO> getAllProjects();
	ProjectDTO updateProject(ProjectDTO projectDTO, int projectId);
	void deleteProject(int projectId);

}
