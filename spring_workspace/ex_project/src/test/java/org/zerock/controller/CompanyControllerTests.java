package org.zerock.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({ "file:src/main/webapp/WEB-INF/spring/root-context.xml",
		"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml" })
@Log4j
public class CompanyControllerTests {

	@Setter(onMethod_ = { @Autowired })
	private WebApplicationContext ctx;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
	}

	// 1. 다중 검색 조건 목록 테스트
	@Test
	public void testList() throws Exception {
		log.info(mockMvc.perform(MockMvcRequestBuilders.get("/company/list").param("pageNum", "1").param("amount", "10")
				.param("type", "NC").param("keyword", "솔루션")).andReturn().getModelAndView().getModelMap());
	}

	// 2. 상호 조회를 통한 수정/조회 폼 이동 (/company/get, /company/modify)
	@Test
	public void testGet() throws Exception {
		log.info(mockMvc.perform(MockMvcRequestBuilders.get("/company/modify").param("companyName", "테크솔루션")
				.param("pageNum", "1").param("amount", "10")).andReturn().getModelAndView().getModelMap());
	}

	// 3. 상호 기준 수정 처리 (POST /company/modify)
	@Test
	public void testModify() throws Exception {
		String resultPage = mockMvc.perform(MockMvcRequestBuilders.post("/company/modify").param("companyName", "테크솔루션") // 수정
																															// 기준
																															// 키
				.param("companyCode", "COMP001").param("ceoName", "김철수").param("businessType", "소프트웨어 솔루션")
				.param("pageNum", "1").param("amount", "10")).andReturn().getModelAndView().getViewName();

		log.info("수정 후 이동 페이지: " + resultPage);
	}

	// 4. 상호 기준 삭제 처리 (POST /company/remove)
	@Test
	public void testRemove() throws Exception {
		String resultPage = mockMvc.perform(MockMvcRequestBuilders.post("/company/remove").param("companyName", "한양무역") // 삭제
																														// 대상
																														// 상호
				.param("pageNum", "1").param("amount", "10")).andReturn().getModelAndView().getViewName();

		log.info("삭제 후 이동 페이지: " + resultPage);
	}
}