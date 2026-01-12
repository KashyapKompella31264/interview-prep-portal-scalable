package com.example.demo.controller;

import com.example.demo.service.CodeExecutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/execute")
public class CodeExecutionController {

    @Autowired
    private CodeExecutionService codeExecutionService;

    @PostMapping
    public Map<String, Object> executeCode(@RequestBody Map<String, String> request) {
        String code = request.get("code");
        String language = request.get("language");
        String questionId = request.get("questionId"); // ✅ Provide default value if input is missing

        if (code == null || language == null) {
            return Map.of("error", "Missing code or language");
        }
        if(questionId==null) {
        	return Map.of("error"," no question id");
        }
        System.out.println("Question id is: "+questionId);
        return codeExecutionService.executeCode(code, language, questionId); // ✅ Correct method call
    }
}
