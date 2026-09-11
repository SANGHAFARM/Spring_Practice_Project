package org.zerock.service;

import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.zerock.domain.CompanyVO;
import org.zerock.domain.Criteria;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
@Log4j
public class CompanyServiceTests {

	@Setter(onMethod_ = @Autowired)
	private CompanyService service;

	@Test
	public void testExist() {
		log.info(service);
		assertNotNull(service);
	}

	// 1. 상호 기준 단건 조회
	@Test
	public void testGet() {
		log.info("서비스 단건 조회: " + service.get("테크솔루션"));
	}

	// 2. 검색 목록 조회
	@Test
	public void testGetList() {
		Criteria cri = new Criteria();
		cri.setType("C"); // 대표자명 검색
		cri.setKeyword("홍길동");

		service.getList(cri).forEach(comp -> log.info(comp));
	}

	// 3. 상호 기준 정보 수정
	@Test
	public void testModify() {
		CompanyVO comp = service.get("테크솔루션");
		if (comp == null)
			return;

		comp.setCeoName("홍길동(수정)");
		comp.setBusinessType("IT 서비스 컨설팅");

		log.info("수정 성공 여부: " + service.modify(comp));
	}

	// 4. 상호 기준 삭제
	@Test
	public void testRemove() {
		// 실제 삭제할 거래처 상호명 입력
		log.info("삭제 성공 여부: " + service.remove("글로벌물류"));
	}
}