package model;

/**
 * task テーブル 1 行分を表すモデルクラス
 */
public class Task {

    private int id;
    private String title;
    private String description;
    private String status;
    private String deadline;     // "YYYY-MM-DD" 形式の文字列として扱う
    private String importance;   // "高" / "中" / "低"
    private int userId;          // user.id への外部キー

    public Task() {
    }

    // 新規登録用（id なし）
    public Task(String title, String description, String status,
                String deadline, String importance, int userId) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.deadline = deadline;
        this.importance = importance;
        this.userId = userId;
    }

    // 更新用（id あり）
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

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public String getDeadline() {
        return deadline;
    }

    public String getImportance() {
        return importance;
    }

    public int getUserId() {
        return userId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public void setImportance(String importance) {
        this.importance = importance;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
