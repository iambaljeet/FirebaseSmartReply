package com.app.firebasesmartreply

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.app.firebasesmartreply.model.SmartReplyDataModel
import com.app.firebasesmartreply.ui.main.MainViewModel
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.times
import org.mockito.Mockito.verify

class MainViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: MainViewModel

    private val observer: Observer<SmartReplyDataModel> = mock()

    @Before
    fun before() {
        viewModel = MainViewModel()
        viewModel.getSmartRepliesLiveData().observeForever(observer)
    }

    @Test
    fun getSmartReply_shoutReturnSmartReply() {
        val smartReply = SmartReplyDataModel(isSuccess = true, isException = true)
        viewModel.getSmartRepliesLiveData().value = smartReply

        val captor = ArgumentCaptor.forClass(SmartReplyDataModel::class.java)

        captor.run {
            verify(observer, times(1)).onChanged(capture())
            assertEquals(smartReply, value)
        }
    }
}