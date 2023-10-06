package com.crystal.listeningcat.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.crystal.listeningcat.databinding.ItemFullMessageBinding
import com.crystal.listeningcat.databinding.ItemMessageBinding
import com.crystal.listeningcat.message.Message

class FullMessageAdapter(private val onClick: (Message, MessageState) -> Unit): ListAdapter<Message, FullMessageAdapter.ViewHolder>( diffUtil) {

    inner class ViewHolder(private val binding: ItemFullMessageBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(message: Message) {
            binding.messageTextView.text = message.message

            binding.playButton.setOnClickListener {
                onClick(message, MessageState.PLAY)
            }

            binding.deleteButton.setOnClickListener {
                onClick(message, MessageState.DELETE)
            }
            binding.editButton.setOnClickListener {
                onClick(message, MessageState.EDIT)
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
            ItemFullMessageBinding.inflate(
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