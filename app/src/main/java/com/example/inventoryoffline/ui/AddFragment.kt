package com.example.inventoryoffline.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.inventoryoffline.data.ItemApplication
import com.example.inventoryoffline.databinding.FragmentAddBinding
import com.example.inventoryoffline.model.ItemViewModel
import com.example.inventoryoffline.model.ItemViewModelFactory


class AddFragment : Fragment() {

    private val viewModel: ItemViewModel by activityViewModels { ItemViewModelFactory((activity?.application as ItemApplication).database.itemDao()) }
    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {


            btnSave.setOnClickListener {
                insert()
            }


            btnView.setOnClickListener {
                val action = AddFragmentDirections.actionAddFragmentToHomeFragment()
                findNavController().navigate(action)
            }

        }
    }

    private fun insert() {
        binding.apply {
            if (isValid()) {
                viewModel.insert(
                    etProduct.text.toString(),
                    etCostPrice.text.toString(),
                    etQuantity.text.toString(),
                    etSellingPrice.text.toString()
                )
                clearText()

            } else {
                Toast.makeText(requireContext(), "please fill all field", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun clearText() {
        binding.apply {
            etProduct.text?.clear()
            etCostPrice.text?.clear()
            etQuantity.text?.clear()
            etSellingPrice.text?.clear()
        }
    }

    private fun isValid(): Boolean {
        binding.apply {
            return viewModel.validate(
                etProduct.text.toString(),
                etCostPrice.text.toString(),
                etQuantity.text.toString(),
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}