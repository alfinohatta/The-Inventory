-- Inventory Management System - Supabase PostgreSQL Schema
-- Jalankan script ini di Supabase SQL Editor

-- Enable UUID extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Tabel users
CREATE TABLE IF NOT EXISTS users (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  username TEXT UNIQUE NOT NULL,
  password_hash TEXT NOT NULL,
  fullname TEXT,
  role TEXT,
  created_at TIMESTAMP DEFAULT NOW()
);

-- Tabel items
CREATE TABLE IF NOT EXISTS items (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  item_code TEXT UNIQUE NOT NULL,
  name TEXT NOT NULL,
  category TEXT,
  unit TEXT,
  min_stock INTEGER DEFAULT 0,
  is_palindrome BOOLEAN DEFAULT FALSE,
  is_active BOOLEAN DEFAULT TRUE,
  created_at TIMESTAMP DEFAULT NOW()
);

-- Tabel locations
CREATE TABLE IF NOT EXISTS locations (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  name TEXT NOT NULL
);

-- Tabel stock_balance
CREATE TABLE IF NOT EXISTS stock_balance (
  item_id UUID PRIMARY KEY REFERENCES items(id) ON DELETE CASCADE,
  quantity INTEGER NOT NULL DEFAULT 0
);

-- Tabel transactions
CREATE TABLE IF NOT EXISTS transactions (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tx_type TEXT CHECK (tx_type IN ('IN','OUT','ADJUST','DISPOSITION')) NOT NULL,
  tx_date DATE NOT NULL,
  reference TEXT,
  created_by UUID REFERENCES users(id),
  notes TEXT,
  created_at TIMESTAMP DEFAULT NOW()
);

-- Tabel transaction_lines
CREATE TABLE IF NOT EXISTS transaction_lines (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  transaction_id UUID REFERENCES transactions(id) ON DELETE CASCADE,
  item_id UUID REFERENCES items(id),
  location_id UUID REFERENCES locations(id),
  qty INTEGER NOT NULL,
  condition_status TEXT CHECK (condition_status IN ('USABLE','NOT_USABLE')) DEFAULT 'USABLE',
  unit_price NUMERIC(12,2)
);

-- Insert data default
INSERT INTO users (username, password_hash, fullname, role) 
VALUES ('admin', 'admin123', 'Administrator', 'ADMIN') 
ON CONFLICT (username) DO NOTHING;

INSERT INTO locations (name) 
VALUES ('Main Warehouse') 
ON CONFLICT DO NOTHING;

-- Buat index untuk performa
CREATE INDEX IF NOT EXISTS idx_items_code ON items(item_code);
CREATE INDEX IF NOT EXISTS idx_items_name ON items(name);
CREATE INDEX IF NOT EXISTS idx_transactions_date ON transactions(tx_date);
CREATE INDEX IF NOT EXISTS idx_transactions_type ON transactions(tx_type);
CREATE INDEX IF NOT EXISTS idx_transaction_lines_item ON transaction_lines(item_id);

-- Function untuk update stock balance otomatis
CREATE OR REPLACE FUNCTION update_stock_balance()
RETURNS TRIGGER AS $$
BEGIN
  -- Insert initial stock balance if not exists
  INSERT INTO stock_balance (item_id, quantity) 
  VALUES (NEW.item_id, 0) 
  ON CONFLICT (item_id) DO NOTHING;
  
  -- Update stock based on transaction type
  IF (NEW.tx_type = 'IN') THEN
    UPDATE stock_balance 
    SET quantity = quantity + NEW.qty 
    WHERE item_id = NEW.item_id;
  ELSIF (NEW.tx_type IN ('OUT','DISPOSITION')) THEN
    UPDATE stock_balance 
    SET quantity = quantity - NEW.qty 
    WHERE item_id = NEW.item_id;
  END IF;
  
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger untuk update stock balance otomatis
DROP TRIGGER IF EXISTS trg_update_stock ON transaction_lines;
CREATE TRIGGER trg_update_stock
  AFTER INSERT ON transaction_lines
  FOR EACH ROW EXECUTE FUNCTION update_stock_balance();

-- Function untuk deteksi palindrom (opsional, bisa di JavaFX)
CREATE OR REPLACE FUNCTION is_palindrome(text_to_check TEXT)
RETURNS BOOLEAN AS $$
DECLARE
  cleaned TEXT;
BEGIN
  -- Remove non-alphanumeric characters and convert to lowercase
  cleaned := LOWER(REGEXP_REPLACE(text_to_check, '[^a-z0-9]', '', 'g'));
  
  -- Check if palindrome
  RETURN cleaned = REVERSE(cleaned);
END;
$$ LANGUAGE plpgsql;

-- View untuk laporan transaksi
CREATE OR REPLACE VIEW transaction_report AS
SELECT 
  t.id,
  t.tx_date,
  t.reference,
  t.tx_type,
  t.notes,
  i.item_code,
  i.name as item_name,
  i.category,
  tl.qty,
  tl.condition_status,
  tl.unit_price,
  u.fullname as created_by_name,
  t.created_at
FROM transactions t
JOIN transaction_lines tl ON t.id = tl.transaction_id
JOIN items i ON tl.item_id = i.id
LEFT JOIN users u ON t.created_by = u.id
WHERE i.is_active = true
ORDER BY t.tx_date DESC, t.created_at DESC;

-- Row Level Security (RLS) - Enable untuk production
-- ALTER TABLE users ENABLE ROW LEVEL SECURITY;
-- ALTER TABLE items ENABLE ROW LEVEL SECURITY;
-- ALTER TABLE transactions ENABLE ROW LEVEL SECURITY;
-- ALTER TABLE transaction_lines ENABLE ROW LEVEL SECURITY;
-- ALTER TABLE stock_balance ENABLE ROW LEVEL SECURITY;

-- Policy untuk public access (development only)
-- CREATE POLICY "Allow public access" ON items FOR ALL USING (true);
-- CREATE POLICY "Allow public access" ON transactions FOR ALL USING (true);
-- CREATE POLICY "Allow public access" ON transaction_lines FOR ALL USING (true);
-- CREATE POLICY "Allow public access" ON stock_balance FOR ALL USING (true);

-- Tampilkan struktur tabel
\d users;
\d items;
\d locations;
\d stock_balance;
\d transactions;
\d transaction_lines;

-- Test data
SELECT 'Users:' as info;
SELECT * FROM users;
SELECT 'Locations:' as info;
SELECT * FROM locations; 