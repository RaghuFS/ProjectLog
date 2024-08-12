package com.finstack.projectlog.entity;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.springframework.validation.annotation.Validated;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "RESOURCE_TABLE", schema = "ADMIN")
@Validated
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class Resource extends Base {

	@Id
	private UUID uuid;
	@Column(name = "FIRST_NAME")
	private String firstName;
	@Column(name = "LAST_NAME")
	private String lastName;
	@Column(name = "BIRTH_DATE")
	private Date birthDate;
	@Column(name = "JOINING_DATE")
	private Date joiningDate;
	@Column(name = "ACTIVE_STATUS")
	private boolean status;
	@ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@JoinTable(name = "RESOURCE_ROLES_TABLE", schema = "ADMIN", joinColumns = {
			@JoinColumn(name = "RESOURCE_ID") }, inverseJoinColumns = { @JoinColumn(name = "ROLE_ID") })
	private List<Role> roles;
	@Column(name = "EMAIL_ADDRESS", unique = true)
	private String emailAddress;
	@Column(name = "REPORTING_TO")
	private String reportingTo;

}
