#!/bin/bash

# Read Python code
cat > /app/script.py

# Read input from stdin
INPUT=""
while IFS= read -r line; do
    INPUT+="$line"$'\n'
done

# Run Python script with timeout
timeout 5 python3 /app/script.py <<< "$INPUT" 2> /app/errors.txt
EXIT_CODE=$?

if [ -s /app/errors.txt ]; then
    cat /app/errors.txt
    exit 1
elif [ $EXIT_CODE -ne 0 ]; then
    echo "Execution timed out or failed."
    exit 1
fi
