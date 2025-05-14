#!/bin/bash

# Create necessary directories
mkdir -p classes

# Compile the Java files
javac -d classes src/main/java/main/*.java src/main/java/Admin/*.java src/main/java/utils/*.java

# Run the application
java -cp classes main.App 