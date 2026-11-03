package com.example.backend.services;

import com.example.backend.models.AppLanguage;
import java.util.HashMap;
import java.util.Map;

/**
 * High-performance OOP Localization Service supporting English and Tamil.
 */
public class LocalizationService {
    private static LocalizationService instance;
    private AppLanguage currentLanguage = AppLanguage.ENGLISH;
    private final Map<String, String> tamilDict = new HashMap<>();

    private LocalizationService() {
        initTamilDictionary();
    }

    public static synchronized LocalizationService getInstance() {
        if (instance == null) {
            instance = new LocalizationService();
        }
        return instance;
    }

    public AppLanguage getCurrentLanguage() {
        return currentLanguage;
    }

    public void setCurrentLanguage(AppLanguage language) {
        this.currentLanguage = language;
    }

    public boolean isTamil() {
        return currentLanguage == AppLanguage.TAMIL;
    }

    public String getString(String key) {
        if (currentLanguage == AppLanguage.TAMIL) {
            String val = tamilDict.get(key);
            if (val != null) return val;
        }
        return key;
    }

    private void initTamilDictionary() {
        // Navigation & General
        tamilDict.put("Home", "முகப்பு");
        tamilDict.put("Maintenance & Issues", "பராமரிப்பு & புகார்கள்");
        tamilDict.put("Payments", "கட்டணங்கள்");
        tamilDict.put("Profile", "சுயவிவரம்");
        tamilDict.put("RentWise", "ரென்ட்வைஸ்");
        tamilDict.put("Tenant", "குடியிருப்பாளர்");
        tamilDict.put("Landlord", "வீட்டு உரிமையாளர்");
        tamilDict.put("Switch Role", "பங்கை மாற்று");
        tamilDict.put("Notifications", "அறிவிப்புகள்");
        tamilDict.put("Language", "மொழி");
        tamilDict.put("Theme", "வண்ண தீம்");
        tamilDict.put("Dark Mode", "இருண்ட பயன்முறை");
        tamilDict.put("Light Mode", "வெளிச்ச பயன்முறை");
        tamilDict.put("Search", "தேடு");
        tamilDict.put("Filter", "வடிகட்டு");
        tamilDict.put("All", "அனைத்தும்");
        tamilDict.put("Cancel", "ரத்து செய்");
        tamilDict.put("Submit", "சமர்ப்பி");
        tamilDict.put("Save", "சேமி");
        tamilDict.put("Success", "வெற்றி");
        tamilDict.put("Close", "மூடு");
        tamilDict.put("View All", "அனைத்தையும் பார்");
        tamilDict.put("Back", "பின்செல்");
        tamilDict.put("Confirm", "உறுதி செய்");
        tamilDict.put("Delete", "நீக்கு");
        tamilDict.put("Edit", "திருத்து");
        tamilDict.put("Status", "நிலை");
        tamilDict.put("Details", "விவரங்கள்");
        tamilDict.put("Active", "செயலில்");
        tamilDict.put("Download", "பதிவிறக்கு");
        tamilDict.put("Upload", "பதிவேற்று");

        // Home Screen
        tamilDict.put("Welcome Back", "மீண்டும் வருக");
        tamilDict.put("Search by location, budget, type...", "இடம், பட்ஜெட், வகை மூலம் தேடுங்கள்...");
        tamilDict.put("Recommended Properties", "பரிந்துரைக்கப்பட்ட வீடுகள்");
        tamilDict.put("Nearby Properties", "அருகிலுள்ள வீடுகள்");
        tamilDict.put("Verified Properties", "சரிபார்க்கப்பட்ட வீடுகள்");
        tamilDict.put("Quick Actions", "விரைவுச் செயல்கள்");
        tamilDict.put("Search House", "வீடு தேடு");
        tamilDict.put("Add Property", "சொத்து சேர்");
        tamilDict.put("Manage Listings", "பட்டியல்களை நிர்வகி");
        tamilDict.put("Saved Properties", "சேமிக்கப்பட்டவை");
        tamilDict.put("Dashboard Summary", "டாஷ்போர்டு சுருக்கம்");
        tamilDict.put("Current Rent", "தற்போதைய வாடகை");
        tamilDict.put("Next Due Date", "அடுத்த செலுத்த வேண்டிய தேதி");
        tamilDict.put("Active Requests", "செயலில் உள்ள புகார்கள்");
        tamilDict.put("Total Properties", "மொத்த சொத்துக்கள்");
        tamilDict.put("Occupied Rooms", "வாடகைக்கு விடப்பட்டவை");
        tamilDict.put("Pending Requests", "நிலுவையில் உள்ள கோரிக்கைகள்");
        tamilDict.put("Monthly Revenue", "மாதாந்திர வருவாய்");
        tamilDict.put("Recently Viewed Properties", "சமீபத்தில் பார்த்த வீடுகள்");
        tamilDict.put("Announcements & Alerts", "அறிவிப்புகள் & எச்சரிக்கைகள்");
        tamilDict.put("Notice Center", "அறிவிப்பு மையம்");
        tamilDict.put("Notice Reminders", "அறிவிப்பு நினைவூட்டல்கள்");
        tamilDict.put("Notice History", "அறிவிப்பு வரலாறு");
        tamilDict.put("Acknowledge Notice", "அறிவிப்பை ஏற்றுக்கொள்");
        tamilDict.put("Acknowledged", "ஏற்றுக்கொள்ளப்பட்டது");
        tamilDict.put("Pending Ack", "ஏற்பு நிலுவையில்");

        // Maintenance & Issues
        tamilDict.put("Raise New Issue", "புதிய புகாரை பதிவு செய்");
        tamilDict.put("Issue Category", "புகார் வகை");
        tamilDict.put("Electrical", "மின்சாரம்");
        tamilDict.put("Plumbing", "குழாய் பழுது");
        tamilDict.put("Carpenter", "மரவேலை");
        tamilDict.put("Others", "மற்றவை");
        tamilDict.put("Priority Level", "முன்னுரிமை நிலை");
        tamilDict.put("Low", "குறைந்த");
        tamilDict.put("Medium", "நடுத்தர");
        tamilDict.put("High", "அவசரம்");
        tamilDict.put("Emergency", "மிக அவசரம்");
        tamilDict.put("Issue Description", "புகார் விளக்கம்");
        tamilDict.put("Describe your problem in detail...", "உங்கள் பிரச்சனையை விரிவாக விவரிக்கவும்...");
        tamilDict.put("Voice Recording Note", "குரல் பதிவு குறிப்பு");
        tamilDict.put("Record Voice Description", "குரல் விளக்கத்தை பதிவு செய்");
        tamilDict.put("Voice Note Attached", "குரல் குறிப்பு இணைக்கப்பட்டது");
        tamilDict.put("Upload Photos / Videos", "புகைப்படம் / வீடியோ பதிவேற்று");
        tamilDict.put("Photos Attached", "புகைப்படங்கள் இணைக்கப்பட்டன");
        tamilDict.put("Submit Complaint", "புகாரை சமர்ப்பி");
        tamilDict.put("Issue Timeline", "புகார் காலவரிசை");
        tamilDict.put("Date & Time Tracking", "தேதி & நேரம் கண்காணிப்பு");
        tamilDict.put("Maintenance History", "பராமரிப்பு வரலாறு");
        tamilDict.put("Tenant-Landlord Chat", "குடியிருப்பாளர்-உரிமையாளர் அரட்டை");
        tamilDict.put("Assigned Technician", "ஒதுக்கப்பட்ட பணியாளர்");
        tamilDict.put("Technician Name", "பணியாளர் பெயர்");
        tamilDict.put("Contact", "தொடர்பு");
        tamilDict.put("Expected Visit Time", "எதிர்பார்க்கப்படும் வருகை நேரம்");
        tamilDict.put("Repair Bill", "பழுதுபார்ப்பு ரசீது");
        tamilDict.put("Service Rating", "சேவை மதிப்பீடு");
        tamilDict.put("Rate Service", "சேவையை மதிப்பிடுங்கள்");
        tamilDict.put("Write feedback...", "கருத்தை எழுதுங்கள்...");
        tamilDict.put("Cost Responsibility", "செலவுப் பொறுப்பு");
        tamilDict.put("Tenant Pays", "குடியிருப்பாளர் செலுத்துகிறார்");
        tamilDict.put("Landlord Pays", "உரிமையாளர் செலுத்துகிறார்");
        tamilDict.put("Shared Cost", "இருவரும் பகிர்வு");
        tamilDict.put("Document Attachments", "ஆவண இணைப்புகள்");
        tamilDict.put("Warranty & Invoices", "உத்தரவாதம் & ரசீதுகள்");
        tamilDict.put("Issue Escalation", "புகார் தீவிரமாக்கல்");
        tamilDict.put("Escalate Issue", "புகாரை தீவிரமாக்கு");
        tamilDict.put("Escalated to Priority Resolution", "அதிமுக்கிய தீர்வுக்கு அனுப்பப்பட்டது");
        tamilDict.put("Advance Timeline Step", "அடுத்த கட்டத்திற்கு நகர்த்து");

        // Payments
        tamilDict.put("Payment Types", "கட்டண வகைகள்");
        tamilDict.put("Monthly Rent", "மாதாந்திர வாடகை");
        tamilDict.put("Security Deposit", "பாதுகாப்பு வைப்புத்தொகை");
        tamilDict.put("Maintenance Fee", "பராமரிப்பு கட்டணம்");
        tamilDict.put("Repair Fee", "பழுதுபார்ப்பு கட்டணம்");
        tamilDict.put("Send Payment", "பணம் செலுத்து");
        tamilDict.put("Request Payment", "பணம் கோரு");
        tamilDict.put("Payment History", "பரிவர்த்தனை வரலாறு");
        tamilDict.put("Upcoming Payments & Reminders", "வரவிருக்கும் கட்டணங்கள் & நினைவூட்டல்");
        tamilDict.put("Late Payment Alerts", "தாமதக் கட்டண எச்சரிக்கைகள்");
        tamilDict.put("Outstanding Balance", "நிலுவைத் தொகை");
        tamilDict.put("Payment Breakdown", "கட்டணப் பிரிவு விவரம்");
        tamilDict.put("Transaction Details", "பரிவர்த்தனை விவரங்கள்");
        tamilDict.put("Payment Method", "செலுத்தும் முறை");
        tamilDict.put("Auto-Pay Enabled", "தானியங்கி கட்டணம் இயக்கப்பட்டது");
        tamilDict.put("Enable Auto-Pay", "தானியங்கி கட்டணத்தை இயக்கு");
        tamilDict.put("Payment Timeline", "கட்டண காலவரிசை");
        tamilDict.put("Receipt", "ரசீது");
        tamilDict.put("Download Receipt", "ரசீதை பதிவிறக்கு");
        tamilDict.put("Download Statement / Report", "அறிக்கையை பதிவிறக்கு");
        tamilDict.put("Digital Acknowledgement", "டிஜிட்டல் ஏற்பு");
        tamilDict.put("Tenant Signed", "குடியிருப்பாளர் கையொப்பமிட்டார்");
        tamilDict.put("Landlord Signed", "உரிமையாளர் கையொப்பமிட்டார்");
        tamilDict.put("Dispute Payment", "கட்டணத்தை மறு/விவாதி");
        tamilDict.put("Raise Dispute", "மறுப்பு எழுப்பு");
        tamilDict.put("Rent Agreement Summary", "வாடகை ஒப்பந்த சுருக்கம்");

        // Profile
        tamilDict.put("Personal Information", "தனிப்பட்ட விவரங்கள்");
        tamilDict.put("Account Details", "கணக்கு விவரங்கள்");
        tamilDict.put("Edit Profile", "சுயவிவரத்தை திருத்து");
        tamilDict.put("Government ID Verification", "அரசு அடையாள சரிபார்ப்பு");
        tamilDict.put("Verified", "சரிபார்க்கப்பட்டது");
        tamilDict.put("Verification Pending", "சரிபார்ப்பு நிலுவையில்");
        tamilDict.put("Upload ID Document", "அடையாள ஆவணத்தை பதிவேற்று");
        tamilDict.put("Change Password", "கடவுச்சொல்லை மாற்று");
        tamilDict.put("Privacy Settings", "தனியுரிமை அமைப்புகள்");
        tamilDict.put("Notification Settings", "அறிவிப்பு அமைப்புகள்");
        tamilDict.put("Language Settings", "மொழி அமைப்புகள்");
        tamilDict.put("Theme Customization", "வண்ண தீம் தேர்வு");
        tamilDict.put("Booking & Rental History", "வாடகை வரலாறு");
        tamilDict.put("Help & Support", "உதவி & ஆதரவு");
        tamilDict.put("Terms & Privacy Policy", "விதிமுறைகள் & தனியுரிமை");
        tamilDict.put("About RentWise", "ரென்ட்வைஸ் பற்றி");
        tamilDict.put("Logout", "வெளியேறு");
    }
}
