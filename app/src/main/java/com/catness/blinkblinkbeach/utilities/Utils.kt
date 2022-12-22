package com.catness.blinkblinkbeach.utilities

/**
 * Turn statement into expression
 * Can be used to turn when statement into expression so that the when statement has to be exhaustive, thus ensuring compile time safety.
 */
val <T> T.exhaustive: T
    get() = this

/**
 * Used to represent states when dealing with API
 */
sealed class APIState {
    object Loading : APIState()
    object Success : APIState()
    data class Error(val message: String? = null) : APIState()
}

/**
 * Used to represent states when dealing with API (with return value)
 */
sealed class APIStateWithValue<out T> {
    object Loading : APIStateWithValue<Nothing>()
    data class Success<out T>(val result: T) : APIStateWithValue<T>()
    data class Error(val message: String? = null) : APIStateWithValue<Nothing>()
}