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
import com.example.bootlegproject.data.model.MyProcess
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.content.Intent





class MyFragmentPagerAdapter(fm: FragmentManager, private var computerJSON: String, private val email: String) :
    FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return MyFragment.newInstance(position, computerJSON,email)
    }

    override fun getCount(): Int = 2

    override fun getPageTitle(position: Int): CharSequence {
        var out = ""
        when (position) {
            0 -> out = "Характеристики"
            1 -> out = "Статистика"
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
        val fragmentViewL: View = inflater.inflate(R.layout.fragment_copmuter_info_l, container, false)
        val text: TextView = fragmentViewL.findViewById(R.id.fragmentComputerInfoL_TextView)
        val shareButton: FloatingActionButton = fragmentViewL.findViewById(R.id.floating)
        lifecycleScope.launch(Dispatchers.Main) {
            val task = async(Dispatchers.IO) {
                val computerStatistic = NetDataSource().computerStatisticRequest(computer!!.computerName.toString(), email)
                val statistic: ComputerStatistic = gson.fromJson(computerStatistic, statisticType)
                statistic
            }
            val statistic = task.await()
            val process0: MyProcess = statistic.processList!![0]
            val process1: MyProcess = statistic.processList[1]
            val process2: MyProcess = statistic.processList[2]
            val process3: MyProcess = statistic.processList[3]
            val process4: MyProcess = statistic.processList[4]

            text.text = "CPU Температура: " + statistic.cpuTemp
                .toString() + "\nRAM Использование: " + statistic.ramLoad
                .toString() + "\nCPU Использование: " + statistic.cpuLoad
                .toString() + "%" + "\nHDD Загруженность: " + statistic.hddLoad
                .toString() + "\nUPTIME: " + statistic.upTime
                .toString() + "\nНаивысшая загруска CPU: " + statistic.processList[1].name.toString() + " " + statistic.processList[1].cpuload
                .toString() + "%" + "\nПоследняя отправка статистики: " + statistic.date
                .toString() + "\n\nСписок процессов:" + "\nPID: Имя: CPU: RAM:" + "\n1) " + process0.PID.toString() + " " + process0.name.toString() + " " + process0.cpuload.toString() + "% " + process0.RSS
                .toString() + "\n2) " + process1.PID.toString() + " " + process1.name.toString() + " " + process1.cpuload.toString() + "% " + process1.RSS
                .toString() + "\n3) " + process2.PID.toString() + " " + process2.name.toString() + " " + process2.cpuload.toString() + "% " + process2.RSS
                .toString() + "\n4) " + process3.PID.toString() + " " + process3.name.toString() + " " + process3.cpuload.toString() + "% " + process3.RSS
                .toString() + "\n5) " + process4.PID.toString() + " " + process4.name.toString() + " " + process4.cpuload.toString() + "% " + process4.RSS

        }
        shareButton.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, text.text)
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
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



