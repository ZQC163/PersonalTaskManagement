package servlet;

import dao.TaskDao;
import model.Task;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

/**
 * TaskServlet
 *
 * /task?action=list   一覧
 * /task?action=add    新規画面
 * /task?action=edit   編集画面
 * /task?action=delete 削除
 */
@WebServlet("/task")
public class TaskServlet extends HttpServlet {

    private TaskDao taskDao = new TaskDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");

        if (action == null || action.equals("list")) {
            list(req, resp);
        } else if (action.equals("add")) {
            showAdd(req, resp);
        } else if (action.equals("edit")) {
            showEdit(req, resp);
        } else if (action.equals("delete")) {
            delete(req, resp);
        }
    }

    /** 一覧表示 */
    private void list(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        List<Task> tasks = taskDao.findAll();
        req.setAttribute("taskList", tasks);

        req.getRequestDispatcher("/list.jsp")
                .forward(req, resp);
    }

    /** 新規画面 */
    private void showAdd(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setAttribute("task", null);
        req.getRequestDispatcher("/form.jsp").forward(req, resp);
    }

    /** 編集画面 */
    private void showEdit(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int id = Integer.parseInt(req.getParameter("id"));
        Task task = taskDao.findById(id);

        req.setAttribute("task", task);
        req.getRequestDispatcher("/form.jsp").forward(req, resp);
    }

    /** 削除 */
    private void delete(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        int id = Integer.parseInt(req.getParameter("id"));
        taskDao.delete(id);

        resp.sendRedirect(req.getContextPath() + "/task?action=list");
    }


    /** 新規・更新POST */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        req.setCharacterEncoding("UTF-8");

        String id = req.getParameter("id");
        String title = req.getParameter("title");
        String description = req.getParameter("description");
        String status = req.getParameter("status");
        String deadline = req.getParameter("deadline");
        String importance = req.getParameter("importance");

        if (id == null || id.equals("")) {
            // 新規登録
            taskDao.insert(new Task(title, description, status, deadline, importance));

        } else {
            // 更新
            taskDao.update(
                    new Task(
                            Integer.parseInt(id),
                            title,
                            description,
                            status,
                            deadline,
                            importance
                    )
            );
        }

        resp.sendRedirect(req.getContextPath() + "/task?action=list");
    }
}

