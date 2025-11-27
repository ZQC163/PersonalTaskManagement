<%@ page contentType="text/html; charset=UTF-8" %>

<html>
<head>
    <meta charset="UTF-8">
    <title>新規タスク</title>
</head>
<body>

<h2>新規タスク</h2>

<form action="add" method="post">

    <p>タイトル：</p>
    <input type="text" name="title">

    <p>内容：</p>
    <textarea name="description"></textarea>

    <p>状態：</p>
    <select name="status">
        <option value="todo">未着手</option>
        <option value="doing">進行中</option>
        <option value="done">完了</option>
    </select>

    <p>締切日：</p>
    <input type="date" name="deadline">

    <p>重要度：</p>
    <select name="importance">
        <option value="高">高</option>
        <option value="中">中</option>
        <option value="低">低</option>
    </select>

    <br><br>
    <input type="submit" value="登録">

</form>

<p><a href="list">一覧に戻る</a></p>

</body>
</html>
