package org.zerock.domain;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data // Getter, Setter, toString 등을 자동 생성
public class BoardVO {

	private Long bno;                       // 게시글 고유 번호 (PK)
	private String title;                   // 게시글 제목
	private String content;                 // 게시글 본문 내용
	private String writer;                  // 작성자 아이디/이름
	private Date regdate;                   // 게시글 등록 일시
	private Date updateDate;                // 게시글 최종 수정 일시
	
	private int replyCnt;                   // 해당 게시글에 달린 총 댓글 개수
	
	private List<BoardAttachVO> attachList; // 게시글에 첨부된 파일 목록
}