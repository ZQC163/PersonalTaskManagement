package model;

/**
 * Task（タスク）エンティティクラス
 *
 * task テーブルの 1 レコードを表し、
 * DAO（DBアクセス）、Servlet（コントローラ）、JSP（画面表示）
 * の間でデータを渡すための JavaBean。
 */
public class Task {

    /** タスクID（主キー） */
    private int id;

    /** タイトル */
    private String title;

    /** 内容（詳細） */
    private String description;

    /** 状態：todo / doing / done */
    private String status;

    /** 締切日（YYYY-MM-DD） */
    private String deadline;

    /** 重要度：高 / 中 / 低 */
    private String importance;


    /** デフォルトコンストラクタ */
    public Task() {}

    /** 新規登録用 */
    public Task(String title, String description, String status,
                String deadline, String importance) {

        this.title = title;
        this.description = description;
        this.status = status;
        this.deadline = deadline;
        this.importance = importance;
    }

    /** 更新用（IDあり） */
    public Task(int id, String title, String description, String status,
                String deadline, String importance) {

        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.deadline = deadline;
        this.importance = importance;
    }

    // ------ Getter / Setter -----

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDeadline() { return deadline; }
    public void setDeadline(String deadline) { this.deadline = deadline; }

    public String getImportance() { return importance; }
    public void setImportance(String importance) { this.importance = importance; }
}
