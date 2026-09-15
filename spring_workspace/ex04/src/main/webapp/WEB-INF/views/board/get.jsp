<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@include file="../includes/header.jsp"%>
<!-- 공통 상단 헤더 템플릿 포함 -->

<!-- 이미지 클릭 시 원본 이미지를 크게 보여주기 위한 배경 오버레이 레이어 -->
<div class='bigPictureWrapper'>
	<div class='bigPicture'></div>
</div>

<style>
.uploadResult {
	width: 100%;
	background-color: gray;
}

.uploadResult ul {
	display: flex;
	flex-flow: row;
	justify-content: center;
	align-items: center;
}

.uploadResult ul li {
	list-style: none;
	padding: 10px;
	align-content: center;
	text-align: center;
}

.uploadResult ul li img {
	width: 100px;
}

.uploadResult ul li span {
	color: white;
}

.bigPictureWrapper {
	position: absolute;
	display: none;
	justify-content: center;
	align-items: center;
	top: 0%;
	width: 100%;
	height: 100%;
	background-color: gray;
	z-index: 100;
	background: rgba(255, 255, 255, 0.5);
}

.bigPicture {
	position: relative;
	display: flex;
	justify-content: center;
	align-items: center;
}

.bigPicture img {
	width: 600px;
}
</style>

<!-- 1. 게시글 본문 영역 -->
<div class="row">
	<div class="col-lg-12">
		<h1 class='page-header'>Board Read</h1>
	</div>
</div>

<div class="row">
	<div class="col-lg-12">
		<div class="panel panel-default">
			<div class="panel-heading">Board Read Page</div>
			<div class="panel-body">
				<!-- 글 번호 표시 (읽기 전용) -->
				<div class="form-group">
					<label>Bno</label> <input class="form-control" name='bno'
						value='<c:out value="${board.bno }"/>' readonly="readonly">
				</div>

				<!-- 글 제목 표시 (읽기 전용) -->
				<div class="form-group">
					<label>Title</label> <input class="form-control" name='title'
						value='<c:out value="${board.title }"/>' readonly="readonly">
				</div>

				<!-- 글 본문 내용 표시 (읽기 전용) -->
				<div class="form-group">
					<label>Text area</label>
					<textarea class="form-control" rows="3" name='content'
						readonly="readonly"><c:out value="${board.content}" /></textarea>
				</div>

				<!-- 작성자 표시 (읽기 전용) -->
				<div class="form-group">
					<label>Writer</label> <input class="form-control" name='writer'
						value='<c:out value="${board.writer }"/>' readonly="readonly">
				</div>

				<!-- 수정 화면 이동 및 목록 이동 버튼 -->
				<button data-oper='modify' class="btn btn-default">Modify</button>
				<button data-oper='list' class="btn btn-info">List</button>

				<!-- 페이지/검색 정보와 글 번호를 안전하게 넘기기 위한 히든 폼 -->
				<form id='operForm' action="/board/modify" method="get">
					<input type='hidden' id='bno' name='bno'
						value='<c:out value="${board.bno}"/>'> <input
						type='hidden' name='pageNum'
						value='<c:out value="${cri.pageNum}"/>'> <input
						type='hidden' name='amount' value='<c:out value="${cri.amount}"/>'>
					<input type='hidden' name='keyword'
						value='<c:out value="${cri.keyword}"/>'> <input
						type='hidden' name='type' value='<c:out value="${cri.type}"/>'>
				</form>
			</div>
		</div>
	</div>
</div>

<!-- 2. 첨부파일 목록 영역 -->
<div class="row">
	<div class="col-lg-12">
		<div class="panel panel-default">
			<div class="panel-heading">Files</div>
			<div class="panel-body">
				<!-- 첨부파일 섬네일 및 다운로드 링크가 동적으로 출력되는 목록 컨테이너 -->
				<div class='uploadResult'>
					<ul></ul>
				</div>
			</div>
		</div>
	</div>
</div>

