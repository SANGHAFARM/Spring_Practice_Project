package org.zerock.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.zerock.domain.Ticket;

import com.google.gson.Gson;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class) // JUnit 실행 시 스프링 컨텍스트 확장 기능 사용
@WebAppConfiguration // 가상 WebApplicationContext 환경 생성
@ContextConfiguration({ "file:src/main/webapp/WEB-INF/spring/root-context.xml",
		"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml" })
@Log4j
public class SampleControllerTests {

	@Setter(onMethod_ = { @Autowired })
	private WebApplicationContext ctx; // 스프링 웹 환경 객체 주입

	private MockMvc mockMvc; // 브라우저 없이 컨트롤러 HTTP 요청을 가상 실행하는 객체

	// 각 테스트 실행 전 MockMvc 인스턴스 초기화 설정
	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
	}

	// 객체를 JSON 문자열로 변환하여 POST 전송 후 HTTP 200 응답 여부 검증
	@Test
	public void testConvert() throws Exception {

		Ticket ticket = new Ticket();
		ticket.setTno(123);
		ticket.setOwner("Admin");
		ticket.setGrade("AAA");

		// Gson 라이브러리를 사용해 Ticket 객체를 JSON 문자열로 직렬화
		String jsonStr = new Gson().toJson(ticket);

		log.info(jsonStr);

		// /sample/ticket URL로 JSON 데이터를 담아 가상 POST 요청 전송 및 200 OK 상태 검증
		mockMvc.perform(post("/sample/ticket").contentType(MediaType.APPLICATION_JSON).content(jsonStr))
				.andExpect(status().is(200));
	}

}