<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>受注画面</title>

<script>
	function doProgram(da) {
		var form = document.forms[0];
		form.doAct.value = da;
		form.submit();
	}
	window.onload = function() {
		var result = "${alertMessage}";
		if (result != "") {
			alert("${alertMessage}");
		}
		var form = document.forms[0];
		var input1 = "${orderbean.customerNo}";
		var input2 = "${orderbean.productNo}";
		if (input1 == "" || input2 == "") {
			form.regist.disabled = true;
		} else {
			form.regist.disabled = false;
		}
	}
</script>

<link rel="stylesheet" href="bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="bootstrap/css/bootstrap-theme.min.css">

<link rel="stylesheet" href="css/all.css">
<link rel="stylesheet" href="css/order.css">

</head>

<body>

	<form action="order" method="post">
		<input type="hidden" name="doAct">
		<input type="hidden" name="leadtime" value="${leadtime}">

		<div class=dai>

			<br>

			<a href="menu.jsp">メニューへ戻る</a>

			<h1>受注画面</h1>


			<table>

				<tr>
					<td class="name">顧客コード</td>
					<td><input type="text" name="customerNo" class="fo" placeholder="入力してください。" required
						value="${orderbean.customerNo}"
						onchange="doProgram('searchCustomer')" ></td>
				</tr>

				<tr>
					<td class="name">顧　客　名</td>
					<td><input type="text" name="customerName"
						value="${orderbean.customerName}" readonly></td>
				</tr>

				<tr>
					<td class="name">品　　　番</td>
					<td><input type="text" name="productNo" class="fo" placeholder="入力してください。" required
						value="${orderbean.productNo}"
						onchange="doProgram('searchProduct')"></td>
				</tr>

				<tr>
					<td class="name">品　　　名</td>
					<td><input type="text" name="productName"
						value="${orderbean.productName}" readonly></td>
				</tr>

				<tr>
					<td class="name">納　　　期</td>
					<td><input type="date" name="deliveryDate"
						value="${orderbean.deliveryDate}"
						onchange="doProgram('checkDate')"></td>
				</tr>

				<tr>
					<td class="name">数　　　量</td>
					<td><input type="text" name="poQty" class="fo" placeholder="入力してください。" required
					value="${orderbean.poQty}" onchange="doProgram('checkQty')"></td>
				</tr>

			</table>


			<div class="form-group">
				<label for="message">-備考-</label>
				<textarea name="etc" id="message" rows="5" cols="10" class="form-control">
				${orderbean.etc}</textarea>
			</div>

			<br>

			<p>
				<button type="button" name="regist" id="regist"
					onClick="doProgram('regist')" disabled>登録</button>
				<button type="button" onClick="doProgram('cancel')">キャンセル</button>
			</p>

		<br>

		</div>
	</form>

</body>
</html>


