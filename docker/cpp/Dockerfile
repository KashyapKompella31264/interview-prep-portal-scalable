# Use a GCC image as the base
FROM gcc:latest

# Set the working directory in the container
WORKDIR /app

# Copy the C++ script or source code
COPY run_cpp.sh /app/run_cpp.sh

# Make the script executable
RUN chmod +x /app/run_cpp.sh

# Set permissions (if needed)
RUN chmod 777 /app

# Command to execute the C++ script
CMD ["/app/run_cpp.sh"]
