package com.example.bootlegproject.ui.info

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.bootlegproject.R
import com.example.bootlegproject.data.NetDataSource
import com.example.bootlegproject.data.model.Computer
import com.example.bootlegproject.data.model.ComputerStatistic
import com.example.bootlegproject.databinding.FragmentComputerInfoBasicBinding
import com.example.bootlegproject.databinding.FragmentComputerInfoLBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.reflect.Type

class ComputerInfoViewModel: ViewModel()
{
    private val gson: Gson = Gson()
    private lateinit var binding1: FragmentComputerInfoBasicBinding
    private lateinit var binding2: FragmentComputerInfoLBinding
    private var statisticType: Type = object : TypeToken<ComputerStatistic?>() {}.type

    fun detailComputerInfo(computer: Computer, email: String): View {
        val text = binding2.fragmentComputerInfoLTextView

        viewModelScope.launch(Dispatchers.Main) {
            val task = async(Dispatchers.IO) {
                val computerStatistic = NetDataSource().computerStatisticRequest(computer.computerName.toString(), email)
                val statistic: ComputerStatistic = gson.fromJson(computerStatistic, statisticType)
                statistic
            }
            val info = task.await()
            text.text = "CPU Temperature = " + info.cpuTemp.toString() +
                    "\nRAM Usage = " + info.ramLoad.toString() +
                    "\nCPU Load = " + info.cpuLoad.toString() +
                    "%" + "\nHDD Load = " + info.hddLoad.toString() +
                    "\nUPTIME = " + info.upTime.toString() +
                    "\n Highest CPU usage = " + info.processList!![1].name
        }
        return binding2.root
    }

    fun baseComputerInfo(computer: Computer): View {
        val textViewName = binding1.fragmentComputerInfoTextName
        val textViewInfo = binding1.fragmentComputerInfoTextBase
        val logo = binding1.fragmentComputerInfoImage

        val disks: List<String?>? = computer.diskList
        val computerOS: String = computer.osName.toString()
        val disksNames = ArrayList<String>()
        for (i in disks!!.indices) {
            val str = disks[i]
            disksNames.add(str!!.substring(0, str.length - 33))
        }
        logo.setImageResource(R.drawable.default_logo)
        when (computerOS) {
            "Windows" -> logo.setImageResource(R.drawable.win_logo)
            "Ubuntu" -> logo.setImageResource(R.drawable.linux_logo)
        }
        textViewName.text = computer.computerName
        textViewInfo.text = """
            OS: ${computer.osName.toString()} ${computer.osVersion.toString()}  
            IPv4: ${computer.computerIPv4.toString()}
            IPv6: ${computer.computerIPv6.toString()}
            Total RAM: ${computer.totalRAM.toString()}
            CPU: ${computer.cpu.toString()}
            Cores / Threads: ${computer.cpuCores.toString()} / ${computer.cpuLogicalCores.toString()}
            MAC: ${computer.computerMac.toString()}
            Disks: ${disksNames.toTypedArray().contentToString()}"""
        return binding1.root
    }
}