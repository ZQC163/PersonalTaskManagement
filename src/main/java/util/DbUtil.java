package util;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * DB接続ユーティリティ
 *
 * DAO から呼び出され、MySQL の Connection を返す。
 */
public class DbUtil {

    private static final String URL =
            "jdbc:mysql://localhost:3306/task_db?useSSL=false&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai";

    private static final String USER = "root";
    private static final String PASSWORD = "root";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL JDBC ドライバ読み込み成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
