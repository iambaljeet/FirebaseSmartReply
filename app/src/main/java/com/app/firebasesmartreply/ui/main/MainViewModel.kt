package com.app.firebasesmartreply.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.firebasesmartreply.model.SmartReplyDataModel
import com.google.firebase.ml.naturallanguage.smartreply.FirebaseSmartReply
import com.google.firebase.ml.naturallanguage.smartreply.FirebaseTextMessage
import com.google.firebase.ml.naturallanguage.smartreply.SmartReplySuggestionResult

class MainViewModel : ViewModel() {
    private val smartRepliesLiveData = MutableLiveData<SmartReplyDataModel>()
    private var smartReply: FirebaseSmartReply? = null

    private val firebaseTextMessage: MutableList<FirebaseTextMessage> = mutableListOf()

    fun initSmartReply(smartReply: FirebaseSmartReply) {
        this.smartReply = smartReply
    }

    fun addLocalUserMessage(userMessage: String) {
        firebaseTextMessage.add(FirebaseTextMessage.createForLocalUser(
                userMessage, System.currentTimeMillis()))
    }

    fun addRemoteUserMessage(userMessage: String, userId: String) {
        firebaseTextMessage.add(FirebaseTextMessage.createForRemoteUser(
                userMessage, System.currentTimeMillis(), userId))
        getFirebaseSmartReply()
    }

    fun getSmartRepliesLiveData(): MutableLiveData<SmartReplyDataModel> {
        return smartRepliesLiveData
    }

    private fun getFirebaseSmartReply() {
        if (firebaseTextMessage.isNotEmpty()) {
            smartReply?.suggestReplies(firebaseTextMessage)
                ?.addOnSuccessListener { smartReplySuggestionResult ->
                    val smartReplyDataModel = when (smartReplySuggestionResult.status) {
                        SmartReplySuggestionResult.STATUS_SUCCESS -> {
                            SmartReplyDataModel(isSuccess = true, isException = false,
                                smartReplySuggestionList = smartReplySuggestionResult.suggestions)
                        }
                        SmartReplySuggestionResult.STATUS_NOT_SUPPORTED_LANGUAGE -> {
                            SmartReplyDataModel(isSuccess = false, isException = false)
                        }
                        SmartReplySuggestionResult.STATUS_NO_REPLY -> {
                            SmartReplyDataModel(isSuccess = false, isException = false)
                        }
                        else -> {
                            SmartReplyDataModel(isSuccess = false, isException = false)
                        }
                    }
                    smartRepliesLiveData.postValue(smartReplyDataModel)
                }
                ?.addOnFailureListener { exception ->
                    val smartReplyDataModel = SmartReplyDataModel(
                        isSuccess = false,
                        isException = true,
                        exception = exception
                    )
                    smartRepliesLiveData.postValue(smartReplyDataModel)
                }
        }

    }
}