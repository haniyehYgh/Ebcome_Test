package com.example.ebcometest.data.remote

import com.example.ebcometest.model.dto.ResultDto
import com.example.ebcometest.model.dto.MessageResponse
import retrofit2.http.*

interface ApiService {

    @GET("729e846c-80db-4c52-8765-9a762078bc82")
    suspend fun getMessageList(): ResultDto<List<MessageResponse>>

}