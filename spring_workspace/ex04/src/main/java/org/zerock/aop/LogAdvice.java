package org.zerock.aop;

import java.util.Arrays;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import lombok.extern.log4j.Log4j;

@Aspect    // AOP 기능을 수행하는 클래스 선언
@Log4j     // 로그 출력을 위한 log 객체 자동 생성
@Component // 스프링 빈(Bean)으로 자동 등록
public class LogAdvice {

	// SampleService의 모든 메서드 실행 전에 호출
	@Before("execution(* org.zerock.service.SampleService*.*(..))")
	public void logBefore() {
		log.info("==========================");
	}

	// doAdd 메서드 실행 전 전달된 파라미터 2개를 확인
	@Before("execution(* org.zerock.service.SampleService*.doAdd(String, String)) && args(str1, str2)")
	public void logBeforeWithParam(String str1, String str2) {
		log.info("str1: " + str1);
		log.info("str2: " + str2);
	}

	// 메서드 실행 중 예외(에러)가 터졌을 때 실행
	@AfterThrowing(pointcut = "execution(* org.zerock.service.SampleService*.*(..))", throwing = "exception")
	public void logException(Exception exception) {
		log.info("Exception....!!!!");
		log.info("exception: " + exception);
	}

	// 메서드 실행 전/후를 감싸서 실행 시간을 밀리초 단위로 측정
	@Around("execution(* org.zerock.service.SampleService*.*(..))")
	public Object logTime(ProceedingJoinPoint pjp) {
		long start = System.currentTimeMillis(); // 시작 시간 측정

		log.info("Target: " + pjp.getTarget()); // 실행 대상 클래스 확인
		log.info("Param: " + Arrays.toString(pjp.getArgs())); // 전달 파라미터 확인

		Object result = null;
		try {
			result = pjp.proceed(); // 실제 핵심 비즈니스 메서드 실행
		} catch (Throwable e) {
			e.printStackTrace();
		}

		long end = System.currentTimeMillis(); // 종료 시간 측정
		log.info("TIME: " + (end - start));     // 소요 시간 출력

		return result; // 원래 메서드의 실행 결과 반환
	}
}