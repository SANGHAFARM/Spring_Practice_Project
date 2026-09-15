package org.zerock.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.domain.SampleVO;
import org.zerock.domain.Ticket;

import lombok.extern.log4j.Log4j;

@RestController // JSP 화면 대신 순수 데이터(JSON, 문자열 등)를 반환하는 REST 컨트롤러 선언
@RequestMapping("/sample") // '/sample'로 시작하는 요청 URL을 공통 처리
@Log4j // 로그 출력을 위한 log 객체 자동 생성
public class SampleContorller {

	// 1. 단순 텍스트 문자열 반환 (한글 깨짐 방지용 produces 속성 설정)
	@GetMapping(value = "/getText", produces = "text/plain; charset=UTF-8")
	public String getText() {
		log.info("MIME TYPE: " + MediaType.TEXT_PLAIN_VALUE);

		return "안녕하세요"; // 웹 브라우저 화면에 순수 텍스트 "안녕하세요" 출력
	}

	// 2. 객체 반환 시 produces로 지원할 데이터 형식(JSON, XML)을 명시적으로 지정
	@GetMapping(value = "/getSample", produces = { MediaType.APPLICATION_JSON_UTF8_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	public SampleVO getSample() {
		return new SampleVO(112, "스타", "로드"); // 객체가 요청에 따라 JSON이나 XML로 자동 변환됨
	}

	// 3. produces 속성을 생략해도 기본 설정에 따라 객체를 JSON/XML로 자동 변환
	@GetMapping(value = "/getSample2")
	public SampleVO getSample2() {
		return new SampleVO(113, "로켓", "라쿤");
	}

	// 4. 리스트(List) 컬렉션을 JSON 배열([ {...}, {...} ]) 형태로 변환하여 반환
	@GetMapping(value = "/getList")
	public List<SampleVO> getList() {

		// 1부터 9까지의 반복 데이터를 기반으로 SampleVO 리스트 생성
		return IntStream.range(1, 10).mapToObj(i -> new SampleVO(i, i + "First", i + " Last"))
				.collect(Collectors.toList());
	}

	// 5. 맵(Map) 컬렉션을 Key:Value 형태의 JSON 객체로 변환하여 반환
	@GetMapping(value = "/getMap")
	public Map<String, SampleVO> getMap() {

		Map<String, SampleVO> map = new HashMap<>();
		map.put("Frist", new SampleVO(111, "그루트", "주니어")); // 'Frist'가 JSON 객체의 키 이름이 됨

		return map;
	}

	// 6. 데이터와 함께 동적으로 HTTP 상태 코드(200, 502 등)를 제어하여 반환
	@GetMapping(value = "/check", params = { "height", "weight" }) // 두 파라미터가 모두 있어야만 호출됨
	public ResponseEntity<SampleVO> check(Double height, Double weight) {

		SampleVO vo = new SampleVO(0, "" + height, "" + weight);

		ResponseEntity<SampleVO> result = null;

		// 키가 150 미만이면 502(BAD_GATEWAY) 에러 상태 전송, 이상이면 200(OK) 정상 상태 전송
		if (height < 150) {
			result = ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(vo);
		} else {
			result = ResponseEntity.status(HttpStatus.OK).body(vo);
		}

		return result;
	}

	// 7. URL 주소 경로 자체에 포함된 변수 값을 추출하여 처리 (@PathVariable)
	@GetMapping("/product/{cat}/{pid}")
	public String[] getPath(@PathVariable("cat") String cat, @PathVariable("pid") Integer pid) {
		// 경로의 {cat}과 {pid} 자리에 들어온 값을 파라미터로 바인딩
		return new String[] { "category: " + cat, "productid: " + pid };
	}

	// 8. 클라이언트가 전송한 JSON 본문 데이터를 자바 객체로 역직렬화 수신 (@RequestBody)
	@PostMapping("/ticket")
	public Ticket convert(@RequestBody Ticket ticket) { // 전송된 JSON 바디가 Ticket 객체로 변환됨

		log.info("convert...........ticket" + ticket);

		return ticket; // 전달받은 객체를 다시 JSON 규격으로 반환
	}
}