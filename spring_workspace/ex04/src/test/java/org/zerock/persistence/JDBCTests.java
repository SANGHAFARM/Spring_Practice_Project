package org.zerock.persistence;

import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.DriverManager;

import org.junit.Test;

import lombok.extern.log4j.Log4j;

@Log4j
public class JDBCTests {
	// 클래스 로딩 시 오라클 JDBC 드라이버를 메모리에 등록
	static {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 순수 JDBC 방식으로 오라클 DB 접속 테스트
	@Test
	public void testConnection() {

		// DB 주소(localhost:1521:XE), 계정(system), 비밀번호(1234)로 연결 시도
		try (Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system", "1234")) {
			log.info(con); // 접속 성공 시 커넥션 정보 출력
		} catch (Exception e) {
			fail(e.getMessage()); // 접속 실패 시 테스트 실패 처리
		}
	}
}