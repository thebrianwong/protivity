package com.thebrianwong.protivity.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.apollographql.apollo3.ApolloClient
import com.example.ExampleQuery
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ChatGPTViewModel: ViewModel() {
    private val _generatedText = mutableStateOf("")
    private val _coroutine = mutableStateOf<CoroutineScope?>(null)
    private val _apolloClient = mutableStateOf<ApolloClient?>(null)

    val generatedText =_generatedText

    fun setCoroutine(coroutine: CoroutineScope) {
        _coroutine.value = coroutine
    }

    fun setApolloClient(apolloClient: ApolloClient) {
        _apolloClient.value = apolloClient
    }

    fun generateText(timeDuration: Long) {
        _coroutine.value?.launch {
            val response = _apolloClient.value?.query(ExampleQuery())?.execute()
            val testArr = response?.data?.continents
            var testString = ""
            testArr?.forEach {cont ->
                testString = testString + cont?.name + ": " + cont?.code + "\n"
            }
            _generatedText.value = testString
        }
    }

    fun resetText() {
        _generatedText.value = "Break's Over!"
    }
}
