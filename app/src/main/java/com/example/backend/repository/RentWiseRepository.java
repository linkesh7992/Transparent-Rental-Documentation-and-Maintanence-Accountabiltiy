package com.example.backend.repository;

import com.example.backend.models.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * RentWise Central Repository implementing OOP Singleton, Repository, and Observable patterns.
 */
public class RentWiseRepository {
    private static RentWiseRepository instance;

    // Active session state
    private User currentUser;
    private UserRole activeRole = UserRole.TENANT;
    private Tenant defaultTenant;
    private Landlord defaultLandlord;
    private ThemePreset currentTheme = ThemePreset.RENTWISE_CYAN;
    private boolean isDarkMode = false;
    private float fontScale = 1.0f;

    // Collections
    private final List<Property> properties = new ArrayList<>();
    private final List<MaintenanceIssue> maintenanceIssues = new ArrayList<>();
    private final List<PaymentRecord> paymentRecords = new ArrayList<>();
    private final List<NoticeItem> notices = new ArrayList<>();
    private final List<NotificationItem> notifications = new ArrayList<>();
    private final Map<String, List<ChatMessage>> issueChatMap = new HashMap<>();
    private final List<String> savedPropertyIds = new ArrayList<>();

    // Change listeners for UI reactivity
    public interface DataChangeListener {
        void onDataChanged();
    }
    private final List<DataChangeListener> listeners = new ArrayList<>();

    private RentWiseRepository() {
        seedInitialData();
    }

    public static synchronized RentWiseRepository getInstance() {
        if (instance == null) {
            instance = new RentWiseRepository();
        }
        return instance;
    }

