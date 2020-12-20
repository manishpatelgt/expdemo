package com.androidtestingdemo.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.androidtestingdemo.adapters.ImageAdapter
import com.androidtestingdemo.adapters.ShoppingItemAdapter
import com.androidtestingdemo.repositories.ShoppingRepository
import com.bumptech.glide.RequestManager
import javax.inject.Inject

class ShoppingFragmentFactory @Inject constructor(
    private val imageAdapter: ImageAdapter,
    private val glide: RequestManager,
    private val shoppingItemAdapter: ShoppingItemAdapter
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {
            ImagePickFragment::class.java.name -> ImagePickFragment(imageAdapter)
            AddShoppingItemFragment::class.java.name -> AddShoppingItemFragment(glide)
            ShoppingFragment::class.java.name -> ShoppingFragment(shoppingItemAdapter)
            else -> super.instantiate(classLoader, className)
        }
    }

}