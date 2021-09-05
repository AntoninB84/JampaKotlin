package com.example.jampa.ui.createtask

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.jampa.*
import com.example.jampa.database.*
import com.example.jampa.database.entities.Reminder
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.temporal.TemporalAdjusters
import java.util.*

class CreateTaskViewModel(val database: ItemDatabase, application: Application): AndroidViewModel(application) {

    private val resources = application.resources

    // Room data
    val categories = database.categoryDao.getAllCategories()
    val types = database.typeDao.getAllTypes()

    lateinit var title: String
    lateinit var description: String
    var category: Long = 0
    var type: Long = 0
    var priority: Int = 0

    var delayType = MutableLiveData<Int>(0)
    var reccurencyHour = MutableLiveData<String>()
    var reccurencyDays = MutableLiveData<Long>(0L)
    var reccurencyNumber = MutableLiveData<Long>(1)
    var reccurencyCustom = MutableLiveData<Int>(0)

    private var annualDate : Long? = 0L
    private var selectedDate = MutableLiveData<Long?>(0L)
    private var reminderDate: Long? = 0L
    var reminderCustomNumber = MutableLiveData<Int>(0)
    var reminderCustomType = MutableLiveData<Int>(0)

    // Only to display complete information about the date
    val completeEndDate = MutableLiveData<String>()
    val completeStartDate = MutableLiveData<String>()
    val completeReminderDate = MutableLiveData<String>()

    private val _recurrencyType = MutableLiveData<Int>(2)
    val recurrencyType
        get() = _recurrencyType

    private val _reminderType = MutableLiveData<Int>(0)
    val reminderType
        get() = _reminderType

    private val _endDate = MutableLiveData<Long>(0L)
    val endDate
        get() = _endDate

    private val _startDate = MutableLiveData<Long>(0L)
    val startDate
        get() = _startDate

    //The hour button is pressed
    private val _hourBtnPressed = MutableLiveData<Boolean>()
    val hourBtnPressed
        get() = _hourBtnPressed

    // One of the date button is pressed
    private val _DateBtnPressed = MutableLiveData<Boolean>()
    val DateBtnPressed
        get() = _DateBtnPressed

    // If endDate is to be set, min date is today
    private val _endDateBtnPressed = MutableLiveData<Boolean>()
    val endDateBtnPressed
        get() = _endDateBtnPressed

    // If we want to remove the selected date, as end/start date is optional
    private val _displayResetEndDate = MutableLiveData<Boolean>()
    val displayResetEndDate
        get() = _displayResetEndDate

    private val _displayResetStartDate = MutableLiveData<Boolean>()
    val displayResetStartDate
        get() = _displayResetStartDate

    private val _displayResetReminderDate = MutableLiveData<Boolean>()
    val displayResetReminderDate
        get() = _displayResetReminderDate

    private val _displayReminderButton = MutableLiveData<Boolean>()
    val displayReminderButton
        get() = _displayReminderButton

    private val _displayReminderDelay = MutableLiveData<Boolean>()
    val displayReminderDelay
        get() = _displayReminderDelay

    private lateinit var lastSelectedDateBtn: String

    private var _errorMessage = MutableLiveData<String>()
    val errorMessage
        get() = _errorMessage

    private lateinit var entries: List<String>

    /** Called from bound RB onClick**/
    fun setRecurrencyType(type: Int){
        _recurrencyType.value = type
        when(type){
            0-> {
                _displayResetEndDate.value = false
                _endDate.value = 0L
                completeEndDate.value = ""
                _displayResetReminderDate.value = false
                reminderDate = 0L
                completeReminderDate.value = ""
            }
            in 1..3 -> {
                _displayReminderDelay.value = false
                _displayReminderButton.value = false
            }
        }
    }
    fun setReminderType(type: Int){
        _reminderType.value = type
    }

    fun selectStartDate(){
        _DateBtnPressed.value = true
        lastSelectedDateBtn = "start"
    }
    fun selectEndDate(){
        _DateBtnPressed.value = true
        _endDateBtnPressed.value = true // The end date cannot be prior to today
        lastSelectedDateBtn = "end"
    }
    fun selectReminderDate(){
        _DateBtnPressed.value = true
        lastSelectedDateBtn = "reminder"
    }
    fun showReminderDelay(){
        _displayReminderDelay.value = true
    }
    fun recHourDateSelected(){
        when(delayType.value){
            in 0..2 -> {
                _hourBtnPressed.value = true
            }
            3 -> {
                _DateBtnPressed.value = true
                lastSelectedDateBtn = "annual"
            }
        }
    }

    /**
     * Date was picked
     * remove pickerDialog
     * affecting values
     * **/
    fun dateSelected(date: String){
        _DateBtnPressed.value = false
        _endDateBtnPressed.value = false
        selectedDate.value = convertStringDateToLong(date)
        when(lastSelectedDateBtn){
            "start" -> {
                _startDate.value = selectedDate.value
                _displayResetStartDate.value = true
                completeStartDate.value = convertLongToCompleteDateString(_startDate.value!!)
            }
            "end" -> {
                _endDate.value = selectedDate.value
                _displayResetEndDate.value = true
                completeEndDate.value = convertLongToCompleteDateString(_endDate.value!!)
            }
            "annual" -> {
                annualDate = selectedDate.value
                _displayReminderButton.value = true
                reccurencyHour.value = convertLongToCompleteDateString(annualDate!!)
            }
            "reminder" -> {
                reminderDate = selectedDate.value
                _displayResetReminderDate.value = true
                completeReminderDate.value = convertLongToCompleteDateString(reminderDate!!)
            }
        }
    }
    fun hourSelected(hour: String){
        _hourBtnPressed.value = false
        reccurencyHour.value = hour
        _displayReminderButton.value = true
    }

