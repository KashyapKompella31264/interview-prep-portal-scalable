#!/bin/bash

# Read C++ code
cat > /app/main.cpp

# Read input
INPUT=""
while IFS= read -r line; do
    INPUT+="$line"$'\n'
done

# Compile C++
g++ /app/main.cpp -o /app/main.out 2> /app/compile_errors.txt
if [ -s /app/compile_errors.txt ]; then
    cat /app/compile_errors.txt
    exit 1
fi

# Run C++ program with timeout
timeout 5 /app/main.out <<< "$INPUT" 2> /app/runtime_errors.txt
EXIT_CODE=$?

if [ -s /app/runtime_errors.txt ]; then
    cat /app/runtime_errors.txt
    exit 1
elif [ $EXIT_CODE -ne 0 ]; then
    echo "Execution timed out or failed."
    exit 1
fi
