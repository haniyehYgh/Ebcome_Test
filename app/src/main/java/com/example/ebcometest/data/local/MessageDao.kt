package com.example.ebcometest.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMessageList(message: MessageEntity)

    @Update
    fun updateMessage(message: MessageEntity)

    @Delete
    fun deleteMessage(message: MessageEntity)

    @Query("Select * From message_table ORDER BY id DESC")

     fun getAllMessage():MutableList<MessageEntity>

    @Query("Select * From message_table WHERE  id Like:id")

    fun getMessage(id: String): MessageEntity

    @Query("DELETE From message_table")
    fun getDeleteAll()

}
