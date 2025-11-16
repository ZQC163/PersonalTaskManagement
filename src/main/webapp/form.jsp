<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="model.Task" %>

<%
    Task task = (Task) request.getAttribute("task");
    boolean isEdit = (task != null);
%>

<html>
<head>
    <meta charset="UTF-8">
    <title><%= isEdit ? "タスク編集" : "新規タスク作成" %></title>
</head>
<body>

<h2><%= isEdit ? "タスク編集" : "新規タスク作成" %></h2>

<form action="task" method="post">

    <% if (isEdit) { %>
        <input type="hidden" name="id" value="<%= task.getId() %>">
    <% } %>

    タイトル：<br>
    <input type="text" name="title" value="<%= isEdit ? task.getTitle() : "" %>">
    <br><br>

    内容：<br>
    <textarea name="description" rows="5" cols="40"><%= isEdit ? task.getDescription() : "" %></textarea>
    <br><br>

    状態：<br>
    <select name="status">
        <option value="todo" <%= isEdit && "todo".equals(task.getStatus()) ? "selected" : "" %>>未着手</option>
        <option value="doing" <%= isEdit && "doing".equals(task.getStatus()) ? "selected" : "" %>>進行中</option>
        <option value="done"  <%= isEdit && "done".equals(task.getStatus()) ? "selected" : "" %>>完了</option>
    </select>
    <br><br>

    <input type="submit" value="保存">
    <a href="task?action=list">戻る</a>

</form>

</body>
</html>
