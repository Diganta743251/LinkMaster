package com.example.link

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme

class LinkDetailActivity : ComponentActivity() {
    companion object {
        const val EXTRA_LINK_ID = "extra_link_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val linkId = intent.getStringExtra(EXTRA_LINK_ID)
        
        setContent {
            MaterialTheme {
                // This activity is now handled by the navigation in MainActivity
                // We can keep this for backward compatibility or remove it entirely
                finish()
            }
        }
    }
}