package org.zerock.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@Log4j
@ContextConfiguration({ "file:src/main/webapp/WEB-INF/spring/root-context.xml" })
public class SampleServiceTests {

	@Setter(onMethod_ = @Autowired)
	private SampleService service; // SampleService 빈 주입

	// AOP 프록시 객체 정상 생성 여부 확인 (클래스 이름에 $$EnhancerBySpringCGLIB 출력 확인)
	@Test
	public void testClass() {

		log.info(service);
		log.info(service.getClass().getName()); // 실제 클래스 타입 출력

	}

	// 숫자 문자열 덧셈 정상 작동 및 AOP @Around 실행 시간 로그 확인
	@Test
	public void test() throws Exception {

		log.info(service.doAdd("123", "456")); // 123 + 456 = 579 반환
	}

	// 숫자가 아닌 문자열 전달 시 AOP @AfterThrowing 예외 감지 동작 검증
	@Test
	public void testAddError() throws Exception {

		log.info(service.doAdd("123", "ABC")); // NumberFormatException 예외 유도

	}

}