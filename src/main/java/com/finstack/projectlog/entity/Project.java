package com.finstack.projectlog.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "PROJECT_TABLE", schema = "WORKDETAILS")
public class Project {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "project_seq")
    @SequenceGenerator(name = "project_seq", sequenceName = "PROJECT_SEQ", allocationSize = 1)
	@Column(name = "PROJECT_ID")
	private int projectId;

	@Column(name = "PROJECT_NAME")
	private String projectName;

	@Column(name = "DESCRIPTION")
	private String description;
}
