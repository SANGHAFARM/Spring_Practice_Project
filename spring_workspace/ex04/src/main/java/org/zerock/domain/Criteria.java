package org.zerock.domain;

import org.springframework.web.util.UriComponentsBuilder;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Criteria {

	private int pageNum;    // 조회할 페이지 번호
	private int amount;     // 한 페이지당 출력할 데이터 개수

	private String type;    // 검색 조건 (T: 제목, C: 내용, W: 작성자)
	private String keyword; // 검색어 키워드

	// 기본 생성자: 기본값으로 1페이지, 10개 설정
	public Criteria() {
		this(1, 10);
	}

	// 페이지 번호와 출력 개수를 직접 지정하는 생성자
	public Criteria(int pageNum, int amount) {
		this.pageNum = pageNum;
		this.amount = amount;
	}

	// 검색 조건을 한 글자씩 쪼개어 배열로 반환 (MyBatis 동적 SQL 처리용)
	public String[] getTypeArr() {
		return type == null ? new String[] {} : type.split("");
	}

	// 현재 페이지 및 검색 파라미터를 유지한 URL 쿼리스트링 문자열 생성
	public String getListLink() {
		UriComponentsBuilder builder = UriComponentsBuilder.fromPath("")
				.queryParam("pageNum", this.pageNum)
				.queryParam("amount", this.getAmount())
				.queryParam("type", this.getType())
				.queryParam("keyword", this.getKeyword());

		return builder.toUriString();
	}
}