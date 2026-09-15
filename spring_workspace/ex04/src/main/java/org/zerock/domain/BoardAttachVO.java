package org.zerock.domain;

import lombok.Data;

@Data // Getter, Setter, toString 등을 자동 생성
public class BoardAttachVO {

	private String uuid;       // 파일 고유 식별자 (PK 역할)
	private String uploadPath; // 실제 파일이 저장된 폴더 경로
	private String fileName;   // 원본 파일 이름
	private boolean fileType;  // 파일 종류 (이미지면 true, 일반 파일이면 false)
	
	private Long bno;          // 해당 파일이 연결된 게시물 번호 (FK 역할)
}