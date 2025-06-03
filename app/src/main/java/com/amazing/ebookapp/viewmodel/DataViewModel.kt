package com.amazing.ebookapp.viewmodel

import android.graphics.drawable.StateListDrawable
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amazing.ebookapp.model.Books
import com.amazing.ebookapp.repository.DataRepository
import com.amazing.ebookapp.utils.ResultState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.Thread.State

class DataViewModel(private val dataRepository : DataRepository)  : ViewModel() {

    private val _addState = MutableStateFlow<ResultState<String>?>(null)
    val addState : StateFlow<ResultState<String>?> get() = _addState

    private val _getState = MutableStateFlow<ResultState<List<Books>>?>(null)
    val getState : StateFlow<ResultState<List<Books>>?> get() = _getState

    private val _updateState = MutableStateFlow<ResultState<String>?>(null)
    val updateState : StateFlow<ResultState<String>?> get() = _updateState

    private val _deleteBook = MutableStateFlow<ResultState<String>?>(null)
    val deleteBook : StateFlow<ResultState<String>?> get() = _deleteBook

    fun addBooks(books: Books){
        viewModelScope.launch {
            _addState.value = ResultState.Loading

            val addResult = dataRepository.addBook(books)

            _addState.value = addResult
        }
    }

    fun getBooks() {
        viewModelScope.launch {
            _getState.value = ResultState.Loading

            val getResult = dataRepository.getBooks()

            _getState.value = getResult
        }
    }

    fun updateBook(books: Books){
        viewModelScope.launch {
            _updateState.value = ResultState.Loading

            val updateResult = dataRepository.updateBook(books)

            _updateState.value = updateResult
        }
    }

    fun deleteBook(book : Books){
        viewModelScope.launch {
            _deleteBook.value = ResultState.Loading

            val deleteResult = dataRepository.deleteBook(book)

            _deleteBook.value = deleteResult
        }
    }



}