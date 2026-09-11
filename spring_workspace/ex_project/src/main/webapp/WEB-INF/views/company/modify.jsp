<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@ include file="../includes/header.jsp"%>

<div class="row">
	<div class="col-lg-12">
		<h1 class="page-header">거래처 정보 수정/삭제</h1>
	</div>
</div>

<div class="row">
	<div class="col-lg-12">
		<div class="panel panel-default">
			<div class="panel-heading">상호 조회를 통한 수정 및 삭제</div>
			<div class="panel-body">
				<form role="form" action="/company/modify" method="post">
					<!-- 검색 및 페이징 상태 보존 히든 파라미터 -->
					<input type="hidden" name="pageNum"
						value="<c:out value='${cri.pageNum}'/>"> <input
						type="hidden" name="amount" value="<c:out value='${cri.amount}'/>">
					<input type="hidden" name="type"
						value="<c:out value='${cri.type}'/>"> <input type="hidden"
						name="keyword" value="<c:out value='${cri.keyword}'/>">

					<div class="form-group">
						<label>거래처 코드</label> <input class="form-control"
							name="companyCode"
							value='<c:out value="${company.companyCode}"/>' readonly>
					</div>

					<div class="form-group">
						<label>상호</label>
						<!-- 상호 조회를 통한 수정/삭제이므로 상호명을 고정 키(readonly)로 사용 -->
						<input class="form-control" name="companyName"
							value='<c:out value="${company.companyName}"/>' readonly>
					</div>

					<div class="form-group">
						<label>대표자명</label> <input class="form-control" name="ceoName"
							value='<c:out value="${company.ceoName}"/>'>
					</div>

					<div class="form-group">
						<label>업태</label> <input class="form-control" name="businessType"
							value='<c:out value="${company.businessType}"/>'>
					</div>

					<!-- 자바스크립트로 제어되는 3가지 액션 버튼 -->
					<button type="submit" data-oper="modify" class="btn btn-default">수정
						(Modify)</button>
					<button type="submit" data-oper="remove" class="btn btn-danger">삭제
						(Remove)</button>
					<button type="submit" data-oper="list" class="btn btn-info">목록
						(List)</button>
				</form>
			</div>
		</div>
	</div>
</div>

<%@ include file="../includes/footer.jsp"%>

<script type="text/javascript">
	$(document).ready(function() {
		var formObj = $("form");

		$('button').on("click", function(e) {
			e.preventDefault(); // 기본 submit 방지

			var operation = $(this).data("oper");

			if (operation === 'remove') {
				if (!confirm("정말 이 거래처를 삭제하시겠습니까?")) {
					return;
				}
				formObj.attr("action", "/company/remove");
			} else if (operation === 'list') {
				formObj.attr("action", "/company/list").attr("method", "get");

				// 검색 및 페이징 정보만 복사해 둠
				var pageNumTag = $("input[name='pageNum']").clone();
				var amountTag = $("input[name='amount']").clone();
				var keywordTag = $("input[name='keyword']").clone();
				var typeTag = $("input[name='type']").clone();

				// 폼 안의 모든 내용(companyCode 포함)을 완전히 비움
				formObj.empty();

				// 목록 이동에 필요한 파라미터만 다시 추가
				formObj.append(pageNumTag);
				formObj.append(amountTag);
				formObj.append(keywordTag);
				formObj.append(typeTag);
			}

			formObj.submit();
		});
	});
</script>