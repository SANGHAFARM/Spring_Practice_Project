<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@include file="../includes/header.jsp"%>
<!-- 공통 상단 헤더 포함 -->

<div class="row">
	<div class="col-lg-12">
		<h1 class="page-header">Tables</h1>
	</div>
</div>

<div class="row">
	<div class="col-lg-12">
		<div class="panel panel-default">
			<div class="panel-heading">
				Board List Page
				<!-- 글 등록 페이지로 이동하는 버튼 -->
				<button id='regBtn' type="button" class="btn btn-xs pull-right">Register
					New Board</button>
			</div>
			<div class="panel-body">
				<!-- 게시글 목록 테이블 -->
				<table width="100%"
					class="table table-striped table-bordered table-hover">
					<thead>
						<tr>
							<th>#번호</th>
							<th>제목</th>
							<th>작성자</th>
							<th>작성일</th>
							<th>수정일</th>
						</tr>
					</thead>

					<!-- 모델에 담겨 넘어온 list 데이터를 반복 출력 -->
					<c:forEach items="${list}" var="board">
						<tr>
							<td><c:out value="${board.bno}" /></td>
							<td>
								<!-- 제목 클릭 시 해당 글 상세 조회로 이동 링크 --> <a class='move'
								href='<c:out value="${board.bno}"/>'> <c:out
										value="${board.title}" /> <b>[ <c:out
											value="${board.replyCnt}" /> ]
								</b> <!-- 댓글 수 표시 -->
							</a>
							</td>
							<td><c:out value="${board.writer}" /></td>
							<td><fmt:formatDate pattern="yyyy-MM-dd"
									value="${board.regdate}" /></td>
							<td><fmt:formatDate pattern="yyyy-MM-dd"
									value="${board.updateDate}" /></td>
						</tr>
					</c:forEach>
				</table>

				<!-- 검색 조건 선택 및 키워드 입력 폼 -->
				<div class='row'>
					<div class="col-lg-12">
						<form id='searchForm' action="/board/list" method='get'>
							<select name='type'>
								<option value=""
									<c:out value="${pageMaker.cri.type == null?'selected':''}"/>>--</option>
								<option value="T"
									<c:out value="${pageMaker.cri.type eq 'T'?'selected':''}"/>>제목</option>
								<option value="C"
									<c:out value="${pageMaker.cri.type eq 'C'?'selected':''}"/>>내용</option>
								<option value="W"
									<c:out value="${pageMaker.cri.type eq 'W'?'selected':''}"/>>작성자</option>
								<option value="TC"
									<c:out value="${pageMaker.cri.type eq 'TC'?'selected':''}"/>>제목
									or 내용</option>
								<option value="TW"
									<c:out value="${pageMaker.cri.type eq 'TW'?'selected':''}"/>>제목
									or 작성자</option>
								<option value="TWC"
									<c:out value="${pageMaker.cri.type eq 'TWC'?'selected':''}"/>>제목
									or 내용 or 작성자</option>
							</select> <input type='text' name='keyword'
								value='<c:out value="${pageMaker.cri.keyword}"/>' /> <input
								type='hidden' name='pageNum'
								value='<c:out value="${pageMaker.cri.pageNum}"/>' /> <input
								type='hidden' name='amount'
								value='<c:out value="${pageMaker.cri.amount}"/>' />
							<button class='btn btn-default'>Search</button>
						</form>
					</div>
				</div>

				<!-- 하단 페이지 번호(페이징) 바 -->
				<div class='pull-right'>
					<ul class="pagination">
						<!-- 이전 페이지 링크 -->
						<c:if test="${pageMaker.prev}">
							<li class="paginate_button previous"><a
								href="${pageMaker.startPage -1}">Previous</a></li>
						</c:if>

						<!-- 페이지 번호 목록 반복 출력 -->
						<c:forEach var="num" begin="${pageMaker.startPage}"
							end="${pageMaker.endPage}">
							<li
								class="paginate_button ${pageMaker.cri.pageNum == num ? 'active' : ''}">
								<a href="${num}">${num}</a>
							</li>
						</c:forEach>

						<!-- 다음 페이지 링크 -->
						<c:if test="${pageMaker.next}">
							<li class="paginate_button next"><a
								href="${pageMaker.endPage +1 }">Next</a></li>
						</c:if>
					</ul>
				</div>

			</div>

			<!-- 페이징 링크 클릭 및 글 조회 이동 시 기준 데이터를 안전하게 넘겨주는 공통 폼 -->
			<form id='actionForm' action="/board/list" method='get'>
				<input type='hidden' name='pageNum' value='${pageMaker.cri.pageNum}'>
				<input type='hidden' name='amount' value='${pageMaker.cri.amount}'>
				<input type='hidden' name='type'
					value='<c:out value="${ pageMaker.cri.type }"/>'> <input
					type='hidden' name='keyword'
					value='<c:out value="${ pageMaker.cri.keyword }"/>'>
			</form>

			<!-- 등록/수정/삭제 후 일회성 알림을 띄우는 모달 창 -->
			<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
				aria-labelledby="myModalLabel" aria-hidden="true">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal"
								aria-hidden="true">&times;</button>
							<h4 class="modal-title" id="myModalLabel">Modal title</h4>
						</div>
						<div class="modal-body">처리가 완료되었습니다.</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-default"
								data-dismiss="modal">Close</button>
						</div>
					</div>
				</div>
			</div>

		</div>
	</div>
