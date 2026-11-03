package com.example.backend.models;

public enum CostResponsibility {
    TENANT_PAYS("Tenant Pays", "குடியிருப்பாளர் செலுத்துகிறார்"),
    LANDLORD_PAYS("Landlord Pays", "உரிமையாளர் செலுத்துகிறார்"),
    SHARED_COST("Shared Cost", "இருவரும் பகிர்வு");

    private final String english;
    private final String tamil;

    CostResponsibility(String english, String tamil) {
        this.english = english;
        this.tamil = tamil;
    }

    public String getEnglish() { return english; }
    public String getTamil() { return tamil; }

    public String getLocalized(boolean isTamil) {
        return isTamil ? tamil : english;
    }
}
