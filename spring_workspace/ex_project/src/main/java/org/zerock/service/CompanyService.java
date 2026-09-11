package org.zerock.service;

import java.util.List;
import org.zerock.domain.CompanyVO;
import org.zerock.domain.Criteria;

public interface CompanyService {

	public void register(CompanyVO company);

	public CompanyVO get(String companyName); // 상호 단건 조회

	public boolean modify(CompanyVO company); // 상호 기준 수정

	public boolean remove(String companyName); // 상호 기준 삭제

	public List<CompanyVO> getList(Criteria cri); // 다중 검색 목록
}