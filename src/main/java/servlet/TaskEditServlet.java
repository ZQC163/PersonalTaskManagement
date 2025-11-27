package servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.TaskDao;
import model.Task;
import model.User;

@WebServlet("/task/edit")
public class TaskEditServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public TaskEditServlet() {
        super();
    }

    // 編集画面
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User loginUser = (session != null) ? (User) session.getAttribute("loginUser") : null;

        if (loginUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int id = Integer.parseInt(request.getParameter("id"));

        TaskDao taskDao = new TaskDao();
        Task task = taskDao.findById(id);

        if (task == null || task.getUserId() != loginUser.getId()) {
            response.sendRedirect(request.getContextPath() + "/task/list");
            return;
        }

        request.setAttribute("task", task);
        request.getRequestDispatcher("/task_edit.jsp").forward(request, response);
    }

    // 更新処理
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        User loginUser = (session != null) ? (User) session.getAttribute("loginUser") : null;

        if (loginUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int id = Integer.parseInt(request.getParameter("id"));
        String title = request.getParameter("title");
        String desc = request.getParameter("description");
        String status = request.getParameter("status");
        String deadline = request.getParameter("deadline");
        String importance = request.getParameter("importance");

        Task updatedTask = new Task(id, title, desc, status, deadline, importance, loginUser.getId());

        TaskDao taskDao = new TaskDao();
        taskDao.update(updatedTask);

        response.sendRedirect(request.getContextPath() + "/task/list");
    }
}
