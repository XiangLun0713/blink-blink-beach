package com.catness.blinkblinkbeach.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val id: String,
    val name: String,
    val email: String,
    val phoneNumber: String,
    val certificateUrls: List<String>
) : Parcelable
