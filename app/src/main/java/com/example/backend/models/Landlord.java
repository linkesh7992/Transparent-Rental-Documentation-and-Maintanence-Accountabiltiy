package com.example.backend.models;

import java.util.ArrayList;
import java.util.List;

public class Landlord extends User {
    private List<String> ownedPropertyIds;
    private int totalProperties;
    private int occupiedRooms;
    private int pendingRequests;
    private double monthlyRevenue;

    public Landlord(String id, String name, String email, String phone, String address,
                    String username, String avatarUrl, boolean isGovtIdVerified,
                    String govtIdType, List<String> ownedPropertyIds, int totalProperties,
                    int occupiedRooms, int pendingRequests, double monthlyRevenue) {
        super(id, name, email, phone, address, username, avatarUrl, isGovtIdVerified, govtIdType, UserRole.LANDLORD);
        this.ownedPropertyIds = ownedPropertyIds != null ? ownedPropertyIds : new ArrayList<>();
        this.totalProperties = totalProperties;
        this.occupiedRooms = occupiedRooms;
        this.pendingRequests = pendingRequests;
        this.monthlyRevenue = monthlyRevenue;
    }

    @Override
    public String getRoleSpecificSummary() {
        return "Landlord - " + totalProperties + " Properties (" + occupiedRooms + " Occupied), Revenue: ₹" + monthlyRevenue;
    }

    public List<String> getOwnedPropertyIds() { return ownedPropertyIds; }
    public void setOwnedPropertyIds(List<String> ownedPropertyIds) { this.ownedPropertyIds = ownedPropertyIds; }

    public int getTotalProperties() { return totalProperties; }
    public void setTotalProperties(int totalProperties) { this.totalProperties = totalProperties; }

    public int getOccupiedRooms() { return occupiedRooms; }
    public void setOccupiedRooms(int occupiedRooms) { this.occupiedRooms = occupiedRooms; }

    public int getPendingRequests() { return pendingRequests; }
    public void setPendingRequests(int pendingRequests) { this.pendingRequests = pendingRequests; }

    public double getMonthlyRevenue() { return monthlyRevenue; }
    public void setMonthlyRevenue(double monthlyRevenue) { this.monthlyRevenue = monthlyRevenue; }
}
