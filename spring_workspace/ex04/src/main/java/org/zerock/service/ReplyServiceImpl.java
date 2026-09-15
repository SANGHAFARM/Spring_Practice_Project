package org.zerock.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.domain.Criteria;
import org.zerock.domain.ReplyPageDTO;
import org.zerock.domain.ReplyVO;
import org.zerock.mapper.BoardMapper;
import org.zerock.mapper.ReplyMapper;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Service // 댓글 서비스 빈 등록
@Log4j
public class ReplyServiceImpl implements ReplyService {

	@Setter(onMethod_ = @Autowired)
	private ReplyMapper mapper; // 댓글 매퍼 의존성 주입

	@Setter(onMethod_ = @Autowired)
	private BoardMapper boardMapper; // 게시글 매퍼 주입 (댓글 수 갱신용)

	// 댓글 등록과 게시글 댓글 수 1 증가를 동시에 처리 (트랜잭션 보장)
	@Transactional
	@Override
	public int register(ReplyVO vo) {

		log.info("register......" + vo);

		boardMapper.updateReplyCnt(vo.getBno(), 1); // 게시글의 replyCnt +1 반영

		return mapper.insert(vo); // 댓글 저장
	}

	// 댓글 단건 조회
	@Override
	public ReplyVO get(Long rno) {
		log.info("get......" + rno);
		return mapper.read(rno);
	}

	// 댓글 내용 수정
	@Override
	public int modify(ReplyVO vo) {
		log.info("modify......" + vo);
		return mapper.update(vo);
	}

	// 댓글 삭제와 게시글 댓글 수 1 감소를 동시에 처리 (트랜잭션 보장)
	@Transactional
	@Override
	public int remove(Long rno) {

		log.info("remove...." + rno);

		ReplyVO vo = mapper.read(rno); // 삭제 전 게시글 번호(bno) 확인용 조회

		boardMapper.updateReplyCnt(vo.getBno(), -1); // 게시글의 replyCnt -1 반영
		return mapper.delete(rno);                  // 댓글 삭제
	}

	// 특정 게시글의 댓글 목록 페이징 리스트 조회
	@Override
	public List<ReplyVO> getList(Criteria cri, Long bno) {
		log.info("get Reply List of a Board " + bno);
		return mapper.getListWithPaging(cri, bno);
	}

	// 총 댓글 수와 댓글 페이징 목록을 ReplyPageDTO 객체로 조합하여 반환
	@Override
	public ReplyPageDTO getListPage(Criteria cri, Long bno) {
		return new ReplyPageDTO(mapper.getCountByBno(bno), mapper.getListWithPaging(cri, bno));
	}

}