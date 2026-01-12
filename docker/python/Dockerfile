# Use a Python image as the base
FROM python:3.9-slim

# Set the working directory in the container
WORKDIR /app

# Copy the Python script
COPY run_python.sh /app/run_python.sh

# Make the script executable
RUN chmod +x /app/run_python.sh

# Set permissions (if needed)
RUN chmod 777 /app

# Command to execute the Python script
CMD ["/app/run_python.sh"]
