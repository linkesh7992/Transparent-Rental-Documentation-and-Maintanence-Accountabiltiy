package com.example.backend.models;

public enum MaintenancePriority {
    LOW("Low", "குறைந்த", 0xFF4CAF50),
    MEDIUM("Medium", "நடுத்தர", 0xFF2196F3),
    HIGH("High", "அவசரம்", 0xFFFF9800),
    EMERGENCY("Emergency", "மிக அவசரம்", 0xFFF44336);

    private final String english;
    private final String tamil;
    private final long colorValue;

    MaintenancePriority(String english, String tamil, long colorValue) {
        this.english = english;
        this.tamil = tamil;
        this.colorValue = colorValue;
    }

    public String getEnglish() { return english; }
    public String getTamil() { return tamil; }
    public long getColorValue() { return colorValue; }

    public String getLocalized(boolean isTamil) {
        return isTamil ? tamil : english;
    }
}
