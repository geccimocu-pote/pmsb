<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!--jstlのリンク -->
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ユーザースマスタ</title>

<script>
	function doProgram(hikisu) {
		var form = document.forms[0];

		form.bunki.value = hikisu;
		form.submit();
	}

	window.onload = function() {
		var result = "${alertMessage}";
		if (result != '' && "${user.userId != '' }") {
			alert("${alertMessage}");
		} else {
			if (result == '' && "${user.userId == ''} ") {
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
<link rel="stylesheet" href="css/user.css">


</head>
<body>

	<form action="userMain" method="post">

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


			<h1>ユーザマスタ</h1>

			<table>

				<tr>
					<td class=name>ユーザＩＤ</td>
					<td><input type="text" name="userId" value="${user.userId}"
						placeholder="登録の際には入力不要。"
						onchange="doProgram('searchId')"></td>
				</tr>

				<tr>
					<td class=name>名　　　前</td>
					<td><input type="text" name="name" value="${user.name}"></td>
				</tr>

				<tr>
					<td class=name>パスワード</td>
					<td><input type="text" name="password"
						value="${user.password}"></td>
				</tr>

				<tr>
					<td class=name>分　　　類</td>
					<td><input type="text" name="dept" value="${user.dept}"></td>
				</tr>

				<tr>
					<td class=name>入　社　日</td>
					<td><input type="text" name="hireDate"
						value="${user.hireDate}"></td>
				</tr>

			</table>


			<div class="form-group">
				<label for="message">-備考-</label>
				<textarea name="etc" id="etc" rows="5" cols="10"
					class="form-control"> ${ProductMaster.etc}</textarea>
			</div>


			<br>

			<p>
				<button id="regist" type="button" onClick="doProgram('touroku')"
				<c:if test="${flag==1}">disabled="disabled"</c:if>>登録</button>
				<button id="regist" type="button" onClick="doProgram('kosin')"
				<c:if test="${flag!=1}">disabled="disabled"</c:if>>更新</button>
				<button type="button" onClick="doProgram('delete')"
				<c:if test="${flag!=1}">disabled="disabled"</c:if>>削除</button>
				<button type="button" onClick="doProgram('clear')">クリア</button>
			</p>

			<br>
<!--
			<table>
				<tr class="error">
					<td></td>
					<td>${alertMessage}</td>
					<td></td>
				</tr>
			</table>
-->
		</div>
	</form>


</body>
</html>



