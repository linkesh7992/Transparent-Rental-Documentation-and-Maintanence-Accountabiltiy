package com.example.backend.services;

import com.example.backend.models.MaintenanceCategory;
import com.example.backend.models.MaintenanceIssue;
import com.example.backend.models.MaintenancePriority;
import com.example.backend.models.MaintenanceStatus;
import com.example.backend.repository.RentWiseRepository;
import java.util.ArrayList;
import java.util.List;

public class MaintenanceService {
    private final RentWiseRepository repository;

    public MaintenanceService() {
        this.repository = RentWiseRepository.getInstance();
    }

    public List<MaintenanceIssue> filterIssues(String searchQuery, MaintenanceCategory category,
                                                MaintenancePriority priority, MaintenanceStatus status) {
        List<MaintenanceIssue> all = repository.getMaintenanceIssues();
        List<MaintenanceIssue> result = new ArrayList<>();
        String q = searchQuery != null ? searchQuery.toLowerCase().trim() : "";

        for (MaintenanceIssue issue : all) {
            boolean matchesQuery = q.isEmpty() ||
                    issue.getTitle().toLowerCase().contains(q) ||
                    issue.getDescription().toLowerCase().contains(q) ||
                    issue.getId().toLowerCase().contains(q);

            boolean matchesCat = category == null || issue.getCategory() == category;
            boolean matchesPri = priority == null || issue.getPriority() == priority;
            boolean matchesStat = status == null || issue.getStatus() == status;

            if (matchesQuery && matchesCat && matchesPri && matchesStat) {
                result.add(issue);
            }
        }
        return result;
    }

    public int getActiveIssueCount() {
        int count = 0;
        for (MaintenanceIssue issue : repository.getMaintenanceIssues()) {
            if (issue.getStatus() != MaintenanceStatus.COMPLETED) {
                count++;
            }
        }
        return count;
    }
}
