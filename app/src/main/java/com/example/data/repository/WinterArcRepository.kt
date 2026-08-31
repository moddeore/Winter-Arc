package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.local.entity.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import java.util.concurrent.TimeUnit

class WinterArcRepository(private val database: AppDatabase) {
    private val arcDao = database.winterArcDao()
    private val categoryDao = database.categoryDao()
    private val goalDao = database.goalDao()
    private val milestoneDao = database.goalMilestoneDao()
    private val progressDao = database.dailyProgressDao()
    private val checkInDao = database.checkInDao()
    private val journalDao = database.journalDao()
    private val achievementDao = database.achievementDao()
    private val profileDao = database.userProfileDao()

    val activeArc: Flow<WinterArcEntity?> = arcDao.getActiveArc()
    val allCategories: Flow<List<CategoryEntity>> = categoryDao.getAllCategories()
    val allActiveGoals: Flow<List<GoalEntity>> = goalDao.getAllActiveGoals()
    val allAchievements: Flow<List<AchievementEntity>> = achievementDao.getAllAchievements()
    val userProfile: Flow<UserProfileEntity?> = profileDao.getUserProfile()
    val allJournalEntries: Flow<List<JournalEntryEntity>> = journalDao.getAllEntries()
    val allCheckIns: Flow<List<CheckInEntity>> = checkInDao.getAllCheckIns()

