<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!-- jstlのリンク -->
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>受注一覧</title>

<script>
	function doProgram(hikisu) {
		var form = document.forms[0];
   <!-- alert(hikisu);  -->
		form.bunki.value = hikisu;
		form.submit();
	}

	window.onload = function() {
		var result = "${alertMessage}";
		if (result != '' && "${screenBean.productNo !=''}") {
			alert("${alertMessage}");
		} else {
			if (result == '' && "${screenBean.productNo ==''}") {
				document.getElementById("regist").textContent = "登録";

			} else {
				document.getElementById("regist").textContent = "更新";

			}
		}
	}
</script>

<!-- bootstrapのリンク -->
<link rel="stylesheet" href="bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="bootstrap/css/bootstrap-theme.min.css">


<!-- cssのリンク -->
<link rel="stylesheet" href="css/all.css">
<link rel="stylesheet" href="css/orderList.css">

</head>
<body>

	<form action="OrderListMain" method="post">


		<input type="hidden" name="bunki">
		<input type="hidden" name="Message" value="${alertMessage}">

		<div class="inputmain">

		<a href="menu.jsp">メニューへ戻る</a>

			<h2>受注一覧</h2>

			<p class="pn">

				品番 <input type="text" name="productNo" value="${screenBean.productNo}"
					onChange="doProgram('searchProductNo')">
			</p>

			<p class="pn">
				品名 <input type="text" name="productName" value="${screenBean.productName}" readonly>
			</p>

				<br>
				<br>

		</div>
	</form>


	<table>

		<c:forEach var="orderlist" items="${orderlist}">

<!-- 受注テーブル -->
	  	<tr>
			<th>注文日</th>
			<th>受注番号</th>
			<th>顧客先コード</th>
			<th>注文個数</th>
			<th>納期</th>
			<th>出荷日</th>
		</tr>

			<tr>
				<td>${orderlist.orderDate}</td>
				<td>${orderlist.poNo}</td>
				<td>${orderlist.customerNo}</td>
				<td>${orderlist.poQty}</td>
				<td>${orderlist.deliveryDate}</td>
				<td>${orderlist.shipDate}</td>
			</tr>

		</c:forEach>

	</table>


</body>
</html>


