package com.thebrianwong.protivity.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.apollographql.apollo3.ApolloClient
import com.example.Query
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ChatGPTViewModel : ViewModel() {
    private val _initializing = mutableStateOf(true)
    private val _currentText = mutableStateOf("")
    private val _nextText = mutableStateOf("")
    private val _coroutine = mutableStateOf<CoroutineScope?>(null)
    private val _apolloClient = mutableStateOf<ApolloClient?>(null)

    val initializing = _initializing
    val currentText = _currentText
    val nextText = _nextText

    fun setCoroutine(coroutine: CoroutineScope) {
        _coroutine.value = coroutine
    }

    fun setApolloClient(apolloClient: ApolloClient) {
        _apolloClient.value = apolloClient
    }

    private fun generateText(timeDuration: Long) {
        _coroutine.value?.launch {
            val serverResponse =
                _apolloClient.value?.query(Query((timeDuration / 1000).toInt()))?.execute()
            val generatedText = serverResponse?.data?.aiText?.content
            _nextText.value = generatedText
                ?: "Uh oh! It seems that I've run out of fun facts at the moment. Try again later!"
        }
    }

    fun changeDisplayText(timeDuration: Long) {
        _currentText.value = _nextText.value
        generateText(timeDuration)
    }

    fun initializeText(timeDuration: Long) {
        _currentText.value =
            "Start the timer and read a fun fact while you wait!" +
                    "\n\n" +
                    "*Fun facts are generated by AI. Although they are fun, they are not necessarily factual."
        generateText(timeDuration)
        _initializing.value = false
    }

    fun resetText() {
        _currentText.value = "Break's Over!"
    }
}
