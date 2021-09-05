package com.example.jampa.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.jampa.database.entities.Task
import com.example.jampa.databinding.TaskListBinding

class TaskAdapter(val clickListener: TaskClickListener):
    ListAdapter<Task, TaskAdapter.ViewHolder>(TaskDiffCallBack()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        holder.bind(getItem(position)!!, clickListener)
        holder.itemView.setOnLongClickListener {
            if(holder.binding.taskDeleteImg.visibility == View.GONE){
                holder.binding.taskDeleteImg.visibility = View.VISIBLE
            }else{
                holder.binding.taskDeleteImg.visibility = View.GONE
            }
            return@setOnLongClickListener true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: TaskListBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(task: Task,
                 clickListener: TaskClickListener){
            binding.task = task
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object{
            fun from(parent: ViewGroup): ViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = TaskListBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

}

class TaskDiffCallBack: DiffUtil.ItemCallback<Task>(){

    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem.item.itemId == newItem.item.itemId
    }

    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem == newItem
    }
}

class TaskClickListener(val clickListener: (taskId: Long) -> Unit){
    fun onClick(task: Task) = clickListener(task.item.itemId)
}