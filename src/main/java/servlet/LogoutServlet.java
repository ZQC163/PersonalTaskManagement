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
 * 本当のログアウト処理を行うサーブレット。
 * セッションを破棄し、ログイン画面へ戻す。
 */
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        // 現在のセッションを取得（存在しない場合は null）
        HttpSession session = req.getSession(false);

        if (session != null) {
            // セッションを完全に破棄する（ログイン情報を消す）
            session.invalidate();
        }

        // ログイン画面へリダイレクト
        resp.sendRedirect(req.getContextPath() + "/login");
    }
}
