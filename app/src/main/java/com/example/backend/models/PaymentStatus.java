package com.example.backend.models;

public enum PaymentStatus {
    PENDING("Pending", "நிலுவையில்", 0xFFFF9800),
    SUCCESSFUL("Successful", "வெற்றி", 0xFF4CAF50),
    FAILED("Failed", "தோல்வி", 0xFFF44336),
    REFUNDED("Refunded", "திருப்பி செலுத்தப்பட்டது", 0xFF9C27B0);

    private final String english;
    private final String tamil;
    private final long colorValue;

    PaymentStatus(String english, String tamil, long colorValue) {
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
