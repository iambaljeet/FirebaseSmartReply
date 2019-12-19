package com.app.firebasesmartreply.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.app.firebasesmartreply.R
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment(), View.OnClickListener {
    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private var smartReply = FirebaseNaturalLanguage.getInstance().smartReply

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        viewModel.initSmartReply(smartReply)

        val firebaseSmartReplyLiveData = viewModel.getSmartRepliesLiveData()

        firebaseSmartReplyLiveData.observe(this, Observer {
            smartReplyDataModel ->
            run {
                if (smartReplyDataModel.isSuccess) {
                    val smartRepliesStringBuilder = StringBuilder()
                    smartReplyDataModel.smartReplySuggestionList?.forEach { smartReply ->
                        smartRepliesStringBuilder.append("${smartReply.text} \n")
                    }
                    textViewSmartRepliesSuggestions.text = smartRepliesStringBuilder
                }
            }
        })

        buttonUser1SendMessage.setOnClickListener(this)
        buttonUser2SendMessage.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.buttonUser1SendMessage -> {
                val user1Message = editTextUser1Message.text.toString()
                viewModel.addLocalUserMessage(user1Message)
                textViewSmartRepliesSuggestions.text = ""
                editTextUser1Message.setText("")
            }
            R.id.buttonUser2SendMessage -> {
                val user2Message = editTextUser2Message.text.toString()
                viewModel.addRemoteUserMessage(user2Message, "100")
                editTextUser2Message.setText("")
            }
        }
    }
}