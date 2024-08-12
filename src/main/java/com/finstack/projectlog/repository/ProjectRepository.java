package com.finstack.projectlog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finstack.projectlog.entity.Project;

public interface ProjectRepository extends JpaRepository<Project, Integer> {

	boolean existsByProjectName(String projectName);
}
