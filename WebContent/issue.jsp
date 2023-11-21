<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!--jstlのリンク -->
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>出荷画面</title>


<link rel="stylesheet" href="bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="bootstrap/css/bootstrap-theme.min.css">

<link rel="stylesheet" href="css/all.css">
<link rel="stylesheet" href="css/issue.css">

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
		var input = "${issuebean.poNo}";
		if (input == "") {
			form.regist.disabled = true;
		} else {
			form.regist.disabled = false;
		}
	}
</script>
</head>
<body>

	<form action="issue" method="post">
		<input type="hidden" name="doAct">

		<div class=dai>

			<br>

			<a href="menu.jsp">メニューへ戻る</a>

			<h1>出荷画面</h1>


			<table>

				<tr>
					<td class=name>受 注 番 号</td>
					<td><input type="text" name="poNo" class="fo" placeholder="入力してください。" required
					value="${issuebean.poNo}" onchange="doProgram('search')"></td>
				</tr>

				<tr>
					<td class=name>受　注　日</td>
					<td><input type="text" name="orderDte"
						value="${issuebean.orderDte}" readonly></td>
				</tr>

				<tr>
					<td class=name>顧客コード</td>
					<td><input type="text" name="customerNo"
						value="${issuebean.customerNo}" readonly></td>
				</tr>

				<tr>
					<td class=name>顧　客　名</td>
					<td><input type="text" name="customerName"
						value="${issuebean.customerName}" readonly></td>
				</tr>

				<tr>
					<td class=name>品　　　番</td>
					<td><input type="text" name="productNo"
						value="${issuebean.productNo}" readonly></td>
				</tr>

				<tr>
					<td class=name>品　　　名</td>
					<td><input type="text" name="productName"
						value="${issuebean.productName}" readonly></td>
				</tr>

				<tr>
					<td class=name>数　　　量</td>
					<td><input type="text" name="poQty" value="${issuebean.poQty}"
						readonly></td>
				</tr>

				<tr>
					<td class=name>出　荷　日</td>
					<td><input type="text" name="shipDate"
						value="${issuebean.shipDate}" readonly></td>
				</tr>

			</table>

			<br>

			<p>
				<button type="button" onClick="doProgram('regist')" id="regist"
					disabled>確定</button>
				<button type="button" onClick="doProgram('cancel')">キャンセル</button>
			</p>

			<br>

		</div>
	</form>

</body>
</html>


