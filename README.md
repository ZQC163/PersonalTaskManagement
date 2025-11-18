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
