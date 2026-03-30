#!/bin/bash

echo "Building Campus Course & Records Manager (CCRM)..."

# Create output directory
mkdir -p build

# Compile all Java files
echo "Compiling Java source files..."
javac -d build -cp "src/main/resources" src/main/java/edu/campus/ccrm/*.java src/main/java/edu/campus/ccrm/**/*.java

if [ $? -eq 0 ]; then
    echo "Compilation successful!"
    echo ""
    echo "To run the application:"
    echo "java -cp build edu.campus.ccrm.CampusRecordsManager"
    echo ""
else
    echo "Compilation failed!"
    exit 1
fi
