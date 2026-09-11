package org.zerock.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.zerock.domain.CompanyVO;
import org.zerock.domain.Criteria;
import org.zerock.mapper.CompanyMapper;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Service
@Log4j
@AllArgsConstructor // 모든 필드를 대상으로 하는 생성자 자동 주입 처리
public class CompanyServiceImpl implements CompanyService {

	// CompanyMapper 주입 (생성자 주입 방식)
	private CompanyMapper mapper;

	@Override
	public void register(CompanyVO company) {
		log.info("register......" + company);
		mapper.insert(company);
	}

	@Override
	public CompanyVO get(String companyName) {
		log.info("get by name......" + companyName);
		return mapper.read(companyName);
	}

	@Override
	public boolean modify(CompanyVO company) {
		log.info("modify by name......" + company);
		return mapper.update(company) == 1;
	}

	@Override
	public boolean remove(String companyName) {
		log.info("remove by name......" + companyName);
		return mapper.delete(companyName) == 1;
	}

	@Override
	public List<CompanyVO> getList(Criteria cri) {
		log.info("get List with criteria: " + cri);
		return mapper.getListWithSearch(cri);
	}
}