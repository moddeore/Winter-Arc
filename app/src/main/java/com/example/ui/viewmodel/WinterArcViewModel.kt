package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.entity.*
import com.example.data.repository.WinterArcRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class AppNavigationTab(val label: String, val iconEmoji: String) {
    HOME("Home", "🏠"),
    GOALS("Goals", "🎯"),
    PROGRESS("Progress", "📊"),
    ACHIEVEMENTS("Badges", "🏆"),
    PROFILE("Profile", "👤")
}

data class GoalWithProgress(
    val goal: GoalEntity,
    val todayProgress: DailyProgressEntity?,
    val milestones: List<GoalMilestoneEntity> = emptyList()
)

data class CategoryProgress(
    val category: CategoryEntity,
    val totalGoals: Int,
    val completedGoals: Int,
    val percentage: Int
)

data class WinterArcUiState(
    val activeArc: WinterArcEntity? = null,
    val userProfile: UserProfileEntity? = null,
    val categories: List<CategoryEntity> = emptyList(),
    val goals: List<GoalEntity> = emptyList(),
    val goalsWithProgress: List<GoalWithProgress> = emptyList(),
    val todayProgressList: List<DailyProgressEntity> = emptyList(),
    val achievements: List<AchievementEntity> = emptyList(),
    val journalEntries: List<JournalEntryEntity> = emptyList(),
    val checkIns: List<CheckInEntity> = emptyList(),
    val todayCheckIn: CheckInEntity? = null,
    val currentDay: Int = 1,
    val totalDays: Int = 90,
    val categoryProgressList: List<CategoryProgress> = emptyList(),
    val xpInLevel: Int = 0,
    val xpNeededForNext: Int = 125,
    val completionRate: Int = 0,
    val daysCompleted: Int = 0,
    val currentTab: AppNavigationTab = AppNavigationTab.HOME,
    val showSplash: Boolean = true,
    val showOnboarding: Boolean = false,
    val selectedGoalForDetails: GoalEntity? = null,
    val showCreateGoalDialog: Boolean = false,
    val showCreateCategoryDialog: Boolean = false,
    val showCheckInDialog: Boolean = false,
    val showJournalDialog: Boolean = false,
    val showBeforeAfterDialog: Boolean = false,
    val unlockedAchievementNotification: AchievementEntity? = null
)

class WinterArcViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    val repository = WinterArcRepository(database)

    private val _uiState = MutableStateFlow(WinterArcUiState())
    val uiState: StateFlow<WinterArcUiState> = _uiState.asStateFlow()

    init {
        // Preload default data if needed
        viewModelScope.launch {
            repository.seedStarterGoalsIfEmpty()
        }

        // Combine primary domain flows
        val arcAndProfileFlow = combine(
            repository.activeArc,
            repository.userProfile
        ) { arc, profile -> Pair(arc, profile) }

        val goalsAndProgressFlow = combine(
            repository.allCategories,
            repository.allActiveGoals,
            repository.getTodayProgress()
        ) { categories, goals, todayProgress ->
            Triple(categories, goals, todayProgress)
        }

        val activityFlow = combine(
            repository.allAchievements,
            repository.allJournalEntries,
            repository.allCheckIns
        ) { achievements, journals, checkIns ->
            Triple(achievements, journals, checkIns)
        }

        viewModelScope.launch {
            combine(
                arcAndProfileFlow,
                goalsAndProgressFlow,
                activityFlow
            ) { (arc, profile), (categories, goals, todayProgress), (achievements, journals, checkIns) ->
                val todayEpochDay = repository.getTodayEpochDay()
                val todayCheckIn = checkIns.find { it.dateEpochDay == todayEpochDay }

                val totalDays = arc?.durationDays ?: 90
                val currentDay = if (arc != null) repository.getDayNumber(arc.startDate, totalDays) else 1

                val goalsWithProg = goals.map { goal ->
                    val prog = todayProgress.find { it.goalId == goal.id }
                    GoalWithProgress(goal = goal, todayProgress = prog)
                }

                // Category progress calculation
                val catProgress = categories.map { cat ->
                    val catGoals = goals.filter { it.categoryId == cat.id }
                    val total = catGoals.size
                    val completed = catGoals.count { g ->
                        val p = todayProgress.find { it.goalId == g.id }
                        p?.isCompleted == true
                    }
                    val pct = if (total > 0) ((completed.toFloat() / total.toFloat()) * 100).toInt() else 0
                    CategoryProgress(category = cat, totalGoals = total, completedGoals = completed, percentage = pct)
                }.filter { it.totalGoals > 0 || it.category.isCustom }

                val totalXp = profile?.totalXp ?: 0
                val (xpInLevel, xpNeeded) = repository.getXpForCurrentLevel(totalXp)

                val completedTodayCount = goalsWithProg.count { it.todayProgress?.isCompleted == true }
                val totalActiveGoalsCount = goals.count { !it.isPaused }
                val compRate = if (totalActiveGoalsCount > 0) {
                    ((completedTodayCount.toFloat() / totalActiveGoalsCount.toFloat()) * 100).toInt()
                } else 0

                val daysWithCheckIns = checkIns.size
                val shouldShowOnboarding = profile?.isOnboardingCompleted == false

                _uiState.update { current ->
                    current.copy(
                        activeArc = arc,
                        userProfile = profile,
                        categories = categories,
                        goals = goals,
                        goalsWithProgress = goalsWithProg,
                        todayProgressList = todayProgress,
                        achievements = achievements,
                        journalEntries = journals,
                        checkIns = checkIns,
                        todayCheckIn = todayCheckIn,
                        currentDay = currentDay,
                        totalDays = totalDays,
                        categoryProgressList = catProgress,
                        xpInLevel = xpInLevel,
                        xpNeededForNext = xpNeeded,
                        completionRate = compRate,
                        daysCompleted = daysWithCheckIns,
                        showOnboarding = shouldShowOnboarding
                    )
                }
            }.collect()
        }
    }

    fun dismissSplash() {
        _uiState.update { it.copy(showSplash = false) }
    }

    fun setNavigationTab(tab: AppNavigationTab) {
        _uiState.update { it.copy(currentTab = tab) }
    }

    fun startWinterArc(
        name: String,
        motivation: String,
        mainObjective: String,
        durationDays: Int = 90,
        baselineFitness: String,
        baselineStudy: String,
        baselineCoding: String,
        baselineScreenTime: String,
        baselineReflection: String
    ) {
        viewModelScope.launch {
            repository.createWinterArc(
                name = name,
                motivation = motivation,
                mainObjective = mainObjective,
                durationDays = durationDays,
                baselineFitness = baselineFitness,
                baselineStudy = baselineStudy,
                baselineCoding = baselineCoding,
                baselineScreenTime = baselineScreenTime,
                baselineReflection = baselineReflection
            )
            repository.seedStarterGoalsIfEmpty()
            _uiState.update { it.copy(showOnboarding = false, currentTab = AppNavigationTab.HOME) }
        }
    }

    fun createCustomGoal(
        name: String,
        description: String,
        categoryId: Long,
        categoryName: String,
        categoryIcon: String,
        goalType: GoalType,
        targetValue: Float,
        targetUnit: String,
        targetFrequency: String,
        difficulty: GoalDifficulty,
        milestones: List<String>
    ) {
        viewModelScope.launch {
            repository.createCustomGoal(
                name = name,
                description = description,
                categoryId = categoryId,
                categoryName = categoryName,
                categoryIcon = categoryIcon,
                goalType = goalType,
                targetValue = targetValue,
                targetUnit = targetUnit,
                targetFrequency = targetFrequency,
                difficulty = difficulty,
                milestones = milestones
            )
            _uiState.update { it.copy(showCreateGoalDialog = false) }
        }
    }

    fun createCustomCategory(name: String, icon: String, colorHex: String) {
        viewModelScope.launch {
            repository.createCustomCategory(name, icon, colorHex)
            _uiState.update { it.copy(showCreateCategoryDialog = false) }
        }
    }

    fun logGoalProgress(goalId: Long, increment: Float, isAbsolute: Boolean = false, notes: String = "") {
        viewModelScope.launch {
            repository.logGoalProgress(goalId, increment, isAbsolute, notes)
        }
    }

    fun toggleGoalCompleted(goalId: Long) {
        viewModelScope.launch {
            val goal = _uiState.value.goals.find { it.id == goalId } ?: return@launch
            val todayProg = _uiState.value.todayProgressList.find { it.goalId == goalId }
            val isCurrentlyCompleted = todayProg?.isCompleted ?: false

            if (!isCurrentlyCompleted) {
                repository.logGoalProgress(goalId, goal.targetValue, isAbsolute = true)
            } else {
                repository.logGoalProgress(goalId, 0f, isAbsolute = true)
            }
        }
    }

    fun togglePauseGoal(goalId: Long, isPaused: Boolean) {
        viewModelScope.launch {
            repository.toggleGoalPause(goalId, isPaused)
            _uiState.update { it.copy(selectedGoalForDetails = null) }
        }
    }

    fun deleteGoal(goalId: Long) {
        viewModelScope.launch {
            repository.deleteGoal(goalId)
            _uiState.update { it.copy(selectedGoalForDetails = null) }
        }
    }

    fun setSelectedGoalForDetails(goal: GoalEntity?) {
        _uiState.update { it.copy(selectedGoalForDetails = goal) }
    }

    fun showCreateGoalDialog(show: Boolean) {
        _uiState.update { it.copy(showCreateGoalDialog = show) }
    }

    fun showCreateCategoryDialog(show: Boolean) {
        _uiState.update { it.copy(showCreateCategoryDialog = show) }
    }

    fun showCheckInDialog(show: Boolean) {
        _uiState.update { it.copy(showCheckInDialog = show) }
    }

    fun showJournalDialog(show: Boolean) {
        _uiState.update { it.copy(showJournalDialog = show) }
    }

    fun showBeforeAfterDialog(show: Boolean) {
        _uiState.update { it.copy(showBeforeAfterDialog = show) }
    }

    fun submitDailyCheckIn(mood: DayMood, reflection: String) {
        viewModelScope.launch {
            val completed = _uiState.value.goalsWithProgress.count { it.todayProgress?.isCompleted == true }
            val total = _uiState.value.goals.size
            repository.submitDailyCheckIn(mood, reflection, completed, total)
            _uiState.update { it.copy(showCheckInDialog = false) }
        }
    }

    fun addJournalEntry(title: String, content: String, moodEmoji: String) {
        viewModelScope.launch {
            repository.addJournalEntry(title, content, moodEmoji, _uiState.value.currentDay)
            _uiState.update { it.copy(showJournalDialog = false) }
        }
    }

    fun deleteJournalEntry(entry: JournalEntryEntity) {
        viewModelScope.launch {
            repository.deleteJournalEntry(entry)
        }
    }

    fun updateProfileSettings(
        username: String,
        avatarEmoji: String,
        goalReminders: Boolean,
        dailyCheckIn: Boolean,
        streakProtection: Boolean,
        morningMotivation: Boolean,
        eveningReflection: Boolean
    ) {
        viewModelScope.launch {
            repository.updateProfileSettings(
                username,
                avatarEmoji,
                goalReminders,
                dailyCheckIn,
                streakProtection,
                morningMotivation,
                eveningReflection
            )
        }
    }

    fun updateArcDuration(durationDays: Int) {
        viewModelScope.launch {
            repository.updateArcDuration(durationDays)
        }
    }

    fun updateArcDetails(name: String, motivation: String, mainObjective: String, durationDays: Int) {
        viewModelScope.launch {
            repository.updateArcDetails(name, motivation, mainObjective, durationDays)
        }
    }

    fun clearAchievementNotification() {
        _uiState.update { it.copy(unlockedAchievementNotification = null) }
    }
}
