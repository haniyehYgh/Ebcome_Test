package com.example.ebcometest.core.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.ebcometest.App
import com.example.ebcometest.core.model.NetworkResult
import com.example.ebcometest.core.model.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

open class BaseAndroidViewModel(application: Application) : AndroidViewModel(application) {

    private var errorHandler = MutableLiveData<String>()
    private var uiState = MutableLiveData<UiState>()

    open fun getNetworkErrors(): LiveData<String> = errorHandler
    open fun getUiState(): LiveData<UiState> = uiState

    protected fun <T> handleApiResponse(apiRequest: Flow<NetworkResult<T>>, data: (T) -> Unit) =
        viewModelScope.launch {
            apiRequest.collect { result ->
                when (result) {
                    is NetworkResult.Success -> result.data?.let { data.invoke(it) }
                    is NetworkResult.InternalError -> errorHandler.value =
                        getApplication<App>().getString(result.messageRes!!)
                    is NetworkResult.NetworkError -> errorHandler.value = result.message!!
                    is NetworkResult.Loaded -> {
                        uiState.value = UiState.HIDELOADING
                    }
                    is NetworkResult.Loading -> {
                        uiState.value = UiState.LOADING
                    }
                }

            }
        }

}