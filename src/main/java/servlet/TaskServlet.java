package servlet;

import dao.TaskDao;
import model.Task;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

/**
 * TaskServlet（タスク管理用コントローラ）
 *
 * ブラウザからのリクエストを受け取り、
 * DAO を呼び出して DB を操作し、
 * JSP に結果を渡す役割を持つ。
 *
 * URLパターン：
 *   /task?action=list
 *   /task?action=add
 *   /task?action=edit&id=◯
 *   /task?action=delete&id=◯
 */
@WebServlet("/task")
public class TaskServlet extends HttpServlet {

    /** DAO（DB操作担当） */
    private TaskDao taskDao = new TaskDao();

    /**
     * GET リクエスト処理
     * 主に画面遷移（一覧、追加画面、編集画面、削除）を担当
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // action パラメータで処理を分岐
        String action = req.getParameter("action");

        if (action == null || action.equals("list")) {
            list(req, resp);

        } else if (action.equals("add")) {
            showAddForm(req, resp);

        } else if (action.equals("edit")) {
            showEditForm(req, resp);

        } else if (action.equals("delete")) {
            delete(req, resp);
        }
    }


    /**
     * タスク一覧画面の表示
     */
    private void list(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        List<Task> list = taskDao.findAll();

        // JSP に値を渡す
        req.setAttribute("taskList", list);

        // list.jsp にフォワード
        req.getRequestDispatcher("/list.jsp").forward(req, resp);
    }


    /**
     * 新規登録画面の表示
     */
    private void showAddForm(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setAttribute("task", null);

        req.getRequestDispatcher("/form.jsp").forward(req, resp);
    }


    /**
     * 編集画面の表示
     */
    private void showEditForm(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int id = Integer.parseInt(req.getParameter("id"));

        Task task = taskDao.findById(id);

        req.setAttribute("task", task);

        req.getRequestDispatcher("/form.jsp").forward(req, resp);
    }


    /**
     * タスク削除
     */
    private void delete(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        int id = Integer.parseInt(req.getParameter("id"));

        taskDao.delete(id);

        // 削除後は一覧へリダイレクト
        resp.sendRedirect(req.getContextPath() + "/task?action=list");
    }


    /**
     * POST リクエスト（登録・更新処理）
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        String idParam = req.getParameter("id");
        String title = req.getParameter("title");
        String description = req.getParameter("description");
        String status = req.getParameter("status");

        // 新規登録
        if (idParam == null || idParam.equals("")) {

            Task t = new Task(title, description, status);

            taskDao.insert(t);

        // 更新
        } else {

            int id = Integer.parseInt(idParam);

            Task t = new Task(id, title, description, status);

            taskDao.update(t);
        }

        // 完了後は一覧へ移動
        resp.sendRedirect(req.getContextPath() + "/task?action=list");
    }
}

