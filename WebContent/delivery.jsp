<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!--jstlのリンク -->
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>納入画面</title>


<link rel="stylesheet" href="bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="bootstrap/css/bootstrap-theme.min.css">

<link rel="stylesheet" href="css/all.css">
<link rel="stylesheet" href="css/delivery.css">

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
		var input1 = "${delibean.orderNo}";
		if (input1 == "") {
			form.regist.disabled = true;
		} else {
			form.regist.disabled = false;
		}
	}
</script>
</head>
<body>

	<form action="delivery" method="post">
		<input type="hidden" name="doAct">

		<div class=dai>

			<br>

			<a href="menu.jsp">メニューへ戻る</a>

			<h1>納入画面</h1>

			<table>

				<tr>
					<td class=name>注 文 番 号</td>
					<td><input type="text" name="orderNo"
						 class="fo" placeholder="入力してください。" required
						value="${delibean.orderNo}" onchange="doProgram('search')"></td>
				</tr>

				<tr>
					<td class=name>発　注　日</td>
					<td><input type="text" name="registdate"
						value="${delibean.registdate}" readonly></td>
				</tr>

				<tr>
					<td class=name>品　　　番</td>
					<td><input type="text" name="productNo"
						value="${delibean.productNo}" readonly></td>
				</tr>

				<tr>
					<td class=name>品　　　名</td>
					<td><input type="text" name="productName"
						value="${delibean.productName}" readonly></td>
				</tr>

				<tr>
					<td class=name>注 文 数 量</td>
					<td><input type="text" name="orderQty"
						value="${delibean.orderQty}" readonly></td>
				</tr>

				<tr>
					<td class=name>納 入 数 量</td>
					<td><input type="text" name="dueQty"
						 class="fo" placeholder="入力してください。" required
						value="${delibean.dueQty}" onchange="doProgram('check')"></td>
				</tr>

			</table>

			<br>

			<p>
				<button type="button" name="regist" id="regist"
					onClick="doProgram('regist')" disabled>確定</button>

				<button type="button" onClick="doProgram('cancel')">キャンセル</button>
			</p>

			<br>
		</div>
	</form>

</body>
</html>


