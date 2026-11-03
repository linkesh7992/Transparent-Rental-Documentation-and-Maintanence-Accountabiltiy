package com.example.backend.models;

public enum NoticeType {
    VACATE_NOTICE("Vacate Notice", "வெளியேறும் அறிவிப்பு"),
    RENT_INCREASE_NOTICE("Rent Increase Notice", "வாடகை உயர்வு அறிவிப்பு"),
    LEASE_RENEWAL_NOTICE("Lease Renewal Notice", "ஒப்பந்த புதுப்பித்தல் அறிவிப்பு"),
    GENERAL_ANNOUNCEMENT("General Announcement", "பொது அறிவிப்பு");

    private final String english;
    private final String tamil;

    NoticeType(String english, String tamil) {
        this.english = english;
        this.tamil = tamil;
    }

    public String getEnglish() { return english; }
    public String getTamil() { return tamil; }

    public String getLocalized(boolean isTamil) {
        return isTamil ? tamil : english;
    }
}
