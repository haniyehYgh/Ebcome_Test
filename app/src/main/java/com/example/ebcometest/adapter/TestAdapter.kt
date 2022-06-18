package com.example.ebcometest.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.ebcometest.core.base.BaseHolder
import com.example.ebcometest.data.local.MessageEntity
import com.example.ebcometest.databinding.ItemTestAdapterBinding
import com.example.ebcometest.model.dto.MessageResponse

class TestAdapter( val callbackDelete: (Int) -> Unit,
                  val callbackFavorite: (Int) -> Unit) : ListAdapter<MessageEntity, TestAdapter.TestViewHolder>(object :
    DiffUtil.ItemCallback<MessageEntity>() {
    override fun areItemsTheSame(oldItem: MessageEntity, newItem: MessageEntity): Boolean =
        oldItem === newItem

    override fun areContentsTheSame(oldItem: MessageEntity, newItem: MessageEntity): Boolean =
        oldItem == newItem
}) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return TestViewHolder(ItemTestAdapterBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: TestViewHolder, position: Int) {
        getItem(position).let {
            holder.bind(it, position)

        }
    }

    inner class TestViewHolder(private val binding: ItemTestAdapterBinding) :
        BaseHolder<MessageEntity>(binding) {
        override fun bind(value: MessageEntity, position: Int) {
            binding.item = value
            binding.executePendingBindings()
            binding.parent.setOnLongClickListener{
                binding.deleteCb.visibility=View.VISIBLE
                true
            }


            binding.deleteCb.setOnClickListener {
              callbackDelete.invoke(position)
            }

        }
    }

}