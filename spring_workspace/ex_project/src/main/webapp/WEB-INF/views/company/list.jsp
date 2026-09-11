<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@ include file="../includes/header.jsp"%>

<div class="row">
	<div class="col-lg-12">
		<h1 class="page-header">거래처 목록</h1>
	</div>
</div>

<div class="row">
	<div class="col-lg-12">
		<div class="panel panel-default">
			<div class="panel-heading">
				거래처 목록
				<button id="regBtn" type="button"
					class="btn btn-xs btn-primary pull-right">새 거래처 등록</button>
			</div>

			<div class="panel-body">
				<!-- 1. 검색 바 (searchForm) -->
				<div class="row">
					<div class="col-lg-12">
						<form id="searchForm" action="/company/list" method="get">
							<select name="type">
								<option value=""
									<c:out value="${cri.type == null ? 'selected' : ''}"/>>--
									검색 조건 --</option>
								<option value="N"
									<c:out value="${cri.type eq 'N' ? 'selected' : ''}"/>>상호</option>
								<option value="C"
									<c:out value="${cri.type eq 'C' ? 'selected' : ''}"/>>대표자명</option>
								<option value="NC"
									<c:out value="${cri.type eq 'NC' ? 'selected' : ''}"/>>상호
									/ 대표자명</option>
							</select> <input type="text" name="keyword"
								value="<c:out value='${cri.keyword}'/>" /> <input type="hidden"
								name="pageNum" value="<c:out value='${cri.pageNum}'/>" /> <input
								type="hidden" name="amount"
								value="<c:out value='${cri.amount}'/>" />
							<button class="btn btn-default">Search</button>
						</form>
					</div>
				</div>

				<!-- 2. 거래처 데이터 테이블 -->
				<table class="table table-striped table-bordered table-hover"
					style="margin-top: 15px;">
					<thead>
						<tr>
							<th>거래처 코드</th>
							<th>상호</th>
							<th>대표자명</th>
							<th>업태</th>
							<th>등록일</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${list}" var="company">
							<tr>
								<td><c:out value="${company.companyCode}" /></td>
								<td>
									<!-- 상호를 클릭하면 수정/상세 화면으로 이동 --> <a class="move"
									href="<c:out value='${company.companyName}'/>"> <c:out
											value="${company.companyName}" />
								</a>
								</td>
								<td><c:out value="${company.ceoName}" /></td>
								<td><c:out value="${company.businessType}" /></td>
								<td><fmt:formatDate pattern="yyyy-MM-dd"
										value="${company.regdate}" /></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>

				<!-- 3. 검색 및 이동 파라미터 전달 전용 actionForm -->
				<form id="actionForm" action="/company/list" method="get">
					<input type="hidden" name="pageNum"
						value="${empty cri.pageNum ? 1 : cri.pageNum}"> <input
						type="hidden" name="amount"
						value="${empty cri.amount ? 10 : cri.amount}"> <input
						type="hidden" name="type" value="<c:out value='${cri.type}'/>">
					<input type="hidden" name="keyword"
						value="<c:out value='${cri.keyword}'/>">
				</form>

				<!-- 4. 결과 안내 모달 (등록/수정/삭제 후 팝업) -->
				<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
					aria-hidden="true">
					<div class="modal-dialog">
						<div class="modal-content">
							<div class="modal-header">
								<button type="button" class="close" data-dismiss="modal"
									aria-hidden="true">&times;</button>
								<h4 class="modal-title">알림</h4>
							</div>
							<div class="modal-body">처리가 완료되었습니다.</div>
							<div class="modal-footer">
								<button type="button" class="btn btn-default"
									data-dismiss="modal">닫기</button>
							</div>
						</div>
					</div>
				</div>

			</div>
		</div>
	</div>
</div>

<%@ include file="../includes/footer.jsp"%>

<script type="text/javascript">
	$(document)
			.ready(
					function() {
						var result = '<c:out value="${result}"/>';

						checkModal(result);

						history.replaceState({}, null, null);

						// 교재 표준 뒤로가기 모달 중복 방지 로직
						function checkModal(result) {
							if (result === '' || history.state) {
								return;
							}
							if (result === 'success') {
								$(".modal-body").html("정상적으로 처리되었습니다.");
							} else if (result.length > 0) {
								$(".modal-body").html(
										"거래처 " + result + " 번이 등록되었습니다.");
							}
							$("#myModal").modal("show");
						}

						// 새 거래처 등록 버튼 클릭
						$("#regBtn").on("click", function() {
							self.location = "/company/register";
						});

						var actionForm = $("#actionForm");

						// 상호 클릭 시 수정/조회 화면 이동
						$(".move")
								.on(
										"click",
										function(e) {
											e.preventDefault();

											actionForm
													.find(
															"input[name='companyName']")
													.remove();
											actionForm
													.append("<input type='hidden' name='companyName' value='"
															+ $(this).attr(
																	"href")
															+ "'>");

											if (!actionForm.find(
													"input[name='keyword']")
													.val()) {
												actionForm.find(
														"input[name='type']")
														.attr("disabled", true);
												actionForm
														.find(
																"input[name='keyword']")
														.attr("disabled", true);
											}

											actionForm.attr("action",
													"/company/modify");
											actionForm.submit();
										});

						// 검색 폼 유효성 체크
						var searchForm = $("#searchForm");
						$("#searchForm button").on(
								"click",
								function(e) {
									if (!searchForm.find("option:selected")
											.val()) {
										alert("검색 조건을 선택하세요.");
										return false;
									}
									if (!searchForm.find(
											"input[name='keyword']").val()) {
										alert("키워드를 입력하세요.");
										return false;
									}
									searchForm.find("input[name='pageNum']")
											.val("1");
									e.preventDefault();
									searchForm.submit();
								});
					});
</script>