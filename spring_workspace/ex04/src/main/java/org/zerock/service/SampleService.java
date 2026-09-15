package org.zerock.service;

public interface SampleService {
	
	// 두 문자열을 숫자로 변환해 더하며 AOP 파라미터/예외 로깅 대상이 되는 메서드
	public Integer doAdd(String str1, String str2) throws Exception;

}