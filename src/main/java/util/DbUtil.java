package util;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * DB 接続ユーティリティクラス
 *
 * DAO から呼び出して、MySQL への Connection を取得する。
 */
public class DbUtil {

    // 自分の環境に合わせて調整すること
    private static final String URL =
            "jdbc:mysql://localhost:3306/task_db?useSSL=false&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai";
    private static final String USER = "root";
    private static final String PASSWORD = "Zqc@84582658";

    static {
        try {
            // JDBC ドライバ読み込み
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL JDBC ドライバ読み込み成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * DB 接続を取得するメソッド
     */
    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
