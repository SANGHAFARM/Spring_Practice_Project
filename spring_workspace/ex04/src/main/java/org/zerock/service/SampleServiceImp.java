package org.zerock.service;

import org.springframework.stereotype.Service;

@Service // 스프링 서비스 빈(Bean)으로 등록
public class SampleServiceImp implements SampleService {

	// 문자열 2개를 정수로 변환 후 합산 (AOP 로깅 및 예외 발생 테스트용)
	@Override
	public Integer doAdd(String str1, String str2) throws Exception {
		
		return Integer.parseInt(str1) + Integer.parseInt(str2);
	}

}