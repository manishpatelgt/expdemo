package com.blepoc.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.blepoc.App.Companion.context
import com.blepoc.R
import com.blepoc.activities.poc1.MainActivity
import com.blepoc.databinding.ActivityHomeBinding

/**
 * Created by Manish Patel on 9/3/2020.
 */
class HomeActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUI()
    }

    fun setupUI() {
        binding.poc1Btn.setOnClickListener(this)
        binding.poc2Btn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.poc1_btn -> {
                startActivity(MainActivity.getIntent())
            }
            R.id.poc2_btn -> {
                startActivity(com.blepoc.activities.poc2.MainActivity.getIntent())
            }
        }
    }

    companion object {
        fun getIntent() = Intent(context, HomeActivity::class.java)
    }
}