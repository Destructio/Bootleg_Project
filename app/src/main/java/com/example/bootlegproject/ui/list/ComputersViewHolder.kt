package com.example.bootlegproject.ui.list

import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.bootlegproject.R
import com.example.bootlegproject.data.model.Computer
import com.example.bootlegproject.databinding.FragmentComputersBinding
import com.example.bootlegproject.ui.info.ComputerInfoActivity
import com.google.gson.Gson

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
            val gson = Gson()
            val bundle = Bundle()

            bundle.putString("computer", gson.toJson(computer))
            bundle.putString("email", email)
            val intent: Intent = Intent(it.context, ComputerInfoActivity::class.java)
            intent.putExtra("computer", Gson().toJson(computer))
            intent.putExtra("email", email)
            startActivity(it.context,intent,null)
        }
    }


}