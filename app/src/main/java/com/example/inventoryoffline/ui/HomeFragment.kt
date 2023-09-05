package com.example.inventoryoffline.ui

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.inventoryoffline.R
import com.example.inventoryoffline.adapter.ItemHomeAdapter
import com.example.inventoryoffline.data.Item
import com.example.inventoryoffline.data.ItemApplication
import com.example.inventoryoffline.databinding.FragmentHomeBinding
import com.example.inventoryoffline.model.ItemViewModel
import com.example.inventoryoffline.model.ItemViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class HomeFragment : Fragment() {

    private val viewModel: ItemViewModel by activityViewModels { ItemViewModelFactory((activity?.application as ItemApplication).database.itemDao()) }

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var fetchItem: Item

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvHomeOperation()

    }

    private fun rvHomeOperation() {


        val adapter = ItemHomeAdapter(onBodyClicked = { item_id ->

            val action = HomeFragmentDirections.actionHomeFragmentToEditFragment("Edit")
            action.itemId = item_id

            findNavController().navigate(action)

        }, onClickedSale = { items, _, sprice, squantity ->

            viewModel.updateHomeItem(items, sprice, squantity)

            if (items.quantity < 1) {

                Toast.makeText(
                    requireContext(), "${items.productName} : Out Of Stock", Toast.LENGTH_SHORT
                ).show()

            }
        })



        viewModel.getAllItems().observe(this.viewLifecycleOwner) { items ->
            items.let {
                adapter.submitList(it)
            }
        }

        binding.RVHome.adapter = adapter

    }


    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_app_bar, menu)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.menuAddItem -> {
                val action = HomeFragmentDirections.actionHomeFragmentToAddFragment("Add Item")
                findNavController().navigate(action)
                true
            }
            R.id.menuStatement -> {
                val action = HomeFragmentDirections.actionHomeFragmentToStatementFragment("Statement")
                findNavController().navigate(action)
                true
            }

            R.id.menuClearAll -> {
//                viewModel.clearDatabase()
                showDialog()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showDialog() {

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Clear All")
            .setMessage("You wont be able to retrieve this data again!!")
            .setNegativeButton("cancel") { _, _ -> }
            .setPositiveButton("proceed") { dialog, _ ->
                viewModel.clearDatabase()
            }
            .show()
    }
}
