package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import model.User;

/**
 * user テーブルにアクセスする DAO
 */
public class UserDao {

	private static final String URL = "jdbc:mysql://localhost:3306/task_db";
	private static final String DB_USER = "root";
	private static final String DB_PASS = "Zqc@84582658";
	
	public UserDao() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	/**
	 * ログイン認証
	 */
	public User login(String username, String password) {

		String sql = "SELECT * FROM user WHERE username = ? AND password = ?";
		
		try (Connection conn = DriverManager.getConnection(URL, DB_USER, DB_PASS);
				PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, username);
			ps.setString(2, password);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				return new User(
						rs.getInt("id"),
						rs.getString("username"),
						rs.getString("password"),
						rs.getString("display_name"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
