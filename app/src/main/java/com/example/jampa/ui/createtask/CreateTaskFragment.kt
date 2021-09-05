package com.example.jampa.ui.createtask

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewTreeViewModelStoreOwner
import com.example.jampa.R
import com.example.jampa.database.ItemDatabase
import com.example.jampa.databinding.FragmentCreateTaskBinding
import com.example.jampa.onTextChanged
import java.util.*

class CreateTaskFragment : Fragment() {

    private lateinit var createTaskViewModel: CreateTaskViewModel
    private var _binding: FragmentCreateTaskBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)

        _binding = FragmentCreateTaskBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val application = requireNotNull(this.activity).application
        val dataSource = ItemDatabase.getInstance(application)
        val viewModelFactory = CreateTaskViewModelFactory(dataSource, application)

        createTaskViewModel =
            ViewModelProvider(this, viewModelFactory).get(CreateTaskViewModel::class.java)
        binding.createTaskViewModel = createTaskViewModel
        binding.lifecycleOwner = this

        observeInputs()
        populateSpinners()
        displayDelayOptions()

        return root
    }

    private fun observeInputs(){
        binding.apply {
            createEtTitle.onTextChanged { createTaskViewModel?.title = it }
            createEtDescription.onTextChanged { createTaskViewModel?.description = it }
            createSpPriority.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?,view: View?,position: Int,id: Long ) {
                        createTaskViewModel?.priority = position
                    }
                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }
            createSpDelayDays.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                        createTaskViewModel?.reccurencyDays?.value = position.toLong()
                    }
                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }
            createEtDateAndNumber.onTextChanged {
                if(it != "") createTaskViewModel?.reccurencyNumber?.value = it.toLong()
            }
            createSpDelayRec.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                        createTaskViewModel?.reccurencyCustom?.value = position
                    }
                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }
            createEtReminderCustomNumber.onTextChanged {
                if (it != "") createTaskViewModel?.reminderCustomNumber?.value = it.toInt()
            }
            createSpDelayReminderCustom.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected( parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                    createTaskViewModel?.reminderCustomType?.value = position
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }
        createTaskViewModel.DateBtnPressed.observe(viewLifecycleOwner, { if(it) dateTimePicking() })
        createTaskViewModel.recurrencyType.observe(viewLifecycleOwner, {displayReccurencyOptions(it)})
        createTaskViewModel.hourBtnPressed.observe(viewLifecycleOwner, { if(it) timePicking() })
        createTaskViewModel.errorMessage.observe(viewLifecycleOwner, {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        })
    }

    /**
     * Only with room data
     * **/
    private fun populateSpinners(){
        createTaskViewModel.categories.observe(viewLifecycleOwner, {
            val categoryAdapter = ArrayAdapter(requireContext(),R.layout.support_simple_spinner_dropdown_item, it.map{it.name} )
            binding.createSpCategory.adapter = categoryAdapter
            binding.createSpCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected( parent: AdapterView<*>?, view: View?, position: Int, id: Long ) {
                    createTaskViewModel.category = it[position].categoryId
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        })
        createTaskViewModel.types.observe(viewLifecycleOwner, {
            val typeAdapter = ArrayAdapter(requireContext(),R.layout.support_simple_spinner_dropdown_item, it.map{it.name} )
            binding.createSpType.adapter = typeAdapter
            binding.createSpType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected( parent: AdapterView<*>?, view: View?, position: Int, id: Long ) {
                    createTaskViewModel.type = it[position].typeId
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        })
    }

    /**
     * Displaying according to recType selection
     * */
    private fun displayReccurencyOptions(recType: Int){
        binding.createLlytEndDate.visibility = View.VISIBLE
        binding.createLlytDelay.visibility = View.GONE
        when(recType){
            0 -> {
                binding.createTvExplanation.setText(R.string.term_type_r_explanation)
                binding.createLlytEndDate.visibility = View.GONE
                binding.createLlytDelay.visibility = View.VISIBLE
            }
            1 -> {
                binding.createTvExplanation.setText(R.string.term_type_ct_explanation)
            }
            2 -> {
                binding.createTvExplanation.setText(R.string.term_type_mt_explanation)
            }
            3-> {
                binding.createTvExplanation.setText(R.string.term_type_lt_explanation)
            }
        }
        // If a date has been selected, display reset option
        createTaskViewModel.displayResetEndDate.observe(viewLifecycleOwner, {
            if(it){
                binding.createBtnResetEndDate.visibility = View.VISIBLE
                binding.createLlytRemindersDate.visibility = View.VISIBLE
                binding.createRgReminderType.visibility = View.VISIBLE
            }
            else{ binding.createBtnResetEndDate.visibility = View.GONE
                binding.createLlytRemindersDate.visibility = View.GONE
                binding.createRgReminderType.visibility = View.GONE
            }
        })
        createTaskViewModel.displayResetStartDate.observe(viewLifecycleOwner, {
            if(it){binding.createBtnResetStartDate.visibility = View.VISIBLE}
            else{ binding.createBtnResetStartDate.visibility = View.GONE }
        })
        createTaskViewModel.displayResetReminderDate.observe(viewLifecycleOwner, {
            if(it){ binding.createBtnResetReminderDate.visibility = View.VISIBLE
                binding.createRgReminderType.visibility = View.VISIBLE}
            else{ binding.createBtnResetReminderDate.visibility = View.GONE
                binding.createRgReminderType.visibility = View.GONE }
        })
    }
    private fun displayDelayOptions(){
        binding.createSpDelayType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected( parent: AdapterView<*>?, view: View?, position: Int, id: Long ) {
                createTaskViewModel.delayType.value = position
                binding.apply{
                    createBtnRecDateAndHour.text = resources.getText(R.string.create_task_btn_hour)
                    createSpDelayDays.visibility = View.GONE
                    createEtDateAndNumber.visibility = View.GONE
                    createSpDelayRec.visibility = View.GONE
                    createLlytStartDate.visibility = View.GONE
                    createBtnRecDateAndHour.visibility = View.VISIBLE
                    createTvRecHour.visibility = View.VISIBLE
                }
                when(position){
                    1 -> {
                        binding.createSpDelayDays.visibility = View.VISIBLE
                    }
                    2 -> {
                        binding.createEtDateAndNumber.visibility = View.VISIBLE
                    }
                    3 -> {
                        binding.createBtnRecDateAndHour.text = resources.getText(R.string.create_task_btn_reminder_date)
                    }
                    4 -> {
                        binding.apply {
                            createEtDateAndNumber.visibility = View.VISIBLE
                            createSpDelayRec.visibility = View.VISIBLE
                            createLlytStartDate.visibility = View.VISIBLE
                            createBtnRecDateAndHour.visibility = View.GONE
                            createTvRecHour.visibility = View.GONE
                        }
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        createTaskViewModel.displayReminderButton.observe(viewLifecycleOwner,{
            if(it && binding.createLlytRemindersCustom.visibility == View.GONE) {
                binding.createBtnReminderDisplayer.visibility = View.VISIBLE
            }
            else{
                binding.createBtnReminderDisplayer.visibility = View.GONE
            }
        })
        createTaskViewModel.displayReminderDelay.observe(viewLifecycleOwner, {
            if(it) {
                binding.createBtnReminderDisplayer.visibility = View.GONE
                binding.createLlytRemindersCustom.visibility = View.VISIBLE
                binding.createRgReminderType.visibility = View.VISIBLE
            }
            else{
                binding.createBtnReminderDisplayer.visibility = View.VISIBLE
                binding.createLlytRemindersCustom.visibility = View.GONE
                binding.createRgReminderType.visibility = View.GONE
            }
        })
    }

    private fun dateTimePicking(){
        val calendar: Calendar = Calendar.getInstance()
        val cDay = calendar.get(Calendar.DAY_OF_MONTH)
        val cMonth = calendar.get(Calendar.MONTH)
        val cYear = calendar.get(Calendar.YEAR)
        val cHour = calendar.get(Calendar.HOUR_OF_DAY)
        val datePickerDialog = DatePickerDialog.OnDateSetListener{ _, year, month, dayOfMonth ->
            val timePickerDialog = TimePickerDialog.OnTimeSetListener{ _: TimePicker?, hourOfDay: Int, minute: Int ->
                val completeDate = "$dayOfMonth/" + (month + 1) + "/$year $hourOfDay:$minute"
                createTaskViewModel.dateSelected(completeDate)
            }
            TimePickerDialog(requireContext(), timePickerDialog, cHour, 0, true).show()
        }
        val dpd = DatePickerDialog(requireContext(), datePickerDialog, cYear,cMonth,cDay)
        createTaskViewModel.endDateBtnPressed.observe(viewLifecycleOwner, {
            if(it) dpd.datePicker.minDate = calendar.timeInMillis
        })
        createTaskViewModel.endDate.observe(viewLifecycleOwner,{
            if(it > 0L) {
                dpd.datePicker.minDate = calendar.timeInMillis
                dpd.datePicker.maxDate = it
            }
        })
        dpd.show()
    }

    private fun timePicking(){
        val calendar: Calendar = Calendar.getInstance()
        val cHour = calendar.get(Calendar.HOUR_OF_DAY)
        val cMin = calendar.get(Calendar.MINUTE)
        var selectedHour = "$cHour:$cMin"
        val timePickerDialog = TimePickerDialog.OnTimeSetListener{ _: TimePicker?, hourOfDay: Int, minute: Int ->
            selectedHour = "$hourOfDay:$minute"
            createTaskViewModel.hourSelected(selectedHour)
            binding.invalidateAll()
        }
        TimePickerDialog(requireContext(), timePickerDialog, cHour, cMin, true).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}