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

@WebServlet("/task/delete")
public class TaskDeleteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public TaskDeleteServlet() {
        super();
    }

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

        taskDao.delete(id, loginUser.getId());

        response.sendRedirect(request.getContextPath() + "/task/list");
    }
}
