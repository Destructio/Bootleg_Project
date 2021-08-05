package com.example.bootlegproject.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.bootlegproject.data.model.Computer
import com.example.bootlegproject.databinding.FragmentComputersBinding
import com.example.bootlegproject.ui.list.ComputersViewHolder

class ComputersRecyclerAdapter(private val computersList: ArrayList<Computer>, private val email: String)
    : RecyclerView.Adapter<ComputersViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComputersViewHolder {
        return ComputersViewHolder(FragmentComputersBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holderComputer: ComputersViewHolder, position: Int) {
        holderComputer.bind(computersList[position], email)
    }

    override fun getItemCount(): Int = computersList.size


}