<!-- 3. 댓글 목록 화면 영역 -->
<div class='row'>
	<div class="col-lg-12">
		<div class="panel panel-default">
			<div class="panel-heading">
				<i class="fa fa-comments fa-fw"></i> Reply
				<!-- 새 댓글 등록 모달을 띄우는 버튼 -->
				<button id='addReplyBtn' class='btn btn-primary btn-xs pull-right'>New
					Reply</button>
			</div>
			<div class="panel-body">
				<!-- 비동기로 조회된 댓글 리스트가 추가되는 태그 -->
				<ul class="chat"></ul>
			</div>
			<!-- 댓글 페이지 번호 바가 들어가는 영역 -->
			<div class="panel-footer"></div>
		</div>
	</div>
</div>

<%@include file="../includes/footer.jsp"%>
<!-- 공통 하단 푸터 템플릿 포함 -->

<!-- 4. 댓글 등록/수정/삭제용 부트스트랩 모달 창 -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">REPLY MODAL</h4>
			</div>
			<div class="modal-body">
				<div class="form-group">
					<label>Reply</label> <input class="form-control" name='reply'
						value='New Reply!!!!'>
				</div>
				<div class="form-group">
					<label>Replyer</label> <input class="form-control" name='replyer'
						value='replyer'>
				</div>
				<div class="form-group">
					<label>Reply Date</label> <input class="form-control"
						name='replyDate' value=''>
				</div>
			</div>
			<div class="modal-footer">
				<!-- 모달 내 수정, 삭제, 등록, 닫기 버튼 -->
				<button id='modalModBtn' type="button" class="btn btn-warning">Modify</button>
				<button id='modalRemoveBtn' type="button" class="btn btn-danger">Remove</button>
				<button id='modalRegisterBtn' type="button" class="btn btn-primary">Register</button>
				<button id='modalCloseBtn' type="button" class="btn btn-default"
					data-dismiss="modal">Close</button>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript" src="/resources/js/reply.js"></script>
<!-- 댓글 Ajax 모듈 스크립트 로드 -->

