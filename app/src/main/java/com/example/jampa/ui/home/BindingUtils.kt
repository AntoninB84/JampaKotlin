package com.example.jampa.ui.home

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.jampa.convertLongToCompleteDateString
import com.example.jampa.convertLongToShortDateString
import com.example.jampa.database.entities.Task

/*
    @BindingAdapter("android:onLongClick")
    fun setOnLongClickListener(
        view: View,
        function: () -> Unit){
        view.setOnLongClickListener{
            function()
            return@setOnLongClickListener true
        }
    }
*/
    @BindingAdapter("taskReccurencyType")
    fun TextView.setTaskReccurencyType(task: Task?){
        task?.let{
            //Faire les transformations ici par rapport aux listes ??
            text = task.item.recurrency.recTyp.toString()
        }
    }

    @BindingAdapter("taskTermDate")
    fun TextView.setTaskTermDate(task: Task?){
        task?.let{
            //Faire les transformations ici par rapport aux listes ??
            text = convertLongToShortDateString(task.item.taskLife.termDate)
        }
    }

    @BindingAdapter("taskTitle")
    fun TextView.setTaskTitle(task: Task?){
        task?.let{
            //Faire les transformations ici par rapport aux listes ??
            text = task.item.title
        }
    }

    @BindingAdapter("taskDescription")
    fun TextView.setTaskDescription(task: Task?){
        task?.let{
            //Faire les transformations ici par rapport aux listes ??
            text = task.item.description
        }
    }