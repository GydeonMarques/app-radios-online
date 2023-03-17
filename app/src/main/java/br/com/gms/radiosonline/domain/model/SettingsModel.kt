package br.com.encoding.dreams.radios.online.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SettingsModel(
    val contactEmail: String,
    val supportEmail: String,
    val freeRadioStationsLimit: Int,
    val premiumVersionUnlockPrice: Double,
    val premiumVersionUnlockMessage: String,
) : Parcelable