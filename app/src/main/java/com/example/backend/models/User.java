package com.example.backend.models;

import java.io.Serializable;
import java.util.Objects;

/**
 * Base User class demonstrating OOP Encapsulation and Polymorphism.
 */
public abstract class User implements Serializable {
    private String id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String username;
    private String avatarUrl;
    private boolean isGovtIdVerified;
    private String govtIdType;
    private UserRole role;

    public User(String id, String name, String email, String phone, String address, String username, String avatarUrl, boolean isGovtIdVerified, String govtIdType, UserRole role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.username = username;
        this.avatarUrl = avatarUrl;
        this.isGovtIdVerified = isGovtIdVerified;
        this.govtIdType = govtIdType;
        this.role = role;
    }

    public abstract String getRoleSpecificSummary();

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    public boolean isGovtIdVerified() { return isGovtIdVerified; }
    public void setGovtIdVerified(boolean govtIdVerified) { isGovtIdVerified = govtIdVerified; }

    public String getGovtIdType() { return govtIdType; }
    public void setGovtIdType(String govtIdType) { this.govtIdType = govtIdType; }

    public UserRole getRole() { return role; }
    protected void setRole(UserRole role) { this.role = role; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
