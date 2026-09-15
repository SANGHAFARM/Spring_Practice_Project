package org.zerock.service;

import java.util.List;
import org.zerock.domain.BoardAttachVO;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;

public interface BoardService {

	// 게시글과 첨부파일을 함께 등록
	public void register(BoardVO board);
	
	// 게시글 1건 상세 조회
	public BoardVO get(Long bno);
	
	// 게시글 내용 수정 및 첨부파일 갱신
	public boolean modify(BoardVO board);
	
	// 게시글 및 관련 첨부파일 데이터 일괄 삭제
	public boolean remove(Long bno);
	
	// 페이징/검색 조건에 맞는 게시글 목록 조회
	public List<BoardVO> getList(Criteria cri);

	// 전체 게시글 개수 조회
	public int getTotal(Criteria cri);
	
	// 특정 게시글 번호에 연결된 첨부파일 목록 조회
	public List<BoardAttachVO> getAttachList(Long bno);
}