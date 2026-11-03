package com.example.backend.models;

public enum ThemePreset {
    RENTWISE_CYAN("RentWise Cyan", "ரென்ட்வைஸ் சியான்", 0xFF00B4D8, 0xFF0077B6, 0xFF03045E),
    EMERALD_TEAL("Emerald Mint", "மரகத பச்சை", 0xFF06D6A0, 0xFF058C42, 0xFF04471C),
    ROYAL_INDIGO("Royal Indigo", "ராயல் நீலம்", 0xFF6366F1, 0xFF4F46E5, 0xFF312E81),
    SUNSET_AMBER("Sunset Amber", "சூரிய அஸ்தமனம்", 0xFFFF9F1C, 0xFFE76F51, 0xFF264653),
    SLATE_DARK("Midnight Slate", "நள்ளிரவு கருப்பு", 0xFF64748B, 0xFF334155, 0xFF0F172A);

    private final String englishName;
    private final String tamilName;
    private final long primaryColor;
    private final long secondaryColor;
    private final long tertiaryColor;

    ThemePreset(String englishName, String tamilName, long primaryColor, long secondaryColor, long tertiaryColor) {
        this.englishName = englishName;
        this.tamilName = tamilName;
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
        this.tertiaryColor = tertiaryColor;
    }

    public String getEnglishName() { return englishName; }
    public String getTamilName() { return tamilName; }
    public long getPrimaryColor() { return primaryColor; }
    public long getSecondaryColor() { return secondaryColor; }
    public long getTertiaryColor() { return tertiaryColor; }

    public String getLocalized(boolean isTamil) {
        return isTamil ? tamilName : englishName;
    }
}
