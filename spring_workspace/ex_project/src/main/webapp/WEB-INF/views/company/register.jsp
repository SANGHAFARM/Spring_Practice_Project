<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ include file="../includes/header.jsp"%>

<div class="row">
	<div class="col-lg-12">
		<h1 class="page-header">거래처 등록</h1>
	</div>
</div>

<div class="row">
	<div class="col-lg-12">
		<div class="panel panel-default">
			<div class="panel-heading">새 거래처 정보 입력</div>
			<div class="panel-body">
				<form role="form" action="/company/register" method="post">
					<div class="form-group">
						<label>거래처 코드</label> <input class="form-control"
							name="companyCode" placeholder="예: COMP001" required>
					</div>
					<div class="form-group">
						<label>상호</label> <input class="form-control" name="companyName"
							placeholder="상호명을 입력하세요" required>
					</div>
					<div class="form-group">
						<label>대표자명</label> <input class="form-control" name="ceoName"
							placeholder="대표자명을 입력하세요" required>
					</div>
					<div class="form-group">
						<label>업태</label> <input class="form-control" name="businessType"
							placeholder="예: 서비스업, 제조업">
					</div>
					<button type="submit" class="btn btn-primary">등록</button>
					<button type="reset" class="btn btn-default">취소</button>
				</form>
			</div>
		</div>
	</div>
</div>

<%@ include file="../includes/footer.jsp"%>