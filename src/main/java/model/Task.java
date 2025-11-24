package model;

/**
 * Task エンティティクラス
 *
 * task テーブルの 1 レコードを表す。
 * ユーザーに紐づくタスク情報を保持する。
 */
public class Task {

    /** タスクID（主キー） */
    private int id;

    /** タイトル */
    private String title;

    /** 詳細内容 */
    private String description;

    /** 状態：todo / doing / done */
    private String status;

    /** 締切日（YYYY-MM-DD） */
    private String deadline;

    /** 重要度：高 / 中 / 低 */
    private String importance;

    /** このタスクを所有するユーザーID（外部キー） */
    private int userId;

    public Task() {}

    /** 新規登録用 */
    public Task(String title, String description, String status,
                String deadline, String importance, int userId) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.deadline = deadline;
        this.importance = importance;
        this.userId = userId;
    }

    /** 更新用 */
    public Task(int id, String title, String description, String status,
                String deadline, String importance, int userId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.deadline = deadline;
        this.importance = importance;
        this.userId = userId;
    }

    // ------- Getter / Setter -------

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getImportance() {
        return importance;
    }

    public void setImportance(String importance) {
        this.importance = importance;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}

