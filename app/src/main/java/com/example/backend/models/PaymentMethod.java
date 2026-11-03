package com.example.backend.models;

public enum PaymentMethod {
    UPI("UPI (GPay / PhonePe / Paytm)", "யுபிஐ (UPI)"),
    CARD("Credit / Debit Card", "அட்டை (Card)"),
    NET_BANKING("Net Banking", "நெட் பேங்கிங்"),
    BANK_TRANSFER("Bank Transfer (NEFT/IMPS)", "வங்கி பரிமாற்றம்");

    private final String english;
    private final String tamil;

    PaymentMethod(String english, String tamil) {
        this.english = english;
        this.tamil = tamil;
    }

    public String getEnglish() { return english; }
    public String getTamil() { return tamil; }

    public String getLocalized(boolean isTamil) {
        return isTamil ? tamil : english;
    }
}
