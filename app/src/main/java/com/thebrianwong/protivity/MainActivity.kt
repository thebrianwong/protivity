package com.thebrianwong.protivity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.thebrianwong.protivity.ui.theme.ProtivityTheme

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "timer")

class MainActivity : ComponentActivity() {
    var shouldSwitchBack = true
    var showToast = false
    var outOfLoop = true

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProtivityTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ProtivityApp(dataStore, window) { changeSwitchBackSetting(it) }
                }
            }
        }
    }

    fun changeSwitchBackSetting(newSetting: Boolean) {
        shouldSwitchBack = newSetting
    }

    override fun onPause() {
        super.onPause()
        if (shouldSwitchBack && outOfLoop) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            showToast = true
        }
    }
    override fun onResume() {
        super.onResume()
        if (showToast) {
            Toast.makeText(
                this,
                "Focus! No Distractions!",
                Toast.LENGTH_SHORT
            ).show()
            showToast = false
            outOfLoop = false

            Handler(Looper.getMainLooper()).postDelayed({
                outOfLoop = true
            }, 0)
        }
    }
}
