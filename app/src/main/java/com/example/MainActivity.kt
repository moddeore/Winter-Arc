package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.dialogs.*
import com.example.ui.screens.achievements.AchievementsScreen
import com.example.ui.screens.goals.GoalsScreen
import com.example.ui.screens.home.HomeScreen
import com.example.ui.screens.onboarding.OnboardingScreen
import com.example.ui.screens.profile.ProfileScreen
import com.example.ui.screens.progress.ProgressScreen
import com.example.ui.screens.splash.SplashScreen
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppNavigationTab
import com.example.ui.viewmodel.WinterArcViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: WinterArcViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WinterArcTheme {
                WinterArcApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun WinterArcApp(viewModel: WinterArcViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    // 1. Splash Screen
    if (uiState.showSplash) {
        SplashScreen(onDismiss = { viewModel.dismissSplash() })
        return
    }

    // 2. Onboarding Flow (if first time)
    if (uiState.showOnboarding) {
        OnboardingScreen(
            onStartArc = { name, motivation, mainObjective, durationDays, baseFit, baseStudy, baseCode, baseScreen, baseRefl ->
                viewModel.startWinterArc(
                    name = name,
                    motivation = motivation,
                    mainObjective = mainObjective,
                    durationDays = durationDays,
                    baselineFitness = baseFit,
                    baselineStudy = baseStudy,
                    baselineCoding = baseCode,
                    baselineScreenTime = baseScreen,
                    baselineReflection = baseRefl
                )
            }
        )
        return
    }

    // 3. Main Screen with Bottom Nav
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg),
        containerColor = DarkBg,
        bottomBar = {
            WinterArcBottomNavigation(
                currentTab = uiState.currentTab,
                onTabSelected = { viewModel.setNavigationTab(it) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState.currentTab) {
                AppNavigationTab.HOME -> HomeScreen(
                    uiState = uiState,
                    onGoalClick = { viewModel.setSelectedGoalForDetails(it) },
                    onToggleGoalComplete = { viewModel.toggleGoalCompleted(it) },
                    onIncrementGoalProgress = { goalId, inc -> viewModel.logGoalProgress(goalId, inc) },
                    onAddGoalClick = { viewModel.showCreateGoalDialog(true) },
                    onOpenCheckIn = { viewModel.showCheckInDialog(true) }
                )
                AppNavigationTab.GOALS -> GoalsScreen(
                    uiState = uiState,
                    onGoalClick = { viewModel.setSelectedGoalForDetails(it) },
                    onCreateGoalClick = { viewModel.showCreateGoalDialog(true) },
                    onCreateCategoryClick = { viewModel.showCreateCategoryDialog(true) },
                    onTogglePause = { goalId, paused -> viewModel.togglePauseGoal(goalId, paused) }
                )
                AppNavigationTab.PROGRESS -> ProgressScreen(
                    uiState = uiState,
                    onOpenBeforeAfter = { viewModel.showBeforeAfterDialog(true) }
                )
                AppNavigationTab.ACHIEVEMENTS -> AchievementsScreen(
                    uiState = uiState
                )
                AppNavigationTab.PROFILE -> ProfileScreen(
                    uiState = uiState,
                    onOpenCheckIn = { viewModel.showCheckInDialog(true) },
                    onOpenNewJournal = { viewModel.showJournalDialog(true) },
                    onDeleteJournalEntry = { viewModel.deleteJournalEntry(it) },
                    onUpdatePreferences = { u, a, r, c, s, m, e ->
                        viewModel.updateProfileSettings(u, a, r, c, s, m, e)
                    },
                    onUpdateArcDuration = { durationDays ->
                        viewModel.updateArcDuration(durationDays)
                    },
                    onUpdateArcDetails = { name, motivation, objective, durationDays ->
                        viewModel.updateArcDetails(name, motivation, objective, durationDays)
                    }
                )
            }
        }
    }

    // DIALOGS & OVERLAYS

    // 1. Create Goal Dialog
    if (uiState.showCreateGoalDialog) {
        CreateGoalDialog(
            categories = uiState.categories,
            onDismiss = { viewModel.showCreateGoalDialog(false) },
            onCreateGoal = { name, desc, catId, catName, catIcon, type, target, unit, freq, diff, milestones ->
                viewModel.createCustomGoal(
                    name = name,
                    description = desc,
                    categoryId = catId,
                    categoryName = catName,
                    categoryIcon = catIcon,
                    goalType = type,
                    targetValue = target,
                    targetUnit = unit,
                    targetFrequency = freq,
                    difficulty = diff,
                    milestones = milestones
                )
            },
            onRequestCreateCategory = {
                viewModel.showCreateCategoryDialog(true)
            }
        )
    }

    // 2. Create Category Dialog
    if (uiState.showCreateCategoryDialog) {
        CreateCategoryDialog(
            onDismiss = { viewModel.showCreateCategoryDialog(false) },
            onCreateCategory = { name, icon, colorHex ->
                viewModel.createCustomCategory(name, icon, colorHex)
            }
        )
    }

    // 3. Goal Details Sheet Dialog
    uiState.selectedGoalForDetails?.let { selectedGoal ->
        val todayProg = uiState.todayProgressList.find { it.goalId == selectedGoal.id }
        GoalDetailsDialog(
            goal = selectedGoal,
            todayProgress = todayProg,
            onDismiss = { viewModel.setSelectedGoalForDetails(null) },
            onTogglePause = { isPaused -> viewModel.togglePauseGoal(selectedGoal.id, isPaused) },
            onDelete = { viewModel.deleteGoal(selectedGoal.id) },
            onToggleMilestone = { milestoneId, isComp ->
                // Handled in repository if needed
            }
        )
    }

    // 4. Daily Check-In Dialog
    if (uiState.showCheckInDialog) {
        val completedCount = uiState.goalsWithProgress.count { it.todayProgress?.isCompleted == true }
        DailyCheckInDialog(
            dayNumber = uiState.currentDay,
            completedGoalsCount = completedCount,
            totalGoalsCount = uiState.goals.size,
            onDismiss = { viewModel.showCheckInDialog(false) },
            onSubmit = { mood, reflection ->
                viewModel.submitDailyCheckIn(mood, reflection)
            }
        )
    }

    // 5. Journal Entry Dialog
    if (uiState.showJournalDialog) {
        JournalEntryDialog(
            dayNumber = uiState.currentDay,
            onDismiss = { viewModel.showJournalDialog(false) },
            onSubmit = { title, content, moodEmoji ->
                viewModel.addJournalEntry(title, content, moodEmoji)
            }
        )
    }

    // 6. Before vs After Dialog
    if (uiState.showBeforeAfterDialog) {
        val completedCount = uiState.goalsWithProgress.count { it.todayProgress?.isCompleted == true }
        BeforeAfterDialog(
            arc = uiState.activeArc,
            currentDay = uiState.currentDay,
            totalDays = uiState.totalDays,
            completedGoalsCount = completedCount,
            totalXp = uiState.userProfile?.totalXp ?: 0,
            onDismiss = { viewModel.showBeforeAfterDialog(false) }
        )
    }
}

@Composable
fun WinterArcBottomNavigation(
    currentTab: AppNavigationTab,
    onTabSelected: (AppNavigationTab) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(24.dp)),
        color = Color(0xF2091224),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x3338BDF8)),
        tonalElevation = 6.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp, horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppNavigationTab.entries.forEach { tab ->
                val isSelected = currentTab == tab

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isSelected) Color(0x3338BDF8) else Color.Transparent)
                        .then(
                            if (isSelected) Modifier.border(
                                1.dp,
                                IceCyanPrimary,
                                RoundedCornerShape(16.dp)
                            ) else Modifier
                        )
                        .clickable { onTabSelected(tab) }
                        .padding(vertical = 6.dp, horizontal = 2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = tab.iconEmoji,
                            fontSize = if (isSelected) 20.sp else 18.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = tab.label,
                            style = Typography.labelSmall,
                            color = if (isSelected) IceWhite else TextSecondary,
                            fontWeight = if (isSelected) FontWeight.Black else FontWeight.Medium,
                            fontSize = 11.sp,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

