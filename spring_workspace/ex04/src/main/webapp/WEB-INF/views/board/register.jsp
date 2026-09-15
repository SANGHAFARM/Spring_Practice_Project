<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@include file="../includes/header.jsp"%>
<!-- 공통 상단 헤더 포함 -->

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
}

.uploadResult ul li img {
	width: 100px;
}

.uploadResult ul li span {
	color: white;
	cursor: pointer;
}
</style>

<div class="row">
	<div class="col-lg-12">
		<h1 class="page-header">Board Register</h1>
	</div>
</div>

<div class="row">
	<div class="col-lg-12">
		<div class="panel panel-default">
			<div class="panel-heading">Board Register</div>
			<div class="panel-body">
				<!-- 신규 게시글 등록을 위한 메인 입력 폼 -->
				<form role="form" action="/board/register" method="post">
					<div class="form-group">
						<label>Title</label> <input class="form-control" name='title'>
					</div>
					<div class="form-group">
						<label>Text area</label>
						<textarea class="form-control" rows="3" name='content'></textarea>
					</div>
					<div class="form-group">
						<label>Writer</label> <input class="form-control" name='writer'>
					</div>
					<button type="submit" class="btn btn-default">Submit
						Button</button>
					<button type="reset" class="btn btn-default">Reset Button</button>
				</form>
			</div>
		</div>
	</div>
</div>

<!-- 첨부파일 선택 및 미리보기 패널 -->
<div class="row">
	<div class="col-lg-12">
		<div class="panel panel-default">
			<div class="panel-heading">File Attach</div>
			<div class="panel-body">
				<div class="form-group uploadDiv">
					<input type="file" name='uploadFile' multiple>
					<!-- 여러 개 파일 선택 가능 -->
				</div>
				<div class='uploadResult'>
					<ul></ul>
					<!-- 업로드 완료된 파일 목록 렌더링 -->
				</div>
			</div>
		</div>
	</div>
</div>

<%@include file="../includes/footer.jsp"%>
<!-- 공통 하단 푸터 포함 -->

<script>
	$(document)
			.ready(
					function(e) {

						var formObj = $("form[role='form']");

						// 1. 게시글 등록 버튼 클릭 시 첨부파일 정보를 hidden 태그로 변환 후 폼 전송
						$("button[type='submit']")
								.on(
										"click",
										function(e) {
											e.preventDefault();
											console.log("submit clicked");

											var str = "";

											// uploadResult 내의 li 태그들을 순회하며 BoardVO의 attachList 속성에 맞게 바인딩
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

											formObj.append(str).submit();
										});

						// 2. 파일 확장자 검사(실행파일/압축파일 차단) 및 파일 용량 체크(5MB)
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

						// 3. 파일 선택 시 자동으로 서버에 비동기(AJAX) 업로드
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
													showUploadResult(result); // 업로드 결과 화면 출력
												}
											});
										});

						// 4. 서버로부터 응답받은 파일 메타데이터 목록을 화면에 렌더링
						function showUploadResult(uploadResultArr) {
							if (!uploadResultArr || uploadResultArr.length == 0) {
								return;
							}

							var uploadUL = $(".uploadResult ul");
							var str = "";

							$(uploadResultArr)
									.each(
											function(i, obj) {
												// 이미지 파일일 때 섬네일 표시
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
													// 일반 파일일 때 첨부 아이콘 표시
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

						// 5. 'x' 버튼 클릭 시 서버 로컬 저장소에서 파일 실제 삭제 및 li 제거
						$(".uploadResult").on("click", "button", function(e) {
							console.log("delete file");
							var targetFile = $(this).data("file");
							var type = $(this).data("type");
							var targetLi = $(this).closest("li");

							$.ajax({
								url : '/deleteFile',
								data : {
									fileName : targetFile,
									type : type
								},
								dataType : 'text',
								type : 'POST',
								success : function(result) {
									alert(result);
									targetLi.remove(); // 화면에서 요소 제거
								}
							});
						});

					});
</script>