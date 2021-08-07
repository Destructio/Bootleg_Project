package com.example.bootlegproject.ui.list

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bootlegproject.adapters.ComputersRecyclerAdapter
import com.example.bootlegproject.data.NetDataSource
import com.example.bootlegproject.data.model.Computer
import com.example.bootlegproject.databinding.FragmentComputersListBinding
import com.example.bootlegproject.ui.login.LoginResult
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*
import java.lang.reflect.Type

/**
 * A fragment representing a list of Items.
 */
class ComputersFragment : Fragment() {

    private lateinit var computerListViewModel: ComputersListViewModel
    private lateinit var binding: FragmentComputersListBinding
    private lateinit var dataLoad: Deferred<ArrayList<Computer>>


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?)
    : View {
        computerListViewModel = ViewModelProvider(this, ComputerListViewModelFactory())
            .get(ComputersListViewModel::class.java)

        val email = arguments?.getString("email").toString()

        binding = FragmentComputersListBinding.inflate(layoutInflater)
        //binding.progressBar.visibility = View.VISIBLE
        binding.recycler.layoutManager = LinearLayoutManager(context)

        getComputersList(email)

        return binding.root
    }

    private fun getComputersList(email: String) {
        val listType: Type = object : TypeToken<ArrayList<String>>() {}.type
        val computerType: Type = object : TypeToken<Computer>() {}.type

        lifecycleScope.launch(Dispatchers.Main) {

            val job = async(Dispatchers.IO) {
                val gson = Gson()
                val computersList = ArrayList<Computer>()

                val json = NetDataSource().computersRequest(email)

                val arrayJsonList: ArrayList<String> = gson.fromJson(json, listType)
                val size = arrayJsonList.size

                for (i in 0 until size) {
                    computersList.add(gson.fromJson(arrayJsonList[i], computerType))
                }
                computersList
            }
            binding.recycler.adapter = ComputersRecyclerAdapter(job.await(), email)
        }
    }

}
class ComputerListViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ComputersListViewModel::class.java)) {
            return ComputersListViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
