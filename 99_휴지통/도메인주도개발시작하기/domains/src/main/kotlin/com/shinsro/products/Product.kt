package com.shinsro.products

import com.shinsro.categories.CategoryId

class Product(
    val name: String,
    val price: Int,
    val categoryId: CategoryId,
)
