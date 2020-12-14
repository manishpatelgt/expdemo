package com.daggerhiltdemo.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.daggerhiltdemo.databinding.ActivityMainBinding
import com.daggerhiltdemo.ui.adapters.MainAdapter
import com.daggerhiltdemo.utils.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var mAdapter: MainAdapter

    private lateinit var mViewBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = mViewBinding.root
        setContentView(view)

        setupUI()
        setupObserver()
    }

    fun setupUI() {
        mAdapter = MainAdapter()
        // Initialize RecyclerView
        mViewBinding.postsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
            adapter = mAdapter
        }
    }

    private fun setupObserver() {
        mainViewModel.posts.observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { mAdapter.setPosts(it) }
                    mViewBinding.postsRecyclerView.visibility = View.VISIBLE
                }
                Status.LOADING -> {
                    mViewBinding.postsRecyclerView.visibility = View.GONE
                }
                Status.ERROR -> {
                    println("error: ${it.message}")
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }
}