package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.Task;

/**
 * task テーブルにアクセスする DAO
 */
public class TaskDao {

    private static final String URL =
            "jdbc:mysql://localhost:3306/task_db";
    private static final String DB_USER = "root";      
    private static final String DB_PASS = "Zqc@84582658";     
    
    public TaskDao() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 指定ユーザーのタスク一覧を取得
     */
    public List<Task> findAllByUser(int userId) {

        List<Task> list = new ArrayList<>();

        String sql = "SELECT * FROM task WHERE user_id = ? ORDER BY id DESC";

        try (Connection conn = DriverManager.getConnection(URL, DB_USER, DB_PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Task t = new Task(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("status"),
                        rs.getString("deadline"),
                        rs.getString("importance"),
                        rs.getInt("user_id")
                );
                list.add(t);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * id で 1 件取得
     */
    public Task findById(int id) {

        String sql = "SELECT * FROM task WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(URL, DB_USER, DB_PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Task(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("status"),
                        rs.getString("deadline"),
                        rs.getString("importance"),
                        rs.getInt("user_id")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 新規登録
     */
    public void insert(Task task) {

        String sql = "INSERT INTO task (title, description, status, deadline, importance, user_id) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, DB_USER, DB_PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, task.getTitle());
            ps.setString(2, task.getDescription());
            ps.setString(3, task.getStatus());
            ps.setString(4, task.getDeadline());
            ps.setString(5, task.getImportance());
            ps.setInt(6, task.getUserId());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新
     */
    public void update(Task task) {

        String sql = "UPDATE task "
                   + "SET title = ?, description = ?, status = ?, deadline = ?, importance = ? "
                   + "WHERE id = ? AND user_id = ?";

        try (Connection conn = DriverManager.getConnection(URL, DB_USER, DB_PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, task.getTitle());
            ps.setString(2, task.getDescription());
            ps.setString(3, task.getStatus());
            ps.setString(4, task.getDeadline());
            ps.setString(5, task.getImportance());
            ps.setInt(6, task.getId());
            ps.setInt(7, task.getUserId());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 削除
     */
    public void delete(int id, int userId) {

        String sql = "DELETE FROM task WHERE id = ? AND user_id = ?";

        try (Connection conn = DriverManager.getConnection(URL, DB_USER, DB_PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.setInt(2, userId);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

