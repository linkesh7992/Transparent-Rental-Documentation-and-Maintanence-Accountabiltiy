package com.example.backend.models;

public enum UserRole {
    TENANT("Tenant", "குடியிருப்பாளர்"),
    LANDLORD("Landlord", "வீட்டு உரிமையாளர்");

    private final String englishName;
    private final String tamilName;

    UserRole(String englishName, String tamilName) {
        this.englishName = englishName;
        this.tamilName = tamilName;
    }

    public String getEnglishName() {
        return englishName;
    }

    public String getTamilName() {
        return tamilName;
    }

    public String getLocalized(boolean isTamil) {
        return isTamil ? tamilName : englishName;
    }
}
