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
public class SampleTxServiceTests {

	@Setter(onMethod_ = { @Autowired })
	private SampleTxService service; // 트랜잭션 서비스 객체 주입

	// 허용 글자 수를 초과하는 문자열을 삽입해 롤백(@Transactional) 검증
	@Test
	public void testLong() {

		// tbl_sample2의 컬럼 허용 크기(50바이트 등)를 초과하는 긴 문자열 준비
		String str = "Starry\r\n" + "Starry night\r\n" + "Paint your palette blue and grey\r\n"
				+ "Look out on a summer's day";

		log.info(str.getBytes().length); // 바이트 길이 출력

		// tbl_sample1은 성공하지만 tbl_sample2에서 실패하여 둘 다 롤백됨
		service.addData(str);
	}
}