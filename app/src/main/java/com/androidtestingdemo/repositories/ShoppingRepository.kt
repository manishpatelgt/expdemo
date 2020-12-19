package com.androidtestingdemo.repositories

import androidx.lifecycle.LiveData
import com.androidtestingdemo.data.local.ShoppingItem
import com.androidtestingdemo.data.remote.responses.ImageResponse
import com.androidtestingdemo.other.Resource
import retrofit2.Response

interface ShoppingRepository {

    suspend fun insertShoppingItem(shoppingItem: ShoppingItem)

    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem)

    fun observeAllShoppingItems(): LiveData<List<ShoppingItem>>

    fun observeTotalPrice(): LiveData<Float>

    suspend fun searchForImage(imageQuery: String): Resource<ImageResponse>
}