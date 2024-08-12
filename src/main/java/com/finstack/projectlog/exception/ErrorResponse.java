package com.finstack.projectlog.exception;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ErrorResponse {

	private LocalDateTime timestamp;
	private String errorMessage;
	private String description;
	private int errorStatus;
}
