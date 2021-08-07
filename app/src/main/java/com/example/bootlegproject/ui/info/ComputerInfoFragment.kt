package com.example.bootlegproject.ui.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.bootlegproject.R
import com.example.bootlegproject.data.NetDataSource
import com.example.bootlegproject.data.model.Computer
import com.example.bootlegproject.data.model.ComputerStatistic
import com.example.bootlegproject.databinding.FragmentComputerInfoBasicBinding
import com.example.bootlegproject.databinding.FragmentComputerInfoBinding
import com.example.bootlegproject.databinding.FragmentComputerInfoLBinding
import com.example.bootlegproject.databinding.FragmentLoginBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.reflect.Type

class ComputerInfoFragment : Fragment() {

    private lateinit var binding: FragmentComputerInfoBinding
    private var email: String = ""
    private var computer: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val computerJSON = arguments?.getString("computer").toString()
        email = arguments?.getString("email").toString()
        computer =  computerJSON
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentComputerInfoBinding.inflate(inflater, container, false)
        binding.pager.adapter = ComputerPagerAdapter(this, computer, email)
        return binding.root
    }


}