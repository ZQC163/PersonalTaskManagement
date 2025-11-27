package servlet;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.TaskDao;
import model.Task;
import model.User;

@WebServlet("/task/list")
public class TaskListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public TaskListServlet() {
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

		TaskDao taskDao = new TaskDao();
		List<Task> list = taskDao.findAllByUser(loginUser.getId());

		request.setAttribute("taskList", list);

		request.getRequestDispatcher("/task_list.jsp").forward(request, response);
	}
}
