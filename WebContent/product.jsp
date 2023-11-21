<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>品番マスタ</title>

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
<link rel="stylesheet" href="css/product.css">

</head>
<body>

	<form action="product" method="post">
		<input type="hidden" name="bunki"> <input type="hidden"
			name="flag" value="${flag}"> <input type="hidden"
			name="Message" value="${alertMessage}">
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

			<h1>品番マスタ</h1>

			<table>

				<tr>
					<td class=name>品　　　　番</td>
					<td><input type="text" name="productNo" id="productNo"
						value="${ProductMaster.productNo}" placeholder="登録の際には入力不要。"
						onchange="doProgram('searchProductNo')"></td>
				</tr>

				<tr>
					<td class=name>品　　　　名</td>
					<td><input type="text" name="productName"
						value="${ProductMaster.productName}"></td>
				</tr>

				<tr>
					<td class=name>仕入先コード</td>
					<td><input type="text" name="supplierNo"
						value="${ProductMaster.supplierNo}"
						onchange="doProgram('searchSupplierNo')"></td>
				</tr>


				<%-- 仕入先マスタテーブルの会社名--%>
				<tr>
					<td class=name>仕 入　先 名</td>
					<td><input type="text" name="supplierName"
						value="${ProductMaster.supplierName}" readonly></td>
				</tr>

				<tr>
					<td class=name>仕 入　単 価</td>
					<td><input type="number" step="0.01" name="unitprice"
						value="${ProductMaster.unitprice}"></td>
				</tr>

				<tr>
					<td class=name>売　　　　価</td>
					<td><input type="number" step="0.01" name="sellingprice"
						value="${ProductMaster.sellingprice}"></td>
				</tr>

				<tr>
					<td class=name>リードタイム</td>
					<td><input type="number" name="leadtime"
						value="${ProductMaster.leadtime}"></td>
				</tr>

				<tr>
					<td class=name>購 買 ロット</td>
					<td><input type="number" name="lot"
						value="${ProductMaster.lot}"></td>
				</tr>

				<tr>
					<td class=name>在 庫　ロ ケ</td>
					<td><input type="text" name="location"
						value="${ProductMaster.location}"></td>
				</tr>

			</table>

			<div class="form-group">
				<label for="message">-備考-</label>
				<textarea name="etc" id="etc" rows="5" cols="10"
					class="form-control"> ${ProductMaster.etc}</textarea>
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



