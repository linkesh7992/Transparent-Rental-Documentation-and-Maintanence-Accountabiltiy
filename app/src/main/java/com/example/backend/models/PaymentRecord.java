package com.example.backend.models;

import java.io.Serializable;

public class PaymentRecord implements Serializable {
    private String id;
    private String propertyId;
    private String propertyTitle;
    private String tenantId;
    private String tenantName;
    private String landlordId;
    private String landlordName;
    private double amount;
    private PaymentType paymentType;
    private PaymentStatus paymentStatus;
    private PaymentMethod paymentMethod;
    private String transactionId;
    private String date;
    private String time;
    private String dueDate;
    private boolean isAutoPay;
    private String receiptNumber;
    private boolean digitalTenantAck;
    private boolean digitalLandlordAck;
    private String tenantAckTime;
    private String landlordAckTime;
    private boolean hasDispute;
    private String disputeReason;
    private String payerRole; // Tenant, Landlord, Shared

    // Payment breakdown
    private double breakdownRent;
    private double breakdownDeposit;
    private double breakdownMaintenance;
    private double breakdownRepair;
    private double breakdownOthers;

    public PaymentRecord(String id, String propertyId, String propertyTitle, String tenantId,
                         String tenantName, String landlordId, String landlordName, double amount,
                         PaymentType paymentType, PaymentStatus paymentStatus, PaymentMethod paymentMethod,
                         String transactionId, String date, String time, String dueDate,
                         boolean isAutoPay, String receiptNumber, boolean digitalTenantAck,
                         boolean digitalLandlordAck, String tenantAckTime, String landlordAckTime,
                         boolean hasDispute, String disputeReason, String payerRole,
                         double breakdownRent, double breakdownDeposit, double breakdownMaintenance,
                         double breakdownRepair, double breakdownOthers) {
        this.id = id;
        this.propertyId = propertyId;
        this.propertyTitle = propertyTitle;
        this.tenantId = tenantId;
        this.tenantName = tenantName;
        this.landlordId = landlordId;
        this.landlordName = landlordName;
        this.amount = amount;
        this.paymentType = paymentType;
        this.paymentStatus = paymentStatus;
        this.paymentMethod = paymentMethod;
        this.transactionId = transactionId;
        this.date = date;
        this.time = time;
        this.dueDate = dueDate;
        this.isAutoPay = isAutoPay;
        this.receiptNumber = receiptNumber;
        this.digitalTenantAck = digitalTenantAck;
        this.digitalLandlordAck = digitalLandlordAck;
        this.tenantAckTime = tenantAckTime;
        this.landlordAckTime = landlordAckTime;
        this.hasDispute = hasDispute;
        this.disputeReason = disputeReason;
        this.payerRole = payerRole;
        this.breakdownRent = breakdownRent;
        this.breakdownDeposit = breakdownDeposit;
        this.breakdownMaintenance = breakdownMaintenance;
        this.breakdownRepair = breakdownRepair;
        this.breakdownOthers = breakdownOthers;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getPropertyId() { return propertyId; }
    public void setPropertyId(String propertyId) { this.propertyId = propertyId; }

    public String getPropertyTitle() { return propertyTitle; }
    public void setPropertyTitle(String propertyTitle) { this.propertyTitle = propertyTitle; }

    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }

    public String getTenantName() { return tenantName; }
    public void setTenantName(String tenantName) { this.tenantName = tenantName; }

    public String getLandlordId() { return landlordId; }
    public void setLandlordId(String landlordId) { this.landlordId = landlordId; }

    public String getLandlordName() { return landlordName; }
    public void setLandlordName(String landlordName) { this.landlordName = landlordName; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public PaymentType getPaymentType() { return paymentType; }
    public void setPaymentType(PaymentType paymentType) { this.paymentType = paymentType; }

    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }

    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public String getDueDate() { return dueDate; }
    public void setDueDate(String dueDate) { this.dueDate = dueDate; }

    public boolean isAutoPay() { return isAutoPay; }
    public void setAutoPay(boolean autoPay) { isAutoPay = autoPay; }

    public String getReceiptNumber() { return receiptNumber; }
    public void setReceiptNumber(String receiptNumber) { this.receiptNumber = receiptNumber; }

    public boolean isDigitalTenantAck() { return digitalTenantAck; }
    public void setDigitalTenantAck(boolean digitalTenantAck) { this.digitalTenantAck = digitalTenantAck; }

    public boolean isDigitalLandlordAck() { return digitalLandlordAck; }
    public void setDigitalLandlordAck(boolean digitalLandlordAck) { this.digitalLandlordAck = digitalLandlordAck; }

    public String getTenantAckTime() { return tenantAckTime; }
    public void setTenantAckTime(String tenantAckTime) { this.tenantAckTime = tenantAckTime; }

    public String getLandlordAckTime() { return landlordAckTime; }
    public void setLandlordAckTime(String landlordAckTime) { this.landlordAckTime = landlordAckTime; }

    public boolean isHasDispute() { return hasDispute; }
    public void setHasDispute(boolean hasDispute) { this.hasDispute = hasDispute; }

    public String getDisputeReason() { return disputeReason; }
    public void setDisputeReason(String disputeReason) { this.disputeReason = disputeReason; }

    public String getPayerRole() { return payerRole; }
    public void setPayerRole(String payerRole) { this.payerRole = payerRole; }

    public double getBreakdownRent() { return breakdownRent; }
    public void setBreakdownRent(double breakdownRent) { this.breakdownRent = breakdownRent; }

    public double getBreakdownDeposit() { return breakdownDeposit; }
    public void setBreakdownDeposit(double breakdownDeposit) { this.breakdownDeposit = breakdownDeposit; }

    public double getBreakdownMaintenance() { return breakdownMaintenance; }
    public void setBreakdownMaintenance(double breakdownMaintenance) { this.breakdownMaintenance = breakdownMaintenance; }

    public double getBreakdownRepair() { return breakdownRepair; }
    public void setBreakdownRepair(double breakdownRepair) { this.breakdownRepair = breakdownRepair; }

    public double getBreakdownOthers() { return breakdownOthers; }
    public void setBreakdownOthers(double breakdownOthers) { this.breakdownOthers = breakdownOthers; }
}
