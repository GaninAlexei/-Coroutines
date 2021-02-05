package com.school.rxhomework

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.launch
import java.util.concurrent.TimeoutException

class ActivityViewModel : ViewModel() {
    private val _state = MutableLiveData<State>(State.Loading)
    val state: LiveData<State>
        get() = _state

    init {
        refreshData()
    }

    private fun refreshData() {
        viewModelScope.launch(Dispatchers.Default) {
            Repository.getPosts()
                    .retry(5) {
                        it is TimeoutException
                    }
                    .catch {
                        _state.postValue(State.Loaded(emptyList()))
                    }
                    .collect {
                        _state.postValue(State.Loaded(it))
                    }
        }
    }

    fun processAction(action: Action) {
        when (action) {
            Action.RefreshData -> refreshData()
        }
    }
}

