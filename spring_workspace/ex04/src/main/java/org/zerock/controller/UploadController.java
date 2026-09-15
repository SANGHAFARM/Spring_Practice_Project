package org.zerock.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.domain.AttachFileDTO;

import lombok.extern.log4j.Log4j;
import net.coobird.thumbnailator.Thumbnailator;

@Controller // 파일 처리 관련 웹 요청을 담당하는 스프링 컨트롤러
@Log4j // 로그 출력을 위한 log 객체 생성
public class UploadController {

	// 1. 단순 Form 방식의 업로드 화면(uploadForm.jsp)으로 이동
	@GetMapping("/uploadForm")
	public void uploadForm() {
		log.info("upload form");
	}

	// 2. Form 태그 전송으로 전달된 다중 파일 업로드 처리
	@PostMapping("/uploadFormAction")
	public void uploadFormPost(MultipartFile[] uploadFile, Model model) {

		String uploadFolder = "C:\\upload"; // 파일이 저장될 기본 루트 폴더

		for (MultipartFile multipartFile : uploadFile) {

			log.info("-------------------------------------");
			log.info("Upload File Name: " + multipartFile.getOriginalFilename());
			log.info("Upload File Size: " + multipartFile.getSize());

			// 원본 파일명을 사용해 저장 파일 객체 생성
			File saveFile = new File(uploadFolder, multipartFile.getOriginalFilename());

			try {
				multipartFile.transferTo(saveFile); // 임시 저장된 파일을 실제 경로로 물리적 저장
			} catch (Exception e) {
				log.error(e.getMessage());
			} // end catch

		} // end for
	}

	// 3. Ajax 방식의 업로드 테스트 화면(uploadAjax.jsp)으로 이동
	@GetMapping("/uploadAjax")
	public void uploadAjax() {
		log.info("upload ajax");
	}

