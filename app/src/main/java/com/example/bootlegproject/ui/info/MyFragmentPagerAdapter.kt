package com.example.bootlegproject.ui.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.lifecycleScope
import com.example.bootlegproject.R
import com.example.bootlegproject.data.NetDataSource
import com.example.bootlegproject.data.model.Computer
import com.example.bootlegproject.data.model.ComputerStatistic
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.reflect.Type
import java.util.*

class MyFragmentPagerAdapter(fm: FragmentManager, private var computerJSON: String, private val email: String) :
    FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return MyFragment.newInstance(position, computerJSON,email)
    }

    override fun getCount(): Int = 2

    override fun getPageTitle(position: Int): CharSequence {
        var out = "PRIVET IZ 2018, TUT SKUCHNO I GRUSTNO"
        when (position) {
            0 -> out = "Статистика"
            1 -> out = "Характеристики"
        }
        return out
    }
}

class MyFragment : Fragment() {
    private val gson: Gson = Gson()
    var position: Int? = null
    var fragmentView: View? = null
    var computerType: Type = object : TypeToken<Computer?>() {}.type
    var statisticType: Type = object : TypeToken<ComputerStatistic?>() {}.type
    private var computer: Computer? = null
    private var email: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        email = arguments?.getString("email").toString()
        computer = gson.fromJson(arguments?.getString("json"), computerType)
        position = arguments?.getInt("pos")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        when (position) {
            0 -> fragmentView = baseComputerInfo(inflater, container!!)
            1 -> fragmentView = detailComputerInfo(inflater, container!!)
        }
        return fragmentView
    }

    private fun detailComputerInfo(inflater: LayoutInflater, container: ViewGroup): View {
        val fragmentViewL: View =
            inflater.inflate(R.layout.fragment_copmuter_info_l, container, false)
        val text: TextView = fragmentViewL.findViewById(R.id.fragmentComputerInfoL_TextView)
        lifecycleScope.launch(Dispatchers.Main) {
            val task = async(Dispatchers.IO) {
                val computerStatistic = NetDataSource().computerStatisticRequest(computer!!.computerName.toString(), email)
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
        return fragmentViewL
    }

    private fun baseComputerInfo(inflater: LayoutInflater, container: ViewGroup): View {
        val fragmentViewB: View =
            inflater.inflate(R.layout.fragment_computer_info_basic, container, false)
        val textViewName: TextView =
            fragmentViewB.findViewById<TextView>(R.id.fragmentComputerInfoTextName)
        val textViewInfo: TextView =
            fragmentViewB.findViewById<TextView>(R.id.fragmentComputerInfoTextBase)
        val logo = fragmentViewB.findViewById<ImageView>(R.id.fragmentComputerInfoImage)
        val disks: List<String?>? = computer!!.diskList
        val computerOS: String = computer!!.osName.toString()
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
        textViewName.text = computer!!.computerName
        textViewInfo.text = """
            OS: ${computer!!.osName.toString()} ${computer!!.osVersion.toString()}  
            IPv4: ${computer!!.computerIPv4.toString()}
            IPv6: ${computer!!.computerIPv6.toString()}
            Total RAM: ${computer!!.totalRAM.toString()}
            CPU: ${computer!!.cpu.toString()}
            Cores / Threads: ${computer!!.cpuCores.toString()} / ${computer!!.cpuLogicalCores.toString()}
            MAC: ${computer!!.computerMac.toString()}
            Disks: ${disksNames.toTypedArray().contentToString()}"""
        return fragmentViewB
    }

    companion object {
        fun newInstance(position: Int, computerJSON: String?, email: String): MyFragment {
            val f = MyFragment()
            val args = Bundle()
            args.putString("json", computerJSON)
            args.putInt("pos", position)
            args.putString("email", email)
            f.arguments = args
            return f
        }
    }
}



