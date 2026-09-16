package org.zerock.service;

import org.springframework.stereotype.Service;

@Service
public class HelloService {
	
	private final HelloRepository helloRepository;

	public HelloService(HelloRepository helloRepository)
	{
		this.helloRepository = helloRepository;
	}
	
    public String hello()
    {
        return helloRepository.getMessage();
    }

}