package com.userservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.BAD_REQUEST)
public class IncorrectPasswordException extends RuntimeException {
	public IncorrectPasswordException(String msg) {
		super(msg);
	}
}
