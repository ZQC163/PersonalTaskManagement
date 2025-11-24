package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import model.User;
import util.DbUtil;

/**
 * UserDao
 *
 * user テーブルへのアクセスを行う DAO クラス。
 * 主にログイン認証で使用する。
 */
public class UserDao {

    /**
     * ユーザー名とパスワードでログイン認証を行う。
     * 該当ユーザーが存在すれば User を返し、存在しなければ null を返す。
     */
    public User login(String username, String password) {

        String sql = "SELECT * FROM user WHERE username = ? AND password = ?";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("display_name")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
