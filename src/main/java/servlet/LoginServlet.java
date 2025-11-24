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
 * ログイン画面表示 & 認証処理を行うサーブレット。
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // ログイン画面へフォワード
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        // DB でユーザー認証
        User user = userDao.login(username, password);

        if (user != null) {
            // ログイン成功 → セッションにユーザー情報を保存
            HttpSession session = req.getSession();
            session.setAttribute("loginUser", user);

            // タスク一覧へリダイレクト
            resp.sendRedirect(req.getContextPath() + "/task?action=list");
        } else {
            // ログイン失敗 → エラーメッセージを表示してログイン画面に戻る
            req.setAttribute("error", "ユーザー名またはパスワードが違います。");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }
}
