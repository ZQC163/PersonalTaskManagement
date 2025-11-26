package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import model.User;
import util.DbUtil;

/**
 * UserDao
 *
 * 【このクラスの役割】
 * - user テーブルに対する DB アクセス専用クラス。
 * - 主に「ログイン認証」のために使う。
 *
 * 【設計の考え方】
 * - Servlet に SQL を書かず、DB 操作は全て DAO に集約する。
 * - これにより、認証ロジックが変わっても Servlet 側を修正しなくてよくなる。
 */
public class UserDao {

    /**
     * ログイン認証を行うメソッド。
     *
     * 【やっていることを順番に説明】
     *
     * 1. username と password を条件に user テーブルを検索する。
     * 2. レコードが見つかれば User オブジェクトを生成して返す。
     * 3. 見つからなければ null を返し、「認証失敗」と判定させる。
     *
     * 【具体的に使っているテクニック】
     *
     * - PreparedStatement:
     *   SQL を "WHERE username = ? AND password = ?" と書いて、
     *   ? 部分に setString() で値を入れることで、SQL インジェクションを防止。
     *
     * - ResultSet:
     *   DB からの検索結果を1行ずつ取り出すためのオブジェクト。
     *   今回は PRIMARY KEY で絞るわけではないが、ユニーク制約がある前提で
     *   「1件だけ返ってくる」想定で実装している。
     *
     * - User モデル:
     *   DB の行をそのまま使わず、User クラスに詰め直すことで、
     *   上位層（Servlet）がテーブルの列名を意識しなくて済むようにしている。
     */
    public User login(String username, String password) {

        // username と password 両方一致するユーザーを探す
        String sql = "SELECT * FROM user WHERE username = ? AND password = ?";

        /**
         * 【try-with-resources を使う理由】
         * - Connection / PreparedStatement / ResultSet を自動的に close してくれる。
         * - finally で close() を書くよりも安全で、コードも短い。
         */
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // 1番目の ? に username をセット
            ps.setString(1, username);

            // 2番目の ? に password をセット
            ps.setString(2, password);

            // SELECT 文を実行 → 結果は ResultSet
            ResultSet rs = ps.executeQuery();

            /**
             * rs.next() の意味：
             * - 1行目にカーソルを進める。
             * - レコードが存在する場合 → true
             *   レコードがない場合 → false
             */
            if (rs.next()) {

                /**
                 * 【User オブジェクトに詰める理由】
                 * - 単に username だけ返すよりも、
                 *   id / display_name もまとめて持っておいた方が
                 *   後の画面表示で便利。
                 * - DB の列名を Java コード内に分散させないため、
                 *   ここだけで getString("xxx") を呼ぶようにする。
                 */
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

        // 認証失敗（該当ユーザーなし）の場合は null を返す
        return null;
    }
}
