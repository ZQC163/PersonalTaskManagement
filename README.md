データベース構成
CREATE DATABASE task_db DEFAULT CHARACTER SET utf8mb4;
USE task_db;

CREATE TABLE task (
  id INT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(100) NOT NULL,
  description TEXT,
  status VARCHAR(20) DEFAULT 'todo'
);
INSERT INTO task (title, description, status) VALUES
('JavaWebの勉強', 'Servlet・JSP・JDBCの基礎を復習する', 'doing'),
('週報の作成', '1週間の業務内容をまとめて上司に提出する', 'todo'),
('ジムトレーニング', 'ランニング30分＋筋トレ20分', 'done'),
('学習ノートの整理', '今週学んだことをMarkdownにまとめる', 'todo'),
('タスク管理システムの改善', '検索・ページング機能を追加予定', 'todo'),
('買い物リスト作成', '今週必要な生活用品をまとめる', 'todo'),
('技術書の読書', '「Javaパフォーマンス最適化」を読む', 'doing'),
('面接準備', '企業研究と模擬質問の練習', 'todo');
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
