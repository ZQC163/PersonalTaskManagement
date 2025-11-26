package servlet;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.TaskDao;
import model.Task;
import model.User;

/**
 * TaskServlet
 *
 * 【このクラスの役割】
 * - タスクに関する全処理を一括管理する Controller。
 * - “/task” という URL でアクセスされ、
 *   action パラメータに応じて処理を切り替える。
 *
 * 【なぜ Servlet を分けず 1 つにまとめているのか？】
 * - add / edit / delete / list が密接に関連しているため、
 *   初学者の私にとって、 1 クラスでまとまっている方が理解しやすい。
 * 
 */

@WebServlet("/task")
public class TaskServlet extends HttpServlet {

    /**
     * DAO をフィールドとして保持する理由：
     * - Servlet は 1 インスタンスで動くため、毎回 new する必要がない。
     * - DB 操作はすべて TaskDao に委譲する。
     */
    private TaskDao taskDao = new TaskDao();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        /**
         * 【action パラメータ取得】
         * - /task?action=list
         * - /task?action=add
         * - /task?action=edit&id=◯◯
         * - /task?action=delete&id=◯◯
         *
         * GET は画面表示専用。
         */
        String action = req.getParameter("action");

        // action が null の場合は list として扱う（デフォルト処理）
        if (action == null) action = "list";

