package com.example.inventoryoffline.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.inventoryoffline.data.Item
import com.example.inventoryoffline.databinding.ItemStatementListBinding

class ItemStatementAdapter :
    ListAdapter<Item, ItemStatementAdapter.ItemStatementViewHolder>(DiffCallback) {

    inner class ItemStatementViewHolder(var binding: ItemStatementListBinding) :
        ViewHolder(binding.root) {
        fun bind(item: Item) {
            binding.apply {
                tvStatementProduct.text = item.productName
                tvStatementSold.text = item.sold.toString()
                tvStatementSellingTprice.text = item.totalSelling.toString()
                tvStatementCostTprice.text = item.totalCost.toString()
                tvStatementTotal.text = item.profitLoss.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemStatementViewHolder {
        return ItemStatementViewHolder(
            ItemStatementListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemStatementViewHolder, position: Int) {
        val items = getItem(position)
        holder.bind(items)

    }

    object DiffCallback : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }
    }
}


