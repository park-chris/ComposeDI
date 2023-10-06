package com.crystal.chat.chatlist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.crystal.chat.R
import com.crystal.chat.databinding.FragmentUserlistBinding

class UserFragment: Fragment(R.layout.fragment_userlist) {

    private lateinit var binding: FragmentUserlistBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUserlistBinding.bind(view)

        val userListAdapter = UserAdapter()
        binding.userListRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = userListAdapter
        }

        userListAdapter.submitList(
            mutableListOf<ChatRoomItem?>().apply {
                add(ChatRoomItem("dfgdsfghdfg", "dsfdsf", "sdfdsf"))
            }
        )
    }



}