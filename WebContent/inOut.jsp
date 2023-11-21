<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>入出庫画面</title>

<link rel="stylesheet" href="bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="bootstrap/css/bootstrap-theme.min.css">

<link rel="stylesheet" href="css/all.css">
<link rel="stylesheet" href="css/inOut.css">

<script type="text/javascript">
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
		var input1 = "${inoutbean.productNo}";
		if (input1 == "") {
			form.regist.disabled = true;
		} else {
			form.regist.disabled = false;
		}
	}
</script>
</head>

<body>
	<div class=dai>

		<br>

		<a href="menu.jsp">メニューへ戻る</a>


		<h1>入出庫画面</h1>

		<form action="inout" method="post">
			<input type="hidden" name="doAct">

			<table>

				<tr>
					<td class=name>品番</td>
					<td><input type="text" name="productNo" class="fo" placeholder="入力してください。" required
						value="${inoutbean.productNo}" onchange="doProgram('searchProduct')"></td>
				</tr>

				<tr>
					<td class=name>品名</td>
					<td><input type="text" name="productName"
						value="${inoutbean.productName}" readonly></td>
				</tr>


				<tr>
					<td></td>
					<td><input class="l" type="radio" name="nyukoSyuko"
						value="nyuko"
						<c:if test="${inoutbean.nyukoSyuko == 'nyuko'}">checked</c:if>>入庫

						<input class="r" type="radio" name="nyukoSyuko" value="syuko"
						<c:if test="${inoutbean.nyukoSyuko == 'syuko'}">checked</c:if>>出庫
					</td>
				</tr>


				<!-- 81行目 inputの中にstyleあり  cssのinput.r

			<tr>
			<td></td>
			<td>	<input class="l" type="radio" name="nyukoSyuko" value="nyuko"
							<c:if test="${inoutbean.nyukoSyuko == 'nyuko'}">checked</c:if>>入庫
				<input style="text-align:right; margin-left: 70px;" class="r"  type="radio" name="nyukoSyuko" value="syuko"
							<c:if test="${inoutbean.nyukoSyuko == 'syuko'}">checked</c:if>>出庫
			</td>
			</tr>

 -->


				<tr>
					<td class=name>数量</td>
					<td><input type="text" name="nyukoQty_or_syukoQty"
						class="fo" placeholder="入力してください。" required
						value="${inoutbean.nyukoQty_or_syukoQty}"
						onchange="doProgram('checkQty')"></td>
				</tr>

				<tr>
					<td class=name>理由</td>
					<td><input type="text" name="reason"
						class="fo" placeholder="入力してください。" required
						value="${inoutbean.reason}"></td>
				</tr>

			</table>

			<br>

			<p>
				<button type="button" name="regist" id="regist"
					onClick="doProgram('regist')" disabled>確定</button>

				<button type="button" onClick="doProgram('cancel')">キャンセル</button>
			</p>

			<br>

		</form>

	</div>
</body>
</html>

