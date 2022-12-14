package com.catness.blinkblinkbeach.utilities

/**
 * Turn statement into expression
 * Can be used to turn when statement into expression so that the when statement has to be exhaustive, thus ensuring compile time safety.
 */
val <T> T.exhaustive: T
    get() = this