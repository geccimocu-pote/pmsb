<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>顧客先マスタ</title>

<script>
	function doProgram(hikisu) {
		var form = document.forms[0];

		form.bunki.value = hikisu;
		form.submit();
	}
	window.onload = function() {
		var result = "${alertMessage}";
		if (result != '' && "${client.customerNo != '' }") {
			alert("${alertMessage}");
		} else {
			if (result == '' && "${client.customerNo == ''} ") {
				document.getElementById("regist").textContent = "登録";
			} else {
				document.getElementById("regist").textContent = "更新";
			}
		}
	}
</script>

<link rel="stylesheet" href="bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="bootstrap/css/bootstrap-theme.min.css">

<link rel="stylesheet" href="css/all.css">
<link rel="stylesheet" href="css/client.css">

</head>
<body>
	<form action="clientMain" method="post">

		<input type="hidden" name="bunki">
		<input type="hidden" name="Message" value="${alertMessage}">
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

			<h1>顧客先マスタ</h1>

			<table>

				<tr>
					<td class=name>顧客コード</td>
					<td><input type="text" name="customerNo"
						value="${client.customerNo}"
						placeholder="登録の際には入力不要。"
						onchange="doProgram('searchId')"></td>
				</tr>

				<tr>
					<td class=name>会　社　名</td>
					<td><input type="text" name="customerName"
						value="${client.customerName}"></td>
				</tr>

				<tr>
					<td class=name>支　店　名</td>
					<td><input type="text" name="branchName"
						value="${client.branchName}"></td>
				</tr>

				<tr>
					<td class=name>郵 便　番 号</td>
					<td><input type="text" name="zipNo" value="${client.zipNo}"></td>
				</tr>

				<tr>
					<td class=name>住　 所　 1</td>
					<td><input type="text" name="address1"
						value="${client.address1}"></td>
				</tr>

				<tr>
					<td class=name>住　 所　 2</td>
					<td><input type="text" name="address2"
						value="${client.address2}"></td>
				</tr>

				<tr>
					<td class=name>住　 所　 3</td>
					<td><input type="text" name="address3"
						value="${client.address3}"></td>
				</tr>

				<tr>
					<td class=name>電 話　番 号</td>
					<td><input type="text" name="tel" value="${client.tel}"></td>
				</tr>

				<tr>
					<td class=name>ＦＡＸ 番 号</td>
					<td><input type="text" name="fax" value="${client.fax}"></td>
				</tr>

				<tr>
					<td class=name>担　当　者</td>
					<td><input type="text" name="manager"
						value="${client.manager}"></td>
				</tr>

				<tr>
					<td class=name>輸 送　Ｌ/Ｔ</td>
					<td><input type="text" name="delivaryLeadtime"
						value="${client.delivaryLeadtime}"></td>
				</tr>

			</table>

			<div class="form-group">
				<label for="message">-備考-</label>
				<textarea name="etc" id="etc" rows="5" cols="10"
					class="form-control">
			  </textarea>
			</div>

			<br>

			<p>
				<button type="button" onClick="doProgram('touroku')"
				<c:if test="${flag==1}">disabled="disabled"</c:if>>登録</button>
				<button type="button" onClick="doProgram('kosin')"
				<c:if test="${flag!=1}">disabled="disabled"</c:if>>更新</button>
				<button type="button" onClick="doProgram('delete')"
				<c:if test="${flag!=1}">disabled="disabled"</c:if>>削除</button>
				<button type="button" onClick="doProgram('clear')">クリア</button>
			</p>

			<br>

		</div>

	</form>
</body>
</html>