    fun getTodayEpochDay(): Long {
        return TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis())
    }

    fun getDayNumber(startDate: Long, totalDays: Int = 90): Int {
        val startDay = TimeUnit.MILLISECONDS.toDays(startDate)
        val today = getTodayEpochDay()
        val dayDiff = (today - startDay).toInt() + 1
        return dayDiff.coerceIn(1, maxOf(totalDays, 1))
    }

    fun getTodayProgress(): Flow<List<DailyProgressEntity>> {
        return progressDao.getProgressForDay(getTodayEpochDay())
    }

    fun getGoalHistory(goalId: Long): Flow<List<DailyProgressEntity>> {
        return progressDao.getHistoryForGoal(goalId)
    }

    fun getMilestonesForGoal(goalId: Long): Flow<List<GoalMilestoneEntity>> {
        return milestoneDao.getMilestonesForGoal(goalId)
    }

    fun getCheckInForDay(dateEpochDay: Long): Flow<CheckInEntity?> {
        return checkInDao.getCheckInForDay(dateEpochDay)
    }

    suspend fun createWinterArc(
        name: String,
        motivation: String,
        mainObjective: String,
        startDate: Long = System.currentTimeMillis(),
        durationDays: Int = 90,
        baselineFitness: String = "",
        baselineStudy: String = "",
        baselineCoding: String = "",
        baselineScreenTime: String = "",
        baselineReflection: String = ""
    ): Long {
        val endDate = startDate + TimeUnit.DAYS.toMillis(durationDays.toLong())
        val arc = WinterArcEntity(
            name = name.ifBlank { "My Winter Arc 2026" },
            motivation = motivation,
            mainObjective = mainObjective,
            startDate = startDate,
            endDate = endDate,
            durationDays = durationDays,
            baselineFitness = baselineFitness,
            baselineStudy = baselineStudy,
            baselineCoding = baselineCoding,
            baselineScreenTime = baselineScreenTime,
            baselineReflection = baselineReflection,
            isActive = true
        )
        val arcId = arcDao.insertArc(arc)

        // Mark onboarding complete in profile
        val profile = profileDao.getUserProfileSync() ?: UserProfileEntity(id = 1)
        profileDao.insertOrUpdateProfile(profile.copy(isOnboardingCompleted = true))

        return arcId
    }

    suspend fun updateArcDuration(durationDays: Int) {
        val active = arcDao.getActiveArc().firstOrNull() ?: return
        val validDays = durationDays.coerceIn(7, 365)
        val newEndDate = active.startDate + TimeUnit.DAYS.toMillis(validDays.toLong())
        arcDao.updateArc(active.copy(durationDays = validDays, endDate = newEndDate))
    }

    suspend fun updateArcDetails(name: String, motivation: String, mainObjective: String, durationDays: Int) {
        val active = arcDao.getActiveArc().firstOrNull() ?: return
        val validDays = durationDays.coerceIn(7, 365)
        val newEndDate = active.startDate + TimeUnit.DAYS.toMillis(validDays.toLong())
        arcDao.updateArc(
            active.copy(
                name = name.ifBlank { active.name },
                motivation = motivation.ifBlank { active.motivation },
                mainObjective = mainObjective.ifBlank { active.mainObjective },
                durationDays = validDays,
                endDate = newEndDate
            )
        )
    }

    suspend fun createCustomCategory(name: String, icon: String, colorHex: String): Long {
        val category = CategoryEntity(
            name = name.trim(),
            icon = icon.ifBlank { "✨" },
            colorHex = colorHex.ifBlank { "#38BDF8" },
            isCustom = true
        )
        return categoryDao.insertCategory(category)
    }

    suspend fun createCustomGoal(
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
        milestones: List<String> = emptyList()
    ): Long {
        val activeArc = arcDao.getActiveArc().firstOrNull()
        val arcId = activeArc?.id ?: 1L

        val goal = GoalEntity(
            arcId = arcId,
            name = name.trim(),
            description = description.trim(),
            categoryId = categoryId,
            categoryName = categoryName,
            categoryIcon = categoryIcon,
            goalType = goalType,
            targetValue = targetValue,
            targetUnit = targetUnit.ifBlank { "times" },
            targetFrequency = targetFrequency.ifBlank { "Daily" },
            difficulty = difficulty
        )
        val goalId = goalDao.insertGoal(goal)

        if (milestones.isNotEmpty()) {
            val milestoneEntities = milestones.mapIndexed { index, title ->
                GoalMilestoneEntity(
                    goalId = goalId,
                    title = title,
                    isCompleted = false,
                    orderIndex = index
                )
            }
            milestoneDao.insertMilestones(milestoneEntities)
        }

        return goalId
    }

    suspend fun updateGoal(goal: GoalEntity) {
        goalDao.updateGoal(goal)
    }

    suspend fun toggleGoalPause(goalId: Long, isPaused: Boolean) {
        goalDao.setGoalPaused(goalId, isPaused)
    }

    suspend fun deleteGoal(goalId: Long) {
        goalDao.deleteGoalById(goalId)
    }

    suspend fun logGoalProgress(
        goalId: Long,
        incrementOrNewProgress: Float,
        isAbsolute: Boolean = false,
        notes: String = ""
    ): Int {
        val goal = goalDao.getGoalById(goalId) ?: return 0
        val today = getTodayEpochDay()
        val existingProgress = progressDao.getProgressForGoalAndDay(goalId, today)

        val newProgressVal = if (isAbsolute) {
            incrementOrNewProgress
        } else {
            (existingProgress?.currentProgress ?: 0f) + incrementOrNewProgress
        }

        val isNowCompleted = newProgressVal >= goal.targetValue
        val wasCompleted = existingProgress?.isCompleted ?: false

        var xpEarned = existingProgress?.xpEarned ?: 0
        var xpDiff = 0

        if (isNowCompleted && !wasCompleted) {
            xpDiff = goal.difficulty.xpReward
            xpEarned += xpDiff
        }

        val updatedProgressEntity = DailyProgressEntity(
            id = existingProgress?.id ?: 0,
            goalId = goalId,
            dateEpochDay = today,
            currentProgress = newProgressVal,
            targetValue = goal.targetValue,
            isCompleted = isNowCompleted,
            xpEarned = xpEarned,
            notes = notes,
            updatedAt = System.currentTimeMillis()
        )
        progressDao.insertOrUpdateProgress(updatedProgressEntity)

        // Update goal stats if newly completed
        if (isNowCompleted && !wasCompleted) {
            val newStreak = goal.currentStreak + 1
            val bestStreak = maxOf(goal.bestStreak, newStreak)
            goalDao.updateGoal(
                goal.copy(
                    currentStreak = newStreak,
                    bestStreak = bestStreak,
                    totalXpEarned = goal.totalXpEarned + xpDiff,
                    currentProgressValue = newProgressVal
                )
            )
            addXpToProfile(xpDiff)
            checkAndUpdateAchievements()
        } else {
            goalDao.updateGoal(goal.copy(currentProgressValue = newProgressVal))
        }

        return xpDiff
    }

    suspend fun toggleMilestone(milestoneId: Long, goalId: Long, isCompleted: Boolean) {
        val completedAt = if (isCompleted) System.currentTimeMillis() else null
        milestoneDao.setMilestoneCompleted(milestoneId, isCompleted, completedAt)
        if (isCompleted) {
            addXpToProfile(15)
        }
    }

    suspend fun submitDailyCheckIn(
        mood: DayMood,
        reflection: String,
        completedCount: Int,
        totalCount: Int
    ) {
        val today = getTodayEpochDay()
        val existing = checkInDao.getCheckInForDaySync(today)
        val xpReward = if (existing == null) 100 else 0

        val checkIn = CheckInEntity(
            dateEpochDay = today,
            mood = mood,
            reflection = reflection,
            completedGoalsCount = completedCount,
            totalGoalsCount = totalCount,
            xpEarned = 100,
            timestamp = System.currentTimeMillis()
        )
        checkInDao.insertCheckIn(checkIn)

        if (xpReward > 0) {
            addXpToProfile(xpReward)
            // Add automatic journal entry if reflection is not empty
            if (reflection.isNotBlank()) {
                val activeArc = arcDao.getActiveArc().firstOrNull()
                val dayNum = activeArc?.let { getDayNumber(it.startDate) } ?: 1
                journalDao.insertEntry(
                    JournalEntryEntity(
                        dateEpochDay = today,
                        title = "Day $dayNum Check-In Reflection",
                        content = reflection,
                        moodEmoji = mood.emoji,
                        dayNumber = dayNum
                    )
                )
            }
            checkAndUpdateAchievements()
        }
    }

    suspend fun addJournalEntry(title: String, content: String, moodEmoji: String, dayNum: Int) {
        journalDao.insertEntry(
            JournalEntryEntity(
                dateEpochDay = getTodayEpochDay(),
                title = title.ifBlank { "Daily Arc Log" },
                content = content,
                moodEmoji = moodEmoji,
                dayNumber = dayNum
            )
        )
    }

    suspend fun deleteJournalEntry(entry: JournalEntryEntity) {
        journalDao.deleteEntry(entry)
    }

    suspend fun addXpToProfile(xp: Int) {
        val currentProfile = profileDao.getUserProfileSync() ?: UserProfileEntity(id = 1)
        val newTotalXp = currentProfile.totalXp + xp
        val newLevel = calculateLevel(newTotalXp)
        profileDao.addXpAndLevel(xp, newLevel)
    }

    fun calculateLevel(totalXp: Int): Int {
        // Level 1: 0 - 100 XP, Level 2: 100 - 250, etc. (smooth curve)
        return (1 + (totalXp / 125)).coerceIn(1, 99)
    }

    fun getXpForCurrentLevel(totalXp: Int): Pair<Int, Int> {
        val level = calculateLevel(totalXp)
        val currentLevelBase = (level - 1) * 125
        val xpInLevel = totalXp - currentLevelBase
        val xpNeededForNext = 125
        return Pair(xpInLevel, xpNeededForNext)
    }

    suspend fun updateProfileSettings(
        username: String,
        avatarEmoji: String,
        goalReminders: Boolean,
        dailyCheckIn: Boolean,
        streakProtection: Boolean,
        morningMotivation: Boolean,
        eveningReflection: Boolean
    ) {
        val profile = profileDao.getUserProfileSync() ?: UserProfileEntity(id = 1)
        profileDao.insertOrUpdateProfile(
            profile.copy(
                username = username,
                avatarEmoji = avatarEmoji,
                goalRemindersEnabled = goalReminders,
                dailyCheckInEnabled = dailyCheckIn,
                streakProtectionEnabled = streakProtection,
                morningMotivationEnabled = morningMotivation,
                eveningReflectionEnabled = eveningReflection
            )
        )
    }

    suspend fun checkAndUpdateAchievements() {
        val profile = profileDao.getUserProfileSync() ?: return
        val checkIns = checkInDao.getAllCheckIns().firstOrNull() ?: emptyList()
        val allGoals = goalDao.getAllActiveGoals().firstOrNull() ?: emptyList()
        val allProgress = progressDao.getAllProgressHistory().firstOrNull() ?: emptyList()

        val completedGoalsCount = allProgress.count { it.isCompleted }
        val checkInCount = checkIns.size

        // Check First Step
        if (completedGoalsCount >= 1) {
            unlockAchievement("first_step")
        }
        // Check 7 Day Warrior
        if (checkInCount >= 7 || profile.currentStreak >= 7) {
            unlockAchievement("7_day_warrior")
        }
        // Check Frozen Mind
        if (checkInCount >= 30 || profile.currentStreak >= 30) {
            unlockAchievement("frozen_mind")
        }
        // Check Locked In
        if (completedGoalsCount >= 15) {
            unlockAchievement("locked_in")
        }
        // Check Cold Discipline
        if (completedGoalsCount >= 50) {
            unlockAchievement("night_owl")
        }
    }

    private suspend fun unlockAchievement(id: String) {
        achievementDao.unlockAchievement(id, System.currentTimeMillis())
    }

    suspend fun seedStarterGoalsIfEmpty() {
        val currentGoals = goalDao.getAllActiveGoals().firstOrNull()
        if (currentGoals.isNullOrEmpty()) {
            val categories = categoryDao.getAllCategories().firstOrNull() ?: return
            val codingCat = categories.find { it.name == "Coding" }
            val studyCat = categories.find { it.name == "Study" }
            val fitnessCat = categories.find { it.name == "Fitness" }

            if (codingCat != null) {
                createCustomGoal(
                    name = "Learn Python Fundamentals",
                    description = "Complete core syntax and build 3 practical projects.",
                    categoryId = codingCat.id,
                    categoryName = codingCat.name,
                    categoryIcon = codingCat.icon,
                    goalType = GoalType.DURATION_GOAL,
                    targetValue = 60f,
                    targetUnit = "min",
                    targetFrequency = "Daily",
                    difficulty = GoalDifficulty.MEDIUM
                )
            }
            if (studyCat != null) {
                createCustomGoal(
                    name = "Study Operating Systems",
                    description = "Deep dive into concurrency, memory management & CPU scheduling.",
                    categoryId = studyCat.id,
                    categoryName = studyCat.name,
                    categoryIcon = studyCat.icon,
                    goalType = GoalType.DURATION_GOAL,
                    targetValue = 3f,
                    targetUnit = "hours",
                    targetFrequency = "Daily",
                    difficulty = GoalDifficulty.HARD
                )
            }
            if (fitnessCat != null) {
                createCustomGoal(
                    name = "Cold Morning Workout",
                    description = "High intensity strength & endurance conditioning.",
                    categoryId = fitnessCat.id,
                    categoryName = fitnessCat.name,
                    categoryIcon = fitnessCat.icon,
                    goalType = GoalType.DAILY_HABIT,
                    targetValue = 1f,
                    targetUnit = "session",
                    targetFrequency = "Daily",
                    difficulty = GoalDifficulty.MEDIUM
                )
            }
        }
    }
}
