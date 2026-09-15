package org.zerock.mapper;

import org.apache.ibatis.annotations.Insert;

public interface Sample2Mapper {
	
	// tbl_sample2 테이블에 데이터 삽입 (컬럼 길이 초과 시 롤백 유도 테스트용)
	@Insert("insert into tbl_sample2 (col2) values (#{data})")
	public int insertCol2(String data);

}