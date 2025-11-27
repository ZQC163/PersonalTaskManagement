CREATE DATABASE  task_db;
USE task_db;

-- ユーザーテーブル
CREATE TABLE  user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    display_name VARCHAR(100)
);

-- タスクテーブル（ユーザーごとのタスク）
CREATE TABLE IF NOT EXISTS task (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100),
    description TEXT,
    status VARCHAR(20),
    deadline DATE,
    importance VARCHAR(10),
    user_id INT,
    CONSTRAINT fk_task_user FOREIGN KEY (user_id)
        REFERENCES user(id)
        ON DELETE CASCADE
);

-- サンプルユーザー
INSERT INTO user (username, password, display_name)
VALUES ('testuser', 'pass123', 'テストユーザー');

-- サンプルタスク
INSERT INTO task (title, description, status, deadline, importance, user_id)
VALUES 
('資料作成', '会議用の資料を作成する', '未着手', '2025-12-01', '高', 1),
('メール対応', '顧客メールに返信する', '進行中', '2025-12-05', '中', 1);

CREATE TABLE task_simple (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255),
    description TEXT,
    status VARCHAR(50),
    deadline DATE,
    importance VARCHAR(10)
);

INSERT INTO task_simple (title, description, status, deadline, importance) VALUES
('資料作成', '会議用の資料を作成する', '未着手', '2025-02-01', '高'),
('メール返信', '取引先へ返信', '進行中', '2025-02-05', '中');
