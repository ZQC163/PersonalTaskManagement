<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List, model.Task" %>

<%
    List<Task> list = (List<Task>) request.getAttribute("taskList");
%>

<html>
<head>
    <meta charset="UTF-8">
    <title>タスク一覧</title>
</head>
<body>

<h2>タスク一覧</h2>

<a href="task?action=add">新規タスク作成</a>
<br><br>

<table border="1" cellpadding="6">
    <tr>
        <th>ID</th>
        <th>タイトル</th>
        <th>内容</th>
        <th>状態</th>
        <th>操作</th>
    </tr>

    <% if (list != null)
       for (Task t : list) { %>
    <tr>
        <td><%= t.getId() %></td>
        <td><%= t.getTitle() %></td>
        <td><%= t.getDescription() %></td>
        <td><%= t.getStatus() %></td>
        <td>
            <a href="task?action=edit&id=<%= t.getId() %>">編集</a>
            |
            <a href="task?action=delete&id=<%= t.getId() %>"
                onclick="return confirm('本当に削除しますか？');">削除</a>
        </td>
    </tr>
    <% } %>

</table>

</body>
</html>
