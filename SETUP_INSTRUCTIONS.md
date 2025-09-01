# Instruksi Setup Aplikasi Inventory Management System

## Prerequisites (Yang Harus Diinstall Terlebih Dahulu)

### 1. Java Development Kit (JDK) 11 atau lebih tinggi
- Download dari: https://adoptium.net/ atau https://www.oracle.com/java/technologies/downloads/
- Set JAVA_HOME environment variable
- Pastikan `java -version` berjalan di command prompt

### 2. Maven 3.6 atau lebih tinggi
- Download dari: https://maven.apache.org/download.cgi
- Extract ke folder (misal: C:\Program Files\Apache\maven)
- Set MAVEN_HOME environment variable
- Tambahkan %MAVEN_HOME%\bin ke PATH
- Pastikan `mvn -version` berjalan di command prompt

### 3. MySQL Server 5.7 atau lebih tinggi
- Download dari: https://dev.mysql.com/downloads/mysql/
- Install dan set root password
- Pastikan service MySQL berjalan

## Setup Database

1. **Buat Database MySQL:**
```sql
CREATE DATABASE inventory CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. **Jalankan Script Setup:**
```bash
mysql -u root -p inventory < database_setup.sql
```

Atau copy-paste isi file `database_setup.sql` ke MySQL Workbench/phpMyAdmin.

## Konfigurasi Aplikasi

1. **Edit file `config.properties`:**
```properties
db.url=jdbc:mysql://localhost:3306/inventory
db.username=root
db.password=your_mysql_password
count_non_usable_as_outgoing=true
allow_negative_stock=false
```

## Build dan Jalankan

### Menggunakan Maven (Recommended)
```bash
# Build project
mvn clean compile

# Jalankan aplikasi
mvn javafx:run

# Build JAR executable
mvn clean package
```

### Menggunakan Script
- **Windows:** Double click `run.bat`
- **Linux/Mac:** 
```bash
chmod +x run.sh
./run.sh
```

### Manual Build (Jika Maven tidak tersedia)
```bash
# Compile semua Java files
javac -cp "lib/*" -d target/classes src/main/java/com/inventory/**/*.java

# Copy resources
xcopy /E /I src\main\resources target\classes

# Create JAR
jar cfm target/inventory-system.jar MANIFEST.MF -C target/classes .

# Run JAR
java -jar target/inventory-system.jar
```

## Troubleshooting

### Error "Java not found"
- Install JDK 11+ dan set JAVA_HOME
- Restart command prompt/terminal

### Error "Maven not found"
- Install Maven dan set MAVEN_HOME
- Restart command prompt/terminal

### Error Database Connection
- Pastikan MySQL berjalan
- Periksa username/password di config.properties
- Pastikan database 'inventory' sudah dibuat

### Error JavaFX
- Pastikan Java 11+ terinstall
- Jalankan dengan `mvn javafx:run`

## Struktur File yang Sudah Dibuat

```
inventory/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/inventory/
│       │       ├── controller/          # JavaFX Controllers
│       │       │   ├── MainController.java
│       │       │   └── ItemDialogController.java
│       │       ├── model/               # Data Models
│       │       │   ├── Item.java
│       │       │   ├── Transaction.java
│       │       │   └── TransactionLine.java
│       │       ├── service/             # Business Logic
│       │       │   ├── ItemService.java
│       │       │   └── TransactionService.java
│       │       ├── util/                # Utilities
│       │       │   ├── DatabaseManager.java
│       │       │   ├── ConfigManager.java
│       │       │   └── PalindromeUtil.java
│       │       └── Main.java            # Entry Point
│       └── resources/
│           ├── fxml/                    # FXML UI Files
│           │   ├── MainView.fxml
│           │   └── ItemDialog.fxml
│           └── logback.xml              # Logging Configuration
├── pom.xml                              # Maven Configuration
├── config.properties                    # Application Configuration
├── database_setup.sql                   # Database Setup Script
├── run.bat                              # Windows Run Script
├── run.sh                               # Linux/Mac Run Script
├── .gitignore                           # Git Ignore File
├── README.md                            # Main Documentation
└── SETUP_INSTRUCTIONS.md                # This File
```

## Fitur yang Sudah Diimplementasikan

✅ **Master Data Management**
- CRUD untuk Items (kode, nama, kategori, satuan, min_stock)
- Search dan filter items
- Soft delete

✅ **Transaksi Database**
- Goods In (penerimaan) dengan update stok otomatis
- Goods Out (pengeluaran) dengan validasi stok
- Transaksi DB untuk menghindari race condition

✅ **Deteksi Palindrom**
- Utility class untuk deteksi palindrom
- Label live di UI saat input nama barang
- Disimpan dalam database

✅ **Penilaian Kondisi**
- Kemampuan menandai USABLE/NOT_USABLE
- Kebijakan konfigurasi untuk barang tidak layak
- Transaksi DISPOSITION otomatis

✅ **UI JavaFX**
- Interface dengan tab: Items, Receive, Issue, Reports, Settings
- Dialog untuk add/edit item
- Validasi input dan error handling
- Status bar dan logging

✅ **Konfigurasi**
- File config.properties untuk database dan aplikasi
- Toggle kebijakan barang tidak layak
- Pengaturan database connection

## Fitur yang Akan Diimplementasikan

🔄 **Laporan dan Export**
- Export ke CSV/Excel
- Laporan harian/mingguan/bulanan yang lebih detail
- Dashboard dengan KPI

🔄 **Multi-location Support**
- Support untuk multiple warehouse/location
- Stock balance per location

🔄 **User Management**
- Login system
- Role-based access control
- Audit trail per user

## Quick Start (Setelah Setup)

1. **Jalankan aplikasi:**
```bash
mvn javafx:run
```

2. **Tambah item pertama:**
- Klik tab "Items"
- Klik "Add New Item"
- Isi: Code="ITEM001", Name="Test Item", Category="Test", Unit="pcs"
- Lihat label palindrome berubah live
- Klik Save

3. **Receive items:**
- Klik tab "Receive (Goods In)"
- Pilih item, isi quantity dan reference
- Klik "Receive Items"
- Lihat stok bertambah di tabel Items

4. **Issue items:**
- Klik tab "Issue (Goods Out)"
- Pilih item dan quantity
- Pilih kondisi USABLE/NOT_USABLE
- Klik "Issue Items"
- Lihat stok berkurang

5. **Generate report:**
- Klik tab "Reports"
- Set date range
- Klik "Generate Report"
- Lihat transaksi dalam tabel

## Support

Jika mengalami masalah:
1. Periksa log di console atau file `logs/inventory.log`
2. Pastikan semua prerequisites terinstall
3. Periksa konfigurasi database
4. Restart aplikasi dan database

## Next Steps

Setelah aplikasi berjalan:
1. Tambah beberapa item untuk testing
2. Lakukan transaksi receive dan issue
3. Test fitur palindrom dengan nama seperti "radar", "level", "deed"
4. Test kebijakan barang tidak layak di Settings
5. Generate laporan untuk melihat data transaksi 