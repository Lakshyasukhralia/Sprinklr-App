package com.sukhralia.sprinklrtest.listener

import com.sukhralia.sprinklrtest.model.ProductModel

interface ProductListener {
    fun updateProduct(product : ProductModel)

}