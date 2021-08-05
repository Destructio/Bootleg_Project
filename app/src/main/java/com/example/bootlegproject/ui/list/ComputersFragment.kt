package com.example.bootlegproject.ui.list

import android.os.Bundle
import android.util.Log
import org.koin.core.qualifier.named
import org.koin.android.ext.android.get
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bootlegproject.adapters.ComputersRecyclerAdapter
import com.example.bootlegproject.data.AuthRepository
import com.example.bootlegproject.data.NetDataSource
import com.example.bootlegproject.data.model.Computer
import com.example.bootlegproject.databinding.FragmentComputersListBinding
import com.example.bootlegproject.ui.login.LoginViewModel
import com.example.bootlegproject.ui.login.LoginViewModelFactory
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.androidx.scope.scopeActivity
import java.lang.reflect.Type

/**
 * A fragment representing a list of Items.
 */
class ComputersFragment : Fragment() {

    private lateinit var computerListViewModel: ComputersListViewModel
    private lateinit var binding: FragmentComputersListBinding
    private lateinit var computersList: ArrayList<Computer>
    private val gson: Gson = Gson()
    private val itemsListType = object : TypeToken<java.util.ArrayList<String>>() {}.type

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        //computersList = gson.fromJson(getComputersJobCode.toString(), itemsListType)
        //getComputersJobCode.await()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?)
    : View {
        computerListViewModel = ViewModelProvider(this, ComputerListViewModelFactory())
            .get(ComputersListViewModel::class.java)

        val email = arguments?.getString("email").toString()
        computersList = computerListViewModel.getComputersList(email)

        binding = FragmentComputersListBinding.inflate(layoutInflater)
        //binding.progressBar.visibility = View.VISIBLE
        binding.recycler.layoutManager = LinearLayoutManager(context)
        binding.recycler.adapter = ComputersRecyclerAdapter(computersList, email)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //binding.progressBar.visibility = View.INVISIBLE
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
