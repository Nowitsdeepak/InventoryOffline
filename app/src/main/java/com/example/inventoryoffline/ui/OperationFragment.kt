package com.example.inventoryoffline.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.example.inventoryoffline.data.Item
import com.example.inventoryoffline.data.ItemApplication
import com.example.inventoryoffline.databinding.FragmentOperationBinding
import com.example.inventoryoffline.model.ItemViewModel
import com.example.inventoryoffline.model.ItemViewModelFactory
import com.example.inventoryoffline.model.Operations

//const val TAG = "TAG TEST"

class OperationFragment : Fragment() {

    private val viewModel: ItemViewModel by activityViewModels { ItemViewModelFactory((activity?.application as ItemApplication).database.itemDao()) }

    private var _binding: FragmentOperationBinding? = null
    private val binding get() = _binding!!

    private lateinit var fetchedItem: Item


    private val operationNavArgs: OperationFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentOperationBinding.inflate(inflater)
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.getItemById(operationNavArgs.id).observe(this.viewLifecycleOwner) { item ->
            fetchedItem = item
            bind(fetchedItem)
        }


    }

    private fun bind(item: Item) {

        binding.apply {

            when (operationNavArgs.title) {

                Operations.EDIT_ADD_ITEM.label_name -> {
                    viewEditAddItem.visibility = View.VISIBLE
                    setItem(item)
                    binding.btnSave.setOnClickListener {
                        updateAddItem(fetchedItem)
                    }
                }

                Operations.APPLY_DISCOUNT.label_name -> {
                    viewDiscount.visibility = View.VISIBLE
                    tvTotalSelling.text = fetchedItem.totalSelling.toString()
                    btnSave.setOnClickListener {
                        discount(fetchedItem)
                    }
                }

                Operations.ADD_QUANTITY.label_name -> {
                    viewAddMoreItem.visibility = View.VISIBLE
                    btnSave.setOnClickListener { addQuantity(fetchedItem) }

                }

                Operations.RETURN_ITEM.label_name -> {
                    viewReturn.visibility = View.VISIBLE
                    etPurchased.setText("${fetchedItem.costPrice}")
                    btnSave.setOnClickListener { returnItem(fetchedItem) }
                }

            }

        }

    }


    private fun updateAddItem(fetchedItem: Item) {
        binding.apply {
            Log.d("TAG12", "$fetchedItem")

            if (isValidEntry()) {
                viewModel.updateOperation(
                    fetchedItem,
                    etProductO.text.toString(),
                    etCostPriceO.text.toString(),
                    etQuantityO.text.toString(),
                    etSellingPriceO.text.toString()
                )
                clearText()
            } else {
                Toast.makeText(requireContext(), "please fill all field", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setItem(fetchedItem: Item) {
        binding.apply {
            etProductO.setText(fetchedItem.productName, TextView.BufferType.SPANNABLE)
            etCostPriceO.setText(fetchedItem.costPrice.toString(), TextView.BufferType.SPANNABLE)
            etQuantityO.setText(fetchedItem.quantity.toString(), TextView.BufferType.SPANNABLE)
            etSellingPriceO.setText(
                fetchedItem.sellingPrice.toString(), TextView.BufferType.SPANNABLE
            )
        }
    }


    private fun discount(fetchedItem: Item) {
        val alpha = binding.etDiscountAmount.text

        if (fetchedItem.totalSelling.minus(
                alpha.toString().toInt()
            ) > fetchedItem.totalCost
        ) {

            if (alpha.isNotBlank()) {
                val discount = alpha.toString().toInt()
                viewModel.discountOperation(fetchedItem, discount)
                binding.etDiscountAmount.text.clear()
            }
        } else {
            Toast.makeText(
                requireContext(), "You will go into loss if we proceed sorry :(", Toast.LENGTH_SHORT
            ).show()
        }
    }


    private fun addQuantity(fetchedItem: Item) {
        val alpha = binding.etAddMoreQuantity.text

        if (alpha.isNotBlank()) {
            val quantity = alpha.toString().toInt()
            viewModel.addQuantityOperation(fetchedItem, quantity)
            alpha.clear()
        }
    }


    private fun returnItem(fetchedItem: Item) {
        binding.apply {

//            TODO(If item is older and we don't have now )
            val purchased = etPurchased.text
            val sold = etSold.text
            val rQuantity = etReturnQuantity.text

            if (validateReturn()) {
                viewModel.returnOperation(
                    fetchedItem,
                    purchased.toString().toInt(),
                    sold.toString().toInt(),
                    rQuantity.toString().toInt()
                )
                Toast.makeText(requireContext(), "Saved", Toast.LENGTH_SHORT).show()
                purchased.clear()
                sold.clear()
                rQuantity.clear()
            } else {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    }

    private fun validateReturn(): Boolean {
        binding.apply {
            return etPurchased.text.isNotBlank() && etSold.text.isNotBlank() && etReturnQuantity.text.isNotBlank()
        }
    }


    private fun isValidEntry(): Boolean {
        binding.apply {
            return viewModel.validate(
                etProductO.text.toString(),
                etCostPriceO.text.toString(),
                etQuantityO.text.toString(),
            )
        }
    }

    private fun clearText() {
        binding.apply {
            etProductO.text?.clear()
            etCostPriceO.text?.clear()
            etQuantityO.text?.clear()
            etSellingPriceO.text?.clear()
        }
    }

}



