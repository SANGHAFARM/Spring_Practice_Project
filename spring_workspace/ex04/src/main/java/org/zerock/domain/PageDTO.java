package org.zerock.domain;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PageDTO {

	private int startPage;       // 페이지 바 시작 번호 (예: 1, 11, 21)
	private int endPage;         // 페이지 바 끝 번호 (예: 10, 20, 30)
	private boolean prev, next;  // [이전], [다음] 이동 링크 표시 여부

	private int total;           // 테이블의 전체 데이터 총 개수
	private Criteria cri;        // 현재 조회 기준 정보 (현재 페이지, 개수 등)

	// 전체 데이터 개수와 기준 정보를 전달받아 페이지 번호들을 자동 계산
	public PageDTO(Criteria cri, int total) {

		this.cri = cri;
		this.total = total;

		// 현재 페이지 기준 임시 끝 번호 계산
		this.endPage = (int) (Math.ceil(cri.getPageNum() / 10.0)) * 10;

		// 시작 번호 계산 (끝 번호 - 9)
		this.startPage = this.endPage - 9;

		// 실제 데이터 개수 기반의 진짜 마지막 페이지 번호 계산
		int realEnd = (int) (Math.ceil((total * 1.0) / cri.getAmount()));

		// 계산된 끝 번호가 실제 끝 번호보다 크면 실제 끝 번호로 보정
		if (realEnd < this.endPage) {
			this.endPage = realEnd;
		}

		// 시작 번호가 1보다 클 때만 [이전] 링크 활성화
		this.prev = this.startPage > 1;

		// 계산된 끝 번호가 실제 마지막 페이지보다 작을 때만 [다음] 링크 활성화
		this.next = this.endPage < realEnd;
	}
}