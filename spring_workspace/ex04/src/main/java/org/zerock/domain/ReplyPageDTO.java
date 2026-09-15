package org.zerock.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor // 모든 필드를 인자로 받는 생성자 생성
@Getter
public class ReplyPageDTO {

	private int replyCnt;       // 해당 글에 달린 전체 댓글 총 개수
	private List<ReplyVO> list; // 현재 페이지에 표시할 댓글 목록
}