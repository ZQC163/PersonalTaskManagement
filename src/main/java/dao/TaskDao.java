package dao;

import model.Task;
import util.DbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * TaskDao（タスク用 DAO）
 *
 * task テーブルの CRUD（新規・取得・更新・削除）を担当。
 */
public class TaskDao {

    /**
     * 全タスク取得
     */
    public List<Task> findAll() {

        List<Task> list = new ArrayList<>();

        String sql = "SELECT * FROM task ORDER BY id DESC";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Task t = new Task(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("status"),
                        rs.getString("deadline"),
                        rs.getString("importance")
                );

                list.add(t);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    /**
     * ID 指定で 1 件取得
     */
    public Task findById(int id) {

        String sql = "SELECT * FROM task WHERE id = ?";

        try (Connection conn = DbUtil.getConnection();
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
                        rs.getString("importance")
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

        String sql = "INSERT INTO task(title, description, status, deadline, importance) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, task.getTitle());
            ps.setString(2, task.getDescription());
            ps.setString(3, task.getStatus());
            ps.setString(4, task.getDeadline());
            ps.setString(5, task.getImportance());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 更新
     */
    public void update(Task task) {

        String sql = "UPDATE task SET title=?, description=?, status=?, deadline=?, importance=? WHERE id=?";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, task.getTitle());
            ps.setString(2, task.getDescription());
            ps.setString(3, task.getStatus());
            ps.setString(4, task.getDeadline());
            ps.setString(5, task.getImportance());
            ps.setInt(6, task.getId());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 削除
     */
    public void delete(int id) {

        String sql = "DELETE FROM task WHERE id = ?";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
