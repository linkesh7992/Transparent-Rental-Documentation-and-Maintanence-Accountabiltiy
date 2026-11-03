package com.example.backend.models;

import java.io.Serializable;

public class NoticeItem implements Serializable {
    private String id;
    private String title;
    private String content;
    private NoticeType type;
    private int reminderDays; // 90, 60, 30
    private String effectiveDate;
    private String createdAt;
    private String propertyId;
    private String propertyTitle;
    private String tenantId;
    private String landlordId;
    private boolean acknowledgedByTenant;
    private boolean acknowledgedByLandlord;
    private String tenantAckTimestamp;
    private String landlordAckTimestamp;

    public NoticeItem(String id, String title, String content, NoticeType type, int reminderDays,
                      String effectiveDate, String createdAt, String propertyId, String propertyTitle,
                      String tenantId, String landlordId, boolean acknowledgedByTenant,
                      boolean acknowledgedByLandlord, String tenantAckTimestamp, String landlordAckTimestamp) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.type = type;
        this.reminderDays = reminderDays;
        this.effectiveDate = effectiveDate;
        this.createdAt = createdAt;
        this.propertyId = propertyId;
        this.propertyTitle = propertyTitle;
        this.tenantId = tenantId;
        this.landlordId = landlordId;
        this.acknowledgedByTenant = acknowledgedByTenant;
        this.acknowledgedByLandlord = acknowledgedByLandlord;
        this.tenantAckTimestamp = tenantAckTimestamp;
        this.landlordAckTimestamp = landlordAckTimestamp;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public NoticeType getType() { return type; }
    public void setType(NoticeType type) { this.type = type; }

    public int getReminderDays() { return reminderDays; }
    public void setReminderDays(int reminderDays) { this.reminderDays = reminderDays; }

    public String getEffectiveDate() { return effectiveDate; }
    public void setEffectiveDate(String effectiveDate) { this.effectiveDate = effectiveDate; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getPropertyId() { return propertyId; }
    public void setPropertyId(String propertyId) { this.propertyId = propertyId; }

    public String getPropertyTitle() { return propertyTitle; }
    public void setPropertyTitle(String propertyTitle) { this.propertyTitle = propertyTitle; }

    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }

    public String getLandlordId() { return landlordId; }
    public void setLandlordId(String landlordId) { this.landlordId = landlordId; }

    public boolean isAcknowledgedByTenant() { return acknowledgedByTenant; }
    public void setAcknowledgedByTenant(boolean acknowledgedByTenant) { this.acknowledgedByTenant = acknowledgedByTenant; }

    public boolean isAcknowledgedByLandlord() { return acknowledgedByLandlord; }
    public void setAcknowledgedByLandlord(boolean acknowledgedByLandlord) { this.acknowledgedByLandlord = acknowledgedByLandlord; }

    public String getTenantAckTimestamp() { return tenantAckTimestamp; }
    public void setTenantAckTimestamp(String tenantAckTimestamp) { this.tenantAckTimestamp = tenantAckTimestamp; }

    public String getLandlordAckTimestamp() { return landlordAckTimestamp; }
    public void setLandlordAckTimestamp(String landlordAckTimestamp) { this.landlordAckTimestamp = landlordAckTimestamp; }
}
