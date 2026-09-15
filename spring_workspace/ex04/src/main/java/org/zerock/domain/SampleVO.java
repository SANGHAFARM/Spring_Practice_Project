package org.zerock.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor // 모든 필드를 포함하는 생성자 생성
@NoArgsConstructor  // 기본 생성자 생성
public class SampleVO {
	
	private Integer mno;      // 회원 번호 식별값
	private String firstName; // 이름
	private String lastName;  // 성
}