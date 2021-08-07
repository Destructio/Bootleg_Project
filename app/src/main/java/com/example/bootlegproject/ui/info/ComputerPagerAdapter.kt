package com.example.bootlegproject.ui.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
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


class ComputerPagerAdapter(fragment: Fragment, private val computer: String, val email: String) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment = ComputerInfoFragment2.newInstance(position,computer, email)

}

class ComputerInfoFragment2 : Fragment() {
    private val gson: Gson = Gson()
    private lateinit var viewModel: ComputerInfoViewModel
    private var fragmentView: View? = null
    private var computerType: Type = object : TypeToken<Computer?>() {}.type
    private var position: Int? = null
    private var computer: Computer? = null
    private var email: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        position = arguments?.getInt("pos")
        computer = gson.fromJson(arguments?.getString("json"), computerType)
        email = arguments?.getString("email")
        viewModel = ViewModelProvider(this).get(ComputerInfoViewModel::class.java).apply { }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        when (position) {
            0 -> fragmentView = container?.let { viewModel.baseComputerInfo(computer!!) }
            1 -> fragmentView = container?.let { viewModel.detailComputerInfo(computer!!,email!!) }
        }
        return fragmentView
    }


    companion object
    {
        @JvmStatic
        fun newInstance(position: Int, computerJSON: String?, email: String): Fragment  {
            return Fragment().apply{
                arguments = Bundle().apply {
                    putInt("pos", position)
                    putString("json", computerJSON)
                    putString("email", email)
                }
            }
        }
    }
}
