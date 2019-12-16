package com.tlnacl.randomuser.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Fts4
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
@Fts4
data class User(
    val id: String,
    val name: String,
    val gender: String,
    val dob: String,
    val thumbnailUrl: String,
    val imageUrl: String
) : Parcelable