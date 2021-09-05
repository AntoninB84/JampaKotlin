package com.example.jampa.ui.category

import android.app.Application
import androidx.lifecycle.*
import com.example.jampa.R
import com.example.jampa.database.ItemDatabase
import com.example.jampa.database.entities.Category
import com.example.jampa.database.entities.Type
import kotlinx.coroutines.launch

class CategoryViewModel(val database: ItemDatabase, application: Application): AndroidViewModel(application) {

    private val resources = application.resources

    // Is it type or category
    private val _currentElementType = MutableLiveData<Int>(0)
    val currentElementType
        get() = _currentElementType

    // Title Textview
    var elementTypeTitle = MutableLiveData<String>(resources.getString(R.string.cat_typ_title_cat))

    // EditText current text
    var elementName = MutableLiveData<String>()

    var addButtonTitle = MutableLiveData<String>(application.resources.getString(R.string.cat_typ_add_btn))
    var addButtonState = MutableLiveData<Boolean>(false)
    var delButtonState = MutableLiveData<Boolean>(false)

    // Room data
    val categories = database.categoryDao.getAllCategories()
    val types = database.typeDao.getAllTypes()

    var selectedElementId : Long = 0L

    // We created an element
    private val _elementCreated = MutableLiveData<Boolean>()
    val elementCreated
        get() = _elementCreated


    fun onTextChanged(it: String){
        elementName.value = it
        when(selectedElementId){
            0L ->{
                addButtonState.value = it.length > 2 // True if length > 2
                addButtonTitle.value = resources.getString(R.string.cat_typ_add_btn)
                delButtonState.value = false
            }
            else -> {
                delButtonState.value = true
                when(it.length){
                    0 -> {
                        addButtonState.value = true
                        addButtonTitle.value  = resources.getString(R.string.cat_typ_add_btn_cancel)
                    }
                    in 1..2 ->{
                        addButtonState.value = false
                        addButtonTitle.value = resources.getString(R.string.cat_typ_add_btn_mod)
                    }
                    else -> {
                        addButtonState.value = true
                        addButtonTitle.value = resources.getString(R.string.cat_typ_add_btn_mod)
                    }
                }
            }
        }
    }

    fun addButtonClick(){
        when(selectedElementId){
            0L -> createElement()
            else -> {
                /** Cancel modification**/
                if(elementName.value?.isEmpty() == true){
                    selectedElementId = 0L
                    addButtonTitle.value = resources.getString(R.string.cat_typ_add_btn)
                    addButtonState.value = false
                    delButtonState.value = false
                }
                else updateElement()
            }
        }
    }

    private fun createElement(){
        when(_currentElementType.value){
            0 -> {
                val element = Category(name = elementName.value.toString())
                viewModelScope.launch {
                    database.categoryDao.insert(element)
                }
            }
            1 -> {
                val element = Type(name = elementName.value.toString())
                viewModelScope.launch {
                    database.typeDao.insert(element)
                }
            }
        }

        _elementCreated.value = true
    }
    fun elementCreated(){
        _elementCreated.value = false
    }

    private fun updateElement(){
        when(_currentElementType.value){
            0 -> {
                val element = Category(selectedElementId, elementName.value.toString())
                viewModelScope.launch {
                    database.categoryDao.update(element)
                }
            }
            1 -> {
                val element = Type(selectedElementId, elementName.value.toString())
                viewModelScope.launch {
                    database.typeDao.update(element)
                }
            }
        }
        selectedElementId = 0L
        _elementCreated.value = true
    }

    fun deleteElement(){
        when(_currentElementType.value){
            0 -> {
                val element = Category(selectedElementId)
                viewModelScope.launch {
                    database.categoryDao.delete(element)
                }
            }
            1 -> {
                val element = Type(selectedElementId)
                viewModelScope.launch {
                    database.typeDao.delete(element)
                }
            }
        }
        selectedElementId = 0L
        _elementCreated.value = true
    }

    /** Bound to title TV to change within category/type **/
    fun changeElementType(){
        selectedElementId = 0L
        elementName.value = ""
        addButtonTitle.value = resources.getString(R.string.cat_typ_add_btn)
        addButtonState.value = false
        delButtonState.value = false
        when(_currentElementType.value){
            0 -> {
                elementTypeTitle.value = resources.getString(R.string.cat_typ_title_typ)
                _currentElementType.value = 1
            }
            1 -> {
                elementTypeTitle.value = resources.getString(R.string.cat_typ_title_cat)
                _currentElementType.value = 0
            }

        }
    }

}