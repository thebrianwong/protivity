package com.thebrianwong.protivity.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.apollographql.apollo3.ApolloClient
import com.example.Query
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class AITextViewModel : ViewModel() {
    private val _initializing = mutableStateOf(true)
    private val _displayedNetworkErrorMessage = mutableStateOf(false)
    private val _currentText = mutableStateOf("")
    private val _nextText = mutableStateOf("")
    private val _coroutine = mutableStateOf<CoroutineScope?>(null)
    private val _apolloClient = mutableStateOf<ApolloClient?>(null)
    private val _indicateNetworkErrorCallback = mutableStateOf<(() -> Unit)?>(null)

    val initializing = _initializing
    val currentText = _currentText

    fun setCoroutine(coroutine: CoroutineScope) {
        _coroutine.value = coroutine
    }

    fun setApolloClient(apolloClient: ApolloClient) {
        _apolloClient.value = apolloClient
    }

    fun setIndicateNetworkErrorCallback(callback: () -> Unit) {
        _indicateNetworkErrorCallback.value = callback
    }

    private fun generateText(timeDuration: Long, prioritizeCurrent: Boolean = false) {
        _coroutine.value?.launch {
            if (_apolloClient.value != null) {
                try {
                    val serverResponse =
                        _apolloClient.value?.query(Query((timeDuration / 1000).toInt()))?.execute()
                    val generatedText = serverResponse?.data?.aiText?.content
                    if (prioritizeCurrent && _currentText.value != "Break's Over!") {
                        _currentText.value = generatedText
                            ?: "Uh oh! It seems that I've run out of fun facts at the moment. Try again later!"
                    } else {

                        _nextText.value = generatedText
                            ?: "Uh oh! It seems that I've run out of fun facts at the moment. Try again later!"
                    }
                    _initializing.value = false
                    // this is reset so that if the server crashes again,
                    // the error message can be displayed again
                    _displayedNetworkErrorMessage.value = false
                } catch (e: Exception) {
                    if (!_displayedNetworkErrorMessage.value) {
                        _indicateNetworkErrorCallback.value?.let { it() }
                        _displayedNetworkErrorMessage.value = true
                        // will result in currentText being an empty string,
                        // providing a clearer indicator that there is an issue
                        // since the loading circle will be rendered
                        _nextText.value = ""
                    }
                }
            }

        }
    }

    fun changeDisplayText(timeDuration: Long) {
        if (_nextText.value == "" || _currentText.value == _nextText.value) {
            _currentText.value = ""
            generateText(timeDuration, true)
        } else {
            _currentText.value = _nextText.value
            generateText(timeDuration)
        }
    }

    fun initializeText(timeDuration: Long) {
        _currentText.value =
            "Start the timer and read a fun fact while you wait!" +
                    "\n\n" +
                    "*Fun facts are generated by AI. Although they are fun, they are not necessarily factual."
        generateText(timeDuration)
    }

    fun resetText() {
        _currentText.value = "Break's Over!"
    }
}