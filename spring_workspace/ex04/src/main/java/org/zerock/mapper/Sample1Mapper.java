package org.zerock.mapper;

import org.apache.ibatis.annotations.Insert;

public interface Sample1Mapper {

	// tbl_sample1 테이블에 데이터 삽입 (트랜잭션 성공/롤백 테스트용)
	@Insert("insert into tbl_sample1 (col1) values (#{data})")
	public int insertCol1(String data);
}