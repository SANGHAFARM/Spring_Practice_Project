<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<html>
<head>
<title>Home</title>
</head>
<body>
	<!-- 스프링 MVC 기본 제공 시작 뷰 페이지 -->
	<h1>Hello world!</h1>

	<!-- HomeController에서 모델에 담아 넘겨준 현재 서버 시간을 화면에 출력 -->
	<P>The time on the server is ${serverTime}.</P>
</body>
</html>