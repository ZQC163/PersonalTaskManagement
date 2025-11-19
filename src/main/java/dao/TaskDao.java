package dao;

import model.Task;
import util.DbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * TaskDao（タスクDAO）
 *
 * 「task」テーブルに対して CRUD 操作を行うクラス。
 * Controller（Servlet）から呼び出され、DB と直接通信する役割。
 */
public class TaskDao {

    /**
     * タスクを全件取得する。
     *
     * @return タスク一覧（最新のID順）
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
                        rs.getString("status")
                );

                list.add(t);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }


    /**
     * 指定IDのタスクを取得する
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
                        rs.getString("status")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * タスク新規登録
     */
    public void insert(Task task) {

        String sql = "INSERT INTO task(title, description, status) VALUES (?, ?, ?)";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, task.getTitle());
            ps.setString(2, task.getDescription());
            ps.setString(3, task.getStatus());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * タスク更新
     */
    public void update(Task task) {

        String sql = "UPDATE task SET title = ?, description = ?, status = ? WHERE id = ?";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, task.getTitle());
            ps.setString(2, task.getDescription());
            ps.setString(3, task.getStatus());
            ps.setInt(4, task.getId());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * タスク削除
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
