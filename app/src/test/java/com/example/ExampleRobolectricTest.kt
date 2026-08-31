package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.AppDatabase
import com.example.data.repository.WinterArcRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Winter Arc", appName)
    }

    @Test
    fun `test level calculation curve`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val db = AppDatabase.getDatabase(context)
        val repository = WinterArcRepository(db)

        assertEquals(1, repository.calculateLevel(0))
        assertEquals(1, repository.calculateLevel(100))
        assertEquals(2, repository.calculateLevel(125))
        assertEquals(5, repository.calculateLevel(500))
        assertEquals(9, repository.calculateLevel(1000))
    }
}
