package com.crystal.dotsindicator

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SimpleViewPagerAdapter (private val context: Context, private val list: List<String>): RecyclerView.Adapter<SimpleViewPagerAdapter.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val layout: LinearLayout = view.findViewById<LinearLayout>(R.id.layout)
        private val content: TextView = view.findViewById<TextView>(R.id.contentText)

        fun bind(string: String) {
            content.text = string
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.simple_view_pager, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val string = list[position]
        holder.bind(string)
    }

    override fun getItemCount() = list.size
}