</div>

<%@include file="../includes/footer.jsp"%>
<!-- 공통 하단 푸터 포함 -->

<script type="text/javascript">
	$(document)
			.ready(
					function() {

						var result = '<c:out value="${result}"/>'; // 컨트롤러에서 전송된 FlashAttribute 결과값

						checkModal(result); // 모달 출력 함수 호출

						history.replaceState({}, null, null); // 뒤로가기 시 모달이 다시 뜨지 않도록 브라우저 히스토리 초기화

						// 작업 성공 메시지를 모달에 세팅하고 보여주는 함수
						function checkModal(result) {
							if (result === '' || history.state) {
								return;
							}
							if (parseInt(result) > 0) {
								$(".modal-body").html(
										"게시글 " + parseInt(result)
												+ " 번이 등록되었습니다.");
							}
							$("#myModal").modal("show");
						}

						// 글 등록 버튼 클릭 시 등록 페이지 이동
						$("#regBtn").on("click", function() {
							self.location = "/board/register";
						});

						var actionForm = $("#actionForm");

						// 페이지 번호 클릭 이벤트 (actionForm의 pageNum 변경 후 submit)
						$(".paginate_button a").on(
								"click",
								function(e) {
									e.preventDefault();
									actionForm.find("input[name='bno']")
											.remove(); // 혹시 남아있을 수 있는 bno 제거
									actionForm.attr("action", "/board/list"); // 이동 URL을 목록으로 설정
									actionForm.find("input[name='pageNum']")
											.val($(this).attr("href"));
									actionForm.submit();
								});

						// 게시글 제목(.move) 클릭 시 글 번호(bno)를 붙여서 /board/get으로 폼 전송
						$(".move")
								.on(
										"click",
										function(e) {
											e.preventDefault();
											actionForm
													.find("input[name='bno']")
													.remove();
											actionForm
													.append("<input type='hidden' name='bno' value='"
															+ $(this).attr(
																	"href")
															+ "'>");
											actionForm.attr("action",
													"/board/get");
											actionForm.submit();
										});

						// 검색 버튼 클릭 시 유효성 검사 후 1페이지부터 검색 실행
						var searchForm = $("#searchForm");

						$("#searchForm button").on(
								"click",
								function(e) {
									if (!searchForm.find("option:selected")
											.val()) {
										alert("검색종류를 선택하세요");
										return false;
									}
									if (!searchForm.find(
											"input[name='keyword']").val()) {
										alert("키워드를 입력하세요");
										return false;
									}
									searchForm.find("input[name='pageNum']")
											.val("1"); // 검색 시 1페이지로 리셋
									e.preventDefault();
									searchForm.submit();
								});
					});
</script>