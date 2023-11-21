<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!--jstlのリンク -->
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ログイン画面</title>

<script>
	function doProgram(hikisu) {
		var form = document.forms[0];

		form.bunki.value = hikisu;
		form.submit();
	}
	function doclose() {
		window.close();
	}

	window.onload = function() {
		var result = "${alertMessage}";
		if (result != '' && "${user.name != '' }") {
			alert("${alertMessage}");
		}
	}
</script>

<link rel="stylesheet" href="bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="bootstrap/css/bootstrap-theme.min.css">

<link rel="stylesheet" href="css/all.css">
<link rel="stylesheet" href="css/login.css">

</head>
<body>

	<form action="login" method="post">

		<input type="hidden" name="bunki">
		<input type="hidden" name="Message" value="${alertMessage}">


		<div class=dai>

			<table>
				<tr>
					<td>生産管理<br>システム
					</td>
					<td><h2>ログイン画面</h2>
					<td>
					<td></td>
				</tr>

				<tr>
					<td class="name">ユーザーID</td>
					<td><input type="text" name="userid" value="${user.userId}"
					onchange="doProgram('searchId')"></td>
				</tr>

				<tr>
					<td class="name">ユーザー名</td>
					<td><input type="text" name="name" value="${user.name}" readonly></td>
				</tr>

				<tr>
					<td class="name">パスワード</td>
					<td><input type="text" name="password"></td>
				</tr>
			</table>

			<p>
				<button type="button" onClick="doProgram('login')">ログイン</button>
			</p>

		</div>

	</form>

</body>
</html>


