package org.zerock.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.domain.BoardAttachVO;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;
import org.zerock.mapper.BoardAttachMapper;
import org.zerock.mapper.BoardMapper;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Log4j
@Service // 비즈니스 로직을 처리하는 서비스 빈(Bean)으로 등록
@AllArgsConstructor
public class BoardServiceImpl implements BoardService {

	@Setter(onMethod_ = @Autowired)
	private BoardMapper mapper; // 게시판 매퍼 의존성 주입

	@Setter(onMethod_ = @Autowired)
	private BoardAttachMapper attachMapper; // 첨부파일 매퍼 의존성 주입

	// 게시물 등록 및 첨부파일 저장을 하나의 트랜잭션으로 묶어 처리
	@Transactional
	@Override
	public void register(BoardVO board) {

		log.info("register......" + board);

		mapper.insertSelectKey(board); // 게시글 먼저 등록 후 생성된 bno 획득

		if (board.getAttachList() == null || board.getAttachList().size() <= 0) {
			return;
		}

		// 첨부파일 각각에 생성된 게시물 번호(bno)를 부여하고 DB에 등록
		board.getAttachList().forEach(attach -> {
			attach.setBno(board.getBno());
			attachMapper.insert(attach);
		});
	}

	// 게시물 단건 상세 조회
	@Override
	public BoardVO get(Long bno) {
		log.info("get............" + bno);
		return mapper.read(bno);
	}

	// 기존 첨부파일 DB 데이터를 전체 삭제 후 새로 전송된 파일로 재등록 (트랜잭션 보장)
	@Transactional
	@Override
	public boolean modify(BoardVO board) {

		log.info("modify........." + board);
		
		attachMapper.deleteAll(board.getBno()); // 기존 파일 목록 DB 삭제
		
		boolean modifyResult = mapper.update(board) == 1; // 게시글 본문 수정
		
		// 새로 첨부된 파일이 있으면 재등록
		if (modifyResult && board.getAttachList() != null && board.getAttachList().size() > 0) {
			board.getAttachList().forEach(attach -> {
				attach.setBno(board.getBno());
				attachMapper.insert(attach);
			});
		}

		return modifyResult;
	}

	// 게시글과 연계된 첨부파일 데이터를 함께 삭제 (트랜잭션 보장)
	@Transactional
	@Override
	public boolean remove(Long bno) {

		log.info("remove....." + bno);
		
		attachMapper.deleteAll(bno); // 첨부파일 DB 정보 먼저 삭제

		return mapper.delete(bno) == 1; // 게시글 본문 삭제
	}

	// 페이징 기준에 맞는 게시글 목록 조회
	@Override
	public List<BoardVO> getList(Criteria cri) {
		log.info("get List with criteria: " + cri);
		return mapper.getListWithPaging(cri);
	}

	// 총 게시글 개수 조회
	@Override
	public int getTotal(Criteria cri) {
		log.info("get total count");
		return mapper.getTotalCount(cri);
	}
	
	// 게시글 번호에 속한 첨부파일 목록 조회
	@Override
	public List<BoardAttachVO> getAttachList(Long bno) {
		log.info("get Attach list by bno" + bno);
		return attachMapper.findByBno(bno);
	}
}