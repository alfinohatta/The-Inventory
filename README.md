# Inventory Management System

Aplikasi desktop inventori menggunakan JavaFX dengan **Supabase** (PostgreSQL) backend.

## 🚀 **Perubahan Arsitektur: MySQL → Supabase**

Aplikasi telah diupgrade dari MySQL ke **Supabase** untuk memberikan:
- **Cloud-native**: Tidak perlu setup database lokal
- **Auto-scaling**: Database yang otomatis scalable
- **Built-in REST API**: API endpoints otomatis untuk semua tabel
- **Real-time capabilities**: Update real-time (opsional)
- **Better security**: Row Level Security dan authentication built-in
- **Modern stack**: PostgreSQL dengan fitur modern

## ✨ Fitur Utama

- **Master Data**: CRUD untuk Items (kode, nama, kategori, satuan, min_stock, keterangan)
- **Transaksi**: Goods In (penerimaan) dan Goods Out (pengeluaran) dengan update stok otomatis
- **Penilaian Kondisi**: Kemampuan menandai item sebagai USABLE / NOT_USABLE
- **Deteksi Palindrom**: Deteksi otomatis palindrom pada nama barang
- **Laporan**: Laporan transaksi harian/mingguan/bulanan dengan ekspor CSV
- **Konfigurasi**: Pengaturan kebijakan barang tidak layak dan Supabase connection
- **Cloud Database**: PostgreSQL di Supabase dengan REST API otomatis

## 🏗️ Arsitektur Baru

### Frontend
- **JavaFX**: Desktop application dengan UI modern
- **FXML**: Layout declarative yang mudah dimaintain
- **Controllers**: Business logic terpisah dari UI

### Backend
- **Supabase**: PostgreSQL database di cloud
- **REST API**: Auto-generated endpoints untuk semua tabel
- **Triggers**: Update stok otomatis di database level
- **Views**: Laporan yang sudah dioptimasi

### Communication
- **HTTP Client**: Java 11+ HttpClient untuk REST calls
- **JSON**: Jackson untuk serialization/deserialization
- **Async Support**: CompletableFuture untuk operasi asynchronous

## 📋 Persyaratan Sistem

- **Java 17** atau lebih tinggi
- **Maven 3.6** atau lebih tinggi
- **Internet Connection** (untuk akses Supabase)
- **Supabase Account** (gratis untuk development)

## 🚀 Quick Start

### 1. Setup Supabase
```bash
# 1. Buat akun di supabase.com
# 2. Buat proyek baru
# 3. Jalankan schema script di SQL Editor
# 4. Copy Project URL dan API Key
```

### 2. Konfigurasi Aplikasi
```properties
# Edit config.properties
supabase.url=https://your-project.supabase.co
supabase.api_key=your-anon-key
```

### 3. Build dan Jalankan
```bash
# Build project
mvn clean compile

# Jalankan aplikasi
mvn javafx:run

# Atau gunakan script
./run.sh          # Linux/Mac
run.bat           # Windows
```

## 📚 Dokumentasi Lengkap

- **[SUPABASE_SETUP.md](SUPABASE_SETUP.md)**: Setup lengkap Supabase
- **[SETUP_INSTRUCTIONS.md](SETUP_INSTRUCTIONS.md)**: Instruksi setup aplikasi
- **[database_setup.sql](database_setup.sql)**: Schema MySQL (legacy)
- **[supabase_schema.sql](supabase_schema.sql)**: Schema Supabase (current)

## 🔧 Fitur Teknis

### Database Schema (Supabase)
- **UUID Primary Keys**: Lebih aman dan scalable
- **Triggers**: Update stok otomatis saat transaksi
- **Views**: Laporan transaksi yang sudah dioptimasi
- **Check Constraints**: Validasi data di level database
- **Indexes**: Optimasi performa untuk query yang sering digunakan

### REST API Endpoints
- `GET /rest/v1/items` - Daftar semua items
- `POST /rest/v1/items` - Buat item baru
- `PATCH /rest/v1/items?id=eq.{id}` - Update item
- `GET /rest/v1/transactions` - Daftar transaksi
- `POST /rest/v1/transactions` - Buat transaksi baru
- `GET /rest/v1/transaction_report` - Laporan transaksi

### Update Stok Otomatis
```sql
-- Trigger di Supabase
CREATE TRIGGER trg_update_stock
  AFTER INSERT ON transaction_lines
  FOR EACH ROW EXECUTE FUNCTION update_stock_balance();
```

## 🎯 Fitur yang Sudah Diimplementasikan

