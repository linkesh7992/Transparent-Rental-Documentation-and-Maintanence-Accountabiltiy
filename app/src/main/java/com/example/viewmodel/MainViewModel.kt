package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.backend.models.*
import com.example.backend.repository.RentWiseRepository
import com.example.backend.services.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class MainUiState(
    val currentUser: User? = null,
    val activeRole: UserRole = UserRole.TENANT,
    val currentTheme: ThemePreset = ThemePreset.RENTWISE_CYAN,
    val isDarkMode: Boolean = false,
    val currentLanguage: AppLanguage = AppLanguage.ENGLISH,
    val properties: List<Property> = emptyList(),
    val maintenanceIssues: List<MaintenanceIssue> = emptyList(),
    val paymentRecords: List<PaymentRecord> = emptyList(),
    val notices: List<NoticeItem> = emptyList(),
    val notifications: List<NotificationItem> = emptyList(),
    val savedProperties: List<Property> = emptyList(),
    val unreadNotifCount: Int = 0,
    val activeMaintenanceCount: Int = 0,
    val outstandingBalance: Double = 0.0,
    val totalPaid: Double = 0.0
)

class MainViewModel : ViewModel(), RentWiseRepository.DataChangeListener {
    private final val repo = RentWiseRepository.getInstance()
    private final val propertyService = PropertyService()
    private final val maintenanceService = MaintenanceService()
    private final val paymentService = PaymentService()
    private final val localizationService = LocalizationService.getInstance()

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        repo.addListener(this)
        refreshState()
    }

    override fun onCleared() {
        super.onCleared()
        repo.removeListener(this)
    }

    override fun onDataChanged() {
        refreshState()
    }

    fun refreshState() {
        viewModelScope.launch {
            _uiState.value = MainUiState(
                currentUser = repo.currentUser,
                activeRole = repo.activeRole,
                currentTheme = repo.currentTheme,
                isDarkMode = repo.isDarkMode,
                currentLanguage = localizationService.currentLanguage,
                properties = repo.allProperties,
                maintenanceIssues = repo.maintenanceIssues,
                paymentRecords = repo.paymentRecords,
                notices = repo.notices,
                notifications = repo.notifications,
                savedProperties = repo.savedProperties,
                unreadNotifCount = repo.unreadNotificationCount,
                activeMaintenanceCount = maintenanceService.activeIssueCount,
                outstandingBalance = paymentService.calculateOutstandingBalance(),
                totalPaid = paymentService.calculateTotalPaid()
            )
        }
    }

    fun t(key: String): String {
        return localizationService.getString(key)
    }

    fun setLanguage(language: AppLanguage) {
        localizationService.currentLanguage = language
        refreshState()
    }

    fun switchRole(role: UserRole) {
        repo.switchRole(role)
    }

    fun setTheme(theme: ThemePreset) {
        repo.currentTheme = theme
    }

    fun toggleDarkMode() {
        repo.isDarkMode = !repo.isDarkMode
    }

    fun toggleSaveProperty(propertyId: String) {
        repo.toggleSaveProperty(propertyId)
    }

    fun addProperty(property: Property) {
        repo.addProperty(property)
    }

    fun addMaintenanceIssue(issue: MaintenanceIssue) {
        repo.addMaintenanceIssue(issue)
    }

    fun advanceMaintenanceStep(issueId: String) {
        repo.advanceMaintenanceStep(issueId)
    }

    fun escalateIssue(issueId: String, reason: String) {
        repo.escalateIssue(issueId, reason)
    }

    fun rateMaintenanceService(issueId: String, rating: Int, feedback: String) {
        repo.rateMaintenanceService(issueId, rating, feedback)
    }

    fun getChatMessages(issueId: String): List<ChatMessage> {
        return repo.getChatMessages(issueId)
    }

    fun sendChatMessage(issueId: String, message: String) {
        repo.sendChatMessage(issueId, message)
    }

    fun addPayment(record: PaymentRecord) {
        repo.addPayment(record)
    }

    fun toggleDigitalAck(paymentId: String, isLandlord: Boolean) {
        repo.toggleDigitalAck(paymentId, isLandlord)
    }

    fun raisePaymentDispute(paymentId: String, reason: String) {
        repo.raisePaymentDispute(paymentId, reason)
    }

    fun toggleAutoPay(paymentId: String, enabled: Boolean) {
        repo.toggleAutoPay(paymentId, enabled)
    }

    fun acknowledgeNotice(noticeId: String, isTenant: Boolean) {
        repo.acknowledgeNotice(noticeId, isTenant)
    }

    fun markNotificationsAsRead() {
        repo.markAllNotificationsRead()
    }

    fun updateUserProfile(name: String, email: String, phone: String, address: String) {
        repo.updateUserProfile(name, email, phone, address)
    }

    fun verifyGovernmentId(docType: String) {
        repo.verifyGovernmentId(docType)
    }
}
