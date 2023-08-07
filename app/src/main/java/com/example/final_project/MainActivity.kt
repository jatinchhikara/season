package com.example.final_project

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.final_project.SeasonsFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, SeasonsFragment())
                .commit()
        }
    }
}