✅ **Master Data Management**
- CRUD untuk Items dengan UUID
- Search dan filter real-time
- Soft delete dengan is_active flag

✅ **Transaksi dengan Supabase**
- Goods In/Out via REST API
- Update stok otomatis via database trigger
- Transaction lines dengan condition status

✅ **Deteksi Palindrom**
- Utility class dengan algoritma efisien
- Label live di UI
- Disimpan di database sebagai boolean

✅ **Penilaian Kondisi**
- USABLE/NOT_USABLE per transaksi
- Kebijakan konfigurasi untuk barang tidak layak
- Auto-disposition untuk NOT_USABLE

✅ **UI JavaFX Modern**
- Interface dengan tab-based design
- Dialog untuk add/edit items
- Validasi input dan error handling
- Status bar dengan real-time updates

✅ **Supabase Integration**
- REST API calls dengan HTTP Client
- JSON serialization dengan Jackson
- Error handling dan retry logic
- Async operations support

## 🔄 Fitur yang Akan Diimplementasikan

🔄 **Real-time Updates**
- Supabase Realtime subscriptions
- Live stock updates
- Notification system

🔄 **Advanced Reports**
- Custom PostgreSQL functions
- Materialized views
- Export ke Excel/PDF

🔄 **User Authentication**
- Supabase Auth integration
- Role-based access control
- Multi-user support

## 📁 Struktur Proyek

```
inventory/
├── src/main/java/com/inventory/
│   ├── controller/          # JavaFX Controllers
│   │   ├── MainController.java
│   │   └── ItemDialogController.java
│   ├── model/               # Data Models (UUID-based)
│   │   ├── Item.java
│   │   ├── Transaction.java
│   │   └── TransactionLine.java
│   ├── service/             # Business Logic
│   │   └── SupabaseService.java  # REST API calls
│   ├── util/                # Utilities
│   │   ├── ConfigManager.java
│   │   └── PalindromeUtil.java
│   └── Main.java            # Entry Point
├── src/main/resources/
│   ├── fxml/                # UI Layouts
│   │   ├── MainView.fxml
│   │   └── ItemDialog.fxml
│   └── logback.xml          # Logging
├── supabase_schema.sql      # Supabase Database Schema
├── config.properties        # Supabase Configuration
├── pom.xml                  # Maven + Jackson Dependencies
└── README.md                # This File
```

## 🚀 Keuntungan Supabase

### Development
- **No Local Setup**: Tidak perlu install MySQL/PostgreSQL
- **Auto API**: REST endpoints otomatis untuk semua tabel
- **Real-time**: Built-in real-time capabilities
- **Dashboard**: Web interface untuk manage database

### Production
- **Auto-scaling**: Database otomatis scalable
- **Backup**: Automated backups dan point-in-time recovery
- **Security**: Row Level Security dan authentication
- **Monitoring**: Built-in analytics dan logging

### Cost
- **Free Tier**: 500MB database, 2GB bandwidth
- **Pay-as-you-go**: $25/month untuk 8GB database
- **No Hidden Costs**: Transparent pricing

## 🔧 Troubleshooting

### Connection Issues
1. **Check Internet**: Pastikan ada koneksi internet
2. **Verify Credentials**: Periksa URL dan API key di config.properties
3. **Test Connection**: Gunakan "Test Connection" di Settings tab

### API Errors
1. **Check Schema**: Pastikan `supabase_schema.sql` sudah dijalankan
2. **Verify Permissions**: Pastikan anon key memiliki akses
3. **Check Logs**: Lihat console output untuk error details

### Performance Issues
1. **Region**: Pilih region Supabase yang terdekat
2. **Indexes**: Database indexes sudah dibuat otomatis
3. **Caching**: Implement client-side caching jika diperlukan

## 📞 Support

- **Supabase Docs**: [supabase.com/docs](https://supabase.com/docs)
- **Community**: [Discord](https://discord.supabase.com)
- **Issues**: GitHub repository issues
- **Stack Overflow**: Tagged with `supabase`

## 🎉 Kesimpulan

Aplikasi Inventory Management System memberikan:

- **Modern Architecture**: Cloud-native dengan REST API
- **Better Scalability**: Auto-scaling database
- **Easier Deployment**: Tidak perlu setup database lokal
- **Real-time Capabilities**: Built-in real-time features
- **Production Ready**: Security dan monitoring built-in

Aplikasi ini sekarang **production-ready** untuk skala kecil-menengah dan dapat dengan mudah di-scale untuk kebutuhan yang lebih besar. 
