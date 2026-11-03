package com.example.backend.models;

public enum MaintenanceStatus {
    NEW("New", "புதியது", 0),
    UNSEEN("Unseen", "பார்க்கப்படாதது", 1),
    PENDING("Pending", "நிலுவையில்", 2),
    IN_PROGRESS("In Progress", "செயல்பாட்டில்", 4),
    WAITING_FOR_PAYMENT("Waiting for Payment", "பணம் செலுத்த காத்திருக்கிறது", 5),
    WAITING_FOR_TENANT_VERIFICATION("Waiting for Tenant Verification", "சரிபார்ப்புக்கு காத்திருக்கிறது", 7),
    COMPLETED("Completed", "முடிவடைந்தது", 8);

    private final String english;
    private final String tamil;
    private final int timelineIndex;

    MaintenanceStatus(String english, String tamil, int timelineIndex) {
        this.english = english;
        this.tamil = tamil;
        this.timelineIndex = timelineIndex;
    }

    public String getEnglish() { return english; }
    public String getTamil() { return tamil; }
    public int getTimelineIndex() { return timelineIndex; }

    public String getLocalized(boolean isTamil) {
        return isTamil ? tamil : english;
    }
}