    public void addListener(DataChangeListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeListener(DataChangeListener listener) {
        listeners.remove(listener);
    }

    public void notifyChange() {
        for (DataChangeListener listener : listeners) {
            if (listener != null) {
                listener.onDataChanged();
            }
        }
    }

    private void seedInitialData() {
        // 1. Seed Tenant & Landlord
        defaultTenant = new Tenant(
                "tenant_001",
                "Arun Kumar",
                "arun.kumar@rentwise.in",
                "+91 98401 23456",
                "#402, Sai Residency, Velachery Main Road, Chennai",
                "arunkumar99",
                "avatar_tenant",
                true,
                "Aadhaar Card Verified",
                "prop_001",
                "01 Jan 2026",
                "31 Dec 2026",
                24500.0,
                "05 Oct 2026",
                2
        );

        defaultLandlord = new Landlord(
                "landlord_001",
                "Suresh Ramanathan",
                "suresh.properties@rentwise.in",
                "+91 94440 98765",
                "#12, Greenways Road, R.A Puram, Chennai",
                "suresh_landlord",
                "avatar_landlord",
                true,
                "Passport Verified",
                Arrays.asList("prop_001", "prop_002", "prop_003"),
                6,
                5,
                3,
                148000.0
        );

        currentUser = defaultTenant;
        activeRole = UserRole.TENANT;

        // 2. Seed Properties
        Property p1 = new Property(
                "prop_001",
                "Skyline Heights 3BHK Luxury Flat",
                "ஸ்கைலைன் ஹைட்ஸ் 3BHK சொகுசு பிளாட்",
                "Spacious 3 BHK apartment with modular kitchen, private balcony, gated security and covered car parking. Near Velachery Metro.",
                "Plot 44, Velachery Bypass Road",
                "Chennai",
                24500.0,
                100000.0,
                "Apartment",
                3,
                2,
                1450,
                true,
                true,
                true,
                4.8,
                42,
                "landlord_001",
                "Suresh Ramanathan",
                "+91 94440 98765",
                "tenant_001",
                true,
                true,
                Arrays.asList("Elevator", "24/7 Power Backup", "Gym", "Swimming Pool", "Car Parking", "CCTV Security"),
                Arrays.asList("skyline_1", "skyline_2"),
                "Fully Furnished"
        );

        Property p2 = new Property(
                "prop_002",
                "Greenview Independent Villa",
                "கிரீன்வியூ தனித்தனி வில்லா",
                "Serene 4BHK independent villa with private garden, terrace gazebo, solar heating, and spacious living rooms in Sholinganallur IT corridor.",
                "15/B East Coast Villa Enclave",
                "Chennai",
                48000.0,
                200000.0,
                "Villa",
                4,
                4,
                2600,
                true,
                true,
                false,
                4.9,
                28,
                "landlord_001",
                "Suresh Ramanathan",
                "+91 94440 98765",
                "",
                false,
                false,
                Arrays.asList("Private Garden", "Solar Power", "Smart Locks", "Clubhouse", "EV Charging Point"),
                Arrays.asList("villa_1", "villa_2"),
                "Semi-Furnished"
        );

        Property p3 = new Property(
                "prop_003",
                "Metroline Studio Apartment",
                "மெட்ரோலைன் ஸ்டுடியோ அபார்ட்மெண்ட்",
                "Modern compact studio flat designed for working professionals. High-speed internet enabled, modern kitchenette and gym access.",
                "Tower C, OMR Tech Park Road",
                "Chennai",
                14000.0,
                50000.0,
                "Studio",
                1,
                1,
                620,
                true,
                false,
                true,
                4.6,
                19,
                "landlord_001",
                "Suresh Ramanathan",
                "+91 94440 98765",
                "tenant_002",
                true,
                false,
                Arrays.asList("High Speed Wi-Fi", "RO Water Filter", "Laundry Room", "Security Guard"),
                Arrays.asList("studio_1"),
                "Fully Furnished"
        );

        Property p4 = new Property(
                "prop_004",
                "Heritage 2BHK Garden House",
                "ஹெரிட்டேஜ் 2BHK தோட்ட வீடு",
                "Traditional airy home with antique wooden ceilings, modern bathrooms, organic kitchen garden and quiet neighborhood near Mylapore.",
                "8 South Mada Street, Mylapore",
                "Chennai",
                28000.0,
                120000.0,
                "Independent House",
                2,
                2,
                1100,
                true,
                true,
                true,
                4.7,
                35,
                "landlord_002",
                "Meenakshi Sundaram",
                "+91 98840 54321",
                "",
                false,
                true,
                Arrays.asList("Garden", "Traditional Architecture", "Corporation Water", "Car Porch"),
                Arrays.asList("heritage_1"),
                "Semi-Furnished"
        );

        Property p5 = new Property(
                "prop_005",
                "Palm Grove 3BHK Lakeview Penthouse",
                "பாம் குரோவ் 3BHK ஏரிக்கரை பென்ட்ஹவுஸ்",
                "Stunning penthouse overlooking Pallikaranai marshland and lake with private sun-deck terrace, ambient lighting, and central AC.",
                "22 Lake Bund Road, Medavakkam",
                "Chennai",
                36000.0,
                150000.0,
                "Apartment",
                3,
                3,
                1850,
                true,
                false,
                false,
                4.85,
                51,
                "landlord_001",
                "Suresh Ramanathan",
                "+91 94440 98765",
                "",
                false,
                false,
                Arrays.asList("Lake View", "Sun Deck", "Jacuzzi", "Central AC", "Concierge Desk"),
                Arrays.asList("penthouse_1"),
                "Fully Furnished"
        );

        properties.addAll(Arrays.asList(p1, p2, p3, p4, p5));
        savedPropertyIds.add("prop_001");
        savedPropertyIds.add("prop_004");

        // 3. Seed Maintenance Issues
        Technician tech1 = new Technician("tech_001", "Karthik Raj", "+91 98412 11223", "Senior Electrician", "Today, 4:30 PM", 4.9);
        Technician tech2 = new Technician("tech_002", "Selvamurugan P.", "+91 97908 33445", "Master Plumber", "Tomorrow, 10:00 AM", 4.7);

        MaintenanceIssue issue1 = new MaintenanceIssue(
                "ISS-2026-8801",
                "Geyser MCB tripping in Master Bathroom",
                "The 25A geyser circuit breaker keeps tripping instantly when switched on. Mild burning odor noticed near the switchboard.",
                MaintenanceCategory.ELECTRICAL,
                MaintenancePriority.HIGH,
                MaintenanceStatus.IN_PROGRESS,
                4, // WORK_STARTED
                "prop_001",
                "Skyline Heights 3BHK Luxury Flat",
                "tenant_001",
                "Arun Kumar",
                "landlord_001",
                "28 Aug 2026, 09:15 AM",
                "02 Sep 2026, 11:30 AM",
                Arrays.asList("photo_geyser_1.jpg", "photo_mcb_2.jpg"),
                true,
                18,
                "The breaker trips with a loud spark right after powering the geyser. Requesting urgent inspection.",
                tech1,
                1250.0,
                "https://rentwise.in/bills/INV-8801.pdf",
                true,
                5,
                "Technician arrived on time and replaced the faulty 32A Legrand MCB switch.",
                CostResponsibility.LANDLORD_PAYS,
                Arrays.asList("Havells_Warranty_Card.pdf", "Electrician_Bill_8801.pdf"),
                false,
                ""
        );

        MaintenanceIssue issue2 = new MaintenanceIssue(
                "ISS-2026-8802",
                "Kitchen sink drain pipe leak under granite slab",
                "Continuous seepage under the sink cupboard causing dampness on lower cabinet woodwork.",
                MaintenanceCategory.PLUMBING,
                MaintenancePriority.MEDIUM,
                MaintenanceStatus.PENDING,
                2, // ACKNOWLEDGED
                "prop_001",
                "Skyline Heights 3BHK Luxury Flat",
                "tenant_001",
                "Arun Kumar",
                "landlord_001",
                "01 Sep 2026, 06:40 PM",
                "02 Sep 2026, 08:00 AM",
                Arrays.asList("photo_sink_leak.jpg"),
                false,
                0,
                "",
                tech2,
                650.0,
                "",
                false,
                0,
                "",
                CostResponsibility.SHARED_COST,
                Arrays.asList("Sink_Coupling_Invoice.pdf"),
                false,
                ""
        );

        MaintenanceIssue issue3 = new MaintenanceIssue(
                "ISS-2026-8803",
                "Balcony sliding door roller jam",
                "The right sliding wooden door has come off the lower track and requires realignment.",
                MaintenanceCategory.CARPENTER,
                MaintenancePriority.LOW,
                MaintenanceStatus.COMPLETED,
                8, // COMPLETED
                "prop_001",
                "Skyline Heights 3BHK Luxury Flat",
                "tenant_001",
                "Arun Kumar",
                "landlord_001",
                "15 Aug 2026, 02:20 PM",
                "18 Aug 2026, 05:00 PM",
                new ArrayList<>(),
                false,
                0,
                "",
                new Technician("tech_003", "Dharmendra Woodworks", "+91 94441 55667", "Carpenter", "Completed", 4.8),
                450.0,
                "https://rentwise.in/bills/INV-8803.pdf",
                true,
                5,
                "Smooth sliding restored quickly. Excellent service.",
                CostResponsibility.TENANT_PAYS,
                Arrays.asList("Bill_8803.pdf"),
                false,
                ""
        );

        maintenanceIssues.addAll(Arrays.asList(issue1, issue2, issue3));

        // Seed Chat Messages for issue1
        List<ChatMessage> chatList1 = new ArrayList<>();
        chatList1.add(new ChatMessage("c1", "ISS-2026-8801", UserRole.TENANT, "Arun Kumar", "Hi Suresh sir, the master bathroom geyser breaker tripped suddenly with a small spark.", "28 Aug 09:20 AM", true));
        chatList1.add(new ChatMessage("c2", "ISS-2026-8801", UserRole.LANDLORD, "Suresh Ramanathan", "Hello Arun, thanks for reporting. Please do not turn it on again for safety. I am assigning technician Karthik.", "28 Aug 09:45 AM", true));
        chatList1.add(new ChatMessage("c3", "ISS-2026-8801", UserRole.LANDLORD, "Suresh Ramanathan", "Karthik will reach your flat at 4:30 PM today. Cost will be covered by landlord account.", "28 Aug 10:10 AM", true));
        chatList1.add(new ChatMessage("c4", "ISS-2026-8801", UserRole.TENANT, "Arun Kumar", "Thank you, I will be available at home.", "28 Aug 10:15 AM", true));
        issueChatMap.put("ISS-2026-8801", chatList1);

        // 4. Seed Payments
        PaymentRecord pay1 = new PaymentRecord(
                "PAY-2026-901",
                "prop_001",
                "Skyline Heights 3BHK Luxury Flat",
                "tenant_001",
                "Arun Kumar",
                "landlord_001",
                "Suresh Ramanathan",
                24500.0,
                PaymentType.MONTHLY_RENT,
                PaymentStatus.SUCCESSFUL,
                PaymentMethod.UPI,
                "UPI/HDFC/20260901/88921104",
                "01 Sep 2026",
                "10:14 AM",
                "05 Sep 2026",
                true,
                "REC-RW-2026-0901",
                true,
                true,
                "01 Sep 2026, 10:15 AM",
                "01 Sep 2026, 10:30 AM",
                false,
                "",
                "Tenant",
                24500.0,
                0.0,
                0.0,
                0.0,
                0.0
        );

        PaymentRecord pay2 = new PaymentRecord(
                "PAY-2026-902",
                "prop_001",
                "Skyline Heights 3BHK Luxury Flat",
                "tenant_001",
                "Arun Kumar",
                "landlord_001",
                "Suresh Ramanathan",
                2500.0,
                PaymentType.MAINTENANCE,
                PaymentStatus.SUCCESSFUL,
                PaymentMethod.NET_BANKING,
                "NB/ICICI/20260902/55129988",
                "02 Sep 2026",
                "11:00 AM",
                "05 Sep 2026",
                false,
                "REC-RW-2026-0902",
                true,
                true,
                "02 Sep 2026, 11:02 AM",
                "02 Sep 2026, 11:15 AM",
                false,
                "",
                "Tenant",
                0.0,
                0.0,
                2500.0,
                0.0,
                0.0
        );

        PaymentRecord pay3 = new PaymentRecord(
                "PAY-2026-903",
                "prop_001",
                "Skyline Heights 3BHK Luxury Flat",
                "tenant_001",
                "Arun Kumar",
                "landlord_001",
                "Suresh Ramanathan",
                24500.0,
                PaymentType.MONTHLY_RENT,
                PaymentStatus.PENDING,
                PaymentMethod.UPI,
                "TXN-PENDING-OCT",
                "05 Oct 2026",
                "09:00 AM",
                "05 Oct 2026",
                true,
                "REC-RW-UPCOMING-OCT",
                false,
                false,
                "",
                "",
                false,
                "",
                "Tenant",
                24500.0,
                0.0,
                0.0,
                0.0,
                0.0
        );

        PaymentRecord pay4 = new PaymentRecord(
                "PAY-2026-801",
                "prop_001",
                "Skyline Heights 3BHK Luxury Flat",
                "tenant_001",
                "Arun Kumar",
                "landlord_001",
                "Suresh Ramanathan",
                100000.0,
                PaymentType.SECURITY_DEPOSIT,
                PaymentStatus.SUCCESSFUL,
                PaymentMethod.BANK_TRANSFER,
                "NEFT/SBI/20260101/99221144",
                "01 Jan 2026",
                "03:45 PM",
                "01 Jan 2026",
                false,
                "REC-RW-SEC-2026",
                true,
                true,
                "01 Jan 2026, 03:50 PM",
                "01 Jan 2026, 04:00 PM",
                false,
                "",
                "Tenant",
                0.0,
                100000.0,
                0.0,
                0.0,
                0.0
        );

        paymentRecords.addAll(Arrays.asList(pay1, pay2, pay3, pay4));

        // 5. Seed Notices
        NoticeItem n1 = new NoticeItem(
                "NOT-2026-01",
                "Lease Renewal Window Open (90 Days Reminder)",
                "Your 12-month rental agreement for Skyline Heights #402 expires on 31 Dec 2026. Please indicate your renewal confirmation within the next 30 days.",
                NoticeType.LEASE_RENEWAL_NOTICE,
                90,
                "31 Dec 2026",
                "01 Sep 2026",
                "prop_001",
                "Skyline Heights 3BHK Luxury Flat",
                "tenant_001",
                "landlord_001",
                false,
                true,
                "",
                "01 Sep 2026, 10:00 AM"
        );

        NoticeItem n2 = new NoticeItem(
                "NOT-2026-02",
                "Annual Water Tank Cleaning & Maintenance",
                "Society overhead water tanks will undergo chemical disinfection this Saturday (05 Sep) between 10:00 AM and 02:00 PM. Water supply will be temporarily paused.",
                NoticeType.GENERAL_ANNOUNCEMENT,
                60,
                "05 Sep 2026",
                "02 Sep 2026",
                "prop_001",
                "Skyline Heights 3BHK Luxury Flat",
                "tenant_001",
                "landlord_001",
                true,
                true,
                "02 Sep 2026, 12:00 PM",
                "02 Sep 2026, 09:00 AM"
        );

        NoticeItem n3 = new NoticeItem(
                "NOT-2026-03",
                "Standard 5% Rent Revision Advisory for FY 2027",
                "As per clause 4.2 of the registered lease deed, the standard 5% annual revision will apply upon renewal starting January 2027.",
                NoticeType.RENT_INCREASE_NOTICE,
                30,
                "01 Jan 2027",
                "15 Aug 2026",
                "prop_001",
                "Skyline Heights 3BHK Luxury Flat",
                "tenant_001",
                "landlord_001",
                true,
                true,
                "16 Aug 2026, 02:15 PM",
                "15 Aug 2026, 11:30 AM"
        );

        notices.addAll(Arrays.asList(n1, n2, n3));

        // 6. Seed Notifications
        notifications.add(new NotificationItem("notif_1", "Maintenance Assigned", "Technician Karthik Raj assigned to your Geyser issue (ISS-2026-8801).", "MAINTENANCE", "10m ago", false, UserRole.TENANT));
        notifications.add(new NotificationItem("notif_2", "Rent Payment Received", "₹24,500 for September 2026 rent successfully credited.", "PAYMENT", "1d ago", true, UserRole.LANDLORD));
        notifications.add(new NotificationItem("notif_3", "Notice Issued: Lease Renewal", "90-day Lease Renewal notice issued for flat #402.", "NOTICE", "2d ago", true, UserRole.TENANT));
        notifications.add(new NotificationItem("notif_4", "Scheduled Maintenance Alert", "Water tank cleaning scheduled for 05 Sep.", "ANNOUNCEMENT", "3d ago", true, UserRole.TENANT));
    }

    // Role Switcher
    public void switchRole(UserRole newRole) {
        this.activeRole = newRole;
        if (newRole == UserRole.TENANT) {
            this.currentUser = defaultTenant;
        } else {
            this.currentUser = defaultLandlord;
        }
        notifyChange();
    }

    public UserRole getActiveRole() { return activeRole; }
    public User getCurrentUser() { return currentUser; }
    public Tenant getDefaultTenant() { return defaultTenant; }
    public Landlord getDefaultLandlord() { return defaultLandlord; }

    public ThemePreset getCurrentTheme() { return currentTheme; }
    public void setCurrentTheme(ThemePreset currentTheme) {
        this.currentTheme = currentTheme;
        notifyChange();
    }

    public boolean isDarkMode() { return isDarkMode; }
    public void setDarkMode(boolean darkMode) {
        isDarkMode = darkMode;
        notifyChange();
    }

    public float getFontScale() { return fontScale; }
    public void setFontScale(float fontScale) {
        this.fontScale = fontScale;
        notifyChange();
    }

    // Properties API
    public List<Property> getAllProperties() { return new ArrayList<>(properties); }

    public Property getPropertyById(String id) {
        for (Property p : properties) {
            if (p.getId().equals(id)) return p;
        }
        return null;
    }

    public void addProperty(Property p) {
        properties.add(0, p);
        if (activeRole == UserRole.LANDLORD) {
            defaultLandlord.setTotalProperties(defaultLandlord.getTotalProperties() + 1);
        }
        notifyChange();
    }

    public void toggleSaveProperty(String propertyId) {
        if (savedPropertyIds.contains(propertyId)) {
            savedPropertyIds.remove(propertyId);
        } else {
            savedPropertyIds.add(propertyId);
        }
        for (Property p : properties) {
            if (p.getId().equals(propertyId)) {
                p.setSaved(savedPropertyIds.contains(propertyId));
            }
        }
        notifyChange();
    }

    public List<Property> getSavedProperties() {
        List<Property> list = new ArrayList<>();
        for (Property p : properties) {
            if (savedPropertyIds.contains(p.getId())) {
                list.add(p);
            }
        }
        return list;
    }

    // Maintenance Issues API
    public List<MaintenanceIssue> getMaintenanceIssues() { return new ArrayList<>(maintenanceIssues); }

    public MaintenanceIssue getMaintenanceIssueById(String id) {
        for (MaintenanceIssue m : maintenanceIssues) {
            if (m.getId().equals(id)) return m;
        }
        return null;
    }

    public void addMaintenanceIssue(MaintenanceIssue issue) {
        maintenanceIssues.add(0, issue);
        // Add automatic notification
        notifications.add(0, new NotificationItem(
                "notif_" + System.currentTimeMillis(),
                "New Maintenance Request",
                issue.getTitle() + " submitted with " + issue.getPriority().name() + " priority.",
                "MAINTENANCE",
                "Just now",
                false,
                UserRole.LANDLORD
        ));
        notifyChange();
    }

    public void updateMaintenanceStatus(String issueId, MaintenanceStatus newStatus, int timelineStep) {
        MaintenanceIssue issue = getMaintenanceIssueById(issueId);
        if (issue != null) {
            issue.setStatus(newStatus);
            issue.setCurrentTimelineStepIndex(timelineStep);
            issue.setUpdatedAt(new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(new Date()));
            notifyChange();
        }
    }

    public void advanceMaintenanceStep(String issueId) {
        MaintenanceIssue issue = getMaintenanceIssueById(issueId);
        if (issue != null) {
            int cur = issue.getCurrentTimelineStepIndex();
            if (cur < 8) {
                cur++;
                issue.setCurrentTimelineStepIndex(cur);
                // Update status mapping
                if (cur >= 8) issue.setStatus(MaintenanceStatus.COMPLETED);
                else if (cur >= 7) issue.setStatus(MaintenanceStatus.WAITING_FOR_TENANT_VERIFICATION);
                else if (cur >= 5) issue.setStatus(MaintenanceStatus.WAITING_FOR_PAYMENT);
                else if (cur >= 4) issue.setStatus(MaintenanceStatus.IN_PROGRESS);
                else if (cur >= 2) issue.setStatus(MaintenanceStatus.PENDING);

                issue.setUpdatedAt(new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(new Date()));
                notifyChange();
            }
        }
    }

    public void escalateIssue(String issueId, String reason) {
        MaintenanceIssue issue = getMaintenanceIssueById(issueId);
        if (issue != null) {
            issue.setEscalated(true);
            issue.setEscalationReason(reason);
            issue.setPriority(MaintenancePriority.EMERGENCY);
            notifications.add(0, new NotificationItem(
                    "notif_esc_" + System.currentTimeMillis(),
                    "⚠️ Urgent: Issue Escalated!",
                    "Ticket " + issue.getId() + " has been escalated to Priority Resolution.",
                    "MAINTENANCE",
                    "Just now",
                    false,
                    UserRole.LANDLORD
            ));
            notifyChange();
        }
    }

    public void rateMaintenanceService(String issueId, int rating, String feedback) {
        MaintenanceIssue issue = getMaintenanceIssueById(issueId);
        if (issue != null) {
            issue.setTenantRating(rating);
            issue.setTenantRatingFeedback(feedback);
            notifyChange();
        }
    }

    // Chat API
    public List<ChatMessage> getChatMessages(String issueId) {
        List<ChatMessage> list = issueChatMap.get(issueId);
        if (list == null) {
            list = new ArrayList<>();
            issueChatMap.put(issueId, list);
        }
        return new ArrayList<>(list);
    }

    public void sendChatMessage(String issueId, String message) {
        List<ChatMessage> list = issueChatMap.get(issueId);
        if (list == null) {
            list = new ArrayList<>();
            issueChatMap.put(issueId, list);
        }
        String timeStr = new SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(new Date());
        ChatMessage msg = new ChatMessage(
                "msg_" + System.currentTimeMillis(),
                issueId,
                activeRole,
                currentUser.getName(),
                message,
                timeStr,
                true
        );
        list.add(msg);
        notifyChange();
    }

    // Payments API
    public List<PaymentRecord> getPaymentRecords() { return new ArrayList<>(paymentRecords); }

    public void addPayment(PaymentRecord record) {
        paymentRecords.add(0, record);
        notifications.add(0, new NotificationItem(
                "notif_pay_" + System.currentTimeMillis(),
                "Payment Recorded",
                "Payment of ₹" + record.getAmount() + " processed via " + record.getPaymentMethod().name() + ".",
                "PAYMENT",
                "Just now",
                false,
                activeRole == UserRole.TENANT ? UserRole.LANDLORD : UserRole.TENANT
        ));
        notifyChange();
    }

    public void toggleDigitalAck(String paymentId, boolean isLandlord) {
        for (PaymentRecord p : paymentRecords) {
            if (p.getId().equals(paymentId)) {
                String timeStr = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(new Date());
                if (isLandlord) {
                    p.setDigitalLandlordAck(true);
                    p.setLandlordAckTime(timeStr);
                } else {
                    p.setDigitalTenantAck(true);
                    p.setTenantAckTime(timeStr);
                }
                notifyChange();
                break;
            }
        }
    }

    public void raisePaymentDispute(String paymentId, String reason) {
        for (PaymentRecord p : paymentRecords) {
            if (p.getId().equals(paymentId)) {
                p.setHasDispute(true);
                p.setDisputeReason(reason);
                notifications.add(0, new NotificationItem(
                        "notif_disp_" + System.currentTimeMillis(),
                        "Payment Dispute Raised",
                        "Dispute filed for payment ID: " + paymentId,
                        "PAYMENT",
                        "Just now",
                        false,
                        UserRole.LANDLORD
                ));
                notifyChange();
                break;
            }
        }
    }

    public void toggleAutoPay(String paymentId, boolean enabled) {
        for (PaymentRecord p : paymentRecords) {
            if (p.getId().equals(paymentId)) {
                p.setAutoPay(enabled);
                notifyChange();
                break;
            }
        }
    }

    // Notices API
    public List<NoticeItem> getNotices() { return new ArrayList<>(notices); }

    public void addNotice(NoticeItem notice) {
        notices.add(0, notice);
        notifications.add(0, new NotificationItem(
                "notif_not_" + System.currentTimeMillis(),
                "New Notice: " + notice.getTitle(),
                notice.getContent(),
                "NOTICE",
                "Just now",
                false,
                UserRole.TENANT
        ));
        notifyChange();
    }

    public void acknowledgeNotice(String noticeId, boolean isTenant) {
        for (NoticeItem n : notices) {
            if (n.getId().equals(noticeId)) {
                String timeStr = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(new Date());
                if (isTenant) {
                    n.setAcknowledgedByTenant(true);
                    n.setTenantAckTimestamp(timeStr);
                } else {
                    n.setAcknowledgedByLandlord(true);
                    n.setLandlordAckTimestamp(timeStr);
                }
                notifyChange();
                break;
            }
        }
    }

    // Notifications API
    public List<NotificationItem> getNotifications() { return new ArrayList<>(notifications); }

    public int getUnreadNotificationCount() {
        int count = 0;
        for (NotificationItem n : notifications) {
            if (!n.isRead()) count++;
        }
        return count;
    }

    public void markAllNotificationsRead() {
        for (NotificationItem n : notifications) {
            n.setRead(true);
        }
        notifyChange();
    }

    // Profile & User updates
    public void updateUserProfile(String name, String email, String phone, String address) {
        if (currentUser != null) {
            currentUser.setName(name);
            currentUser.setEmail(email);
            currentUser.setPhone(phone);
            currentUser.setAddress(address);
            notifyChange();
        }
    }

    public void verifyGovernmentId(String docType) {
        if (currentUser != null) {
            currentUser.setGovtIdVerified(true);
            currentUser.setGovtIdType(docType + " Verified");
            notifyChange();
        }
    }
}
