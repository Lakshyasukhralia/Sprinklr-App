package com.sukhralia.sprinklrtest.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_table")
data class ProductModel(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    @ColumnInfo(name = "name")
    var name: String = "",
    @ColumnInfo(name = "category")
    var category: String = "",
    @ColumnInfo(name = "description")
    var description: String = "",
    @Embedded(prefix = "home_")
    var founder: FounderModel = FounderModel("", ""),
    @ColumnInfo(name = "upvotes")
    var upvotes: Long = 0,
    @ColumnInfo(name = "isBookmarked")
    var isBookmarked: Boolean = false,
    @ColumnInfo(name = "url")
    var url: String = "https://en.wikipedia.org/wiki/Wikipedia",
    @ColumnInfo(name = "isUpvoted")
    var isUpvoted: Boolean = false
)

data class FounderModel(
    var name: String,
    var info: String
)
