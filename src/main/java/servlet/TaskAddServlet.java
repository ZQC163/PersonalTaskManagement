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

@WebServlet("/task/add")
public class TaskAddServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public TaskAddServlet() {
        super();
    }

    // 新規画面
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User loginUser = (session != null) ? (User) session.getAttribute("loginUser") : null;

        if (loginUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        request.getRequestDispatcher("/task_add.jsp").forward(request, response);
    }

    // 新規登録
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        User loginUser = (session != null) ? (User) session.getAttribute("loginUser") : null;

        if (loginUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String title = request.getParameter("title");
        String desc = request.getParameter("description");
        String status = request.getParameter("status");
        String deadline = request.getParameter("deadline");
        String importance = request.getParameter("importance");

        Task newTask = new Task(title, desc, status, deadline, importance, loginUser.getId());

        TaskDao taskDao = new TaskDao();
        taskDao.insert(newTask);

        response.sendRedirect(request.getContextPath() + "/task/list");
    }
}
