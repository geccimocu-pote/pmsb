<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>所要量計算画面</title>

<script>
	function doProgram(da) {
		var form = document.forms[0];
		form.bunki.value = da;
		form.submit();
	}
	window.onload = function() {
		var result = "${alertMessage}";
		if (result != "") {
			alert("${alertMessage}");
		}
	}
</script>

<link rel="stylesheet" href="bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="bootstrap/css/bootstrap-theme.min.css">

<link rel="stylesheet" href="css/all.css">
<link rel="stylesheet" href="css/calc.css">

</head>
<body>

	<form action="calc" method="post">


		<input type="hidden" name="bunki">
		<input type="hidden" name="Message" value="${alertMessage}">

		<div class=dai>

			<br>

			<a href="menu.jsp">メニューへ戻る</a>

			<h2>所要量計算画面</h2>

			<table class="not_poi">
				<tr>
					<td>
						<button type="button" id="purhase" class="mini"
							onClick="doProgram('calc')">所要量計算開始</button>
					</td>

					<td>
						<button type="button" id="clear" class="mini"
							onClick="doProgram('cancel')">キャンセル</button>
					</td>

					<td>
						<button type="button" id="insert" class="mini"
							onClick="doProgram('insert')">発注</button>
					</td>
				</tr>
			</table>

			<br>

			<table>

				<caption>発注品一覧</caption>

				<tr>
					<th>仕入先コード</th>
					<th>品番</th>
					<th>発注数量</th>
					<th>仕入納期</th>
				</tr>

				<c:forEach var="listorder" items="${listorder}">

					<tr>
						<td>${listorder.supplierNo}</td>
						<td>${listorder.productNo}</td>
						<td>${listorder.orderQty}</td>
						<td>${listorder.ymd}</td>
					</tr>

				</c:forEach>

			</table>

			<br>

		</div>
	</form>

</body>
</html>


