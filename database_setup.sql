-- Inventory Management System Database Setup
-- Jalankan script ini untuk membuat database dan tabel

-- Buat database
CREATE DATABASE IF NOT EXISTS inventory CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Gunakan database
USE inventory;

-- Tabel users
CREATE TABLE IF NOT EXISTS users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) UNIQUE NOT NULL,
  password_hash VARCHAR(256) NOT NULL,
  fullname VARCHAR(100),
  role VARCHAR(30),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabel items
CREATE TABLE IF NOT EXISTS items (
  id INT AUTO_INCREMENT PRIMARY KEY,
  item_code VARCHAR(50) UNIQUE NOT NULL,
  name VARCHAR(255) NOT NULL,
  category VARCHAR(100),
  unit VARCHAR(50),
  min_stock INT DEFAULT 0,
  is_palindrome BOOLEAN DEFAULT FALSE,
  is_active BOOLEAN DEFAULT TRUE,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabel locations
CREATE TABLE IF NOT EXISTS locations (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL
);

-- Tabel stock_balance
CREATE TABLE IF NOT EXISTS stock_balance (
  item_id INT PRIMARY KEY,
  quantity INT NOT NULL DEFAULT 0,
  FOREIGN KEY (item_id) REFERENCES items(id) ON DELETE CASCADE
);

-- Tabel transactions
CREATE TABLE IF NOT EXISTS transactions (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  tx_type ENUM('IN','OUT','ADJUST','DISPOSITION') NOT NULL,
  tx_date DATE NOT NULL,
  reference VARCHAR(100),
  created_by INT,
  notes TEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (created_by) REFERENCES users(id)
);

-- Tabel transaction_lines
CREATE TABLE IF NOT EXISTS transaction_lines (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  transaction_id BIGINT NOT NULL,
  item_id INT NOT NULL,
  location_id INT,
  qty INT NOT NULL,
  condition_status ENUM('USABLE','NOT_USABLE') DEFAULT 'USABLE',
  unit_price DECIMAL(12,2),
  FOREIGN KEY (transaction_id) REFERENCES transactions(id) ON DELETE CASCADE,
  FOREIGN KEY (item_id) REFERENCES items(id),
  FOREIGN KEY (location_id) REFERENCES locations(id)
);

-- Insert data default
INSERT IGNORE INTO users (username, password_hash, fullname, role) VALUES ('admin', 'admin123', 'Administrator', 'ADMIN');
INSERT IGNORE INTO locations (name) VALUES ('Main Warehouse');

-- Buat index untuk performa
CREATE INDEX idx_items_code ON items(item_code);
CREATE INDEX idx_items_name ON items(name);
CREATE INDEX idx_transactions_date ON transactions(tx_date);
CREATE INDEX idx_transactions_type ON transactions(tx_type);
CREATE INDEX idx_transaction_lines_item ON transaction_lines(item_id);

-- Tampilkan struktur tabel
DESCRIBE users;
DESCRIBE items;
DESCRIBE locations;
DESCRIBE stock_balance;
DESCRIBE transactions;
DESCRIBE transaction_lines;

-- Tampilkan data default
SELECT 'Users:' as info;
SELECT * FROM users;
SELECT 'Locations:' as info;
SELECT * FROM locations; 