package org.zerock.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.mapper.Sample1Mapper;
import org.zerock.mapper.Sample2Mapper;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Service // 비즈니스 서비스 빈으로 등록
@Log4j // 로그 출력을 위한 log 객체 자동 생성
public class SampleTxServiceImpl implements SampleTxService {

	@Setter(onMethod_ = { @Autowired })
	private Sample1Mapper mapper1; // tbl_sample1 매퍼 주입

	@Setter(onMethod_ = { @Autowired })
	private Sample2Mapper mapper2; // tbl_sample2 매퍼 주입

	// 두 테이블 작업 중 하나라도 에러가 발생하면 전체 작업을 롤백(취소)
	@Transactional
	@Override
	public void addData(String value) {

		log.info("mapper1........................");
		mapper1.insertCol1(value); // 컬럼 길이가 긴 tbl_sample1에 저장 (성공)

		log.info("mapper2........................");
		mapper2.insertCol2(value); // 컬럼 길이가 짧은 tbl_sample2에 저장 (글자 수 초과 시 예외 발생 유도)

		log.info("end............................");

	}

}