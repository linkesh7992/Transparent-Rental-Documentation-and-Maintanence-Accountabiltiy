package com.example.backend.models;

public enum MaintenanceTimelineStep {
    SUBMITTED("Submitted", "சமர்ப்பிக்கப்பட்டது"),
    NOTIFIED("Notified", "அறிவிக்கப்பட்டது"),
    ACKNOWLEDGED("Acknowledged", "ஏற்றுக்கொள்ளப்பட்டது"),
    ASSIGNED("Assigned", "பணியாளர் ஒதுக்கப்பட்டது"),
    WORK_STARTED("Work Started", "வேலை தொடங்கியது"),
    PAYMENT_PENDING("Payment Pending", "பணம் நிலுவை"),
    PAYMENT_COMPLETED("Payment Completed", "பணம் செலுத்தப்பட்டது"),
    TENANT_VERIFICATION_PENDING("Tenant Verification Pending", "குடியிருப்பாளர் சரிபார்ப்பு"),
    COMPLETED("Completed", "முழுமை பெற்றது");

    private final String english;
    private final String tamil;

    MaintenanceTimelineStep(String english, String tamil) {
        this.english = english;
        this.tamil = tamil;
    }

    public String getEnglish() { return english; }
    public String getTamil() { return tamil; }

    public String getLocalized(boolean isTamil) {
        return isTamil ? tamil : english;
    }
}