        /**
         * 【switch にした理由】
         * - if-else より可読性が高い
         * - add / edit / delete など “状態ごとに分岐” するのに適している
         */
        switch (action) {
            case "add":
                showAddForm(req, resp);
                break;

            case "edit":
                showEditForm(req, resp);
                break;

            case "delete":
                delete(req, resp);
                break;

            default:
                list(req, resp);
        }
    }


    /**
     * タスク一覧を表示する処理
     *
     * 【このメソッドの責務】
     * - ログイン中のユーザーを session から取得し、
     *   そのユーザーが所有するタスクだけを DB から取得して JSP に渡す。
     */
    private void list(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // セッションからログインユーザーを取得
        User loginUser = (User) req.getSession().getAttribute("loginUser");

        /**
         * 【ログインチェックをここで行う理由】
         * - Filter を使っていないため、この Servlet 内で認証チェックが必要。
         * - loginUser が null の場合、未ログインと判断し、ログイン画面へリダイレクト。
         */
        if (loginUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        /**
         * 【taskDao.findAllByUser(loginUser.getId()) の意味】
         * - DB から “ログインしている本人だけのタスク” を取得する。
         * - 他人のタスクを絶対に見せないためのセキュリティ仕様。
         * - DB のレコード → Task モデルの List<Task> に変換されて返される。
         */
        List<Task> taskList = taskDao.findAllByUser(loginUser.getId());

        // JSP に渡すため request にセット（request スコープ）
        req.setAttribute("taskList", taskList);

        /**
         * 【forward を使う理由】
         * - request の taskList を JSP にそのまま渡すため。
         * - redirect にすると request が消えるので使えない。
         */
        req.getRequestDispatcher("/list.jsp").forward(req, resp);
    }


    /**
     * タスク追加画面表示
     *
     * 【なぜこのメソッドが必要なのか】
     * - 新規登録のためのフォーム（form.jsp）を表示するため。
     * - form.jsp は新規と編集の “共通画面” のため、
     *   新規の場合は task=null を渡すことで「空欄フォーム」になる。
     */
    private void showAddForm(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // ログインチェック
        if (req.getSession().getAttribute("loginUser") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        /**
         * 新規登録なので task を null として JSP に渡す
         * → JSP 側で “isEdit=false” として扱わせる
         */
        req.setAttribute("task", null);

        req.getRequestDispatcher("/form.jsp").forward(req, resp);
    }

    /**
     * タスク編集画面表示
     *
     * 【編集処理の前にやるべきこと】
     * - どのタスクを編集するのか？ → URLの id=◯◯ から取得
     * - DB からその ID のタスクを1件だけ取得（findById）
     * - form.jsp に “元の値” を初期値として渡す
     */
    private void showEditForm(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // 認証チェック
        if (req.getSession().getAttribute("loginUser") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // URL の ?id=◯◯ を取得（数値に変換）
        int id = Integer.parseInt(req.getParameter("id"));

        // 編集対象のタスクを DB から取得
        Task task = taskDao.findById(id);

        // JSP に渡す（form.jsp が “編集モード” で表示される）
        req.setAttribute("task", task);

        req.getRequestDispatcher("/form.jsp").forward(req, resp);
    }


    /**
     * タスク削除処理
     *
     * 【このメソッドの動作】
     * - URL の id=◯◯ を取得して、そのレコードを DB から削除する。
     *
     * 【セキュリティ視点】
     * - 本来は「このタスクの user_id == loginUser.id か？」の確認が必要。
     * - 学習用途のため省略されているが、実務では必須。
     */
    private void delete(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        // 認証チェック
        if (req.getSession().getAttribute("loginUser") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // URL の id=◯◯ を取得
        int id = Integer.parseInt(req.getParameter("id"));

        // DAO に削除させる
        taskDao.delete(id);

        // 一覧画面に戻る
        resp.sendRedirect(req.getContextPath() + "/task?action=list");
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        // POST の文字化けを防ぐ（日本語入力対応）
        req.setCharacterEncoding("UTF-8");

        /**
         * 【ログインユーザーの取得】
         *
         * なぜ毎回取得するのか？
         * - セッションに保存されているログイン情報（User）から userId を取得するため。
         * - userId は “誰のタスクか？” を紐づける最重要情報。
         * - これをフォームから受け取ると偽装される危険があるため、
         *   必ずセッションから取る（セキュリティ対策）。
         */
        User loginUser = (User) req.getSession().getAttribute("loginUser");
        if (loginUser == null) {
            // 未ログインで POST されるケースを排除する（セキュリティ）
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // DB に保存するための userId（信頼できる値）
        int userId = loginUser.getId();


        /**
         * 【フォームから各フィールドを取得】
         *
         * なぜ getParameter() を使う？
         * - input, textarea, select の値はすべて String として渡ってくる。
         * - Servlet 標準のパラメータ取得は getParameter()。
         * - null の可能性を考慮するため、一旦 String として受け、後で変換する。
         */
        String idStr       = req.getParameter("id");          // 新規 → null／空欄, 編集 → 数値
        String title       = req.getParameter("title");
        String description = req.getParameter("description");
        String status      = req.getParameter("status");
        String deadline    = req.getParameter("deadline");
        String importance  = req.getParameter("importance");


        /**
         * 【新規 or 更新の分岐】
         *
         * - form.jsp は add/edit 共通のため、hidden で id を渡す。
         * - id が空、または null → 新規登録と判断。
         * - id に値がある → その id のレコードを更新。
         *
         * 1つの POST で insert と update を統一する
         * 
         */
        if (idStr == null || idStr.isEmpty()) {

            /**
             * 🔵【新規登録：insert】
             *
             * なぜ Task コンストラクタは 2 種類あるのか？
             * - “新規は id がまだ存在しない”
             *   → id を持たないコンストラクタで作るべき。
             * - 更新は id が必要
             *   → id 付きコンストラクタを使う。
             */
            Task task = new Task(
                    title, description, status,
                    deadline, importance, userId
            );

            // DAO に insert を任せる（Servlet が SQL を意識しないため）
            taskDao.insert(task);

        } else {

            /**
             * 🔴【更新処理：update】
             *
             * - idStr は String なので、整数に変換する必要がある。
             * - ここで変換ミスすると NumberFormatException になるが、
             *   form.jsp で正しい値しか送ってこない設計なので問題なし。
             */
            int id = Integer.parseInt(idStr);

            // 更新用 Task インスタンスを作成
            Task task = new Task(
                    id, title, description,
                    status, deadline, importance, userId
            );

            // 指定 ID のレコードを更新
            taskDao.update(task);
        }

        /**
         * 【PRG パターン（Post → Redirect → Get）】
         *
         * - これにより画面リロード時にフォームが再送信されない。
         * - /task?action=list に飛ぶことで “一覧画面” を GET で表示できる。
         */
        resp.sendRedirect(req.getContextPath() + "/task?action=list");
    }
}
