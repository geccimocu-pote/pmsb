<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!--jstlのリンク -->
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>マスタ登録</title>


<link rel="stylesheet" href="bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="bootstrap/css/bootstrap-theme.min.css">

<link rel="stylesheet" href="css/all.css">
<link rel="stylesheet" href="css/master.css">

</head>
<body>

	<div class=dai>


		<table class="ari">

			<tr>
				<td class="le">
				<a href="MasterMain">ログアウト
			</a></td>
				<td class="ri"><a href="menu.jsp">メニューへ戻る</a></td>
			</tr>

		</table>



		<h2>マスタ登録画面</h2>


		<table>

			<tr>
				<td><button type="button" onClick="location.href='product.jsp'">品番マスタ</button></td>
				<td><button type="button" onClick="location.href='client.jsp'">顧客先マスタ</button></td>
			</tr>

			<tr>
				<td><button type="button"
						onClick="location.href='supplier.jsp'">仕入先マスタ</button></td>
				<td><button type="button" onClick="location.href='user.jsp'">ユーザマスタ</button></td>
			</tr>

		</table>


	</div>

</body>
</html>


