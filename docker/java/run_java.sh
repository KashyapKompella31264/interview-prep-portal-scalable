#!/bin/bash

# Read Java code
cat > /app/Main.java

# Read input
INPUT=""
while IFS= read -r line; do
    INPUT+="$line"$'\n'
done

# Compile Java
javac -d /app /app/Main.java 2> /app/compile_errors.txt
if [ -s /app/compile_errors.txt ]; then
    cat /app/compile_errors.txt
    exit 1
fi

# Run Java program with timeout
timeout 5 java -cp /app Main <<< "$INPUT" 2> /app/runtime_errors.txt
EXIT_CODE=$?

if [ -s /app/runtime_errors.txt ]; then
    cat /app/runtime_errors.txt
    exit 1
elif [ $EXIT_CODE -ne 0 ]; then
    echo "Execution timed out or failed."
    exit 1
fi
