package org.zerock.mapper;

import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.zerock.domain.CompanyVO;
import org.zerock.domain.Criteria;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
@Log4j
public class CompanyMapperTests {

    @Setter(onMethod_ = @Autowired)
    private CompanyMapper mapper;

    // 1. 등록 테스트
    @Test
    public void testInsert() {
        CompanyVO company = new CompanyVO();
        company.setCompanyCode("COMP555");
        company.setCompanyName("현대상사");
        company.setCeoName("정대표");
        company.setBusinessType("무역업");

        mapper.insert(company);
        log.info("등록 완료: " + company);
    }

    // 2. 상호 조회를 통한 단건 조회 테스트
    @Test
    public void testRead() {
        // DB에 존재하는 상호명 입력
        CompanyVO company = mapper.read("현대상사");
        log.info("상호 단건 조회 결과: " + company);
    }

    // 3. 다중 조건 검색 목록 테스트 (상호 / 대표자명)
    @Test
    public void testSearch() {
        Criteria cri = new Criteria();
        cri.setType("NC");      // 상호(N) or 대표자명(C)
        cri.setKeyword("홍길동");

        List<CompanyVO> list = mapper.getListWithSearch(cri);
        list.forEach(comp -> log.info(comp));
    }

    // 4. 상호 기준 정보 수정 테스트
    @Test
    public void testUpdate() {
        CompanyVO company = new CompanyVO();
        // WHERE 조건의 기준이 되는 상호
        company.setCompanyName("현대상사");
        // 변경할 정보 세팅
        company.setCeoName("정수정");
        company.setBusinessType("물류/유통업");

        int count = mapper.update(company);
        log.info("UPDATE COUNT: " + count);
    }

    // 5. 상호 기준 삭제 테스트
    @Test
    public void testDelete() {
        int count = mapper.delete("현대상사");
        log.info("DELETE COUNT: " + count);
    }
}