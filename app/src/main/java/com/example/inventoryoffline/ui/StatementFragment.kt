package com.example.inventoryoffline.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.inventoryoffline.adapter.ItemStatementAdapter
import com.example.inventoryoffline.data.ItemApplication
import com.example.inventoryoffline.databinding.FragmentStatementBinding
import com.example.inventoryoffline.model.ItemViewModel
import com.example.inventoryoffline.model.ItemViewModelFactory


class StatementFragment : Fragment() {

    private val viewModel: ItemViewModel by activityViewModels { ItemViewModelFactory((activity?.application as ItemApplication).database.itemDao()) }

    private var _binding: FragmentStatementBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentStatementBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ItemStatementAdapter()


        viewModel.getAllItems().observe(this.viewLifecycleOwner) { items ->
            var sum = 0

            items.map {
                sum += it.profitLoss
            }

            items.let {
                adapter.submitList(it)
            }

            binding.tvTotalAmt.text = kotlin.math.abs(sum).toString()

            if (sum < 0) {
                val btnIndicator = binding.indicatorProfitLoss
                btnIndicator.setBackgroundColor(Color.parseColor("#FF9A9A"))
                btnIndicator.text = "Loss"

            }

        }


        binding.RVStatement.adapter = adapter


    }


}