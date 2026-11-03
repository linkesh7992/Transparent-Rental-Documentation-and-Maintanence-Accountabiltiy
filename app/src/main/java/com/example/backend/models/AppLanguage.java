package com.example.backend.models;

public enum AppLanguage {
    ENGLISH("English", "English", "en"),
    TAMIL("Tamil", "தமிழ்", "ta");

    private final String englishName;
    private final String nativeName;
    private final String code;

    AppLanguage(String englishName, String nativeName, String code) {
        this.englishName = englishName;
        this.nativeName = nativeName;
        this.code = code;
    }

    public String getEnglishName() { return englishName; }
    public String getNativeName() { return nativeName; }
    public String getCode() { return code; }
}
