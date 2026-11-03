package com.example.backend.services;

import com.example.backend.models.Property;
import com.example.backend.repository.RentWiseRepository;
import java.util.ArrayList;
import java.util.List;

public class PropertyService {
    private final RentWiseRepository repository;

    public PropertyService() {
        this.repository = RentWiseRepository.getInstance();
    }

    public List<Property> searchProperties(String query, String propertyType, Double minBudget, Double maxBudget) {
        List<Property> all = repository.getAllProperties();
        List<Property> result = new ArrayList<>();
        String q = query != null ? query.toLowerCase().trim() : "";

        for (Property p : all) {
            boolean matchesQuery = q.isEmpty() ||
                    p.getTitle().toLowerCase().contains(q) ||
                    p.getTitleTamil().contains(q) ||
                    p.getAddress().toLowerCase().contains(q) ||
                    p.getCity().toLowerCase().contains(q);

            boolean matchesType = propertyType == null || propertyType.equalsIgnoreCase("All") ||
                    p.getPropertyType().equalsIgnoreCase(propertyType);

            boolean matchesBudget = (minBudget == null || p.getPrice() >= minBudget) &&
                    (maxBudget == null || p.getPrice() <= maxBudget);

            if (matchesQuery && matchesType && matchesBudget) {
                result.add(p);
            }
        }
        return result;
    }

    public List<Property> getRecommendedProperties() {
        List<Property> list = new ArrayList<>();
        for (Property p : repository.getAllProperties()) {
            if (p.isRecommended()) list.add(p);
        }
        return list;
    }

    public List<Property> getNearbyProperties() {
        List<Property> list = new ArrayList<>();
        for (Property p : repository.getAllProperties()) {
            if (p.isNearby()) list.add(p);
        }
        return list;
    }

    public List<Property> getVerifiedProperties() {
        List<Property> list = new ArrayList<>();
        for (Property p : repository.getAllProperties()) {
            if (p.isVerified()) list.add(p);
        }
        return list;
    }
}
