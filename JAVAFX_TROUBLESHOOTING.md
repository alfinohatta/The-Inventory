# 🚨 JavaFX Troubleshooting Guide

## 🔍 **Error: JavaFX runtime components are missing**

Error ini terjadi karena JavaFX runtime components tidak tersedia di classpath atau ada masalah kompatibilitas versi.

## 🎯 **Solusi Berdasarkan Java Version**

### **Java 17 (Recommended)**
```bash
# Build project
mvn clean compile

# Run dengan JavaFX Maven plugin
mvn javafx:run

# Atau gunakan launcher script
run-javafx.bat    # Windows
./run-javafx.sh   # Linux/Mac
```

### **Java 23 (Current)**
```bash
# Build project
mvn clean compile

# Run dengan JavaFX Maven plugin (Recommended)
mvn javafx:run

# Atau gunakan Java 23 launcher script
run-java23.bat    # Windows
./run-java23.sh   # Linux/Mac

# Atau manual command dengan semua opens yang diperlukan
java --module-path "target/classes" \
     --add-modules javafx.controls,javafx.fxml \
     --add-opens javafx.graphics/javafx.scene=ALL-UNNAMED \
     --add-opens javafx.controls/javafx.scene.control=ALL-UNNAMED \
     --add-opens javafx.base/javafx.beans.property=ALL-UNNAMED \
     --add-opens javafx.base/javafx.beans.value=ALL-UNNAMED \
     --add-opens javafx.base/javafx.beans=ALL-UNNAMED \
     --add-opens javafx.base/javafx.collections=ALL-UNNAMED \
     --add-opens javafx.graphics/javafx.css=ALL-UNNAMED \
     --add-opens javafx.graphics/javafx.geometry=ALL-UNNAMED \
     --add-opens javafx.graphics/javafx.scene.text=ALL-UNNAMED \
     --add-opens javafx.graphics/javafx.scene.transform=ALL-UNNAMED \
     -cp "target/classes" \
     com.inventory.Main
```

## 🚀 **Multiple Launch Options**

### **Option 1: Maven JavaFX Plugin (Recommended)**
```bash
mvn javafx:run
```
**Keuntungan**: Otomatis handle semua module path dan dependencies
**Kekurangan**: Perlu Maven terinstall

### **Option 2: JavaFX Launcher Scripts**
- **Windows**: `run-javafx.bat` atau `run-java23.bat`
- **Linux/Mac**: `run-javafx.sh` atau `run-java23.sh`

**Keuntungan**: Tidak perlu Maven, langsung Java command
**Kekurangan**: Perlu build project dulu dengan `mvn clean compile`

### **Option 3: Manual Java Command**
```bash
java --module-path "target/classes" \
     --add-modules javafx.controls,javafx.fxml \
     [additional --add-opens flags] \
     -cp "target/classes" \
     com.inventory.Main
```

## 🔧 **Troubleshooting Steps**

### **Step 1: Check Java Version**
```bash
java -version
```
**Expected**: Java 17+ atau Java 23
**Problem**: Java 8-11 tidak support JavaFX built-in

### **Step 2: Check Project Build**
```bash
# Pastikan target/classes ada
ls target/classes

# Build ulang jika perlu
mvn clean compile
```

### **Step 3: Check JavaFX Dependencies**
```bash
# Lihat dependencies yang di-download
mvn dependency:tree | grep javafx

# Expected output:
# org.openjfx:javafx-controls:jar:17.0.2:compile
# org.openjfx:javafx-fxml:jar:17.0.2:compile
```

### **Step 4: Check Module Path**
```bash
# Pastikan JavaFX modules tersedia
java --list-modules | grep javafx

# Expected output:
# javafx.controls
# javafx.fxml
# javafx.graphics
# javafx.base
```

## 🚨 **Common Issues & Solutions**

### **Issue 1: Module not found: javafx.controls**
**Cause**: JavaFX modules tidak tersedia di module path
**Solution**: 
```bash
# Gunakan Maven JavaFX plugin
mvn javafx:run

# Atau download JavaFX SDK dan set module path
export PATH_TO_FX="path/to/javafx-sdk/lib"
java --module-path $PATH_TO_FX --add-modules javafx.controls,javafx.fxml ...
```

### **Issue 2: ClassNotFoundException: javafx.application.Application**
**Cause**: JavaFX runtime tidak tersedia di classpath
**Solution**: Pastikan menggunakan `--add-modules` bukan `-cp` untuk JavaFX

### **Issue 3: Access denied (javafx.graphics)**
**Cause**: Module system restrictions di Java 9+
**Solution**: Tambahkan `--add-opens` flags yang diperlukan

### **Issue 4: JavaFX version compatibility**
**Problem**: Java 23 + JavaFX 17 tidak kompatibel
**Solution**: 
- Upgrade ke JavaFX 21 (sudah diupdate di pom.xml)
- Atau downgrade ke Java 17

## 📋 **Version Compatibility Matrix**

| Java Version | JavaFX Version | Status | Notes |
|--------------|----------------|---------|-------|
| Java 8       | JavaFX 8       | ❌ EOL  | Tidak support modern features |
| Java 11      | JavaFX 11      | ⚠️ Limited | Perlu manual module path |
| Java 17      | JavaFX 17      | ✅ Best   | Recommended combination |
| Java 21      | JavaFX 21      | ✅ Good   | Modern features |
| Java 23      | JavaFX 21      | ⚠️ Limited | Compatibility issues |

## 🎯 **Recommended Setup**

### **For Development (Recommended)**
```bash
# Install Java 17
# Download dari adoptium.net

# Build project
mvn clean compile

# Run dengan Maven
mvn javafx:run
```

### **For Production**
```bash
# Package dengan Maven Shade plugin
mvn clean package

# Run JAR
java -jar target/inventory-system-1.0.0.jar
```

## 🔍 **Debug Commands**

### **Check JavaFX Modules**
```bash
java --list-modules | grep javafx
```

### **Check Dependencies**
```bash
mvn dependency:tree
```

### **Check Classpath**
```bash
mvn exec:exec -Dexec.executable="java" -Dexec.args="-cp %classpath com.inventory.Main"
```

### **Verbose Module Loading**
```bash
java --module-path "target/classes" \
     --add-modules javafx.controls,javafx.fxml \
     --add-opens javafx.graphics/javafx.scene=ALL-UNNAMED \
     -cp "target/classes" \
     -verbose:module \
     com.inventory.Main
```

## 📞 **Getting Help**

### **If Maven JavaFX Plugin Fails**
1. Check Maven version: `mvn -version`
2. Check Java version: `java -version`
3. Clean and rebuild: `mvn clean compile`
4. Check internet connection for dependencies

### **If Direct Java Command Fails**
1. Check if `target/classes` exists
2. Verify all `--add-opens` flags are included
3. Try with Maven JavaFX plugin instead
4. Consider downgrading to Java 17

### **If Still Having Issues**
1. Check [JavaFX Documentation](https://openjfx.io/)
2. Check [Maven JavaFX Plugin](https://github.com/openjfx/javafx-maven-plugin)
3. Check Java version compatibility
4. Consider using Java 17 + JavaFX 17 combination

## 🎉 **Success Criteria**

Aplikasi berhasil dijalankan jika:
- ✅ JavaFX window muncul
- ✅ UI elements ter-render dengan benar
- ✅ Tidak ada error di console
- ✅ Aplikasi bisa di-interact (klik, input, dll)

---

**Remember**: Java 17 + JavaFX 17 adalah kombinasi yang paling stabil dan recommended untuk development! 