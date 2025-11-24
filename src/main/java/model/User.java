package model;

/**
 * User エンティティクラス
 * user テーブルの 1 レコードを表す。
 * ログイン認証や画面表示（ユーザー名表示）に使用する。
 */
public class User {

    /** ユーザーID（主キー） */
    private int id;

    /** ログイン用ユーザー名 */
    private String username;

    /** パスワード */
    private String password;

    /** 画面に表示する名前 */
    private String displayName;

    public User() {}

    public User(int id, String username, String password, String displayName) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.displayName = displayName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
