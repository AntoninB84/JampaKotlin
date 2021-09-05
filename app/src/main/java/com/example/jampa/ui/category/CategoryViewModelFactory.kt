package com.example.jampa.ui.category

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.jampa.database.ItemDatabase
import com.example.jampa.ui.home.HomeViewModel
import java.lang.IllegalArgumentException

class CategoryViewModelFactory(
    private val dataSource: ItemDatabase,
    private val application: Application): ViewModelProvider.Factory  {


    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(CategoryViewModel::class.java)){
            return CategoryViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown VM class")
    }
}