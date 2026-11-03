package com.example.backend.models;

import java.io.Serializable;

public class ChatMessage implements Serializable {
    private String id;
    private String issueId;
    private UserRole senderRole;
    private String senderName;
    private String message;
    private String timestamp;
    private boolean isRead;

    public ChatMessage(String id, String issueId, UserRole senderRole, String senderName, String message, String timestamp, boolean isRead) {
        this.id = id;
        this.issueId = issueId;
        this.senderRole = senderRole;
        this.senderName = senderName;
        this.message = message;
        this.timestamp = timestamp;
        this.isRead = isRead;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getIssueId() { return issueId; }
    public void setIssueId(String issueId) { this.issueId = issueId; }

    public UserRole getSenderRole() { return senderRole; }
    public void setSenderRole(UserRole senderRole) { this.senderRole = senderRole; }

    public String getSenderName() { return senderName; }
    public void setSenderName(String senderName) { this.senderName = senderName; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }
}
