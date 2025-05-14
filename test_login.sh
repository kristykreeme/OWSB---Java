#!/bin/bash

# Create classes directory if it doesn't exist
mkdir -p classes

# Compile all Java files
javac -d classes src/main/Admin/*.java

# Run the application
java -cp classes main.Admin.MainApp 