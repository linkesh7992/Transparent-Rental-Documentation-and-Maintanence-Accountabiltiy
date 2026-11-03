package com.example.backend.models;

import java.io.Serializable;

public class NotificationItem implements Serializable {
    private String id;
    private String title;
    private String body;
    private String type; // MAINTENANCE, PAYMENT, NOTICE, ANNOUNCEMENT, SYSTEM
    private String timestamp;
    private boolean isRead;
    private UserRole targetRole;

    public NotificationItem(String id, String title, String body, String type, String timestamp, boolean isRead, UserRole targetRole) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.type = type;
        this.timestamp = timestamp;
        this.isRead = isRead;
        this.targetRole = targetRole;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }

    public UserRole getTargetRole() { return targetRole; }
    public void setTargetRole(UserRole targetRole) { this.targetRole = targetRole; }
}
