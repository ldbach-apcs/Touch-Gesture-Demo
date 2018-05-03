package com.example.cpu02351_local.touchgesturedemo

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast

class SimpleStringAdapter(private val data: ArrayList<String>) : RecyclerView.Adapter<SimpleStringAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_simple_layout, parent, false)
        v.setOnClickListener {
            Toast.makeText(parent.context, "Click not blocked", Toast.LENGTH_SHORT).show()
        }
        return ViewHolder(v)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.v.findViewById<TextView>(R.id.displayText).text = data[position]
    }

    class ViewHolder(val v: View) : RecyclerView.ViewHolder(v)
}

