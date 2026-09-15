package org.zerock.domain;

import java.util.Date;
import lombok.Data;

@Data // Getter, Setter, toString 등을 자동 생성
public class ReplyVO {

  private Long rno;        // 댓글 고유 번호 (PK)
  private Long bno;        // 댓글이 달린 게시물 번호 (FK)

  private String reply;    // 댓글 내용 본문
  private String replyer;  // 댓글 작성자
  private Date replyDate;  // 댓글 등록 일시
  private Date updateDate; // 댓글 최종 수정 일시
}