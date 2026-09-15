package org.zerock.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;

public interface BoardMapper {

    // 전체 게시글 목록 조회
    public List<BoardVO> getList();

    // 페이징 및 검색 조건(Criteria)에 맞춘 게시글 목록 조회
    public List<BoardVO> getListWithPaging(Criteria cri);

    // 단순 게시글 등록 처리
    public void insert(BoardVO board);

    // 게시글 등록 후 생성된 시퀀스 번호(bno)를 객체에 즉시 저장
    public Integer insertSelectKey(BoardVO board);

    // 게시글 번호(bno)로 단건 상세 정보 조회
    public BoardVO read(Long bno);

    // 게시글 번호(bno)로 해당 게시글 삭제 (삭제된 행 수 반환)
    public int delete(Long bno);

    // 게시글 제목, 내용, 수정일 수정 처리
    public int update(BoardVO board);
    
    // 검색 조건이 반영된 전체 게시글 총 개수 조회
    public int getTotalCount(Criteria cri);
    
    // 댓글 등록/삭제에 맞춰 해당 게시글의 댓글 수(replyCnt) 증감 처리
    public void updateReplyCnt(@Param("bno") Long bno, @Param("amount") int amount);

}