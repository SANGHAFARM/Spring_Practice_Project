package org.zerock.service;

import org.springframework.stereotype.Component;

@Component
public class HelloRepository {

	public String getMessage() {
		return "Hello Spring";
	}
}
