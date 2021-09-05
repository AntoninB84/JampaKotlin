package com.example.jampa.ui.home

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jampa.R
import com.example.jampa.database.ItemDatabase
import com.example.jampa.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val application = requireNotNull(this.activity).application
        val dataSource = ItemDatabase.getInstance(application)
        val viewModelFactory = HomeViewModelFactory(dataSource, application)

        homeViewModel =
            ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)

        binding.homeViewModel = homeViewModel

        val adapter = TaskAdapter(TaskClickListener { taskId ->
            // Here, we listen to the first time an item is clicked on.
            // In the Observer, below, we observe every change in the boolean from VM
            homeViewModel.onTaskClicked(taskId)
        })
        binding.taskList.adapter = adapter

        homeViewModel.tasks.observe(viewLifecycleOwner, Observer {
            it?.let{
                adapter.submitList(it)
            }
        })
        binding.setLifecycleOwner(this)

        homeViewModel.navigateToTaskInfos.observe(viewLifecycleOwner, Observer { task ->
            task?.let{
                Toast.makeText(context, task.toString(), Toast.LENGTH_SHORT).show()
                homeViewModel.onTaskInfosNavigated()
            }
        })

        val manager = LinearLayoutManager(activity)
        binding.taskList.layoutManager = manager

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}