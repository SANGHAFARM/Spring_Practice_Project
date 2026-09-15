<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Upload Form</title>
</head>
<body>

	<!-- 전통적인 HTML form 기반 멀티파트 파일 업로드 폼 -->
	<form action="uploadFormAction" method="post"
		enctype="multipart/form-data">
		<!-- 여러 파일 선택을 지원하는 멀티파트 인풋 필드 -->
		<input type='file' name='uploadFile' multiple>

		<!-- 서버로 파일 전송 실행 버튼 -->
		<button>Submit</button>
	</form>

</body>
</html>