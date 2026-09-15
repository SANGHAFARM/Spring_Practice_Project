<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@include file="../includes/header.jsp"%>
<!-- 상단 공통 헤더 포함 -->

<!-- 원본 이미지 레이어 -->
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

<div class="row">
	<div class="col-lg-12">
		<h1 class='page-header'>Board Modify</h1>
	</div>
</div>

<div class="row">
	<div class="col-lg-12">
		<div class="panel panel-default">
			<div class="panel-heading">Board Modify Page</div>
			<div class="panel-body">
				<!-- 글 수정 및 삭제 처리를 위한 메인 폼 -->
				<form role="form" action="/board/modify" method="post">
					<!-- 기존 페이징 및 검색 정보 유지용 히든 필드 -->
					<input type='hidden' name='pageNum'
						value='<c:out value="${cri.pageNum}"/>'> <input
						type='hidden' name='amount' value='<c:out value="${cri.amount}"/>'>
					<input type='hidden' name='type'
						value='<c:out value="${cri.type}"/>'> <input type='hidden'
						name='keyword' value='<c:out value="${cri.keyword}"/>'>

					<!-- 수정 불가 항목들 -->
					<div class="form-group">
						<label>Bno</label> <input class="form-control" name='bno'
							value='<c:out value="${board.bno }"/>' readonly="readonly">
					</div>

					<!-- 수정 가능 항목 (제목) -->
					<div class="form-group">
						<label>Title</label> <input class="form-control" name='title'
							value='<c:out value="${board.title }"/>'>
					</div>

					<!-- 수정 가능 항목 (본문 내용) -->
					<div class="form-group">
						<label>Text area</label>
						<textarea class="form-control" rows="3" name='content'><c:out
								value="${board.content}" /></textarea>
					</div>

					<div class="form-group">
						<label>Writer</label> <input class="form-control" name='writer'
							value='<c:out value="${board.writer}"/>' readonly="readonly">
					</div>

					<div class="form-group">
						<label>RegDate</label> <input class="form-control" name='regDate'
							value='<fmt:formatDate pattern="yyyy/MM/dd" value="${board.regdate}" />'
							readonly="readonly">
					</div>

					<div class="form-group">
						<label>Update Date</label> <input class="form-control"
							name='updateDate'
							value='<fmt:formatDate pattern="yyyy/MM/dd" value="${board.updateDate}" />'
							readonly="readonly">
					</div>

					<!-- 액션 분기를 위한 세 종류의 버튼 -->
					<button type="submit" data-oper='modify' class="btn btn-default">Modify</button>
					<button type="submit" data-oper='remove' class="btn btn-danger">Remove</button>
					<button type="submit" data-oper='list' class="btn btn-info">List</button>
				</form>
			</div>
		</div>
	</div>
</div>

<!-- 첨부파일 관리(신규 업로드 및 기존 파일 목록) 패널 -->
<div class="row">
	<div class="col-lg-12">
		<div class="panel panel-default">
			<div class="panel-heading">Files</div>
			<div class="panel-body">
				<div class="form-group uploadDiv">
					<input type="file" name='uploadFile' multiple="multiple">
					<!-- 추가 파일 선택 -->
				</div>
				<div class='uploadResult'>
					<ul></ul>
					<!-- 업로드/기존 첨부파일 목록 렌더링 -->
				</div>
			</div>
		</div>
	</div>
</div>

<%@include file="../includes/footer.jsp"%>
<!-- 하단 공통 푸터 포함 -->

