package com.example.backend.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Property implements Serializable {
    private String id;
    private String title;
    private String titleTamil;
    private String description;
    private String address;
    private String city;
    private double price;
    private double securityDeposit;
    private String propertyType; // Apartment, Villa, Independent House, Studio
    private int bedrooms;
    private int bathrooms;
    private int squareFeet;
    private boolean isVerified;
    private boolean isRecommended;
    private boolean isNearby;
    private double rating;
    private int reviewCount;
    private String landlordId;
    private String landlordName;
    private String landlordPhone;
    private String currentTenantId;
    private boolean isOccupied;
    private boolean isSaved;
    private List<String> amenities;
    private List<String> imageUrls;
    private String furnishingStatus; // Fully Furnished, Semi-Furnished, Unfurnished

    public Property(String id, String title, String titleTamil, String description, String address,
                    String city, double price, double securityDeposit, String propertyType,
                    int bedrooms, int bathrooms, int squareFeet, boolean isVerified,
                    boolean isRecommended, boolean isNearby, double rating, int reviewCount,
                    String landlordId, String landlordName, String landlordPhone,
                    String currentTenantId, boolean isOccupied, boolean isSaved,
                    List<String> amenities, List<String> imageUrls, String furnishingStatus) {
        this.id = id;
        this.title = title;
        this.titleTamil = titleTamil;
        this.description = description;
        this.address = address;
        this.city = city;
        this.price = price;
        this.securityDeposit = securityDeposit;
        this.propertyType = propertyType;
        this.bedrooms = bedrooms;
        this.bathrooms = bathrooms;
        this.squareFeet = squareFeet;
        this.isVerified = isVerified;
        this.isRecommended = isRecommended;
        this.isNearby = isNearby;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.landlordId = landlordId;
        this.landlordName = landlordName;
        this.landlordPhone = landlordPhone;
        this.currentTenantId = currentTenantId;
        this.isOccupied = isOccupied;
        this.isSaved = isSaved;
        this.amenities = amenities != null ? amenities : new ArrayList<>();
        this.imageUrls = imageUrls != null ? imageUrls : new ArrayList<>();
        this.furnishingStatus = furnishingStatus;
    }

    public String getDisplayTitle(boolean isTamil) {
        if (isTamil && titleTamil != null && !titleTamil.isEmpty()) {
            return titleTamil;
        }
        return title;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getTitleTamil() { return titleTamil; }
    public void setTitleTamil(String titleTamil) { this.titleTamil = titleTamil; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public double getSecurityDeposit() { return securityDeposit; }
    public void setSecurityDeposit(double securityDeposit) { this.securityDeposit = securityDeposit; }

    public String getPropertyType() { return propertyType; }
    public void setPropertyType(String propertyType) { this.propertyType = propertyType; }

    public int getBedrooms() { return bedrooms; }
    public void setBedrooms(int bedrooms) { this.bedrooms = bedrooms; }

    public int getBathrooms() { return bathrooms; }
    public void setBathrooms(int bathrooms) { this.bathrooms = bathrooms; }

    public int getSquareFeet() { return squareFeet; }
    public void setSquareFeet(int squareFeet) { this.squareFeet = squareFeet; }

    public boolean isVerified() { return isVerified; }
    public void setVerified(boolean verified) { isVerified = verified; }

    public boolean isRecommended() { return isRecommended; }
    public void setRecommended(boolean recommended) { isRecommended = recommended; }

    public boolean isNearby() { return isNearby; }
    public void setNearby(boolean nearby) { isNearby = nearby; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public int getReviewCount() { return reviewCount; }
    public void setReviewCount(int reviewCount) { this.reviewCount = reviewCount; }

    public String getLandlordId() { return landlordId; }
    public void setLandlordId(String landlordId) { this.landlordId = landlordId; }

    public String getLandlordName() { return landlordName; }
    public void setLandlordName(String landlordName) { this.landlordName = landlordName; }

    public String getLandlordPhone() { return landlordPhone; }
    public void setLandlordPhone(String landlordPhone) { this.landlordPhone = landlordPhone; }

    public String getCurrentTenantId() { return currentTenantId; }
    public void setCurrentTenantId(String currentTenantId) { this.currentTenantId = currentTenantId; }

    public boolean isOccupied() { return isOccupied; }
    public void setOccupied(boolean occupied) { isOccupied = occupied; }

    public boolean isSaved() { return isSaved; }
    public void setSaved(boolean saved) { isSaved = saved; }

    public List<String> getAmenities() { return amenities; }
    public void setAmenities(List<String> amenities) { this.amenities = amenities; }

    public List<String> getImageUrls() { return imageUrls; }
    public void setImageUrls(List<String> imageUrls) { this.imageUrls = imageUrls; }

    public String getFurnishingStatus() { return furnishingStatus; }
    public void setFurnishingStatus(String furnishingStatus) { this.furnishingStatus = furnishingStatus; }
}
