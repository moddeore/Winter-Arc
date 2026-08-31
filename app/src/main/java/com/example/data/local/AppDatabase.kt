package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.local.dao.*
import com.example.data.local.entity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        WinterArcEntity::class,
        CategoryEntity::class,
        GoalEntity::class,
        GoalMilestoneEntity::class,
        DailyProgressEntity::class,
        CheckInEntity::class,
        JournalEntryEntity::class,
        AchievementEntity::class,
        UserProfileEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun winterArcDao(): WinterArcDao
    abstract fun categoryDao(): CategoryDao
    abstract fun goalDao(): GoalDao
    abstract fun goalMilestoneDao(): GoalMilestoneDao
    abstract fun dailyProgressDao(): DailyProgressDao
    abstract fun checkInDao(): CheckInDao
    abstract fun journalDao(): JournalDao
    abstract fun achievementDao(): AchievementDao
    abstract fun userProfileDao(): UserProfileDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "winter_arc_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateInitialData(database)
                    }
                }
            }
        }

        suspend fun populateInitialData(db: AppDatabase) {
            // Default Categories per user requirements
            val defaultCategories = listOf(
                CategoryEntity(name = "Fitness", icon = "💪", colorHex = "#EF4444", isCustom = false),
                CategoryEntity(name = "Study", icon = "📚", colorHex = "#3B82F6", isCustom = false),
                CategoryEntity(name = "Coding", icon = "💻", colorHex = "#10B981", isCustom = false),
                CategoryEntity(name = "Mind", icon = "🧠", colorHex = "#8B5CF6", isCustom = false),
                CategoryEntity(name = "Finance", icon = "💰", colorHex = "#F59E0B", isCustom = false),
                CategoryEntity(name = "Reading", icon = "📖", colorHex = "#EC4899", isCustom = false),
                CategoryEntity(name = "Sleep", icon = "😴", colorHex = "#6366F1", isCustom = false),
                CategoryEntity(name = "Digital Detox", icon = "📵", colorHex = "#14B8A6", isCustom = false),
                CategoryEntity(name = "Personal", icon = "✨", colorHex = "#F43F5E", isCustom = false)
            )
            db.categoryDao().insertCategories(defaultCategories)

            // Gamified Achievements per requirements
            val defaultAchievements = listOf(
                AchievementEntity(
                    id = "first_step",
                    title = "First Step ❄️",
                    description = "Complete your first goal during the Winter Arc.",
                    icon = "❄️",
                    xpReward = 50,
                    progressTarget = 1
                ),
                AchievementEntity(
                    id = "7_day_warrior",
                    title = "7 Day Warrior 🔥",
                    description = "Complete goals for 7 consecutive days.",
                    icon = "🔥",
                    xpReward = 150,
                    progressTarget = 7
                ),
                AchievementEntity(
                    id = "locked_in",
                    title = "Locked In ⚔️",
                    description = "Complete 100% of your daily goals for 7 days.",
                    icon = "⚔️",
                    xpReward = 250,
                    progressTarget = 7
                ),
                AchievementEntity(
                    id = "frozen_mind",
                    title = "Frozen Mind 🧠",
                    description = "Maintain a 30-day streak of relentless discipline.",
                    icon = "🧊",
                    xpReward = 500,
                    progressTarget = 30
                ),
                AchievementEntity(
                    id = "perfect_week",
                    title = "Perfect Week 💯",
                    description = "Complete 100% of all scheduled goals for one whole week.",
                    icon = "💯",
                    xpReward = 300,
                    progressTarget = 7
                ),
                AchievementEntity(
                    id = "the_summit",
                    title = "The Summit 🏔️",
                    description = "Conquer the entire 90-day Winter Arc challenge.",
                    icon = "🏔️",
                    xpReward = 1000,
                    progressTarget = 90
                ),
                AchievementEntity(
                    id = "night_owl",
                    title = "Cold Discipline 🌙",
                    description = "Complete 50 total custom goal actions.",
                    icon = "🌙",
                    xpReward = 200,
                    progressTarget = 50
                )
            )
            db.achievementDao().insertAchievements(defaultAchievements)

            // User Profile
            val defaultProfile = UserProfileEntity(
                id = 1,
                username = "Arc Warrior",
                avatarEmoji = "❄️",
                level = 1,
                totalXp = 0,
                currentStreak = 0,
                bestStreak = 0,
                isOnboardingCompleted = false
            )
            db.userProfileDao().insertOrUpdateProfile(defaultProfile)
        }
    }
}
