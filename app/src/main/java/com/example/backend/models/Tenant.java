package com.example.backend.models;

public class Tenant extends User {
    private String currentPropertyId;
    private String leaseStartDate;
    private String leaseEndDate;
    private double monthlyRent;
    private String nextDueDate;
    private int activeMaintenanceCount;

    public Tenant(String id, String name, String email, String phone, String address,
                  String username, String avatarUrl, boolean isGovtIdVerified,
                  String govtIdType, String currentPropertyId, String leaseStartDate,
                  String leaseEndDate, double monthlyRent, String nextDueDate,
                  int activeMaintenanceCount) {
        super(id, name, email, phone, address, username, avatarUrl, isGovtIdVerified, govtIdType, UserRole.TENANT);
        this.currentPropertyId = currentPropertyId;
        this.leaseStartDate = leaseStartDate;
        this.leaseEndDate = leaseEndDate;
        this.monthlyRent = monthlyRent;
        this.nextDueDate = nextDueDate;
        this.activeMaintenanceCount = activeMaintenanceCount;
    }

    @Override
    public String getRoleSpecificSummary() {
        return "Tenant - Rent: ₹" + monthlyRent + " (Due: " + nextDueDate + ")";
    }

    public String getCurrentPropertyId() { return currentPropertyId; }
    public void setCurrentPropertyId(String currentPropertyId) { this.currentPropertyId = currentPropertyId; }

    public String getLeaseStartDate() { return leaseStartDate; }
    public void setLeaseStartDate(String leaseStartDate) { this.leaseStartDate = leaseStartDate; }

    public String getLeaseEndDate() { return leaseEndDate; }
    public void setLeaseEndDate(String leaseEndDate) { this.leaseEndDate = leaseEndDate; }

    public double getMonthlyRent() { return monthlyRent; }
    public void setMonthlyRent(double monthlyRent) { this.monthlyRent = monthlyRent; }

    public String getNextDueDate() { return nextDueDate; }
    public void setNextDueDate(String nextDueDate) { this.nextDueDate = nextDueDate; }

    public int getActiveMaintenanceCount() { return activeMaintenanceCount; }
    public void setActiveMaintenanceCount(int activeMaintenanceCount) { this.activeMaintenanceCount = activeMaintenanceCount; }
}
