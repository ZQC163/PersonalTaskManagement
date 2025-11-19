プロジェクト構成
src/
 ├── model/
 │     └── Task.java
 ├── dao/
 │     └── TaskDao.java
 ├── util/
 │     └── DbUtil.java
 └── servlet/
       └── TaskServlet.java

WebContent/
 ├── list.jsp
 └── form.jsp
WEB-INF/
 └── web.xml



データベース構成
CREATE TABLE task (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255),
    description TEXT,
    status VARCHAR(20),
    deadline DATE,
    importance VARCHAR(10)
);

INSERT INTO task (title, description, status, deadline, importance) VALUES
('要件定義書の作成', '顧客向けの要件定義書をまとめる', 'doing', '2025-02-10', '高'),
('デザイン修正', 'トップページUIの修正作業', 'todo', '2025-02-14', '中'),
('テストケース作成', '新機能に対するテスト項目を作成', 'todo', '2025-02-18', '高'),
('コードレビュー', 'PR内容を確認しレビューする', 'doing', '2025-02-09', '中'),
('バグ修正（検索機能）', '検索結果が正しく表示されない問題の修正', 'todo', '2025-02-15', '高'),
('DBバックアップ', '毎月の定期バックアップを実施', 'done', '2025-02-01', '低'),
('ミーティング資料作成', '週次MTG資料を準備する', 'doing', '2025-02-12', '中'),
('ログ監視改善', 'エラーログの分析と改善案提出', 'todo', '2025-02-25', '低'),
('API仕様書更新', '新規APIの仕様書を更新', 'todo', '2025-02-20', '高'),
('新機能アイデア出し', '次期バージョンの候補を検討する', 'todo', '2025-02-28', '低');

要件定義
● ToDo(タスク管理)アプリ要件
○ ToDoを登録できる
○ 登録したToDoを一覧できる
○ 登録したToDoを編集、削除できる
○ ToDoの状態の変更ができる (登録したToDoの終了チェックができる )
● ToDo1件に含まれる情報(必須)
○ 題名
○ 内容(重複あり)
○ 期限(年月日)
○ 状態(終了/未着手/作業中など)
