<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>在庫確認画面</title>

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
<link rel="stylesheet" href="css/stock.css">

</head>

<body>

	<form action="stock" method="post">

		<input type="hidden" name="bunki">
		<input type="hidden" name="Message" value="${alertMessage}">



		<div class="inputmain">

			<br>

			<a href="menu.jsp">メニューへ戻る</a>

			<h1>在庫確認画面</h1>

			<br>

			<p class="pn">
				品番 <input type="text" name="productNo" value="${name.productNo}"
					onchange="doProgram('seachkood')">
			</p>

			<p class="pn">
				品名 <input type="text" value="${name.productName}" readonly>
			</p>

			<br>


			<table>
				<tr>
					<th>年月日</th>
					<th>顧客先</th>
					<th>受注番号</th>
					<th>受注数</th>
					<th>仕入先コード</th>
					<th>発注番号</th>
					<th>発注数</th>
					<th>在庫数</th>
				</tr>

				<c:forEach var="listorder" items="${listorder}">

					<tr>
						<td>${listorder.ymd}</td>
						<td>${listorder.customerNo}</td>
						<td>${listorder.poNo}</td>
						<td>${listorder.poQty}</td>
						<td>${listorder.supplierNo}</td>
						<td>${listorder.orderNo}</td>
						<td>${listorder.orderQty}</td>
						<td>${listorder.stockQty}</td>
					</tr>

				</c:forEach>

			</table>

		</div>

	</form>

</body>
</html>


