package org.zerock.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.zerock.domain.Criteria;
import org.zerock.domain.ReplyVO;

public interface ReplyMapper {

	// 신규 댓글 DB 등록
	public int insert(ReplyVO vo);
	
	// 댓글 번호(rno)로 특정 댓글 1건 조회
	public ReplyVO read(Long rno);
	
	// 댓글 번호(rno)로 댓글 삭제
	public int delete(Long rno);
	
	// 댓글 내용 및 수정일시 변경 처리
	public int update(ReplyVO reply);
	
	// 특정 게시글(bno)의 댓글 목록을 페이징 조건에 맞춰 조회
	public List<ReplyVO> getListWithPaging(@Param("cri") Criteria cri, @Param("bno") Long bno);
	
	// 특정 게시글(bno)에 등록된 전체 댓글 수 조회
	public int getCountByBno(Long bno);
}