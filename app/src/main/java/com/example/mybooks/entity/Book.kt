package com.example.mybooks.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Book(
    var id: Int = 0,
    var title: String? = null,
    var author: String? = null,
    var genre: String? = null,
    var quotes: String? = null,
    var date: String? = null
) : Parcelable