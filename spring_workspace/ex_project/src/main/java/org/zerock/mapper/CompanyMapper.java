package org.zerock.mapper;

import java.util.List;

import org.zerock.domain.CompanyVO;
import org.zerock.domain.Criteria;

public interface CompanyMapper {

	public void insert(CompanyVO company);
	
	public CompanyVO read(String companyName);
	
	public List<CompanyVO> getListWithSearch(Criteria cri);
	
	public int update(CompanyVO company);
	
	public int delete(String companyName);
}
