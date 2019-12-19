package com.app.firebasesmartreply.model

import com.google.firebase.ml.naturallanguage.smartreply.SmartReplySuggestion

data class SmartReplyDataModel(val isSuccess: Boolean,
                               val isException: Boolean,
                               val exception: Exception? = null,
                               val smartReplySuggestionList: List<SmartReplySuggestion>? = null)