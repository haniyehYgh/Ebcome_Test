package com.example.ebcometest.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.ebcometest.data.local.DatabaseConstants.Companion.USER_TABLE_NAME
import com.google.gson.annotations.SerializedName

@Entity(tableName = USER_TABLE_NAME)
 data class MessageEntity (
 @PrimaryKey
 val id: String,
 val title: String,
 val description: String,
 val  image: String,
 val unread: Boolean

)

