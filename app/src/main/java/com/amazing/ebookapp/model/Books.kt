package com.amazing.ebookapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Books(
    val id : String = "",
    val bookName : String = "",
    val author : String = "",
    val description : String = "",
    val category : String = "",
    val bookImageUrl : String = "",
    val bookUrl : String = ""
) : Parcelable