	// 4. Ajax 파일 업로드 처리 (날짜별 폴더 생성, UUID 적용, 섬네일 생성 후 DTO 목록 JSON 반환)
	@PostMapping(value = "/uploadAjaxAction", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody // 브라우저 화면 전환 없이 업로드된 파일 정보 리스트를 JSON 데이터로 응답
	public ResponseEntity<List<AttachFileDTO>> uploadAjaxPost(MultipartFile[] uploadFile) {

		List<AttachFileDTO> list = new ArrayList<>(); // 업로드된 파일 정보들을 담을 리스트
		String uploadFolder = "C:\\upload";

		String uploadFolderPath = getFolder(); // 오늘 날짜 기반 경로(yyyy/MM/dd) 획득
		// make folder --------
		File uploadPath = new File(uploadFolder, uploadFolderPath);

		// 오늘 날짜에 해당하는 디렉터리가 없으면 자동 생성
		if (uploadPath.exists() == false) {
			uploadPath.mkdirs();
		}

		for (MultipartFile multipartFile : uploadFile) {

			AttachFileDTO attachDTO = new AttachFileDTO(); // 개별 파일 정보를 담을 객체 생성

			String uploadFileName = multipartFile.getOriginalFilename();

			// IE 브라우저의 경우 파일 전체 경로가 전달되므로 순수 파일명만 추출
			uploadFileName = uploadFileName.substring(uploadFileName.lastIndexOf("\\") + 1);
			log.info("only file name: " + uploadFileName);
			attachDTO.setFileName(uploadFileName);

			UUID uuid = UUID.randomUUID(); // 동일 파일명 덮어쓰기 방지를 위한 고유 식별자(UUID) 생성

			uploadFileName = uuid.toString() + "_" + uploadFileName; // "UUID_파일명" 조합

			try {
				File saveFile = new File(uploadPath, uploadFileName);
				multipartFile.transferTo(saveFile); // 실제 서버 폴더에 파일 저장

				attachDTO.setUuid(uuid.toString());
				attachDTO.setUploadPath(uploadFolderPath);

				// 저장된 파일이 이미지인지 검사
				if (checkImageType(saveFile)) {

					attachDTO.setImage(true);

					// 섬네일 파일명은 앞에 's_'를 붙여 생성
					FileOutputStream thumbnail = new FileOutputStream(new File(uploadPath, "s_" + uploadFileName));

					// 가로 100px, 세로 100px 크기의 섬네일 이미지 파일 생성
					Thumbnailator.createThumbnail(multipartFile.getInputStream(), thumbnail, 100, 100);

					thumbnail.close();
				}

				list.add(attachDTO); // 리스트에 개별 파일 결과 추가

			} catch (Exception e) {
				e.printStackTrace();
			}

		} // end for
		return new ResponseEntity<>(list, HttpStatus.OK); // 200 OK 상태와 함께 업로드 결과 리스트 반환
	}

	// 5. 오늘 날짜를 "년\월\일" 형식의 경로 문자열로 변환하는 보조 메서드
	private String getFolder() {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String str = sdf.format(date);

		return str.replace("-", File.separator); // OS 환경에 맞춰 하이픈(-)을 경로 구분자로 치환
	}

	// 6. 업로드된 파일이 실제 이미지인지 MIME 타입으로 판별하는 보조 메서드
	private boolean checkImageType(File file) {

		try {
			String contentType = Files.probeContentType(file.toPath()); // MIME 타입 확인
			return contentType.startsWith("image"); // MIME 타입이 "image/"로 시작하는지 여부 반환

		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

	// 7. 이미지 파일의 바이트 데이터를 읽어 브라우저 화면에 직접 렌더링(/display)
	@GetMapping("/display")
	@ResponseBody
	public ResponseEntity<byte[]> getFile(String fileName) {

		log.info("fileName: " + fileName);
		File file = new File("c:\\upload\\" + fileName); // 요청된 파일 객체 생성

		log.info("file: " + file);
		ResponseEntity<byte[]> result = null;

		try {
			HttpHeaders header = new HttpHeaders();
			// 파일 확장자에 맞는 MIME 타입을 응답 헤더(Content-Type)에 추가 (예: image/jpeg)
			header.add("Content-Type", Files.probeContentType(file.toPath()));
			// 파일 내용을 바이트 배열로 변환하여 헤더와 함께 200 OK로 반환
			result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	// 8. 첨부파일 다운로드 처리 (브라우저별 한글 인코딩 처리 및 파일 스트림 전송)
	@GetMapping(value = "/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ResponseBody
	public ResponseEntity<Resource> downloadFile(@RequestHeader("User-Agent") String userAgent, String fileName) {

		Resource resource = new FileSystemResource("c:\\upload\\" + fileName); // 로컬 파일 리소스화

		// 파일이 실제로 존재하지 않으면 404 NOT FOUND 응답
		if (resource.exists() == false) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		String resourceName = resource.getFilename();
		// 다운로드 시에는 파일명 앞에 붙은 UUID_ 부분을 잘라내고 원래 이름으로 복원
		String resourceOriginalName = resourceName.substring(resourceName.indexOf("_") + 1);

		HttpHeaders headers = new HttpHeaders();
		try {
			String downloadName = null;

			// 브라우저 헤더 정보(User-Agent)에 따라 파일명 한글 인코딩 분기 처리
			if (userAgent.contains("Trident")) {
				log.info("IE browser"); // 인터넷 익스플로러 처리
				downloadName = URLEncoder.encode(resourceOriginalName, "UTF-8").replaceAll("\\+", " ");
			} else if (userAgent.contains("Edge")) {
				log.info("Edge browser"); // 엣지 브라우저 처리
				downloadName = URLEncoder.encode(resourceOriginalName, "UTF-8");
			} else {
				log.info("Chrome browser"); // 크롬 및 기타 브라우저 처리
				downloadName = new String(resourceOriginalName.getBytes("UTF-8"), "ISO-8859-1");
			}

			log.info("downloadName: " + downloadName);

			// 브라우저가 화면에 띄우지 않고 다운로드 창을 띄우도록 헤더 설정
			headers.add("Content-Disposition", "attachment; filename=" + downloadName);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return new ResponseEntity<Resource>(resource, headers, HttpStatus.OK); // 파일 리소스 반환
	}

	// 9. 첨부파일 삭제 처리 (일반 파일은 1개 삭제, 이미지는 원본과 섬네일 2개 모두 삭제)
	@PostMapping("/deleteFile")
	@ResponseBody
	public ResponseEntity<String> deleteFile(String fileName, String type) {

		log.info("deleteFile: " + fileName);
		File file;

		try {
			// URL 인코딩되어 전달된 파일명을 디코딩한 후 파일 객체 생성
			file = new File("c:\\upload\\" + URLDecoder.decode(fileName, "UTF-8"));

			file.delete(); // 전달된 파일 삭제 (이미지의 경우 섬네일 파일이 먼저 삭제됨)

			// 이미지인 경우 원본 큰 이미지 파일도 찾아서 함께 삭제
			if (type.equals("image")) {
				// 경로 중간의 's_'를 제거하여 원본 파일 경로 추출
				String largeFileName = file.getAbsolutePath().replace("s_", "");
				log.info("largeFileName: " + largeFileName);

				file = new File(largeFileName);
				file.delete(); // 원본 파일 삭제
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<String>("deleted", HttpStatus.OK); // 성공 메시지 "deleted" 반환
	}

}