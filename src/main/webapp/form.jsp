<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="model.Task" %>

<%--
  【form.jsp の役割】
  - タスクの「新規登録」と「編集」の両方で使う共通フォーム画面。
  - どちらのモードで使うかは、request に入っている task オブジェクトの有無で判断する。
  - ここでは DB アクセスは一切行わず、「入力フォームの表示」に限定する。
--%>

<%
    // TaskServlet から setAttribute("task", ...) されたものを受け取る。
    // 新規のとき：null が渡される。
    // 編集のとき：編集対象の Task インスタンスが渡される。
    Task task = (Task) request.getAttribute("task");

    // task が null → 新規モード
    // task が null でない → 編集モード
    boolean isEdit = (task != null);
%>

<html>
<head>
    <meta charset="UTF-8">
    <title>
        <%-- タイトルもモードに応じて切り替える（新規 or 編集） --%>
        <% if (isEdit) { %>
            タスク編集
        <% } else { %>
            新規タスク作成
        <% } %>
    </title>
</head>
<body>

<h2>
    <% if (isEdit) { %>
        タスク編集
    <% } else { %>
        新規タスク作成
    <% } %>
</h2>

<%--
  【フォーム全体の設計】
  - action="task" : TaskServlet に送ることで、Servlet が処理（insert or update）を判断する。
  - method="post" : DB の状態を変更するリクエストは POST を使うのが REST 的にも正しい。
--%>
<form action="task" method="post" style="margin-top: 20px;">

    <%-- 
      編集モードのときだけ「どのレコードを更新するか」を示す id を送る。
      新規登録のときは、DB 側の AUTO_INCREMENT に任せるため id を送らない。
    --%>
    <% if (isEdit) { %>
        <input type="hidden" name="id" value="<%= task.getId() %>">
    <% } %>

    <%-- タイトル入力欄 --%>
    <div style="margin-bottom: 10px;">
        <label>タイトル：</label><br>

        <%--
          新規：空欄
          編集：既存のタイトルを初期表示
          → if 文で出し分ける
        --%>
        <input type="text" name="title" size="40"
        <% if (isEdit) { %>
            value="<%= task.getTitle() %>"
        <% } %> >
    </div>

    <%-- 内容入力欄（複数行） --%>
    <div style="margin-bottom: 10px;">
        <label>内容：</label><br>

        <%--
          textarea は value 属性ではなく “タグ内の文字列” が値になる。
          そのため、編集モードのときだけ out.print(task.getDescription()) を呼び出す。
        --%>
        <textarea name="description" rows="5" cols="40"><%
            if (isEdit) {
                out.print(task.getDescription());
            }
        %></textarea>
    </div>

    <%-- 状態選択（todo / doing / done） --%>
    <div style="margin-bottom: 10px;">
        <label>状態：</label><br>

        <%--
          select + option の組み合わせ。
          編集モードのときに「今の状態」と一致する option に selected を付ける。
          → これにより「今どういう状態か」が画面上でも分かりやすい。
        --%>
        <select name="status">
            <option value="todo"
            <% if (isEdit && "todo".equals(task.getStatus())) { %>
                selected
            <% } %>>
                未着手
            </option>

            <option value="doing"
            <% if (isEdit && "doing".equals(task.getStatus())) { %>
                selected
            <% } %>>
                進行中
            </option>

            <option value="done"
            <% if (isEdit && "done".equals(task.getStatus())) { %>
                selected
            <% } %>>
                完了
            </option>
        </select>
    </div>

    <%-- 締切日入力（HTML5 の date 型） --%>
    <div style="margin-bottom: 10px;">
        <label>締切日：</label><br>

        <%--
          <input type="date"> の value は "YYYY-MM-DD" 形式の文字列が入る。
          DB では deadline を String として扱っているため、そのまま入れられる。
        --%>
        <input type="date" name="deadline"
        <% if (isEdit) { %>
            value="<%= task.getDeadline() %>"
        <% } %> >
    </div>

    <%-- 重要度選択（高・中・低） --%>
    <div style="margin-bottom: 10px;">
        <label>重要度：</label><br>

        <%--
          重要度も「現在の値」に応じて selected を切り替える。
          "高" "中" "低" を固定値で使っているので、equals で判定している。
        --%>
        <select name="importance">
            <option value="高"
            <% if (isEdit && "高".equals(task.getImportance())) { %>
                selected
            <% } %>>
                高
            </option>

            <option value="中"
            <% if (isEdit && "中".equals(task.getImportance())) { %>
                selected
            <% } %>>
                中
            </option>

            <option value="低"
            <% if (isEdit && "低".equals(task.getImportance())) { %>
                selected
            <% } %>>
                低
            </option>
        </select>
    </div>

    <%-- 送信ボタンと「一覧へ戻る」リンク --%>
    <input type="submit" value="保存">
    <a href="task?action=list" style="margin-left: 20px;">戻る</a>

</form>

</body>
</html>
