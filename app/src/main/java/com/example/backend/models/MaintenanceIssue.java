package com.example.backend.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MaintenanceIssue implements Serializable {
    private String id;
    private String title;
    private String description;
    private MaintenanceCategory category;
    private MaintenancePriority priority;
    private MaintenanceStatus status;
    private int currentTimelineStepIndex; // 0 to 8 corresponding to MaintenanceTimelineStep enum
    private String propertyId;
    private String propertyTitle;
    private String tenantId;
    private String tenantName;
    private String landlordId;
    private String createdAt;
    private String updatedAt;
    private List<String> photoUris;
    private boolean hasVoiceRecording;
    private int voiceDurationSeconds;
    private String voiceTranscription;
    private Technician assignedTechnician;
    private double repairBillAmount;
    private String repairBillUrl;
    private boolean isRepairBillUploaded;
    private int tenantRating; // 1 to 5
    private String tenantRatingFeedback;
    private CostResponsibility costResponsibility;
    private List<String> documentAttachments;
    private boolean isEscalated;
    private String escalationReason;

    public MaintenanceIssue(String id, String title, String description, MaintenanceCategory category,
                            MaintenancePriority priority, MaintenanceStatus status, int currentTimelineStepIndex,
                            String propertyId, String propertyTitle, String tenantId, String tenantName,
                            String landlordId, String createdAt, String updatedAt, List<String> photoUris,
                            boolean hasVoiceRecording, int voiceDurationSeconds, String voiceTranscription,
                            Technician assignedTechnician, double repairBillAmount, String repairBillUrl,
                            boolean isRepairBillUploaded, int tenantRating, String tenantRatingFeedback,
                            CostResponsibility costResponsibility, List<String> documentAttachments,
                            boolean isEscalated, String escalationReason) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.priority = priority;
        this.status = status;
        this.currentTimelineStepIndex = currentTimelineStepIndex;
        this.propertyId = propertyId;
        this.propertyTitle = propertyTitle;
        this.tenantId = tenantId;
        this.tenantName = tenantName;
        this.landlordId = landlordId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.photoUris = photoUris != null ? photoUris : new ArrayList<>();
        this.hasVoiceRecording = hasVoiceRecording;
        this.voiceDurationSeconds = voiceDurationSeconds;
        this.voiceTranscription = voiceTranscription;
        this.assignedTechnician = assignedTechnician;
        this.repairBillAmount = repairBillAmount;
        this.repairBillUrl = repairBillUrl;
        this.isRepairBillUploaded = isRepairBillUploaded;
        this.tenantRating = tenantRating;
        this.tenantRatingFeedback = tenantRatingFeedback;
        this.costResponsibility = costResponsibility != null ? costResponsibility : CostResponsibility.LANDLORD_PAYS;
        this.documentAttachments = documentAttachments != null ? documentAttachments : new ArrayList<>();
        this.isEscalated = isEscalated;
        this.escalationReason = escalationReason;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public MaintenanceCategory getCategory() { return category; }
    public void setCategory(MaintenanceCategory category) { this.category = category; }

    public MaintenancePriority getPriority() { return priority; }
    public void setPriority(MaintenancePriority priority) { this.priority = priority; }

    public MaintenanceStatus getStatus() { return status; }
    public void setStatus(MaintenanceStatus status) { this.status = status; }

    public int getCurrentTimelineStepIndex() { return currentTimelineStepIndex; }
    public void setCurrentTimelineStepIndex(int currentTimelineStepIndex) { this.currentTimelineStepIndex = currentTimelineStepIndex; }

    public String getPropertyId() { return propertyId; }
    public void setPropertyId(String propertyId) { this.propertyId = propertyId; }

    public String getPropertyTitle() { return propertyTitle; }
    public void setPropertyTitle(String propertyTitle) { this.propertyTitle = propertyTitle; }

    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }

    public String getTenantName() { return tenantName; }
    public void setTenantName(String tenantName) { this.tenantName = tenantName; }

    public String getLandlordId() { return landlordId; }
    public void setLandlordId(String landlordId) { this.landlordId = landlordId; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }

    public List<String> getPhotoUris() { return photoUris; }
    public void setPhotoUris(List<String> photoUris) { this.photoUris = photoUris; }

    public boolean isHasVoiceRecording() { return hasVoiceRecording; }
    public void setHasVoiceRecording(boolean hasVoiceRecording) { this.hasVoiceRecording = hasVoiceRecording; }

    public int getVoiceDurationSeconds() { return voiceDurationSeconds; }
    public void setVoiceDurationSeconds(int voiceDurationSeconds) { this.voiceDurationSeconds = voiceDurationSeconds; }

    public String getVoiceTranscription() { return voiceTranscription; }
    public void setVoiceTranscription(String voiceTranscription) { this.voiceTranscription = voiceTranscription; }

    public Technician getAssignedTechnician() { return assignedTechnician; }
    public void setAssignedTechnician(Technician assignedTechnician) { this.assignedTechnician = assignedTechnician; }

    public double getRepairBillAmount() { return repairBillAmount; }
    public void setRepairBillAmount(double repairBillAmount) { this.repairBillAmount = repairBillAmount; }

    public String getRepairBillUrl() { return repairBillUrl; }
    public void setRepairBillUrl(String repairBillUrl) { this.repairBillUrl = repairBillUrl; }

    public boolean isRepairBillUploaded() { return isRepairBillUploaded; }
    public void setRepairBillUploaded(boolean repairBillUploaded) { isRepairBillUploaded = repairBillUploaded; }

    public int getTenantRating() { return tenantRating; }
    public void setTenantRating(int tenantRating) { this.tenantRating = tenantRating; }

    public String getTenantRatingFeedback() { return tenantRatingFeedback; }
    public void setTenantRatingFeedback(String tenantRatingFeedback) { this.tenantRatingFeedback = tenantRatingFeedback; }

    public CostResponsibility getCostResponsibility() { return costResponsibility; }
    public void setCostResponsibility(CostResponsibility costResponsibility) { this.costResponsibility = costResponsibility; }

    public List<String> getDocumentAttachments() { return documentAttachments; }
    public void setDocumentAttachments(List<String> documentAttachments) { this.documentAttachments = documentAttachments; }

    public boolean isEscalated() { return isEscalated; }
    public void setEscalated(boolean escalated) { isEscalated = escalated; }

    public String getEscalationReason() { return escalationReason; }
    public void setEscalationReason(String escalationReason) { this.escalationReason = escalationReason; }
}
