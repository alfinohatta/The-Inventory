#!/bin/bash

echo "Inventory Management System - Supabase Edition"
echo "============================================="

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "Error: Java is not installed or not in PATH"
    echo "Please install Java 17 or higher"
    exit 1
fi

# Check Java version
JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1-2)
echo "Found Java version: $JAVA_VERSION"

if [[ $(echo "$JAVA_VERSION >= 17" | bc -l) -eq 0 ]]; then
    echo "Error: Java 17 or higher is required"
    echo "Current version: $JAVA_VERSION"
    exit 1
fi

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "Error: Maven is not installed or not in PATH"
    echo "Please install Maven 3.6 or higher"
    exit 1
fi

echo "Building project..."
mvn clean compile

if [ $? -ne 0 ]; then
    echo "Error: Build failed"
    exit 1
fi

echo "Starting application with JavaFX..."
mvn javafx:run 