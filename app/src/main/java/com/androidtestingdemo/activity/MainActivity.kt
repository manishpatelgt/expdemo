package com.androidtestingdemo.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.androidtestingdemo.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}