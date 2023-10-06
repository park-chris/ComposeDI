package com.crystal.listeningcat

import android.os.Handler.Callback
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.crystal.listeningcat.databinding.ItemMessageBinding
import com.crystal.listeningcat.message.Message

class MessageAdapter(private val onClick: (String) -> Unit): ListAdapter<Message, MessageAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding: ItemMessageBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(message: Message) {
            binding.messageTextView.text = message.message

            binding.root.setOnClickListener {
                onClick(message.message)
            }
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Message>() {
            override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
                return oldItem.index == newItem.index
            }

            override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemMessageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = currentList[position]
        return holder.bind(message)
    }
}