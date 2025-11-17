package model;

/**
 * Task（タスク）エンティティクラス
 *
 * このクラスは task テーブルの1レコードを表します。
 * JavaBeans の形式で作成されており、
 * - Servlet（コントローラ）
 * - DAO（データアクセス）
 * - JSP（画面表示）
 * の間でデータを受け渡しするために使用されます。
 *
 * フィールド：
 *  id          … 主キー（AUTO_INCREMENT）
 *  title       … タスクのタイトル
 *  description … タスクの内容・詳細
 *  status      … タスクの状態（todo / doing / done）
 */
public class Task {

    /** タスクID（主キー） */
    private int id;

    /** タスクタイトル */
    private String title;

    /** タスク内容（説明） */
    private String description;

    /** タスク状態：todo（未着手） / doing（進行中） / done（完了） */
    private String status;


    /** デフォルトコンストラクタ */
    public Task() {}


    /** 新規登録用（IDなし） */
    public Task(String title, String description, String status) {
        this.title = title;
        this.description = description;
        this.status = status;
    }


    /** 更新用（IDあり） */
    public Task(int id, String title, String description, String status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
    }


    /** Getter / Setter ----------------------------- */

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }


    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }


    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }


    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }
}
