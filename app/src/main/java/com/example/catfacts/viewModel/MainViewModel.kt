package com.example.catfacts.viewModel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catfacts.dataLayer.database.AppDatabase
import com.example.catfacts.dataLayer.entities.Fact
import com.example.catfacts.dataLayer.network.NetworkModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineExceptionHandler

class MainViewModel(context: Context) : ViewModel() {
    private val database = AppDatabase.getDatabase(context)
    private val factDao = database.factDao()
    private val catFactService = NetworkModule.createCatFactService()

    var currentFact = mutableStateOf<Fact?>(null)
    var errorMessage = mutableStateOf("")

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e("MainViewModel", "Error occurred: ${throwable.message}")
        viewModelScope.launch(Dispatchers.Main) {
            errorMessage.value = "Failed to load data: ${throwable.localizedMessage}"
        }
    }

    init {
        fetchFact()
    }

    fun fetchFact() {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val response = catFactService.fetchFact()
            if (response.isSuccessful) {
                response.body()?.let { fact ->
                    factDao.insertFact(fact)
                    viewModelScope.launch(Dispatchers.Main) {
                        currentFact.value = fact
                    }
                }
            } else {
                throw Exception("Failed to fetch fact: ${response.errorBody()?.string()}")
            }
        }
    }

    fun showNextFact() {
        fetchFact()
    }

    fun showPreviousFact() {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            currentFact.value?.id?.let { currentId ->
                val prevId = currentId - 1
                factDao.getFactById(prevId)?.let { prevFact ->
                    viewModelScope.launch(Dispatchers.Main) {
                        currentFact.value = prevFact
                    }
                } ?: throw Exception("No previous fact available.")
            }
        }
    }
}
