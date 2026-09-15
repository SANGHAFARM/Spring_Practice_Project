package org.zerock.domain;

import lombok.Data;

@Data // Getter, Setter, toString 등을 자동 생성
public class Ticket {
	private int tno;      // 티켓 고유 번호
	private String owner; // 티켓 소유자 이름
	private String grade; // 티켓 등급
}