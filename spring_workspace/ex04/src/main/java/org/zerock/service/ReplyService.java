package org.zerock.service;

import java.util.List;
import org.zerock.domain.Criteria;
import org.zerock.domain.ReplyPageDTO;
import org.zerock.domain.ReplyVO;

public interface ReplyService {

	// 댓글 신규 등록
	public int register(ReplyVO vo);

	// 댓글 단건 상세 조회
	public ReplyVO get(Long rno);

	// 댓글 본문 내용 수정
	public int modify(ReplyVO vo);

	// 댓글 번호로 삭제
	public int remove(Long rno);

	// 특정 게시글의 댓글 목록 단순 페이징 조회
	public List<ReplyVO> getList(Criteria cri, Long bno);
	
	// 댓글 목록과 전체 댓글 개수를 함께 묶어서 반환
	public ReplyPageDTO getListPage(Criteria cri, Long bno);

}