package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Question;
import com.example.demo.model.TestCase;
import com.example.demo.repository.QuestionRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CodeExecutionService {
	@Autowired
	private QuestionRepository questionRepository;
	
	public Map<String, Object> executeCode(String code, String language, String questionId) {
        Map<String, Object> response = new HashMap<>();
        List<Map<String, String>> results = new ArrayList<>();

        // ✅ Fetch the Question and Test Cases from MongoDB
        Optional<Question> questionOptional = questionRepository.findById(questionId);
        if (questionOptional.isEmpty()) {
            response.put("error", "Question not found!");
            return response;
        }
        List<TestCase> testCases = questionOptional.get().getTestcases();

        // ✅ Execute the code for each test case
        try {
            for (TestCase testCase : testCases) {
                Map<String, String> result;
                
                switch (language.toLowerCase()) {
                    case "java":
                        result = executeJavaCode(code, testCase.getInput());
                        break;
                    case "python":
                        result = executePythonDocker(code, testCase.getInput());
                        break;
                    case "cpp":
                        result = executeCppDocker(code, testCase.getInput());
                        break;
                    default:
                        result = new HashMap<>();
                        result.put("error", "Unsupported language: " + language);
                }

                // ✅ Compare actual vs expected output
                String actualOutput = result.getOrDefault("output", "").trim();
                String expectedOutput = testCase.getExpectedOutput().trim();
                System.out.print("the testcase is :"+testCase.getExpectedOutput());
                boolean isCorrect = actualOutput.equals(expectedOutput);
                System.out.println("the actual output is: "+actualOutput);
                System.out.println("the expected output is: "+expectedOutput);
                System.out.println("the boolean value is:"+isCorrect);
                // ✅ Store result
                result.put("expectedOutput", expectedOutput);
                result.put("status", isCorrect ? "PASSED ✅" : "FAILED ❌");
                
                results.add(result);
            }

            response.put("results", results);
            System.out.println(results);
        } catch (Exception e) {
            response.put("error", "Execution failed: " + e.getMessage());
        }

        return response;
    }

    // ✅ Execute Java Code
    private Map<String, String> executeJavaCode(String code, String input) throws IOException, InterruptedException {
        Map<String, String> result = new HashMap<>();

        File tempDir = new File("temp");
        if (!tempDir.exists()) tempDir.mkdir();

        File javaFile = new File("temp/Main.java");
        try (FileWriter writer = new FileWriter(javaFile)) {
            writer.write(code);
        }

        // 🕒 Measure Compilation Time
        long compileStart = System.nanoTime();
        Process compileProcess = new ProcessBuilder("javac", "temp/Main.java").redirectErrorStream(true).start();
        Map<String,String> compileResults=getProcessOutput(compileProcess);
        String compileErrors = compileResults.get("stderr");
        
        int compileExitCode = compileProcess.waitFor();
        long compileEnd = System.nanoTime();
        long compileTime = (compileEnd - compileStart) / 1_000_000; // Convert to milliseconds

        if (compileExitCode != 0) {
            result.put("error", "Compilation Error:\n" + compileErrors);
            System.out.print("the errors are: "+compileErrors);
            result.put("compileTime", compileTime + " ms");
            return result;
        }

        // 🕒 Measure Execution Time
        long execStart = System.nanoTime();
        Process runProcess = new ProcessBuilder("java", "-cp", "temp", "Main").start();
        result = executeWithInput(runProcess, input);
        long execEnd = System.nanoTime();
        long execTime = (execEnd - execStart) / 1_000_000; // Convert to 
        result.put("compileTime", compileTime + " ms");
        result.put("executionTime", execTime + " ms");
        System.err.println("the errors are: " + compileErrors); // Use System.err for errors
        return result;
    }

    // ✅ Execute Python via Docker
    private Map<String, String> executePythonDocker(String code, String input) throws IOException, InterruptedException {
        return executeDockerContainer(code, input, "python_executor", "py");
    }
    // ✅ Execute C++ via Docker
    private Map<String, String> executeCppDocker(String code, String input) throws IOException, InterruptedException {
        return executeDockerContainer(code, input, "cpp_executor", "cpp");
    }

    // ✅ Execute C++ & Python in Docker
    private Map<String, String> executeDockerContainer(String code, String input, String serviceName, String extension)
            throws IOException, InterruptedException {
        
        Map<String, String> result = new HashMap<>();
        String filePath = "/app/temp_code." + extension;

        // Write the code into the container
        ProcessBuilder writeCodeProcess = new ProcessBuilder(
            "docker", "exec", "-i", serviceName, "sh", "-c", "cat > " + filePath
        );

        Process writeProcess = writeCodeProcess.start();
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(writeProcess.getOutputStream()))) {
            writer.write(code);
            writer.flush();
        }

        int writeExitCode = writeProcess.waitFor();
        if (writeExitCode != 0) {
            result.put("error", "Backend is Down Please try again later,Sorry for the inconvience ");
            return result;
        }

        // ✅ Compile C++ Code if Needed
        long compileTime = 0;
        if (serviceName.equals("cpp_executor")) {
            long compileStart = System.nanoTime();
            ProcessBuilder compileProcess = new ProcessBuilder(
                "docker", "exec", "-i", serviceName, "sh", "-c", 
                "g++ " + filePath + " -o /app/temp_code.out"
            );
            Process compile = compileProcess.start();
            compile.waitFor();
            long compileEnd = System.nanoTime();
            compileTime = (compileEnd - compileStart) / 1_000_000; // Convert to milliseconds

            // Check if compilation succeeded
            if (compile.exitValue() != 0) {
                result.put("error", "Compilation failed. Check syntax.");
                result.put("compileTime", compileTime + " ms");
                return result;
            }
        }

        // ✅ Run Code Inside the Container
        String executionCommand = serviceName.equals("cpp_executor") 
            ? "/app/temp_code.out"  // C++ Binary
            : "python3 " + filePath; // Python Script

        long execStart = System.nanoTime();
        ProcessBuilder executeProcess = new ProcessBuilder(
            "docker", "exec", "-i", serviceName, "sh", "-c", executionCommand
        );

        Process process = executeProcess.start();
        result = executeWithInput(process, input);
        long execEnd = System.nanoTime();
        long execTime = (execEnd - execStart) / 1_000_000; // Convert to milliseconds

        if (serviceName.equals("cpp_executor")) {
            result.put("compileTime", compileTime + " ms");
        }
        result.put("executionTime", execTime + " ms");
        return result;
    }

    // ✅ Execute Process with Input Handling
    private Map<String, String> executeWithInput(Process process, String input) throws IOException, InterruptedException {
        long execStart = System.nanoTime();
        if (input != null && !input.isEmpty()) {
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()))) {
                writer.write(input);
                writer.newLine();
                writer.flush();
            }
        }

        Map<String, String> result = getProcessOutput(process);
        int exitCode = process.waitFor();

        long execEnd = System.nanoTime();
        long execTime = (execEnd - execStart) / 1_000_000;

        result.put("executionTime", execTime + " ms");

        if (exitCode != 0 && result.get("error").isEmpty()) {
            result.put("error", "Process exited with code " + exitCode);
        }

        return result;
    }





    // ✅ Read Process Output (stdout & stderr)
    private Map<String, String> getProcessOutput(Process process) throws IOException {
        StringBuilder stdout = new StringBuilder();
        StringBuilder stderr = new StringBuilder();

        try (
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                stdout.append(line).append("\n");
            }
            while ((line = errorReader.readLine()) != null) {
                stderr.append(line).append("\n");
            }
        }

        Map<String, String> outputMap = new HashMap<>();
        outputMap.put("output", stdout.toString().trim());
        return outputMap;
    }

}
