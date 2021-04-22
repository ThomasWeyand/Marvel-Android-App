package br.com.thomas.weyandmarvel.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CharacterDetailVO(
    val id: Long,
    val name: String,
    val thumbnailPath: String,
    val description: String,
    var isFavorite: Boolean
) : Parcelable