<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>仕入先マスタ</title>

<script>
	function doProgram(hikisu) {
		var form = document.forms[0];
		form.bunki.value = hikisu;

		if (hikisu == 'register') {
			var result = window.confirm("本当に登録してもよろしいですか？")
			if (!result) {
				return;
			}
		}
		if (hikisu == 'update') {
			var result = window.confirm("本当に更新してもよろしいですか？")
			if (!result) {
				return;
			}
		}
		if (hikisu == 'delete') {
			var result = window.confirm("本当に削除してもよろしいですか？")
			if (!result) {
				return;
			}
		}
		form.submit();
	}

	window.onload = function() {
		var result = "${alertMessage}";
		if (result !== "") {
			alert("${alertMessage}");
		}
	}
</script>

<link rel="stylesheet" href="bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="bootstrap/css/bootstrap-theme.min.css">

<link rel="stylesheet" href="css/all.css">
<link rel="stylesheet" href="css/supplier.css">


</head>

<body>
	<form action="supplier" method="post">
		<input type="hidden" name="bunki">
		<input type="hidden" name="Message" value="${alertMessage}">
 		<input type="hidden" name="state" id="stat" value="${state}">

 		<c:choose>
			<c:when test="${empty state}"><%request.setAttribute("state","新規登録"); %></c:when>
			<c:otherwise></c:otherwise>
		</c:choose>

		<div class=dai>

			<br>

			<table class="top">
			<tr>
				<td class="state">状態 ：${state}</td>
				<td class="menu"><a href="menu.jsp">メニューへ戻る</a></td>
			</tr>
			</table>

			<h1>仕入先マスタ</h1>

			<table>

				<tr>
					<td class=name>仕入先コード</td>
					<td><input type="text" name="supplierNo"
						value="${SupplierMaster.supplierNo}"
						placeholder="登録の際には入力不要。"
						onchange="doProgram('searchSupplierNo')"></td>
				</tr>

				<tr>
					<td class=name>会　 社　 名</td>
					<td><input type="text" name="supplierName"
						value="${SupplierMaster.supplierName}"></td>
				</tr>

				<tr>
					<td class=name>支　 店　 名</td>
					<td><input type="text" name="branchName"
						value="${SupplierMaster.branchName}"></td>
				</tr>

				<tr>
					<td class=name>郵 便　番 号</td>
					<td><input type="text" name="zipNo"
						value="${SupplierMaster.zipNo}"></td>
				</tr>

				<tr>
					<td class=name>住　 所　 1</td>
					<td><input type="text" name="address1"
						value="${SupplierMaster.address1}"></td>
				</tr>

				<tr>
					<td class=name>住　 所　 2</td>
					<td><input type="text" name="address2"
						value="${SupplierMaster.address2}"></td>
				</tr>

				<tr>
					<td class=name>住　 所　 3</td>
					<td><input type="text" name="address3"
						value="${SupplierMaster.address3}"></td>
				</tr>

				<tr>
					<td class=name>電 話　番 号</td>
					<td><input type="text" name="tel"
						value="${SupplierMaster.tel}"></td>
				</tr>

				<tr>
					<td class=name>ＦＡＸ 番 号</td>
					<td><input type="text" name="fax"
						value="${SupplierMaster.fax}"></td>
				</tr>

				<tr>
					<td class=name>担　 当　 者</td>
					<td><input type="text" name="manager"
						value="${SupplierMaster.manager}"></td>
				</tr>

			</table>


			<div class="form-group">
				<label for="message">-備考-</label>
				<textarea name="etc" id="etc" rows="5" cols="10"
					class="form-control" ${SupplierMaster.etc}>  </textarea>
			</div>

			<br>

			<p>
				<button type="button" class="" onClick="doProgram('register')"
					<c:if test="${flag==1}">disabled="disabled"</c:if>>登録</button>
				<button type="button" class="" onClick="doProgram('update')"
					<c:if test="${flag!=1}">disabled="disabled"</c:if>>更新</button>
				<button type="button" class="" onClick="doProgram('delete')"
					<c:if test="${flag!=1}">disabled="disabled"</c:if>>削除</button>
				<button type="button" class="" onClick="doProgram('clear')">クリア</button>
			</p>

			<br>

		</div>
	</form>


</body>
</html>



