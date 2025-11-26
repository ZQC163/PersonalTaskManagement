<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List, model.Task, model.User" %>

<%--
  【list.jsp の役割】
  - ログイン中ユーザーのタスク一覧を表形式で表示する。
  - DB からの取得処理は TaskServlet と TaskDao が担当し、
    JSP は「渡された List<Task> を表示するだけ」という役割に限定する。
--%>

<%
    // TaskServlet#list() で request にセットされた一覧データ
    List<Task> list = (List<Task>) request.getAttribute("taskList");

    // セッションからログイン中のユーザー情報を取得
    User loginUser = (User) session.getAttribute("loginUser");
%>

<html>
<head>
    <meta charset="UTF-8">
    <title>タスク一覧</title>
</head>
<body>

<h2>タスク一覧</h2>

<%-- ログインユーザーの表示 --%>
<p>
    ログインユーザー：
    <strong>
        <%
            // loginUser が null になることは想定していないが、
            // 念のため null チェックしてから表示する。
            if (loginUser != null) {
                out.print(loginUser.getDisplayName());
            }
        %>
    </strong>
</p>

<%--
  「新規作成」と「ログアウト」のリンク。
  - 新規作成：/task?action=add → TaskServlet#doGet → showAddForm()
  - ログアウト：/logout → LogoutServlet#doGet()
--%>
<div style="margin-bottom: 15px;">
    <a href="task?action=add">＋ 新規タスク作成</a>
    <a href="logout" style="margin-left: 20px;">ログアウト</a>
</div>

<%--
  タスク一覧テーブル
  - border / cellpadding などは最低限の見やすさのために指定。
--%>
<table border="1" cellpadding="8" cellspacing="0">
    <tr>
        <th>ID</th>
        <th>タイトル</th>
        <th>内容</th>
        <th>状態</th>
        <th>締切日</th>
        <th>重要度</th>
        <th>操作</th>
    </tr>

<%
    /**
     * list が null でない場合のみループする。
     * - TaskServlet 側で空の List を渡している場合もあるが、
     *   null の可能性も一応考慮して条件分岐している。
     */
    if (list != null) {

        // 拡張 for 文で List<Task> を1件ずつ取り出す
        for (Task t : list) {
%>
    <tr>
        <%-- 1列目：タスクID --%>
        <td><%= t.getId() %></td>

        <%-- 2列目：タイトル --%>
        <td><%= t.getTitle() %></td>

        <%-- 3列目：内容（description） --%>
        <td><%= t.getDescription() %></td>

        <%-- 4列目：状態（todo / doing / done） --%>
        <td><%= t.getStatus() %></td>

        <%-- 5列目：締切日（文字列のまま表示） --%>
        <td><%= t.getDeadline() %></td>

        <%-- 6列目：重要度（高・中・低） --%>
        <td><%= t.getImportance() %></td>

        <%--
          7列目：操作（編集 / 削除）
          - どのレコードを対象にするかを URL の id パラメータで渡す。
          - 編集：/task?action=edit&id=◯◯
          - 削除：/task?action=delete&id=◯◯
        --%>
        <td>
            <a href="task?action=edit&id=<%= t.getId() %>">編集</a>
            |
            <a href="task?action=delete&id=<%= t.getId() %>"
               onclick="return confirm('本当に削除しますか？');">
                削除
            </a>
        </td>
    </tr>
<%
        } // end for
    } // end if
%>

</table>

</body>
</html>

