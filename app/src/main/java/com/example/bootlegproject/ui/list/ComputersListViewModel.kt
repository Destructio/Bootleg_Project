package com.example.bootlegproject.ui.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bootlegproject.data.NetDataSource
import com.example.bootlegproject.data.model.Computer
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*
import java.lang.reflect.Type

class ComputersListViewModel() : ViewModel() {

    fun getComputersList(email: String): ArrayList<Computer> {
        val listType: Type = object : TypeToken<ArrayList<String>>() {}.type
        val computerType: Type = object : TypeToken<Computer>() {}.type
        val gson = Gson()
        val computersList = ArrayList<Computer>()

        viewModelScope.launch(Dispatchers.IO) {

            val json = NetDataSource().computersRequest(email)
            val arrayJsonList: ArrayList<String> = gson.fromJson(json, listType)
            val size = arrayJsonList.size

            for (i in 0 until size) {
                computersList.add(gson.fromJson(arrayJsonList[i], computerType))
            }
            delay(6000) //TODO REMOVE THIS SHIT
        }

        Log.d("---------------------", " Empty ArrayList? - ${computersList.isEmpty()}")
        return computersList
    }


}