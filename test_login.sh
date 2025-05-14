#!/bin/bash

# Create classes directory if it doesn't exist
mkdir -p classes

# Compile all Java files
javac -d classes src/main/java/*.java

# Run the application
java -cp classes main.java.MainApp 