package org.zerock.mapper;

import java.util.List;
import org.zerock.domain.BoardAttachVO;

public interface BoardAttachMapper {

	// 첨부파일 1건의 메타데이터를 DB에 등록
	public void insert(BoardAttachVO vo);
	
	// UUID 고유값을 기준으로 특정 첨부파일 데이터 삭제
	public void delete(String uuid);
	
	// 특정 게시물 번호(bno)에 첨부된 모든 파일 목록 조회
	public List<BoardAttachVO> findByBno(Long bno);
	
	// 특정 게시물에 속한 모든 첨부파일 데이터를 일괄 삭제
	public void deleteAll(Long bno);
	
	// 어제 날짜 등 과거에 업로드된 파일 목록 조회 (어긋난 파일 자동 정리용)
	public List<BoardAttachVO> getOldFiles();
}