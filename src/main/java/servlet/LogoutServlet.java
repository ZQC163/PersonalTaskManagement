package servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * LogoutServlet
 *
 * 【このクラスの目的】
 * - ログアウト処理を行い、セッション情報を完全に消す。
 *
 * 【なぜ専用 Servlet が必要なのか】
 * - ログアウトは「セッション破棄」という明確な処理が必要なため、
 *   JSP に書くのではなく、Controller として Servlet に記述すべき。
 *
 * 【セキュリティの観点】
 * - session.invalidate() を実行すると、セッション ID が完全無効になるため、
 *   セッション固定攻撃を防ぐ効果がある。
 */

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        /**
         * 【getSession(false) を使う理由】
         * - false を指定すると「すでに存在するセッションだけ取得」する。
         * - もし未ログイン状態で /logout を叩かれても、新しいセッションを作らない。
         */
        HttpSession session = req.getSession(false);

        if (session != null) {
            /**
             * 【invalidate() の動作】
             * - セッション内部のデータをすべて削除
             * - セッション ID も無効化
             * - 実際にはサーバ側のセッション管理領域からも削除される
             *
             * removeAttribute() では不十分な理由：
             * - セッション自体は生き残るので、セキュリティ上弱い
             *   → 完全破棄が必要
             */
            session.invalidate();
        }

        /**
         * 【ログイン画面への redirect】
         * - forward ではなく redirect を使う理由：
         *     ① URL を /login に戻す
         *     ② F5 押下による再操作を防ぐ
         */
        resp.sendRedirect(req.getContextPath() + "/login");
    }
}
