<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Upload with Ajax</title>
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
	cursor: pointer;
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
</head>
<body>
	<h1>Upload with Ajax</h1>

	<!-- 파일 선택 입력 태그 컨테이너 -->
	<div class='uploadDiv'>
		<input type='file' name='uploadFile' multiple>
	</div>

	<!-- 비동기 업로드 결과 목록 -->
	<div class='uploadResult'>
		<ul></ul>
	</div>

	<!-- 수동 업로드 실행 버튼 -->
	<button id='uploadBtn'>Upload</button>

	<!-- 원본 이미지 확대 팝업용 레이어 -->
	<div class='bigPictureWrapper'>
		<div class='bigPicture'></div>
	</div>

	<script src="https://code.jquery.com/jquery-3.3.1.min.js"
		integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8="
		crossorigin="anonymous"></script>

	<script>
		// 업로드 파일 확장자 필터링 및 5MB 용량 제한
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

		// 이미지 섬네일 클릭 시 원본 이미지를 부드럽게 확대하는 함수
		function showImage(fileCallPath) {
			$(".bigPictureWrapper").css("display", "flex").show();
			$(".bigPicture").html("<img src='/display?fileName=" + encodeURI(fileCallPath) + "'>")
					.animate({width : '100%', height : '100%'}, 1000);
		}

		$(document).ready(function() {

			// 파일 선택 input 태그를 업로드 후 깨끗하게 비우기 위한 복제(Clone) 객체
			var cloneObj = $(".uploadDiv").clone();
			var uploadResult = $(".uploadResult ul");

			// 서버로부터 반환받은 업로드 결과 배열을 화면에 렌더링
			function showUploadedFile(uploadResultArr) {
				var str = "";

				$(uploadResultArr).each(function(i, obj) {
					// 일반 파일이면 다운로드 링크와 기본 아이콘 추가
					if (!obj.image) {
						var fileCallPath = encodeURIComponent(obj.uploadPath + "/" + obj.uuid + "_" + obj.fileName);
						str += "<li><div><a href='/download?fileName=" + fileCallPath + "'>"
								+ "<img src='/resources/img/attach.png'>" + obj.fileName + "</a>"
								+ "<span data-file=\'" + fileCallPath + "\' data-type='file'> x </span>"
								+ "</div></li>";
					// 이미지 파일이면 원본 확대 링크와 섬네일 이미지 추가
					} else {
						var fileCallPath = encodeURIComponent(obj.uploadPath + "/s_" + obj.uuid + "_" + obj.fileName);
						var originPath = (obj.uploadPath + "\\" + obj.uuid + "_" + obj.fileName).replace(new RegExp(/\\/g), "/");

						str += "<li><a href=\"javascript:showImage(\'" + originPath + "\')\">"
								+ "<img src='/display?fileName=" + fileCallPath + "'></a>"
								+ "<span data-file=\'" + fileCallPath + "\' data-type='image'> x </span>"
								+ "</li>";
					}
				});

				uploadResult.append(str);
			}

			// 파일 목록의 'x' 버튼 클릭 시 비동기로 서버 파일 삭제 요청
			$(".uploadResult").on("click", "span", function(e) {
				var targetFile = $(this).data("file");
				var type = $(this).data("type");
				var targetLi = $(this).closest("li");

				$.ajax({
					url : '/deleteFile',
					data : {fileName : targetFile, type : type},
					dataType : 'text',
					type : 'POST',
					success : function(result) {
						alert(result);
						targetLi.remove(); // 삭제 성공 시 화면 li 제거
					}
				});
			});

			// 확대된 이미지 클릭 시 원본 창 닫기
			$(".bigPictureWrapper").on("click", function(e) {
				$(".bigPicture").animate({width : '0%', height : '0%'}, 1000);
				setTimeout(() => { $(this).hide(); }, 1000);
			});

			// 'Upload' 버튼 클릭 시 FormData 객체를 생성해 파일 전송
			$("#uploadBtn").on("click", function(e) {
				var formData = new FormData();
				var inputFile = $("input[name='uploadFile']");
				var files = inputFile[0].files;

				for (var i = 0; i < files.length; i++) {
					if (!checkExtension(files[i].name, files[i].size)) {
						return false;
					}
					formData.append("uploadFile", files[i]);
				}

				$.ajax({
					url : '/uploadAjaxAction',
					processData : false, // FormData 전송을 위해 자동 쿼리스트링 변환 끄기
					contentType : false, // multipart/form-data 헤더 자동 설정 유도
					data : formData,
					type : 'POST',
					dataType : 'json',
					success : function(result) {
						console.log(result);
						showUploadedFile(result); // 업로드된 결과 목록 렌더링
						$(".uploadDiv").html(cloneObj.html()); // input 파일 선택창 초기화
					}
				});
			});

		});
	</script>
</body>
</html>