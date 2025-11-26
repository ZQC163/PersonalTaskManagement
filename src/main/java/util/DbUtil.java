package util;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * DbUtil
 *
 * 【役割】
 * - DB 接続処理を1か所にまとめるユーティリティクラス。
 * - “どの DB に接続しているか” をアプリ全体から隠蔽する。
 *
 * 【なぜ必要？】
 * - Servlet や DAO が毎回 DriverManager.getConnection を呼ぶと、
 *   コードが重複し、接続情報変更時に全ファイルを書き換える必要がある。
 * - DbUtil にまとめることで一箇所で済む。
 */
public class DbUtil {

    /**
     * 【static final で定義している理由】
     *
     * - 接続 URL／ユーザー名／パスワードは固定値（定数）。
     * - final：変更不可 → 間違った再代入を防ぐ
     * - static：インスタンス化せずに使える、メモリ効率が良い
     */
    private static final String URL =
            "jdbc:mysql://localhost:3306/task_db?useSSL=false&serverTimezone=Asia/Tokyo";

    private static final String USER = "root";
    private static final String PASSWORD = "Zqc@84582658";

    /**
     * 【static Connection getConnection() の意味】
     *
     * - “DbUtil.getConnection()” と書くだけで接続できる。
     * - インスタンス化不要
     * - DAO が毎回同じ方法で接続できるため、コードが統一される。
     *
     * 【DriverManager.getConnection() の理由】
     * - MySQL 公式ドライバ（mysql-connector-j）と必ず連携する仕組み。
     *
     *
     * 
     *   
     */
    public static Connection getConnection() throws Exception {

        /**
         * 【なぜ try-with-resources をここで使わない？】
         * - Connection は呼び出し元（DAO）が終了時に close() する責務を持つため。
         * - ここで try-with-resources を使うと、即 close されてしまい使えない。
         *
         * しかし DAO 側では try-with-resources を使って
         * Connection を “自動 close” している。
         */
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
