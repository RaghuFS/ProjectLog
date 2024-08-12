package com.finstack.projectlog.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finstack.projectlog.model.ProjectDTO;
import com.finstack.projectlog.service.ProjectService;

@RestController
@RequestMapping("/projects")
public class ProjectController {

	private final ProjectService projectService;

	@Autowired
	public ProjectController(ProjectService projectService) {
		this.projectService = projectService;
	}

	@PostMapping
	public ResponseEntity<ProjectDTO> addProject(@Valid @RequestBody ProjectDTO projectDTO) {
		ProjectDTO savedProjectDTO = projectService.addProject(projectDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedProjectDTO);
	}

	@GetMapping
	public ResponseEntity<List<ProjectDTO>> getAllProjects() {

		List<ProjectDTO> allProjects = projectService.getAllProjects();
		return ResponseEntity.ok().body(allProjects);
	}

	@GetMapping("/{projectId}")
	public ResponseEntity<ProjectDTO> getprojectById(@PathVariable(name = "projectId") int projectId) {

		ProjectDTO project = projectService.getProject(projectId);
		return ResponseEntity.ok().body(project);
	}

	@PutMapping("/{projectId}")
	public ResponseEntity<ProjectDTO> updateProject(@Valid @RequestBody ProjectDTO projectDTO,
			@PathVariable(name = "projectId") int projectId) {

		ProjectDTO updateProject = projectService.updateProject(projectDTO, projectId);
		return ResponseEntity.ok().body(updateProject);
	}

	@DeleteMapping("/{projectId}")
	public ResponseEntity<Void> deleteProject(@PathVariable(name = "projectId") int projectId) {

		projectService.deleteProject(projectId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
