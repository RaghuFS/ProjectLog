package com.finstack.projectlog.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finstack.projectlog.entity.Project;
import com.finstack.projectlog.jwtauth.configuration.service.JWTUtil;
import com.finstack.projectlog.mapper.ProjectMapper;
import com.finstack.projectlog.model.ProjectDTO;
import com.finstack.projectlog.repository.ProjectRepository;

@Service
public class ProjectServiceImpl implements ProjectService {

	private final ProjectRepository projectRepository;
	private final ProjectMapper projectMapper;
	private final JWTUtil jwtUtil;
	 

	
	  @Autowired
	  public ProjectServiceImpl(ProjectRepository projectRepository,ProjectMapper projectMapper ,JWTUtil jwtUtil) {
		  this.projectRepository =  projectRepository; 
		  this.projectMapper = projectMapper; 
		  this.jwtUtil =  jwtUtil; 
	 }
	 

	  
	
	@Override
	@Transactional
	public ProjectDTO addProject(ProjectDTO projectDTO) {

		
		  String rolesFromRequest = jwtUtil.extractRolesFromRequest(); List<String>
		  rolesList = Arrays.asList(rolesFromRequest.split(","));
		  if(!rolesList.contains("WORKLOG_ADMIN")) {
			  throw new RuntimeException("unauthorized exception"); }
		 
		  if(projectRepository.existsByProjectName(projectDTO.getProjectName())) {
			  throw new RuntimeException("Project already exists");
		  }
		Project save = projectRepository.save(projectMapper.projectDTOToProject(projectDTO));
		return projectMapper.projectToProjectDTO(save);
	}

	@Override
	@Transactional(readOnly = true)
	public ProjectDTO getProject(int projectId) {

		Project project = projectRepository.findById(projectId)
				.orElseThrow(() -> new ResourceNotFoundException("Project not found with id " + projectId));
		return projectMapper.projectToProjectDTO(project);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ProjectDTO> getAllProjects() {

		List<Project> projects = projectRepository.findAll();
		return projectMapper.projectsToProjectDTOs(projects);
	}

	@Override
	@Transactional
	public ProjectDTO updateProject(ProjectDTO projectDTO, int projectId) {

		Project existProject = projectRepository.findById(projectId)
				.orElseThrow(() -> new ResourceNotFoundException("Project not found with id )" + projectId));
		
		String rolesFromRequest = jwtUtil.extractRolesFromRequest(); List<String>
		  rolesList = Arrays.asList(rolesFromRequest.split(","));
		if(!rolesList.contains("WORKLOG_ADMIN")) {
			  throw new RuntimeException("unauthorized exception"); 
		}

		existProject.setDescription(projectDTO.getDescription());
		existProject.setProjectName(projectDTO.getProjectName());
		return projectMapper.projectToProjectDTO(projectRepository.save(existProject));
	}

	@Override
	@Transactional
	public void deleteProject(int projectId) {

		if (!projectRepository.existsById(projectId))
			throw new ResourceNotFoundException("Project not found with id " + projectId);
		
		String rolesFromRequest = jwtUtil.extractRolesFromRequest(); List<String>
		  rolesList = Arrays.asList(rolesFromRequest.split(","));
		  if(!rolesList.contains("WORKLOG_ADMIN")) {
			  throw new RuntimeException("unauthorized exception"); 
		  }

		projectRepository.deleteById(projectId);
	}

}
