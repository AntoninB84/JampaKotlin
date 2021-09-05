package com.example.jampa.ui.createtask

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.jampa.database.ItemDatabase
import com.example.jampa.ui.category.CategoryViewModel
import java.lang.IllegalArgumentException

class CreateTaskViewModelFactory(
    private val dataSource: ItemDatabase,
    private val application: Application): ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(CreateTaskViewModel::class.java)){
                return CreateTaskViewModel(dataSource, application) as T
            }
            throw IllegalArgumentException("Unknown VM class")
        }

}