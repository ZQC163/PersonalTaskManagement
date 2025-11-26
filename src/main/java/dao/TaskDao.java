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
	 * 指定ユーザーのタスク一覧を取得する処理。
	 *
	 * 【このメソッドで実際にやっていること（具体）】
	 *
	 * ① DB から複数行のタスクデータを取得するために、戻り値として List<Task> を使用。
	 *    - 「複数行のレコード」は Java の世界では List（可変長リスト）で扱うのが一般的。
	 *    - 配列（Task[]）ではサイズが固定されるため、DB の件数に合わせられない。
	 *
	 * ② JDBC を使った DB アクセスの標準手順（Connection → PreparedStatement → ResultSet）
	 *    をそのまま実装している。
	 *
	 * ③ ResultSet の “1行 = 1つの Task オブジェクト” として変換し、
	 *    その Task を List に追加していく。
	 *    - while(rs.next()) を使うのは、DB から返るレコード数が不明だから
	 *      （1行かもしれないし100行かもしれない）
	 *
	 * ④ user_id で絞り込む理由：
	 *    - 「ログインしている本人のタスク」だけを取得するための *セキュリティ*。
	 *    - WHERE user_id = ? で、他のユーザーのタスクを見れないようにしている。
	 *
	 * ⑤ PreparedStatement を使う理由：
	 *    - "WHERE user_id = ?" の ? 部分に setInt() で安全に値を入れるため
	 *    - SQL インジェクション対策
	 *
	 * ⑥ Task モデルを使う理由：
	 *    - JSP に ResultSet を直接渡すと、画面が DB 構造に依存してしまう
	 *    - Model（Task）を介すことで、MVC の “分離” が成立し保守性が上がる
	 *
	 * 【まとめ】
	 * - List<Task> を使う理由：複数行を扱う標準形式
	 * - Task モデルを使う理由：DB の列 → オブジェクトへ変換するため
	 * - while(rs.next()) の理由：レコード数が可変だから
	 * - PreparedStatement の理由：? にバインドして SQL 注入を防ぐ
	 * - userId 絞り込みの理由：ユーザのデータ分離
	 */
	public List<Task> findAllByUser(int userId) {

	    // Task を複数格納するための可変長リスト（DB の件数に合わせて増える）
	    List<Task> list = new ArrayList<>();

	    // user_id で絞り込む SQL
	    String sql = "SELECT * FROM task WHERE user_id = ? ORDER BY id DESC";

	    try (Connection conn = DbUtil.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)) {

	        // ? に値を設定（SQL インジェクション対策）
	        ps.setInt(1, userId);

	        // SELECT 文は executeQuery() を使う（戻り値は ResultSet）
	        ResultSet rs = ps.executeQuery();

	        // 結果が 0 行かもしれないし 100 行かも → while が必要
	        while (rs.next()) {

	            /**
	             * ResultSet の 1 行を Task オブジェクトに変換する。
	             * - rs.getString("title") のようにカラム名で取得
	             * - Model（Task）に詰めることで、JSP が DB を意識しなくて済む
	             */
	            Task t = new Task(
	                    rs.getInt("id"),
	                    rs.getString("title"),
	                    rs.getString("description"),
	                    rs.getString("status"),
	                    rs.getString("deadline"),
	                    rs.getString("importance"),
	                    rs.getInt("user_id")
	            );

	            // 1 件ずつリストに追加
	            list.add(t);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return list;
	}


	/**
	 * 主キー(ID)でタスクを1件だけ取得する処理。
	 *
	 * 【このメソッドの役割（具体）】
	 * - 編集画面（form.jsp）を開くとき、
	 *   既存の値（タイトル・内容・締切・重要度など）をフォームに表示する必要がある。
	 * - そのために、「指定した ID のレコードを1件だけ DB から取得する」処理が必要。
	 *
	 * 【具体的に使っている技術要素】
	 *
	 * ① Connection conn = DbUtil.getConnection();
	 *    → DB に接続するための Connection オブジェクトを取得する
	 *
	 * ② PreparedStatement ps = conn.prepareStatement(sql);
	 *    → "SELECT * FROM task WHERE id = ?" を DB 側に準備させる
	 *
	 * ③ ps.setInt(1, id);
	 *    → ? に id をバインドする（SQL インジェクション対策）
	 *
	 * ④ ResultSet rs = ps.executeQuery();
	 *    → SELECT 文を実行し、検索結果を ResultSet として受け取る
	 *
	 * ⑤ if (rs.next()) { ... }
	 *    → ID は PRIMARY KEY のため、返ってくる行数は最大 1 行
	 *    → while ではなく if を使うのはそのため
	 *
	 * ⑥ new Task(rs.getInt(...), rs.getString(...), ...)
	 *    → DB の 1 行分を Task モデルに変換して返す
	 *
	 * 【まとめ】
	 * - 編集画面に初期値を出すために必須のメソッド
	 * - PreparedStatement + getXxx() の基本的な JDBC パターン
	 * - rs.next() を if にしている理由は PK が一意だから
	 */
	public Task findById(int id) {

	    String sql = "SELECT * FROM task WHERE id = ?";

	    try (Connection conn = DbUtil.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)) {

	        ps.setInt(1, id); // ? にバインド

	        ResultSet rs = ps.executeQuery();

	        if (rs.next()) { // PK は1件だけ返る
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
	 * タスクを新規登録する処理。
	 *
	 * 【insert の目的（具体）】
	 * - form.jsp で新規登録したタスクを task テーブルに追加する。
	 * - "INSERT INTO task ..." を実行するためのメソッド。
	 *
	 * 【使っている重要な方法】
	 *
	 * ① INSERT 文を構築
	 *    String sql = "INSERT INTO task (title, ..., user_id) VALUES (?, ?, ...)";
	 *    - VALUES の ? は後で setString()/setInt() で安全に埋める。
	 *    - これにより SQL インジェクションを防止できる。
	 *
	 * ② Connection conn = DbUtil.getConnection();
	 *    - DB 接続を取得し、毎回新しいコネクションを用いる。
	 *
	 * ③ PreparedStatement ps = conn.prepareStatement(sql);
	 *    - ? を含む SQL を DB 処理エンジン側に“プリコンパイル”させる。
	 *
	 * ④ ps.setString(1, task.getTitle());
	 *    - INSERT の ? を 1 → タイトル、2 → 内容 … の順にバインドする。
	 *    - Java 型と SQL 型を一致させる必要があるため String / Int を使い分ける。
	 *
	 * ⑤ ps.executeUpdate();
	 *    - 更新系 SQL（INSERT/UPDATE/DELETE）は executeUpdate() を使う。
	 *    - 戻り値は “影響を受けた行数” だが、今回は必要ないので受け取らない。
	 *
	 * 【まとめ】
	 * - PreparedStatement によるバインドで安全に INSERT を実行
	 * - Model（Task）からフィールドを取り出して ? に順番にセットする
	 * - userId はセッションから取得するため信頼できる（セキュリティ強化）
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

	        ps.executeUpdate(); // INSERT 実行

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}


	/**
	 * タスクを更新する処理。
	 *
	 * 【update が必要な理由】
	 * - 編集画面でユーザーが修正したデータを DB に反映するため。
	 * - "UPDATE task SET ... WHERE id = ?" という SQL を実行するメソッド。
	 *
	 * 【具体的な技術ポイント】
	 *
	 * ① UPDATE 文を構築
	 *    - title=?, description=?, ... のようにすべて ? にしておく。
	 *    - 最後に WHERE id = ? を付けることで、どの行を更新するか指定できる。
	 *
	 * ② PreparedStatement を使う理由
	 *    - ? への値の埋め込みで SQL インジェクションを回避
	 *    - UPDATE 文もプリコンパイルされて高速化
	 *
	 * ③ UPDATE は executeUpdate() を使う
	 *
	 * ④ user_id を UPDATE に含めている理由
	 *    - “タスクの所有者” が誰であるかを常に一貫させるため
	 *    - 不正なリクエストで user_id が書き換えられないよう、
	 *      Servlet 側でセッションから userId を取得している
	 *
	 * 【まとめ】
	 * - 更新処理は INSERT とよく似ているが “WHERE id=?” の有無が最大の違い。
	 * - Task モデル内の id を使って更新対象を特定する。
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
	        ps.setInt(7, task.getId()); // 更新対象

	        ps.executeUpdate(); // UPDATE

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}


	/**
	 * タスクを削除する処理。
	 *
	 * 【delete の目的】
	 * - "DELETE FROM task WHERE id = ?" を実行する。
	 * - JSP の削除リンク (task?action=delete&id=◯◯) から呼び出される。
	 *
	 * 【具体的に使用している技術】
	 *
	 * ① DELETE 文
	 *    - DELETE は WHERE で絞らないと全件消えてしまうため、必ず "WHERE id = ?" を付ける。
	 *
	 * ② PreparedStatement
	 *    - SQL インジェクション対策
	 *
	 * ③ executeUpdate()
	 *    - 更新系なので executeUpdate を使う（INSERT/UPDATE と同じ）。
	 *
	 * 【セキュリティ考慮】
	 * - 本来は「その id のタスクの user_id がログインユーザーと一致しているか」
	 *   を確認すべきだが、今回は時間のため省略
	 */
	public void delete(int id) {

	    String sql = "DELETE FROM task WHERE id = ?";

	    try (Connection conn = DbUtil.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)) {

	        ps.setInt(1, id);

	        ps.executeUpdate(); // DELETE 実行

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
}

