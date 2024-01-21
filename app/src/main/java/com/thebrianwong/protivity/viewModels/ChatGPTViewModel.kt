package com.thebrianwong.protivity.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.apollographql.apollo3.ApolloClient
import com.example.Query
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ChatGPTViewModel : ViewModel() {
    private val _generatedText = mutableStateOf("")
    private val _coroutine = mutableStateOf<CoroutineScope?>(null)
    private val _apolloClient = mutableStateOf<ApolloClient?>(null)

    val generatedText = _generatedText

    fun setCoroutine(coroutine: CoroutineScope) {
        _coroutine.value = coroutine
    }

    fun setApolloClient(apolloClient: ApolloClient) {
        _apolloClient.value = apolloClient
    }

    fun generateText(timeDuration: Long) {
        _coroutine.value?.launch {
            val serverResponse = _apolloClient.value?.query(Query((timeDuration / 1000).toInt()))?.execute()
            val generatedText = serverResponse?.data?.aiText?.content
            _generatedText.value = generatedText
                ?: "Uh oh! It seems that I've run out of fun facts at the moment. Try again later!"
        }
    }

    fun resetText() {
        _generatedText.value = "Break's Over!"
    }
}
