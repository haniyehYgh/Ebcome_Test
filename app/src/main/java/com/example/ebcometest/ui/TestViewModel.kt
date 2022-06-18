package com.example.ebcometest.ui

import android.app.Application
import com.example.ebcometest.core.base.BaseAndroidViewModel
import com.example.ebcometest.data.remote.Repository
import com.example.ebcometest.model.dto.MessageResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class TestViewModel @Inject constructor(
    application: Application,
    private val repository: Repository
) : BaseAndroidViewModel(application) {


    fun getMessageList(callback: (List<MessageResponse>) -> Unit) {
        handleApiResponse(repository.remoteRepository.getMessageList(), callback)
    }
}