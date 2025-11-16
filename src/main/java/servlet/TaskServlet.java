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
            showAddForm(req, resp);
        } else if (action.equals("edit")) {
            showEditForm(req, resp);
        } else if (action.equals("delete")) {
            delete(req, resp);
        }
    }

    private void list(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        List<Task> list = taskDao.findAll();
        req.setAttribute("taskList", list);
        req.getRequestDispatcher("/list.jsp").forward(req, resp);
    }

    private void showAddForm(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setAttribute("task", null);
        req.getRequestDispatcher("/form.jsp").forward(req, resp);
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int id = Integer.parseInt(req.getParameter("id"));
        Task task = taskDao.findById(id);

        req.setAttribute("task", task);
        req.getRequestDispatcher("/form.jsp").forward(req, resp);
    }

    private void delete(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        int id = Integer.parseInt(req.getParameter("id"));
        taskDao.delete(id);
        resp.sendRedirect(req.getContextPath() + "/task?action=list");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        String id = req.getParameter("id");
        String title = req.getParameter("title");
        String description = req.getParameter("description");
        String status = req.getParameter("status");

        if (id == null || id.equals("")) {
            taskDao.insert(new Task(title, description, status));
        } else {
            taskDao.update(new Task(Integer.parseInt(id), title, description, status));
        }

        resp.sendRedirect(req.getContextPath() + "/task?action=list");
    }
}
