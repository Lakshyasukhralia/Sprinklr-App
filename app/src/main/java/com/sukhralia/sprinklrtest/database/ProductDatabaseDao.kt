package com.sukhralia.sprinklrtest.database

import androidx.room.*
import com.sukhralia.sprinklrtest.model.ProductModel
import kotlinx.coroutines.Deferred

@Dao
interface ProductDatabaseDao {

    @Insert
    fun insert(productModel: ProductModel)

    @Insert
    fun insertAll(dealModel : List<ProductModel>)

    @Update
    fun update(productModel: ProductModel)

    @Query("SELECT * FROM product_table WHERE id = :key")
    fun get(key : Long) : ProductModel

    @Delete
    fun delete(list : List<ProductModel>)

    @Query("DELETE FROM product_table")
    fun clear()

    @Query("SELECT * FROM product_table ORDER BY id ASC")
    fun getAllProducts() : List<ProductModel>

    @Query("SELECT * FROM product_table WHERE category = :category ORDER BY id ASC")
    fun getAllProductsByCategory(category: String) : List<ProductModel>

    @Query("SELECT * FROM product_table WHERE isBookmarked = :bookmark  ORDER BY id ASC")
    fun getAllProductsByBookmark(bookmark: Boolean) : List<ProductModel>

    @Query("SELECT * FROM product_table ORDER BY id DESC LIMIT 1")
    fun getRecentProduct() : ProductModel?

}