package org.zerock.persistence;

import static org.junit.Assert.fail;

import java.sql.Connection;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class) // 스프링 테스트 러너 실행
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml") // root-context.xml 설정 로드
@Log4j
public class DataSourceTests {

	@Setter(onMethod_ = { @Autowired })
	private DataSource dataSource; // HikariCP 커넥션 풀 객체 주입

	@Setter(onMethod_ = { @Autowired })
	private SqlSessionFactory sqlSessionFactory; // MyBatis 세션 팩토리 주입

	// MyBatis SqlSession 및 DB 커넥션 오픈 테스트
	@Test
	public void testMyBatis() {

		// try-with-resources: 세션과 커넥션을 사용 후 자동 반납(close)
		try (SqlSession session = sqlSessionFactory.openSession(); Connection con = session.getConnection();) {

			log.info(session); // 마이바티스 세션 객체 로그 출력
			log.info(con);     // 실제 DB 커넥션 객체 로그 출력

		} catch (Exception e) {
			fail(e.getMessage()); // 연결 실패 시 테스트 실패 처리
		}

	}
}