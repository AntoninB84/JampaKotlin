package com.example.jampa.ui.home

import android.app.Application
import androidx.lifecycle.*
import com.example.jampa.database.*
import com.example.jampa.database.entities.Category
import com.example.jampa.database.entities.Task
import com.example.jampa.database.entities.Type
import kotlinx.coroutines.launch

class HomeViewModel(val database: ItemDatabase, application: Application): AndroidViewModel(application) {

    val tasks = database.taskDao.getTasks()

    private val _navigateToTaskInfos = MutableLiveData<Long?>()
    val navigateToTaskInfos
        get() = _navigateToTaskInfos


   /* var state = State(
        currentState = 1,
        previousState = 0,
        fails = 0,
        successes = 1
    )
    var recurrency = Recurrency(
        recTyp = 0,
        days = "1,1,0,1,1,1,1",
        delayNb = 0,
        delayType = 1,
        hours = "0"
    )
    var taskLife = TaskLife(
        creationDate = System.currentTimeMillis(),
        startingDate = 0L,
        termDate = System.currentTimeMillis()
    )
    var item = Item(
        title = "Test",
        description = "Test description",
        priority = 2,
        categoryId = 1,
        typeId = 1,
        state = state,
        recurrency = recurrency,
        taskLife = taskLife
    )
    var category = Category(name = "Catégorie de test")
    var type = Type(name = "Test du type")*/

    init{
        viewModelScope.launch{
//            insertType(type)
//            insertCategory(category)
            //insert(item)
            //updateItem(item)
            //clearDB()
            //deleteItem(item)

//            var test = database.taskDao.getTasks()
//            Log.i("Jampatest", test.toString())
//            Log.i("Jampatest", convertLongToDateString(System.currentTimeMillis()))
        }

    }

    fun onTaskClicked(id: Long){
        _navigateToTaskInfos.value = id
    }
    fun onTaskInfosNavigated(){
        _navigateToTaskInfos.value = null
    }

    private suspend fun insertCategory(category: Category){
        database.categoryDao.insert(category)
    }
    private suspend fun insertType(type: Type){
        database.typeDao.insert(type)
    }
    private suspend fun insert(item: Item) {
        database.taskDao.insert(item)
    }
    private suspend fun clearDB(){
        database.taskDao.clearItemDB()
    }
    private suspend fun updateItem(item: Item){
        database.taskDao.update(item)
    }
    private suspend fun deleteItem(item: Item){
        database.taskDao.delete(item)
    }
}