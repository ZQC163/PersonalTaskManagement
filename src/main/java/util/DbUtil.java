package util;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * DbUtil（データベース接続ユーティリティ）
 *
 * MySQL への接続を行うユーティリティクラス。
 * DAO から呼び出され、Connection オブジェクトを返します。
 */
public class DbUtil {

    /** 接続URL（ポートやDB名は自分の環境に合わせる） */
    private static final String URL =
            "jdbc:mysql://localhost:3306/task_db?useSSL=false&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai";

    /** DBユーザー名（自分の MySQL の設定に合わせる） */
    private static final String USER = "root";

    /** DBパスワード（自分の MySQL の設定に合わせる） */
    private static final String PASSWORD = "root";


    /**
     * JDBCドライバのロード
     * プロジェクト起動時に1回だけ実行される。
     */
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL JDBC ドライバ読み込み成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * DB接続を取得する
     *
     * @return Connection（SQLを実行するためのオブジェクト）
     */
    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
