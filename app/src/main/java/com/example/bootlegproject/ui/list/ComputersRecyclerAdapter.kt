package com.example.bootlegproject.ui.list

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView

import com.example.bootlegproject.placeholder.PlaceholderContent.PlaceholderItem
import com.example.bootlegproject.databinding.FragmentComputersBinding

class ComputersRecyclerAdapter(
    private val values: List<PlaceholderItem>
) : RecyclerView.Adapter<ComputersRecyclerAdapter.ComputerViewHolder>() {

    //TODO: https://www.youtube.com/watch?v=eOI0C8V3kzg&list=PLa2T1zmZ6w5KzKoh9M91vk1LBqpc-WtoS&index=10&t=1974s

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComputerViewHolder {

        return ComputerViewHolder(
            FragmentComputersBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holderComputer: ComputerViewHolder, position: Int) {
        val item = values[position]
        holderComputer.idView.text = item.id
        holderComputer.contentView.text = item.content
    }

    override fun getItemCount(): Int = values.size

    inner class ComputerViewHolder(binding: FragmentComputersBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.itemNumber
        val contentView: TextView = binding.content

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }

}