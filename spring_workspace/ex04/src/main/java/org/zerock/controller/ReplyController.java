package org.zerock.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.domain.Criteria;
import org.zerock.domain.ReplyPageDTO;
import org.zerock.domain.ReplyVO;
import org.zerock.service.ReplyService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@RequestMapping("/replies/") // '/replies/' 경로로 들어오는 모든 요청을 기본 매핑
@RestController // 뷰(JSP) 대신 순수 데이터(JSON/XML)를 반환하는 컨트롤러 선언
@Log4j // 로그 출력을 위한 log 객체 자동 생성
@AllArgsConstructor // ReplyService 의존성 주입을 위한 생성자 자동 생성
public class ReplyController {

	private ReplyService service; // 댓글 비즈니스 로직을 수행할 서비스 객체

	// 1. 댓글 등록 처리 (JSON 데이터를 수신하여 DB 등록 후 결과 문자열 전송)
	@PostMapping(value = "/new", consumes = "application/json", produces = { MediaType.TEXT_PLAIN_VALUE })
	public ResponseEntity<String> create(@RequestBody ReplyVO vo) { // @RequestBody: 요청된 JSON을 객체로 변환

		log.info("ReplyVO: " + vo);

		int insertCount = service.register(vo); // 서비스 호출 및 추가된 행 수 반환

		log.info("Reply INSERT COUNT: " + insertCount);

		// 정상 등록(1개 성공)이면 200 OK + "success" 문자열 반환, 실패 시 500 에러 반환
		return insertCount == 1 ? new ResponseEntity<>("success", HttpStatus.OK)
				: new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	// 2. 특정 게시글의 댓글 목록 페이징 조회 (URL 경로에서 게시물번호, 페이지번호 추출)
	@GetMapping(value = "/pages/{bno}/{page}", produces = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_UTF8_VALUE })
	public ResponseEntity<ReplyPageDTO> getList(@PathVariable("page") int page, // URL 경로의 {page} 값을 추출
			@PathVariable("bno") Long bno) { // URL 경로의 {bno} 값을 추출

		Criteria cri = new Criteria(page, 10); // 요청 페이지 번호와 페이지당 개수(10개) 설정

		log.info("get Reply List bno: " + bno);
		log.info("cri:" + cri);

		// 댓글 페이징 데이터(댓글 리스트 + 댓글 수)와 함께 200 OK 전송
		return new ResponseEntity<>(service.getListPage(cri, bno), HttpStatus.OK);
	}

	// 3. 특정 댓글 1건 단건 조회
	@GetMapping(value = "/{rno}", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE })
	public ResponseEntity<ReplyVO> get(@PathVariable("rno") Long rno) { // URL의 {rno} 댓글 번호 추출

		log.info("get: " + rno);

		// 댓글 데이터 단건 조회 후 200 OK 전송
		return new ResponseEntity<>(service.get(rno), HttpStatus.OK);
	}

	// 4. 댓글 삭제 처리 (DELETE 요청 방식)
	@DeleteMapping(value = "/{rno}", produces = { MediaType.TEXT_PLAIN_VALUE })
	public ResponseEntity<String> remove(@PathVariable("rno") Long rno) { // URL의 {rno} 댓글 번호 추출

		log.info("remove: " + rno);

		// 삭제 성공 시 "success"와 200 OK 반환, 실패 시 500 에러 반환
		return service.remove(rno) == 1 ? new ResponseEntity<>("success", HttpStatus.OK)
				: new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	// 5. 댓글 내용 수정 처리 (PUT/PATCH 요청 방식 혼용 지원)
	@RequestMapping(method = { RequestMethod.PUT,
			RequestMethod.PATCH }, value = "/{rno}", consumes = "application/json", produces = {
					MediaType.TEXT_PLAIN_VALUE })
	public ResponseEntity<String> modify(@RequestBody ReplyVO vo, // 수정할 데이터 본문(JSON)을 객체로 변환
			@PathVariable("rno") Long rno) { // 수정 대상이 되는 URL의 {rno} 댓글 번호 추출

		vo.setRno(rno); // 수정한 댓글 번호를 객체에 주입

		log.info("rno: " + rno);
		log.info("modify: " + vo);

		// 수정 성공 시 "success"와 200 OK 반환, 실패 시 500 에러 반환
		return service.modify(vo) == 1 ? new ResponseEntity<>("success", HttpStatus.OK)
				: new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

}