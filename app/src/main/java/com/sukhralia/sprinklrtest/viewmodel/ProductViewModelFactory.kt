package com.sukhralia.sprinklrtest.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sukhralia.sprinklrtest.database.ProductDatabaseDao
import kotlinx.coroutines.ExperimentalCoroutinesApi

class ProductViewModelFactory(private val database : ProductDatabaseDao,private val application: Application) : ViewModelProvider.Factory  {

    @ExperimentalCoroutinesApi
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
            return ProductViewModel(
                database, application
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}