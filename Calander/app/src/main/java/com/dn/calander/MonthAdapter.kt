package com.dn.calander

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class MonthAdapter: RecyclerView.Adapter<MonthAdapter.MonthViewHolder>() {
    val center = Int.MAX_VALUE / 2
    private var calendar = Calendar.getInstance()

    inner class MonthViewHolder(val layout: View): RecyclerView.ViewHolder(layout)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.month_list_item, parent, false)
        return MonthViewHolder(view)
    }

    override fun onBindViewHolder(holder: MonthViewHolder, position: Int) {
        calendar.time = Date()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.add(Calendar.MONTH, position - center)
        holder.layout.
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}