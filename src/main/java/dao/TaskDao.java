package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.Task;
import util.DbUtil;

/**
 * TaskDao
 *
 * task テーブルへの CRUD（一覧・取得・登録・更新・削除）を担当する DAO クラス。
 */
public class TaskDao {

    /**
     * ログイン中ユーザーのタスク一覧を取得する。
     * user_id で絞り込みを行うことにより、他ユーザーのタスクは表示されない。
     */
    public List<Task> findAllByUser(int userId) {

        List<Task> list = new ArrayList<>();

        String sql = "SELECT * FROM task WHERE user_id = ? ORDER BY id DESC";

        try (Connection conn = DbUtil.getConnection();
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
     * 指定された ID のタスクを 1 件取得する。
     * 編集画面表示時に使用する。
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
     * タスク新規登録
     */
    public void insert(Task task) {

        String sql = "INSERT INTO task (title, description, status, deadline, importance, user_id) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DbUtil.getConnection();
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
     * タスク更新
     */
    public void update(Task task) {

        String sql = "UPDATE task " +
                     "SET title = ?, description = ?, status = ?, deadline = ?, importance = ?, user_id = ? " +
                     "WHERE id = ?";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, task.getTitle());
            ps.setString(2, task.getDescription());
            ps.setString(3, task.getStatus());
            ps.setString(4, task.getDeadline());
            ps.setString(5, task.getImportance());
            ps.setInt(6, task.getUserId());
            ps.setInt(7, task.getId());

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