    fun resetStartDate(){
        _startDate.value = 0L
        completeStartDate.value = ""
        _displayResetStartDate.value = false
    }
    fun resetEndDate(){
        _endDate.value = 0L
        completeEndDate.value = ""
        _displayResetEndDate.value = false
    }
    fun resetReminderDate(){
        reminderDate = 0L
        completeReminderDate.value = ""
        _displayResetReminderDate.value = false
    }
    fun resetReminderDelay(){
        _displayReminderDelay.value = false
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createTask(){

        if(checkInputs()){
            var now: Calendar = Calendar.getInstance()

            val taskLife = TaskLife(convertCalendarToLong(now))
            val reminder = Reminder()
            val reccurence = Recurrency(_recurrencyType.value!!)
            val item = Item(title=title,
                description=description,
                priority = priority,
                categoryId = category,
                typeId = type,
                state = State(),
                taskLife = taskLife,
                recurrency = reccurence)

            when(_recurrencyType.value){
                0 -> {
                    item.recurrency.delayType = delayType.value!!
                    reminder.reminderCustomType = reminderCustomType.value!!
                    reminder.reminderCustomNumber = reminderCustomNumber.value!!
                    reminder.reminderType = reminderType.value!!

                    when(delayType.value){
                        0->{

                            item.recurrency.hour = reccurencyHour.value!!
                            reminder.reminderCustomType = 0

                            if(checkHour()) {
                                val timeTest = Calendar.getInstance()
                                timeTest.set(Calendar.HOUR_OF_DAY, entries[0].toInt())
                                timeTest.set(Calendar.MINUTE, entries[1].toInt())

                                if(timeTest > now) item.taskLife.termDate = convertCalendarToLong(timeTest)
                                else timeTest.set(Calendar.DAY_OF_MONTH, timeTest.get(Calendar.DAY_OF_MONTH)+1)

                                item.taskLife.termDate = convertCalendarToLong(timeTest)

                                taskCreation(item, reminder)
                            }
                        }
                        1->{

                            item.recurrency.hour = reccurencyHour.value!!
                            item.recurrency.day = reccurencyDays.value!!

                            if(checkHour()) {
                                val timeTest = Calendar.getInstance()
                                var test = TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY)
                                Log.i("JampaTest", test.toString())
                                //timeTest.set(Calendar.DAY_OF_MONTH, TemporalAdjusters.next(DayOfWeek.of(reccurencyDays.value.toInt())))
                                timeTest.set(Calendar.HOUR_OF_DAY, entries[0].toInt())
                                timeTest.set(Calendar.MINUTE, entries[1].toInt())


                                //taskCreation(item, reminder)
                            }
                        }
                        2 -> {
                            item.recurrency.hour = reccurencyHour.value!!
                            item.recurrency.day = reccurencyNumber.value!!
                            if(checkHour() && checkDay()) taskCreation(item, reminder)
                        }
                        3->{
                            item.recurrency.day = annualDate!!
                            if(checkAnnualDate()) taskCreation(item, reminder)
                        }
                        4->{
                            item.taskLife.startingDate = startDate.value!!
                            item.recurrency.day = reccurencyNumber.value!!
                            item.recurrency.customRec = reccurencyCustom.value!!
                            if(checkDay()) taskCreation(item, reminder)
                        }
                    }
                }
                in 1..3 -> {
                    if (reminderDate != 0L) {
                        reminder.reminderDate = reminderDate!!
                        reminder.reminderType = reminderType.value!!
                    }
                    if(endDate.value != 0L ) item.taskLife.termDate = endDate.value!!
                    taskCreation(item, reminder)
                }
            }
        }
        else{
            _errorMessage.value = resources.getString(R.string.create_invalid_inputs_message)
        }
    }
    private fun taskCreation(item: Item, reminder: Reminder){
        viewModelScope.launch {
            val itemId: Long = database.taskDao.insert(item)
            reminder.itemId = itemId
            if(reminder.reminderDate > 0
                || reminder.reminderCustomNumber > 0) database.reminderDao.insert(reminder)
            //database.reminderDao.delete(reminderTest)
            //database.taskDao.delete(database.taskDao.getTask(1))
            //todo ne pas oublier de quitter le fragment
        }
    }

    private fun checkInputs(): Boolean {
        return ::title.isInitialized && title != "" && ::description.isInitialized && description != ""
    }
    private fun checkHour(): Boolean{
        return if (!reccurencyHour.value.isNullOrEmpty()) {
            entries = reccurencyHour.value!!.split(":")
            true
        }
        else {
            _errorMessage.value = resources.getString(R.string.create_invalid_hour_message)
            false
        }
    }
    private fun checkDay(): Boolean{
        return if(reccurencyDays.value != 0L || reccurencyDays.value!! > 0) true
        else{
            _errorMessage.value = resources.getString(R.string.create_invalid_days_message)
            false
        }
    }
    private fun checkAnnualDate(): Boolean{
        return if(annualDate != 0L ) true
        else{
            _errorMessage.value = resources.getString(R.string.create_invalid_date_message)
            false
        }
    }
}