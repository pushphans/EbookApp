package com.amazing.ebookapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.amazing.ebookapp.repository.DataRepository

class DataVMFactory(private val dataRepo: DataRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DataViewModel(dataRepo) as T
    }
}