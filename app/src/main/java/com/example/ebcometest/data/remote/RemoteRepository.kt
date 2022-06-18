package com.example.ebcometest.data.remote

import com.example.ebcometest.core.base.BaseNetworkRepository
import com.example.ebcometest.core.model.NetworkResult
import com.example.ebcometest.model.dto.MessageResponse
import kotlinx.coroutines.flow.Flow


abstract class RemoteRepository : BaseNetworkRepository() {

    abstract fun getMessageList(): Flow<NetworkResult<List<MessageResponse>>>
}

class RemoteRepositoryImpl(private val apiService: ApiService) : RemoteRepository() {

    override fun getMessageList(): Flow<NetworkResult<List<MessageResponse>>> =
        handleResult { apiService.getMessageList() }

}