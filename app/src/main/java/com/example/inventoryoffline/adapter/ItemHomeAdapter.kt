package com.example.inventoryoffline.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.inventoryoffline.data.Item
import com.example.inventoryoffline.databinding.ItemHomeListBinding


class ItemHomeAdapter(
    private val onBodyClicked: ((Long) -> Unit),
    private val onClickedSale: ((item: Item, position: Int, s_price: Int, s_quantity: Int) -> Unit),

    ) : ListAdapter<Item, ItemHomeAdapter.ItemHomeViewHolder>(DiffCallback) {

    inner class ItemHomeViewHolder(var binding: ItemHomeListBinding) : ViewHolder(binding.root) {
        fun bind(item: Item) {
            binding.items = item
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHomeViewHolder {
        return ItemHomeViewHolder(
            ItemHomeListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemHomeViewHolder, position: Int) {
        val items = getItem(position)
        holder.bind(items)


        holder.itemView.setOnClickListener {
            onBodyClicked(items.id)
        }

        holder.binding.apply {


            var count = 0
            val sQuantity = tvHomeQuantity.text.toString().toInt()

            ibHomeIncrease.setOnClickListener {
                if (sQuantity > count) count++
                etHomeSellingQuantity.setText("$count")

            }
            ibHomeDecrease.setOnClickListener {
                if (count > 0) count--
                etHomeSellingQuantity.setText("$count")

            }

            etHomeSellingPrice.setOnClickListener { etHomeSellingPrice.text?.clear() }

            btnHomeSale.setOnClickListener {

                onClickedSale(
                    items,
                    position,
                    etHomeSellingPrice.text.toString().toInt(),
                    etHomeSellingQuantity.text.toString().toInt()
                )
            }
        }

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

