<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
</div>
<!-- /#page-wrapper 끝 -->

</div>
<!-- /#wrapper 끝 -->

<!-- 부트스트랩 코어 자바스크립트 -->
<script src="/resources/vendor/bootstrap/js/bootstrap.min.js"></script>

<!-- 사이드바 메뉴 슬라이드 플러그인 -->
<script src="/resources/vendor/metisMenu/metisMenu.min.js"></script>

<!-- 데이터테이블 플러그인 스크립트 -->
<script src="/resources/vendor/datatables/js/jquery.dataTables.min.js"></script>
<script
	src="/resources/vendor/datatables-plugins/dataTables.bootstrap.min.js"></script>
<script
	src="/resources/vendor/datatables-responsive/dataTables.responsive.js"></script>

<!-- SB Admin 2 메인 테마 제어 스크립트 -->
<script src="/resources/dist/js/sb-admin-2.js"></script>

<!-- 테이블 반응형 플러그인 초기화 및 사이드바 초기 접힘 상태 설정 -->
<script>
	$(document).ready(
			function() {
				$('#dataTables-example').DataTable({
					responsive : true
				});
				$(".sidebar-nav").attr("class",
						"sidebar-nav navbar-collapse collapse").attr(
						"aria-expanded", 'false').attr("style", "height:1px");
			});
</script>

</body>
</html>