<script type="text/javascript">
	$(document)
			.ready(
					function() {

						// [첨부파일 목록 비동기 조회 및 화면 출력 즉시 실행 함수]
						(function() {
							var bno = '<c:out value="${board.bno}"/>';

							// 컨트롤러에서 첨부파일 JSON 데이터를 받아옴
							$
									.getJSON(
											"/board/getAttachList",
											{
												bno : bno
											},
											function(arr) {
												console.log(arr);
												var str = "";

												$(arr)
														.each(
																function(i,
																		attach) {
																	// 이미지 파일이면 섬네일 이미지 출력
																	if (attach.fileType) {
																		var fileCallPath = encodeURIComponent(attach.uploadPath
																				+ "/s_"
																				+ attach.uuid
																				+ "_"
																				+ attach.fileName);

																		str += "<li data-path='" + attach.uploadPath + "' data-uuid='" + attach.uuid + "' data-filename='" + attach.fileName + "' data-type='" + attach.fileType + "' ><div>";
																		str += "<img src='/display?fileName="
																				+ fileCallPath
																				+ "'>";
																		str += "</div></li>";
																	} else {
																		// 일반 파일이면 다운로드용 기본 아이콘 출력
																		str += "<li data-path='" + attach.uploadPath + "' data-uuid='" + attach.uuid + "' data-filename='" + attach.fileName + "' data-type='" + attach.fileType + "' ><div>";
																		str += "<span> "
																				+ attach.fileName
																				+ "</span><br/>";
																		str += "<img src='/resources/img/attach.png'>";
																		str += "</div></li>";
																	}
																});

												$(".uploadResult ul").html(str);
											});
						})();

						// 첨부파일 클릭 이벤트 (이미지는 원본 확대, 일반 파일은 다운로드 처리)
						$(".uploadResult").on(
								"click",
								"li",
								function(e) {
									console.log("view image");
									var liObj = $(this);
									var path = encodeURIComponent(liObj
											.data("path")
											+ "/"
											+ liObj.data("uuid")
											+ "_"
											+ liObj.data("filename"));

									if (liObj.data("type")) {
										showImage(path.replace(
												new RegExp(/\\/g), "/")); // 이미지 확대 함수 호출
									} else {
										self.location = "/download?fileName="
												+ path; // 일반 파일 다운로드 실행
									}
								});

						// 원본 이미지 클릭 시 서서히 축소하며 창 닫기
						$(".bigPictureWrapper").on("click", function(e) {
							$(".bigPicture").animate({
								width : '0%',
								height : '0%'
							}, 1000);
							setTimeout(function() {
								$('.bigPictureWrapper').hide();
							}, 1000);
						});

						// 원본 이미지를 화면 중앙에 확대 출력하는 함수
						function showImage(fileCallPath) {
							$(".bigPictureWrapper").css("display", "flex")
									.show();
							$(".bigPicture").html(
									"<img src='/display?fileName="
											+ fileCallPath + "'>").animate({
								width : '100%',
								height : '100%'
							}, 1000);
						}

						// 게시글 수정/목록 버튼 이벤트 처리
						var operForm = $("#operForm");

						// Modify 클릭 시 수정 페이지(/board/modify)로 폼 전송
						$("button[data-oper='modify']").on(
								"click",
								function(e) {
									operForm.attr("action", "/board/modify")
											.submit();
								});

						// List 클릭 시 글 번호(bno)를 제거하고 목록 페이지(/board/list)로 이동
						$("button[data-oper='list']").on("click", function(e) {
							operForm.find("#bno").remove();
							operForm.attr("action", "/board/list").submit();
						});

						// 댓글 페이징 및 목록 렌더링
						var bnoValue = '<c:out value="${board.bno}"/>';
						var replyUL = $(".chat");

						showList(1); // 페이지 로드 시 첫 페이지 댓글 목록 출력

						// 댓글 목록을 가져와 HTML 태그를 조립하는 함수
						function showList(page) {
							console.log("show list " + page);

							replyService
									.getList(
											{
												bno : bnoValue,
												page : page || 1
											},
											function(replyCnt, list) {
												// page가 -1로 넘어오면 마지막 페이지로 이동 (댓글 추가 시 사용)
												if (page == -1) {
													pageNum = Math
															.ceil(replyCnt / 10.0);
													showList(pageNum);
													return;
												}

												var str = "";
												if (list == null
														|| list.length == 0) {
													replyUL.html("");
													return;
												}

												// 댓글 목록 순회하며 li 태그 동적 생성
												for (var i = 0, len = list.length || 0; i < len; i++) {
													str += "<li class='left clearfix' data-rno='" + list[i].rno + "'>";
													str += "  <div><div class='header'><strong class='primary-font'>["
															+ list[i].rno
															+ "] "
															+ list[i].replyer
															+ "</strong>";
													str += "    <small class='pull-right text-muted'>"
															+ replyService
																	.displayTime(list[i].replyDate)
															+ "</small></div>";
													str += "    <p>"
															+ list[i].reply
															+ "</p></div></li>";
												}

												replyUL.html(str);
												showReplyPage(replyCnt); // 하단 댓글 페이지 번호 생성 함수 호출
											});
						}

						var pageNum = 1;
						var replyPageFooter = $(".panel-footer");

						// 댓글 하단 페이지네이션(1, 2, 3...) 연산 및 화면 렌더링 함수
						function showReplyPage(replyCnt) {
							var endNum = Math.ceil(pageNum / 10.0) * 10;
							var startNum = endNum - 9;
							var prev = startNum != 1;
							var next = false;

							if (endNum * 10 >= replyCnt) {
								endNum = Math.ceil(replyCnt / 10.0);
							}
							if (endNum * 10 < replyCnt) {
								next = true;
							}

							var str = "<ul class='pagination pull-right'>";
							if (prev) {
								str += "<li class='page-item'><a class='page-link' href='"
										+ (startNum - 1)
										+ "'>Previous</a></li>";
							}

							for (var i = startNum; i <= endNum; i++) {
								var active = pageNum == i ? "active" : "";
								str += "<li class='page-item " + active + " '><a class='page-link' href='" + i + "'>"
										+ i + "</a></li>";
							}

							if (next) {
								str += "<li class='page-item'><a class='page-link' href='"
										+ (endNum + 1) + "'>Next</a></li>";
							}
							str += "</ul></div>";
							replyPageFooter.html(str);
						}

						// 댓글 페이지 번호 클릭 시 해당 페이지 목록 다시 로드
						replyPageFooter.on("click", "li a", function(e) {
							e.preventDefault();
							pageNum = $(this).attr("href");
							showList(pageNum);
						});

						// 댓글 등록/수정/삭제 모달 조작 로직
						var modal = $(".modal");
						var modalInputReply = modal.find("input[name='reply']");
						var modalInputReplyer = modal
								.find("input[name='replyer']");
						var modalInputReplyDate = modal
								.find("input[name='replyDate']");

						var modalModBtn = $("#modalModBtn");
						var modalRemoveBtn = $("#modalRemoveBtn");
						var modalRegisterBtn = $("#modalRegisterBtn");

						// 'New Reply' 버튼 클릭 시 입력 모드로 모달 띄우기
						$("#addReplyBtn").on("click", function(e) {
							modal.find("input").val("");
							modalInputReplyDate.closest("div").hide(); // 등록일 숨김
							modal.find("button[id !='modalCloseBtn']").hide(); // 수정/삭제 버튼 숨김
							modalRegisterBtn.show(); // 등록 버튼만 표시
							$(".modal").modal("show");
						});

						// 모달의 'Register' 버튼 클릭 시 댓글 등록 비동기 요청
						modalRegisterBtn.on("click", function(e) {
							var reply = {
								reply : modalInputReply.val(),
								replyer : modalInputReplyer.val(),
								bno : bnoValue
							};

							replyService.add(reply, function(result) {
								alert(result);
								modal.find("input").val("");
								modal.modal("hide");
								showList(-1); // 마지막 페이지로 이동해 새 댓글 확인
							});
						});

						// 모달의 'Remove' 버튼 클릭 시 댓글 삭제 요청
						modalRemoveBtn.on("click", function(e) {
							var rno = modal.data("rno");

							replyService.remove(rno, function(result) {
								alert(result);
								modal.modal("hide");
								showList(pageNum); // 현재 페이지 유지
							});
						});

						// 모달의 'Modify' 버튼 클릭 시 댓글 내용 수정 요청
						modalModBtn.on("click", function(e) {
							var reply = {
								rno : modal.data("rno"),
								reply : modalInputReply.val()
							};

							replyService.update(reply, function(result) {
								alert(result);
								modal.modal("hide");
								showList(pageNum); // 현재 페이지 유지
							});
						});

						// 댓글 특정 항목 클릭 시 상세 내용을 모달에 채워 팝업 (이벤트 위임)
						$(".chat")
								.on(
										"click",
										"li",
										function(e) {
											var rno = $(this).data("rno");

											replyService
													.get(
															rno,
															function(reply) {
																modalInputReply
																		.val(reply.reply);
																modalInputReplyer
																		.val(reply.replyer);
																modalInputReplyDate
																		.val(
																				replyService
																						.displayTime(reply.replyDate))
																		.attr(
																				"readonly",
																				"readonly");
																modal
																		.data(
																				"rno",
																				reply.rno); // 모달에 rno 바인딩

																modal
																		.find(
																				"button[id !='modalCloseBtn']")
																		.hide();
																modalModBtn
																		.show(); // 수정 버튼 노출
																modalRemoveBtn
																		.show(); // 삭제 버튼 노출
																$(".modal")
																		.modal(
																				"show");
															});
										});

					});
</script>