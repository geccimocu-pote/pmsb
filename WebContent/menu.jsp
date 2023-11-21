<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!--jstlのリンク -->
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>メニュー画面</title>


<link rel="stylesheet" href="bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="bootstrap/css/bootstrap-theme.min.css">

<link rel="stylesheet" href="css/all.css">
<link rel="stylesheet" href="css/menu.css">


</head>
<body>

	<div class=dai>
<form action="menu" method="post">
<a href="MenuMain">ログアウト</a>

</form>
		<h2>メニュー画面</h2>


		<table>

			<tr>
				<td><button type="button" onClick="location.href='order.jsp'">受注画面
					</button></td>
				<td><button type="button" onClick="location.href='inOut.jsp'">入出庫画面</button></td>
			</tr>

			<tr>
				<td><button type="button"
						onClick="location.href='delivery.jsp'">納入画面</button></td>
				<td><button type="button" onClick="location.href='issue.jsp'">出荷画面
					</button></td>
			</tr>
			<tr>
				<td><button type="button" onClick="location.href='stock.jsp'">在庫確認画面</button></td>
				<td><button type="button"
						onClick="location.href='orderList.jsp'">受注一覧</button></td>
			</tr>
			<tr>
				<td><button type="button" onClick="location.href='calc.jsp'">所要量計算</button></td>
				<td><button type="button" onClick="location.href='master.jsp'">マスタ登録
					</button></td>
			</tr>

		</table>

	</div>

</body>
</html>


