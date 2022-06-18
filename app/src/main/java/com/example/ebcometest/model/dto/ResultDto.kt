package com.example.ebcometest.model.dto

import androidx.annotation.Keep

@Keep
class ResultDto<T> {
    @Keep
    var code : Int = 0

    @Keep
    var message: String? = null

    @Keep
    var messages: T? = null
}