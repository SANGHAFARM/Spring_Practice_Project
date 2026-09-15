package org.zerock.service;

import org.springframework.stereotype.Component;

@Component
public class HelloPrinter {

	private final HelloService helloService;

    public HelloPrinter(HelloService helloService) {   	
    	
    	this.helloService = helloService;
    }
    
    public String print() {
    	return helloService.hello();
    }
    
}