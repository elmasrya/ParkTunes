package io.parkmobile.parktunes.models

import android.os.Parcel
import android.os.Parcelable
import android.widget.ImageView

data class Song(
    val wrapperType: String?,
    val kind: String?,
    val artistId: String?,
    val collectionId: String?,
    val trackId: String?,
    val artistName: String?,
    val collectionName: String?,
    val trackName: String?,
    val trackExplicitness: String?,
    val collectionCensoredName: String?,
    val trackCensoredName: String?,
    val artistViewUrl: String?,
    val collectionViewUrl: String?,
    val trackViewUrl: String?,
    val previewUrl: String?,
    val artworkUrl100: String?,
    val collectionPrice: String?,
    val trackPrice: String?,
    val releaseDate: String?,
    val collectionExplicitness: String?,
    val discCount: String?,
    val discNumber: String?,
    val trackCount: String?,
    val trackNumber: String?,
    val trackTimeMillis: String?,
    val country: String?,
    val currency: String?,
    val primaryGenreName: String?,
    val isStreamable: String?,
    val imageView: ImageView?
)