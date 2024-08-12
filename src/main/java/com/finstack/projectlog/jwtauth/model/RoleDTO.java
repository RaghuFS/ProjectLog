package com.finstack.projectlog.jwtauth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RoleDTO {

	@JsonIgnore
	private UUID uuid;

	private String name;
}
