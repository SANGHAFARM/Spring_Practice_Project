package org.zerock.task;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.zerock.domain.BoardAttachVO;
import org.zerock.mapper.BoardAttachMapper;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Log4j
@Component // 스프링이 관리하는 스케줄러 컴포넌트 빈으로 등록
public class FileCheckTask {

	@Setter(onMethod_ = { @Autowired })
	private BoardAttachMapper attachMapper; // DB 첨부파일 테이블 조회를 위한 매퍼 주입

	// 어제 날짜 기준 '년/월/일' 폴더 경로 문자열 생성
	private String getFolderYesterDay() {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Calendar cal = Calendar.getInstance();

		cal.add(Calendar.DATE, -1); // 오늘 날짜에서 하루를 뺌

		String str = sdf.format(cal.getTime());

		return str.replace("-", File.separator);
	}

	// 매일 새벽 2시마다 정기적으로 자동 실행 (cron = "초 분 시 일 월 요일")
	@Scheduled(cron = "0 0 2 * * *")
	public void checkFiles() throws Exception {

		log.warn("File Check Task run......................");
		log.warn(new Date());

		// 1. 어제 날짜로 DB에 정상 등록된 첨부파일 목록 조회
		List<BoardAttachVO> fileList = attachMapper.getOldFiles();

		// 2. DB에 기록된 원본 파일들의 실제 하드디스크 경로 리스트 생성
		List<Path> fileListPaths = fileList.stream()
				.map(vo -> Paths.get("C:\\upload", vo.getUploadPath(), vo.getUuid() + "_" + vo.getFileName()))
				.collect(Collectors.toList());

		// 3. 이미지 파일인 경우 섬네일(s_) 파일 경로도 삭제 방지 목록에 추가
		fileList.stream().filter(vo -> vo.isFileType() == true)
				.map(vo -> Paths.get("C:\\upload", vo.getUploadPath(), "s_" + vo.getUuid() + "_" + vo.getFileName()))
				.forEach(p -> fileListPaths.add(p));

		log.warn("===========================================");

		fileListPaths.forEach(p -> log.warn(p));

		// 4. 하드디스크의 실제 어제 날짜 폴더 탐색
		File targetDir = Paths.get("C:\\upload", getFolderYesterDay()).toFile();

		// 5. 폴더 내 파일 중 DB 목록(fileListPaths)에 없는 불필요한 고아 파일만 필터링
		File[] removeFiles = targetDir.listFiles(file -> fileListPaths.contains(file.toPath()) == false);

		log.warn("-----------------------------------------");
		// 6. 글 등록을 취소하여 남아있던 쓰레기 파일들을 디스크에서 영구 삭제
		for (File file : removeFiles) {

			log.warn(file.getAbsolutePath());

			file.delete();
		}
	}
}