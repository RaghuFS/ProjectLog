package com.finstack.projectlog.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.finstack.projectlog.entity.Project;
import com.finstack.projectlog.model.ProjectDTO;

@Mapper
public interface ProjectMapper {

	ProjectDTO projectToProjectDTO(Project project);

	Project projectDTOToProject(ProjectDTO projectDTO);

	List<ProjectDTO> projectsToProjectDTOs(List<Project> projects);

	List<Project> projectDTOsToProjects(List<ProjectDTO> projectDTOs);

	/*
	 * @Mapping(target = "projectId",ignore = true) ProjectDTO
	 * projectToProjectDtoWithoutId(Project project);
	 * 
	 * @Mapping(target = "projectId", ignore = true) Project
	 * projectDTOToProjectWithoutId(ProjectDTO projectDTO);
	 */
}
