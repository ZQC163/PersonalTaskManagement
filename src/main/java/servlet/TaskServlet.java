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
 * タスク一覧・登録・編集・削除をまとめて処理するサーブレット。
 * URL パターン：/task
 */
@WebServlet("/task")
public class TaskServlet extends HttpServlet {

    private TaskDao taskDao = new TaskDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        if (action == null) {
            action = "list";
        }

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
            case "list":
            default:
                list(req, resp);
                break;
        }
    }

    /**
     * タスク一覧画面表示
     */
    private void list(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // ログインユーザーの取得
        User loginUser = (User) req.getSession().getAttribute("loginUser");
        if (loginUser == null) {
            // 未ログインならログイン画面へ
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // 自分のタスクだけ取得
        List<Task> taskList = taskDao.findAllByUser(loginUser.getId());
        req.setAttribute("taskList", taskList);

        req.getRequestDispatcher("/list.jsp").forward(req, resp);
    }

    /**
     * 新規登録画面表示
     */
    private void showAddForm(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // ログインチェック
        User loginUser = (User) req.getSession().getAttribute("loginUser");
        if (loginUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        req.setAttribute("task", null);
        req.getRequestDispatcher("/form.jsp").forward(req, resp);
    }

    /**
     * 編集画面表示
     */
    private void showEditForm(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // ログインチェック
        User loginUser = (User) req.getSession().getAttribute("loginUser");
        if (loginUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String idStr = req.getParameter("id");
        int id = Integer.parseInt(idStr);

        Task task = taskDao.findById(id);
        req.setAttribute("task", task);

        req.getRequestDispatcher("/form.jsp").forward(req, resp);
    }

    /**
     * 削除処理
     */
    private void delete(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        // ログインチェック
        User loginUser = (User) req.getSession().getAttribute("loginUser");
        if (loginUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String idStr = req.getParameter("id");
        int id = Integer.parseInt(idStr);

        taskDao.delete(id);

        resp.sendRedirect(req.getContextPath() + "/task?action=list");
    }

    /**
     * 登録・更新処理（POST）
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        req.setCharacterEncoding("UTF-8");

        // ログインチェック
        User loginUser = (User) req.getSession().getAttribute("loginUser");
        if (loginUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        int userId = loginUser.getId();

        String idStr = req.getParameter("id");
        String title = req.getParameter("title");
        String description = req.getParameter("description");
        String status = req.getParameter("status");
        String deadline = req.getParameter("deadline");
        String importance = req.getParameter("importance");

        if (idStr == null || idStr.isEmpty()) {
            // 新規登録
            Task task = new Task(title, description, status, deadline, importance, userId);
            taskDao.insert(task);
        } else {
            // 更新
            int id = Integer.parseInt(idStr);
            Task task = new Task(id, title, description, status, deadline, importance, userId);
            taskDao.update(task);
        }

        resp.sendRedirect(req.getContextPath() + "/task?action=list");
    }
}
