<%@ page contentType="text/html; charset=UTF-8" %>

<html>
<head>
    <meta charset="UTF-8">
    <title>ログイン</title>
</head>
<body>

<h2>ログイン</h2>

<%
    String error = (String) request.getAttribute("error");
    if (error != null) {
%>
    <p style="color:red;"><%= error %></p>
<%
    }
%>

<form action="login" method="post">

    <p>ユーザー名：</p>
    <input type="text" name="username">

    <p>パスワード：</p>
    <input type="password" name="password">

    <br><br>
    <input type="submit" value="ログイン">

</form>

</body>
</html>
