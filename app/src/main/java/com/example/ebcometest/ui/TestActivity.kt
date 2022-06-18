package com.example.ebcometest.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.room.Room
import com.example.ebcometest.R
import com.example.ebcometest.adapter.TestAdapter
import com.example.ebcometest.core.parent.ParentActivity
import com.example.ebcometest.data.local.AppDatabase
import com.example.ebcometest.data.local.DatabaseConstants.Companion.DATABASE_NAME
import com.example.ebcometest.data.local.MessageEntity
import com.example.ebcometest.databinding.ActivityTestBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TestActivity : ParentActivity<TestViewModel, ActivityTestBinding>() {


    private val adapter by lazy {
        TestAdapter(callbackDelete = {
            deleteMessage(it)
        }, callbackFavorite = {
        })
    }

    private val messageDb by lazy {

        Room.databaseBuilder(this, AppDatabase::class.java, DATABASE_NAME)
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()

    }

    private lateinit var message: MessageEntity
    private var img: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {

            viewModel.getMessageList { testList ->

                for (i in 0 until testList!!.size) {

                    img = if (testList[i].image != null) {
                        testList[i].image
                    } else {
                        ""
                    }
                    message = MessageEntity(
                        testList[i].id,
                        testList[i].title,
                        testList[i].description,
                        img,
                        testList[i].unread
                    )
                    messageDb.messageDao().insertMessageList(message)
                }
                setupListMessage()
            }

            //todo create save message handle change tab public message and save message
            tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    when (tab.position) {
                        0 -> {
                            messageCl.visibility = View.VISIBLE
                            noMessageCl.visibility = View.GONE
                        }
                        1 -> {
                            messageCl.visibility = View.GONE
                            noMessageCl.visibility = View.VISIBLE
                        }
                    }
                }
                override fun onTabUnselected(tab: TabLayout.Tab) {}
                override fun onTabReselected(tab: TabLayout.Tab) {}
            })

        }

    }

    override fun getResourceLayoutId(): Int = R.layout.activity_test

    override fun getViewModelClass(): Class<TestViewModel> = TestViewModel::class.java


    override fun onShowLoading() {
        super.onShowLoading()
        if (!binding.loadingView.loadingUiRoot.isVisible) {
            binding.loadingView.loadingUiRoot.visibility = View.VISIBLE
        }
    }

    override fun onHideLoading() {
        super.onHideLoading()
        if (binding.loadingView.loadingUiRoot.isVisible) {
            binding.loadingView.loadingUiRoot.visibility = View.GONE
        }
    }


    private fun setupListMessage() {

        if (messageDb.messageDao().getAllMessage().isNotEmpty()) {
            binding.recycler.adapter = adapter
            adapter.submitList(messageDb.messageDao().getAllMessage())
            binding.tabLayout.getTabAt(0)?.orCreateBadge?.number =
                messageDb.messageDao().getAllMessage().size
        }
    }

    private fun deleteMessage(pos: Int) {
        val messageEntity = MessageEntity(
            messageDb.messageDao().getAllMessage()[pos].id,
            messageDb.messageDao().getAllMessage()[pos].title,
            messageDb.messageDao().getAllMessage()[pos].description,
            messageDb.messageDao().getAllMessage()[pos].image,
            messageDb.messageDao().getAllMessage()[pos].unread
        )
        messageDb.messageDao().deleteMessage(messageEntity)
        setupListMessage()
    }


}
