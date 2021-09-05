package com.example.jampa.ui.category

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.example.jampa.R
import com.example.jampa.database.ItemDatabase
import com.example.jampa.database.entities.Category
import com.example.jampa.databinding.FragmentCategoryBinding
import com.example.jampa.databinding.FragmentCreateTaskBinding
import com.example.jampa.onTextChanged
import com.example.jampa.ui.home.HomeViewModelFactory


class CategoryFragment : Fragment() {

    private lateinit var catTypViewModel: CategoryViewModel
    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        // Inflate the layout for this fragment
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val application = requireNotNull(this.activity).application
        val dataSource = ItemDatabase.getInstance(application)
        val viewModelFactory = CategoryViewModelFactory(dataSource, application)

        catTypViewModel = ViewModelProvider(this, viewModelFactory).get(CategoryViewModel::class.java)
        binding.catTypViewModel = catTypViewModel
        binding.lifecycleOwner = this

        observeInputs()

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear() // We don't want to have search options in this fragment
    }

    private fun observeInputs(){
        /** The element has been created so we need to empty ET and notify VM **/
        catTypViewModel.elementCreated.observe(viewLifecycleOwner, {
            if(it) {
                catTypViewModel.elementName.value = ""
                catTypViewModel.elementCreated()
            }
        })
        /** Changing display to element type**/
        catTypViewModel.currentElementType.observe(viewLifecycleOwner, { ElType ->
            when(ElType){
                0 -> {
                    catTypViewModel.categories.observe(viewLifecycleOwner, {
                        val elementAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, it.map { it.name } )
                        binding.catTypListview.adapter = elementAdapter
                        binding.catTypListview.setOnItemClickListener{ _, _, position, _ ->
                            catTypViewModel.addButtonTitle.value = resources.getString(R.string.cat_typ_add_btn_mod)
                            catTypViewModel.delButtonState.value = true
                            catTypViewModel.elementName.value = it[position].name
                            catTypViewModel.selectedElementId = it[position].categoryId
                        }
                    })
                }
                1 -> {
                    catTypViewModel.types.observe(viewLifecycleOwner, {
                        val elementAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, it.map { it.name } )
                        binding.catTypListview.adapter = elementAdapter
                        binding.catTypListview.setOnItemClickListener{ _, _, position, _ ->
                            catTypViewModel.addButtonTitle.value = resources.getString(R.string.cat_typ_add_btn_mod)
                            catTypViewModel.delButtonState.value = true
                            catTypViewModel.elementName.value = it[position].name
                            catTypViewModel.selectedElementId = it[position].typeId
                        }
                    })
                }
            }
        })

        binding.catTypEtName.onTextChanged {
            catTypViewModel.onTextChanged(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}