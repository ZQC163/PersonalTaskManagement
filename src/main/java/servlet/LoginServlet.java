package servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.UserDao;
import model.User;

/**
 * LoginServlet
 *
 * 【このクラスを作る理由】
 * - 「ログイン画面の表示（GET）」と「ログイン認証（POST）」の2つの責務を持つ。
 * - ユーザーが最初にアクセスする入口になるため、
 *   セッション開始、認証処理、画面遷移を一括管理する必要がある。
 *
 * 【MVC 的思考】
 * - DB アクセスは DAO 層（UserDao）に任せる。
 * - 画面表示は JSP（login.jsp）に任せる。
 * - LoginServlet は Controller として、処理の流れだけを決める。
 */

@WebServlet("/login")  // /login にアクセスされるとこの Servlet が呼ばれる
public class LoginServlet extends HttpServlet {

    // DAO をフィールド化する理由：
    // ① 毎回 new UserDao() する必要がない
    // ② Servlet は通常シングルトンとして動くので、この書き方で問題ない
    private UserDao userDao = new UserDao();

    /**
     * GET：ログイン画面表示
     *
     * 【なぜ doGet で login.jsp を表示するのか】
     * - ユーザーが URL に直接 /login と入力した場合も画面を表示させるため。
     * - GET は “画面表示専用” と考えるのが Web 開発の基本。
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        /**
         * 【forward を使う理由】
         * - request の内容（エラーメッセージなど）を保持したまま JSP へ送れる。
         * - 画面表示は JSP に任せる＝Controller と View の責務分離。
         */
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    /**
     * POST：ログイン認証
     *
     * 【このメソッドがやっていること】
     * ① フォームから送られた username/password を getParameter で取得する。
     * ② DAO の login() を呼び出して、DB の user テーブルと照合する。
     * ③ 認証成功 → Session にユーザー情報をセット。
     * ④ 認証失敗 → エラーメッセージを request に入れて login.jsp を再表示。
     *
     * 【なぜ GET と POST を分けるのか】
     * - GET は画面表示
     * - POST はデータ処理
     * Web 開発の基本方針に合わせて責務を明確にしている。
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // 文字化け防止。POST のときに必ず書く。
        req.setCharacterEncoding("UTF-8");

        // フォームの input name="xxx" と対応
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        /**
         * 【なぜ DAO に処理を委譲するのか】
         * - Servlet の責務を “ビジネスロジックを書かない” ようにするため。
         * - 認証ロジックが変更されても Servlet に影響しないようにするため。
         *
         * UserDao#login は成功したら User モデル、失敗したら null を返す。
         */
        User user = userDao.login(username, password);

        if (user != null) {

            /**
             * 【HttpSession を使う理由】
             * - ログイン状態を複数画面で保持するため
             * - cookie に session id が保存され、次のリクエストでも同じセッションが識別される
             *
             * getSession()：存在しなければ新しいセッションを作成する。
             */
            HttpSession session = req.getSession();

            // セッションに “ログインユーザー情報” を保存
            session.setAttribute("loginUser", user);

            /**
             * 【sendRedirect を使う理由】
             *
             * ① PRG パターン（Post-Redirect-Get）
             *    - ログイン後に F5 を押してもフォーム送信が再実行されないようにする
             *
             * ② URL が /task?action=list に変わり、ブックマークしやすくなる
             */
            resp.sendRedirect(req.getContextPath() + "/task?action=list");

        } else {

            /**
             * 【認証失敗時の処理】
             * - エラーメッセージを request にセット
             * - forward を使う理由：再表示時にメッセージを保持したいから
             * - redirect にすると request が消えるので使えない
             */
            req.setAttribute("error", "ユーザー名またはパスワードが違います。");

            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }
}
