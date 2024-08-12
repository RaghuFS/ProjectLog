package com.finstack.projectlog.jwtauth.model;

import java.util.Date;
import java.util.List;

import com.finstack.projectlog.entity.TransactionStatusEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserDTO {

	private String userId;
	// @Size(min = 2, groups = "BasicInfo.class" ,message = "First name should be of
	// minimum 2 characters")
	private String firstName;
	private String lastName;
	private Date effectiveDate;
	private Date expiryDate;
	private boolean status;
	private List<RoleDTO> roles;
//	private List<CustomerDTO> customers;
	private String emailAddress;
	private TransactionStatusEnum transactionStatus;
	private boolean deleteFlag;
}
