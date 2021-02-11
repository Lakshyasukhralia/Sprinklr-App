package com.sukhralia.sprinklrtest.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sukhralia.sprinklrtest.constants.ALL
import com.sukhralia.sprinklrtest.constants.BOOKMARKED
import com.sukhralia.sprinklrtest.database.ProductDatabaseDao
import com.sukhralia.sprinklrtest.model.ProductModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class ProductViewModel(val database: ProductDatabaseDao, application: Application) :
    AndroidViewModel(application) {

    val databaseResponse = MutableStateFlow<ResponseState>(ResponseState.Empty)

    init {
//        fetchAllProducts(ALL)
    }

    fun clearDb(){
        viewModelScope.launch(Dispatchers.IO) {
            databaseResponse.value = ResponseState.Loading
            database.clear()
        }
    }

    fun insertAllProducts(productModelList: List<ProductModel>) {
        viewModelScope.launch(Dispatchers.IO) {
            databaseResponse.value = ResponseState.Loading
            database.clear()
            try {
                val result = database.insertAll(productModelList)
                fetchAllProducts(ALL)
            } catch (t: Throwable) {
                databaseResponse.value = ResponseState.Error(t.toString())
            }
        }

    }

    fun updateProduct(productModel: ProductModel) {
        viewModelScope.launch(Dispatchers.IO) {
            databaseResponse.value = ResponseState.Loading
            try {
                val result = database.update(productModel)
                databaseResponse.value = ResponseState.SuccessUpdate(productModel)
            } catch (t: Throwable) {
                databaseResponse.value = ResponseState.Error(t.toString())
            }
        }

    }

    fun fetchAllProducts(category : String) {
        viewModelScope.launch(Dispatchers.IO) {
            databaseResponse.value = ResponseState.Loading
            delay(1000)
            var result : List<ProductModel>? = null
            try {

                when(category){
                    ALL -> { result = database.getAllProducts()}
                    BOOKMARKED -> { result = database.getAllProductsByBookmark(true)}
                    else -> {result =  database.getAllProductsByCategory(category) }
                }

                databaseResponse.value = ResponseState.Success(result)
            } catch (t: Throwable) {
                databaseResponse.value = ResponseState.Error(t.toString())
            }
        }
    }

    sealed class ResponseState {
        data class Success(val data: List<ProductModel>) : ResponseState()
        data class SuccessUpdate(val data: ProductModel) : ResponseState()
        data class Error(val error: String) : ResponseState()
        object Loading : ResponseState()
        object Empty : ResponseState()
    }
}