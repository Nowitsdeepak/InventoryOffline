package com.example.inventoryoffline.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.inventoryoffline.databinding.ItemOperationsNameBinding
import com.example.inventoryoffline.model.Operations


//data class OperationNameModel(val operationName: String)


class OperationNameAdapter(
    private val onClickOperationName: ((String) -> Unit),
    private val onDeleteClicked: ((String) -> Unit),
) : ListAdapter<Operations, OperationNameAdapter.OperationNameAdapterViewHolder>(
    DiffOperationCallback
) {

    inner class OperationNameAdapterViewHolder(var binding: ItemOperationsNameBinding) :
        ViewHolder(binding.root) {
        fun bind(oName: Operations) {
            binding.tvOperationName.text = oName.label_name
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): OperationNameAdapterViewHolder {
        return OperationNameAdapterViewHolder(
            ItemOperationsNameBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: OperationNameAdapterViewHolder, position: Int) {
        val name = getItem(position)
        holder.bind(name)

        if (name.label_name == Operations.DELETE_ITEM.label_name) {
           holder.itemView.setOnClickListener { onDeleteClicked("Delete") }
        } else {
            holder.itemView.setOnClickListener {
                val operationName = holder.binding.tvOperationName.text.toString()
                onClickOperationName(operationName)
            }

        }

    }


    object DiffOperationCallback : DiffUtil.ItemCallback<Operations>() {
        override fun areItemsTheSame(
            oldItem: Operations,
            newItem: Operations,
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: Operations,
            newItem: Operations,
        ): Boolean {
            return oldItem == newItem
        }

    }
}