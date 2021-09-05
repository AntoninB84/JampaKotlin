package com.example.jampa

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.room.TypeConverter
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * Take the Long milliseconds returned by the system and stored in Room,
 * and convert it to a nicely formatted string for display.
 *
 * EEEE - Display the long letter version of the weekday
 * MMM - Display the letter abbreviation of the nmotny
 * dd-yyyy - day in month and full year numerically
 * HH:mm - Hours and minutes in 24hr format
 * EEEE MMM-dd-yyyy' Time: 'HH:mm
 */
@SuppressLint("SimpleDateFormat")
fun convertLongToCompleteDateString(systemTime: Long): String {
    return SimpleDateFormat("EEE dd-MMM-yyyy HH:mm", Locale.FRANCE)
        .format(systemTime).toString()
}

@SuppressLint("SimpleDateFormat")
fun convertLongToLongDateString(systemTime: Long): String{
    return SimpleDateFormat("dd/MM/yyyy HH:mm")
        .format(systemTime).toString()
}

@SuppressLint("SimpleDateFormat")
fun convertLongToShortDateString(systemTime: Long): String{
    return SimpleDateFormat("dd/MM/yyyy")
        .format(systemTime).toString()
}
fun convertStringDateToLong(date: String): Long{
    return SimpleDateFormat("dd/MM/yyyy HH:mm").parse(date).time
}
/*
fun convertStringToLong(string: String): Long{
    return LocalDateTime.parse(string, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
}*/

fun convertCalendarToLongDateString(calendar: Calendar): String? {
    return SimpleDateFormat("dd/MM/yyyy HH:mm").format(calendar.time)
}
fun convertCalendarToLong(calendar: Calendar): Long {
    return calendar.time.time
}

fun EditText.onTextChanged(onTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            onTextChanged.invoke(s.toString())
        }
        override fun afterTextChanged(editable: Editable?) {
        }
    })
}