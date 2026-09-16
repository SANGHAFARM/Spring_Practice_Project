package org.zerock.service;

import org.springframework.stereotype.Repository;

@Repository
public class HelloRepository {

	public String getMessage() {
		return "Hello Spring";
	}
}