<script type="text/javascript">
	$(document)
			.ready(
					function() {

						// 1. 기존에 등록되어 있던 첨부파일 목록 비동기 로드
						(function() {
							var bno = '<c:out value="${board.bno}"/>';

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
																	// 기존 파일도 삭제 버튼(x)을 함께 렌더링
																	if (attach.fileType) {
																		var fileCallPath = encodeURIComponent(attach.uploadPath
																				+ "/s_"
																				+ attach.uuid
																				+ "_"
																				+ attach.fileName);

																		str += "<li data-path='" + attach.uploadPath + "' data-uuid='" + attach.uuid + "' data-filename='" + attach.fileName + "' data-type='" + attach.fileType + "' ><div>";
																		str += "<span> "
																				+ attach.fileName
																				+ "</span>";
																		str += "<button type='button' data-file=\'" + fileCallPath + "\' data-type='image' class='btn btn-warning btn-circle'><i class='fa fa-times'></i></button><br>";
																		str += "<img src='/display?fileName="
																				+ fileCallPath
																				+ "'>";
																		str += "</div></li>";
																	} else {
																		str += "<li data-path='" + attach.uploadPath + "' data-uuid='" + attach.uuid + "' data-filename='" + attach.fileName + "' data-type='" + attach.fileType + "' ><div>";
																		str += "<span> "
																				+ attach.fileName
																				+ "</span><br/>";
																		str += "<button type='button' data-file=\'" + fileCallPath + "\' data-type='file' class='btn btn-warning btn-circle'><i class='fa fa-times'></i></button><br>";
																		str += "<img src='/resources/img/attach.png'>";
																		str += "</div></li>";
																	}
																});

												$(".uploadResult ul").html(str);
											});
						})();

						// 2. 신규 첨부파일 업로드 유효성 검사 규칙 (특정 확장자 차단 및 5MB 용량 제한)
						var regex = new RegExp("(.*?)\\.(exe|sh|zip|alz)$");
						var maxSize = 5242880;

						function checkExtension(fileName, fileSize) {
							if (fileSize >= maxSize) {
								alert("파일 사이즈 초과");
								return false;
							}
							if (regex.test(fileName)) {
								alert("해당 종류의 파일은 업로드할 수 없습니다.");
								return false;
							}
							return true;
						}

						// 3. 신규 파일 선택 시 즉시 비동기(AJAX) 업로드 실행
						$("input[type='file']")
								.change(
										function(e) {
											var formData = new FormData();
											var inputFile = $("input[name='uploadFile']");
											var files = inputFile[0].files;

											for (var i = 0; i < files.length; i++) {
												if (!checkExtension(
														files[i].name,
														files[i].size)) {
													return false;
												}
												formData.append("uploadFile",
														files[i]);
											}

											$.ajax({
												url : '/uploadAjaxAction',
												processData : false,
												contentType : false,
												data : formData,
												type : 'POST',
												dataType : 'json',
												success : function(result) {
													console.log(result);
													showUploadResult(result); // 업로드 결과 화면 반영
												}
											});
										});

						// 4. 새로 업로드된 첨부파일을 화면 목록에 추가
						function showUploadResult(uploadResultArr) {
							if (!uploadResultArr || uploadResultArr.length == 0) {
								return;
							}

							var uploadUL = $(".uploadResult ul");
							var str = "";

							$(uploadResultArr)
									.each(
											function(i, obj) {
												if (obj.image) {
													var fileCallPath = encodeURIComponent(obj.uploadPath
															+ "/s_"
															+ obj.uuid
															+ "_"
															+ obj.fileName);
													str += "<li data-path='" + obj.uploadPath + "' data-uuid='" + obj.uuid + "' data-filename='" + obj.fileName + "' data-type='" + obj.image + "'>";
													str += "<div><span> "
															+ obj.fileName
															+ "</span>";
													str += "<button type='button' data-file=\'" + fileCallPath + "\' data-type='image' class='btn btn-warning btn-circle'><i class='fa fa-times'></i></button><br>";
													str += "<img src='/display?fileName="
															+ fileCallPath
															+ "'>";
													str += "</div></li>";
												} else {
													var fileCallPath = encodeURIComponent(obj.uploadPath
															+ "/"
															+ obj.uuid
															+ "_"
															+ obj.fileName);
													str += "<li data-path='" + obj.uploadPath + "' data-uuid='" + obj.uuid + "' data-filename='" + obj.fileName + "' data-type='" + obj.image + "'>";
													str += "<div><span> "
															+ obj.fileName
															+ "</span>";
													str += "<button type='button' data-file=\'" + fileCallPath + "\' data-type='file' class='btn btn-warning btn-circle'><i class='fa fa-times'></i></button><br>";
													str += "<img src='/resources/img/attach.png'>";
													str += "</div></li>";
												}
											});

							uploadUL.append(str);
						}

						// 5. 'x' 버튼 클릭 시 화면 목록(li)에서만 삭제 (실제 DB/디스크 반영은 수정 폼 전송 시 처리)
						$(".uploadResult").on("click", "button", function(e) {
							console.log("delete file");
							if (confirm("Remove this file? ")) {
								var targetLi = $(this).closest("li");
								targetLi.remove();
							}
						});

						// 6. 폼 전송 버튼 제어 (수정 / 삭제 / 목록)
						var formObj = $("form");

						$('button')
								.on(
										"click",
										function(e) {
											e.preventDefault();
											var operation = $(this)
													.data("oper");
											console.log(operation);

											// 삭제 버튼 클릭 시 /board/remove 로 액션 전환
											if (operation === 'remove') {
												formObj.attr("action",
														"/board/remove");

												// 목록 버튼 클릭 시 페이징 정보만 챙겨서 GET 방식으로 /board/list 이동
											} else if (operation === 'list') {
												formObj.attr("action",
														"/board/list").attr(
														"method", "get");
												var pageNumTag = $(
														"input[name='pageNum']")
														.clone();
												var amountTag = $(
														"input[name='amount']")
														.clone();
												var keywordTag = $(
														"input[name='keyword']")
														.clone();
												var typeTag = $(
														"input[name='type']")
														.clone();

												formObj.empty(); // 폼 내부 필드를 비우고 페이징 파라미터만 보존
												formObj.append(pageNumTag)
														.append(amountTag)
														.append(keywordTag)
														.append(typeTag);

												// 수정 버튼 클릭 시 남아있는 첨부파일 정보를 hidden 태그로 수집하여 전송
											} else if (operation === 'modify') {
												console.log("submit clicked");
												var str = "";

												$(".uploadResult ul li")
														.each(
																function(i, obj) {
																	var jobj = $(obj);
																	str += "<input type='hidden' name='attachList["
																			+ i
																			+ "].fileName' value='"
																			+ jobj
																					.data("filename")
																			+ "'>";
																	str += "<input type='hidden' name='attachList["
																			+ i
																			+ "].uuid' value='"
																			+ jobj
																					.data("uuid")
																			+ "'>";
																	str += "<input type='hidden' name='attachList["
																			+ i
																			+ "].uploadPath' value='"
																			+ jobj
																					.data("path")
																			+ "'>";
																	str += "<input type='hidden' name='attachList["
																			+ i
																			+ "].fileType' value='"
																			+ jobj
																					.data("type")
																			+ "'>";
																});
												formObj.append(str);
											}

											formObj.submit();
										});

					});
</script>