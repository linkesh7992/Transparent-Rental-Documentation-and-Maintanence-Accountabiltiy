package com.example.backend.models;

public enum PaymentType {
    MONTHLY_RENT("Monthly Rent Payment", "மாதாந்திர வாடகை கட்டணம்"),
    SECURITY_DEPOSIT("Security Deposit / Advance", "முன்பணம் / பாதுகாப்பு வைப்புத்தொகை"),
    MAINTENANCE("Maintenance Payment", "பராமரிப்பு கட்டணம்"),
    REPAIR_SERVICE("Repair Service Payment", "பழுதுபார்ப்பு சேவை கட்டணம்");

    private final String english;
    private final String tamil;

    PaymentType(String english, String tamil) {
        this.english = english;
        this.tamil = tamil;
    }

    public String getEnglish() { return english; }
    public String getTamil() { return tamil; }

    public String getLocalized(boolean isTamil) {
        return isTamil ? tamil : english;
    }
}
