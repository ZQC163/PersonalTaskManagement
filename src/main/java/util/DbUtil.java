package util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbUtil {

    private static final String URL = "jdbc:mysql://localhost:3306/task_db?useSSL=false&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai";
    private static final String USER = "root";     
    private static final String PASSWORD = "Zqc@84582658"; // 修改为你的

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // MySQL 8.x 驱动
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
