<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="model.Task" %>

<%
    Task task = (Task) request.getAttribute("task");
%>

<html>
<head>
    <meta charset="UTF-8">
    <title>タスク編集</title>
</head>
<body>

<h2>タスク編集</h2>

<form action="edit" method="post">

    <input type="hidden" name="id" value="<%= task.getId() %>">

    <p>タイトル：</p>
    <input type="text" name="title" value="<%= task.getTitle() %>">

    <p>内容：</p>
    <textarea name="description"><%= task.getDescription() %></textarea>

    <p>状態：</p>
    <select name="status">
        <option value="todo" <% if ("todo".equals(task.getStatus())) out.print("selected"); %>>未着手</option>
        <option value="doing" <% if ("doing".equals(task.getStatus())) out.print("selected"); %>>進行中</option>
        <option value="done" <% if ("done".equals(task.getStatus())) out.print("selected"); %>>完了</option>
    </select>

    <p>締切日：</p>
    <input type="date" name="deadline" value="<%= task.getDeadline() %>">

    <p>重要度：</p>
    <select name="importance">
        <option value="高" <% if ("高".equals(task.getImportance())) out.print("selected"); %>>高</option>
        <option value="中" <% if ("中".equals(task.getImportance())) out.print("selected"); %>>中</option>
        <option value="低" <% if ("低".equals(task.getImportance())) out.print("selected"); %>>低</option>
    </select>

    <br><br>
    <input type="submit" value="更新">

</form>

<p><a href="list">一覧に戻る</a></p>

</body>
</html>
