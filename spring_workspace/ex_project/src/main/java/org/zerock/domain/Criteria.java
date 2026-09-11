package org.zerock.domain;

import org.springframework.web.util.UriComponentsBuilder;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Criteria {

	// 1. 페이징 관련 필드 (필요시 사용, 기본 1페이지 10개)
	private int pageNum;
	private int amount;

	// 2. 검색 관련 필드
	private String type; // 검색 조건 (N: 상호, C: 대표자명, NC: 상호+대표자명)
	private String keyword; // 검색 키워드

	public Criteria() {
		this(1, 10);
	}

	public Criteria(int pageNum, int amount) {
		this.pageNum = pageNum;
		this.amount = amount;
	}

	// MyBatis 동적 태그에서 #{typeArr} 또는 collection="typeArr"로 호출되는 핵심 메서드
	public String[] getTypeArr() {

		return type == null ? new String[] {} : type.split("");
	}

	public String getListLink() {

		UriComponentsBuilder builder = UriComponentsBuilder.fromPath("").queryParam("pageNum", this.pageNum)
				.queryParam("amount", this.getAmount()).queryParam("type", this.getType())
				.queryParam("keyword", this.getKeyword());

		return builder.toUriString();

	}

}