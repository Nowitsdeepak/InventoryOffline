package com.example.inventoryoffline.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.inventoryoffline.adapter.OperationNameAdapter
import com.example.inventoryoffline.data.ItemApplication
import com.example.inventoryoffline.databinding.FragmentEditBinding
import com.example.inventoryoffline.model.ItemViewModel
import com.example.inventoryoffline.model.ItemViewModelFactory
import com.example.inventoryoffline.model.Operations
import com.example.inventoryoffline.model.TAG
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class EditFragment : Fragment() {

    private val viewModel: ItemViewModel by activityViewModels { ItemViewModelFactory((activity?.application as ItemApplication).database.itemDao()) }
    private val selectedItemId: EditFragmentArgs by navArgs()

    private var _binding: FragmentEditBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentEditBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = OperationNameAdapter(

            onClickOperationName = { operationName ->

                val action =
                    EditFragmentDirections.actionEditFragmentToOperationFragment(operationName)
                action.id = selectedItemId.itemId
                findNavController().navigate(action)


            }, onDeleteClicked = {
                showDialog(selectedItemId.itemId)
            })

        val oList = listOf(
            Operations.EDIT_ADD_ITEM,
            Operations.APPLY_DISCOUNT,
            Operations.ADD_QUANTITY,
            Operations.RETURN_ITEM,
            Operations.DELETE_ITEM
        )

        adapter.submitList(oList)

        binding.RVOperation.adapter = adapter

    }

    private fun showDialog(id: Long) {
        val dialog = MaterialAlertDialogBuilder(requireContext()).setTitle("Delete")
            .setMessage("Are you sure to delete this")
            .setNegativeButton("cancel") { dialog, which -> }
            .setPositiveButton("ok") { dialog, which -> delete(id) }
        dialog.show()
    }

    private fun delete(id: Long) {
        viewModel.delete(id)
        val action = EditFragmentDirections.actionEditFragmentToHomeFragment()
        findNavController().navigate(action)
    }


}

