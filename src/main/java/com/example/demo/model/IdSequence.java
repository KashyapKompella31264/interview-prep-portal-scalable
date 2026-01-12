package com.example.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "id_sequences")
public class IdSequence {
    @Id
    private String role;
    private int lastAssignedId;

    public IdSequence() {}

    public IdSequence(String role, int lastAssignedId) {
        this.role = role;
        this.lastAssignedId = lastAssignedId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getLastAssignedId() {
        return lastAssignedId;
    }

    public void setLastAssignedId(int lastAssignedId) {
        this.lastAssignedId = lastAssignedId;
    }
}
