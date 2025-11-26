<%@ page contentType="text/html; charset=UTF-8" %>

<%--
  【login.jsp の役割】

  - ログイン画面の UI（View）を表示する。
  - ログイン処理（認証ロジック）は LoginServlet が行う。
  - JSP は「画面の表示」だけを担当し、ビジネスロジックは絶対に書かない。

  → MVC の「View」にあたる。
--%>

<html>
<head>
    <meta charset="UTF-8">
    <title>ログイン</title>
</head>
<body>

<h2>ログイン</h2>

<%
    /*
     * 【エラーメッセージ表示の仕組み】
     *
     * LoginServlet#doPost() で
     *   req.setAttribute("error", "メッセージ")
     * がセットされて forward されてくる。
     *
     * request.getAttribute("error") で受け取れる。
     *
     *  forward を使っているから request 属性が残る
     * 
     */
    String error = (String) request.getAttribute("error");

    // 最初のアクセス時は null、認証失敗時のみ表示するため判断が必要
    if (error != null) {
%>
        <p style="color:red;"><%= error %></p>
<%
    }
%>

<!--
  【form の基本設計】

  action="login"
    - /login は LoginServlet の URL パターン。
    - login.jsp に直接送信するのではなく、
      必ず LoginServlet に POST → doPost() が呼ばれる。

  method="post"
    - パスワードなど機密情報を URL に含めないため。
    - GET は URL に全部表示されるのでセキュリティ上 NG。

  フォームの本質は：

    JSP → (POST) → LoginServlet#doPost → 認証処理
-->
<form action="login" method="post" style="margin-top: 20px;">

    <!-- ユーザー名入力欄 -->
    <div style="margin-bottom: 10px;">
        <label>ユーザー名：</label><br>

        <%--
           input の name="username" が
           LoginServlet の req.getParameter("username") と対応している。

           つまり、この name が一致していないと Servlet が受け取れない。
        --%>
        <input type="text" name="username">
    </div>

    <!-- パスワード入力欄 -->
    <div style="margin-bottom: 10px;">
        <label>パスワード：</label><br>

        <%--
          type="password" にすることで入力値がマスクされる。
          セキュリティ的に最低限必要。
        --%>
        <input type="password" name="password">
    </div>

    <!-- ログインボタン：LoginServlet の doPost が呼ばれる -->
    <input type="submit" value="ログイン">

</form>

</body>
</html>

