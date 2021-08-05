package com.example.bootlegproject.ui.list

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.bootlegproject.R
import com.example.bootlegproject.data.model.Computer
import com.example.bootlegproject.databinding.FragmentComputersBinding

class ComputersViewHolder(private val binding: FragmentComputersBinding)
    : RecyclerView.ViewHolder(binding.root) {

    fun bind(computer: Computer,  email: String){
        binding.name.text = computer.computerName

        val computerIcon = when (computer.osName) {
            "Windows" -> (R.drawable.win_logo)
            "Ubuntu" -> (R.drawable.linux_logo)
            else -> (R.drawable.default_logo)
        }
        binding.imageView.setImageResource(computerIcon)

        binding.root.setOnClickListener{
            val bundle = Bundle()
            bundle.putString("computerName",binding.name.text.toString())
            bundle.putString("email", email)
            it.findNavController().navigate(R.id.action_computersFragment_to_computerInfoFragment,bundle)
        }
    }


}