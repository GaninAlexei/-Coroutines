package com.school.rxhomework

sealed class State {
    object Loading : State()
    data class Loaded(val content: List<MainActivity.Adapter.Item>) : State()
}
