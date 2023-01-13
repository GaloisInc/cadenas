package com.hashapps.butkusapp

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.hashapps.butkusapp.ui.components.LockScreenOrientation
import com.hashapps.butkusapp.ui.models.DecodeViewModel
import com.hashapps.butkusapp.ui.models.EncodeViewModel
import com.hashapps.butkusapp.ui.theme.ButkusAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val encodeViewModel: EncodeViewModel by viewModels()
        val decodeViewModel: DecodeViewModel by viewModels()

        setContent {
            LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            ButkusAppTheme {
                ButkusApp(
                    encodeViewModel = encodeViewModel,
                    decodeViewModel = decodeViewModel,
                )
            }
        }
    }
}
