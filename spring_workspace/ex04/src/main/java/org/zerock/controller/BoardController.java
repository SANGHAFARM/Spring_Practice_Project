package org.zerock.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.domain.BoardAttachVO;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;
import org.zerock.domain.PageDTO;
import org.zerock.service.BoardService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Controller // 스프링 MVC 컨트롤러 빈으로 등록
@Log4j // 로그 출력을 위한 log 객체 자동 생성
@RequestMapping("/board/*") // '/board/'로 시작하는 모든 웹 요청 매핑
@AllArgsConstructor // service 객체를 주입받기 위한 생성자 자동 생성
public class BoardController {

	private BoardService service; // 핵심 비즈니스 로직을 처리하는 서비스 객체

	// 서버 하드디스크에 저장된 실제 첨부파일(원본 및 섬네일)을 삭제하는 보조 메서드
	private void deleteFiles(List<BoardAttachVO> attachList) {

		if (attachList == null || attachList.size() == 0) {
			return;
		}

		log.info("delete attach files...................");
		log.info(attachList);

		attachList.forEach(attach -> {
			try {
				// 1. 원본 파일 경로 추출 후 파일이 존재하면 로컬에서 삭제
				Path file = Paths.get(
						"C:\\upload\\" + attach.getUploadPath() + "\\" + attach.getUuid() + "_" + attach.getFileName());

				Files.deleteIfExists(file);

				// 2. 파일 종류가 이미지일 경우 생성되어 있던 섬네일(s_) 파일도 함께 삭제
				if (Files.probeContentType(file).startsWith("image")) {

					Path thumbNail = Paths.get("C:\\upload\\" + attach.getUploadPath() + "\\s_" + attach.getUuid() + "_"
							+ attach.getFileName());

					Files.delete(thumbNail);
				}

			} catch (Exception e) {
				log.error("delete file error" + e.getMessage());
			} // end catch
		}); // end forEach
	}

	// 게시물 목록 조회 (페이징 데이터 및 검색 조건 포함)
	@GetMapping("/list")
	public void list(Criteria cri, Model model) {

		log.info("list: " + cri);
		// 현재 페이지에 해당하는 글 목록을 Model에 담아 JSP로 전달
		model.addAttribute("list", service.getList(cri));

		// 검색 조건이 반영된 전체 데이터 개수 조회
		int total = service.getTotal(cri);

		log.info("total: " + total);

		// 하단 페이지 번호(1, 2, 3...) 연산 정보를 담은 PageDTO를 Model에 추가
		model.addAttribute("pageMaker", new PageDTO(cri, total));
	}

	// 게시물 및 첨부파일 등록 처리
	@PostMapping("/register")
	public String register(BoardVO board, RedirectAttributes rttr) {

		log.info("==========================");
		log.info("register: " + board);

		// 폼에서 넘어온 첨부파일 목록 확인
		if (board.getAttachList() != null) {
			board.getAttachList().forEach(attach -> log.info(attach));
		}

		log.info("==========================");

		service.register(board); // DB에 게시물 및 첨부파일 저장

		// 등록 직후 모달 알림에 띄울 생성된 글 번호를 일회성(Flash)으로 전달
		rttr.addFlashAttribute("result", board.getBno());

		return "redirect:/board/list"; // 글 목록 페이지로 리다이렉트 이동
	}

	// 단건 조회 화면(/get) 및 수정 입력 폼(/modify)으로 이동
	@GetMapping({ "/get", "/modify" })
	public void get(@RequestParam("bno") Long bno, @ModelAttribute("cri") Criteria cri, Model model) {

		log.info("/get or /modify");
		// 글 번호(bno)로 게시물 데이터를 조회해 Model에 담음 (cri는 @ModelAttribute로 자동 유지)
		model.addAttribute("board", service.get(bno));
	}

	// 게시물 내용 수정 처리
	@PostMapping("/modify")
	public String modify(BoardVO board, @ModelAttribute("cri") Criteria cri, RedirectAttributes rttr) {
		log.info("modify:" + board);

		// 수정 성공 시 일회성 결과 플래그 전달
		if (service.modify(board)) {
			rttr.addFlashAttribute("result", "success");
		}
		// 수정 전 보고 있던 페이지 번호와 검색 조건을 쿼리 스트링에 붙여서 목록으로 이동
		return "redirect:/board/list" + cri.getListLink();
	}

	// 게시물 및 첨부파일 삭제 처리
	@PostMapping("/remove")
	public String remove(@RequestParam("bno") Long bno, Criteria cri, RedirectAttributes rttr) {

		log.info("remove..." + bno);

		// DB 삭제 전 먼저 삭제할 첨부파일 목록 정보 확보
		List<BoardAttachVO> attachList = service.getAttachList(bno);

		// DB에서 글과 첨부파일 데이터 삭제가 성공하면 실제 하드디스크 파일도 제거
		if (service.remove(bno)) {
			deleteFiles(attachList); // 로컬 저장소 파일 삭제
			rttr.addFlashAttribute("result", "success");
		}

		// 기존 페이지 및 검색 조건 링크를 유지하며 목록으로 복귀
		return "redirect:/board/list" + cri.getListLink();
	}

	// 단순 글 등록 화면(register.jsp)으로 이동
	@GetMapping("/register")
	public void register() {

	}

	// 특정 게시물의 첨부파일 목록을 JSON 형태로 비동기(Ajax) 응답
	@GetMapping(value = "/getAttachList", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody // JSP 뷰가 아닌 HTTP 바디에 순수 데이터(JSON)를 반환하도록 지정
	public ResponseEntity<List<BoardAttachVO>> getAttachList(Long bno) {
		log.info("getAttachList " + bno);

		// 첨부파일 리스트 데이터와 함께 정상 HTTP 상태(200 OK)를 전송
		return new ResponseEntity<>(service.getAttachList(bno), HttpStatus.OK);
	}

}