package org.zerock.mapper;

import java.util.List;
import java.util.stream.IntStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.zerock.domain.Criteria;
import org.zerock.domain.ReplyVO;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@RunWith(SpringRunner.class) // 스프링 JUnit 테스트 실행기 적용
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml") // 루트 설정 파일 로드
@Log4j
public class ReplyMapperTests {

	// 테스트용으로 DB에 미리 존재하는 게시글 번호 5개 지정
	private Long[] bnoArr = { 361L, 362L, 363L, 364L, 365L };

	@Setter(onMethod_ = @Autowired)
	private ReplyMapper mapper; // 댓글 매퍼 빈 주입

	// 10개의 더미 댓글을 생성해 지정된 게시글들에 순차 등록 테스트
	@Test
	public void testCreate() {

		IntStream.rangeClosed(1, 10).forEach(i -> {

			ReplyVO vo = new ReplyVO();

			// 게시물 번호 순환 할당 (361 ~ 365)
			vo.setBno(bnoArr[i % 5]);
			vo.setReply("댓글 테스트 " + i);
			vo.setReplyer("replyer" + i);

			mapper.insert(vo);
		});

	}

	// 매퍼 인터페이스 객체가 정상 주입되었는지 확인
	@Test
	public void testMapper() {

		log.info(mapper);
	}

	// 1번 댓글 단건 조회 동작 검증
	@Test
	public void testRead() {

		Long targetRno = 1L;

		ReplyVO vo = mapper.read(targetRno);

		log.info(vo);
	}

	// 1번 댓글 삭제 동작 검증
	@Test
	public void testDelete() {

		Long targetRno = 1L;

		mapper.delete(targetRno);
	}

	// 2번 댓글 내용 수정 동작 검증
	@Test
	public void testUpdate() {

		Long targetRno = 2L;
		ReplyVO vo = mapper.read(targetRno);

		vo.setReply("Update Reply ");

		int count = mapper.update(vo); // 수정된 행 수 반환

		log.info("UPDATE COUNT: " + count);
	}

	// 기본 페이징 조건(1페이지 10개)으로 특정 게시물의 댓글 목록 조회 검증
	@Test
	public void testList() {

		Criteria cri = new Criteria();

		List<ReplyVO> replies = mapper.getListWithPaging(cri, bnoArr[0]);

		replies.forEach(reply -> log.info(reply));
	}

	// 2페이지 10개 조건으로 특정 게시물의 댓글 페이징 조회 검증
	@Test
	public void testList2() {

		Criteria cri = new Criteria(2, 10);

		List<ReplyVO> replies = mapper.getListWithPaging(cri, 3145745L);

		replies.forEach(reply -> log.info(reply));

	}
}