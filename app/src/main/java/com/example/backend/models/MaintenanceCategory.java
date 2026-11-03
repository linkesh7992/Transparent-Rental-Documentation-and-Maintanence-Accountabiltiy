package com.example.backend.models;

public enum MaintenanceCategory {
    ELECTRICAL("Electrical", "மின்சாரம்"),
    PLUMBING("Plumbing", "குழாய் பழுது"),
    CARPENTER("Carpenter", "மரவேலை"),
    OTHERS("Others", "மற்றவை");

    private final String english;
    private final String tamil;

    MaintenanceCategory(String english, String tamil) {
        this.english = english;
        this.tamil = tamil;
    }

    public String getEnglish() { return english; }
    public String getTamil() { return tamil; }

    public String getLocalized(boolean isTamil) {
        return isTamil ? tamil : english;
    }
}
