package com.shinsro.categories

class Category(val id: CategoryId)
class CategoryId(private val value: String) : CharSequence by value
