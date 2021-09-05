package com.example.jampa.ui.home

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.jampa.database.ItemDatabase
import java.lang.IllegalArgumentException

class HomeViewModelFactory (
    private val dataSource: ItemDatabase,
    private val application: Application) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(HomeViewModel::class.java)){
            return HomeViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown VM class")
    }
    }