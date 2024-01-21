package com.example.biletzone.models.requests

import android.os.Parcel
import android.os.Parcelable

data class RegisterData(
    val firstName: String?,
    val lastName: String?,
    val email: String?,
    val phoneNumber: String?,
    val password: String?,
    val role: String?,
    val isNotificationsEnabled: Boolean
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(firstName)
        parcel.writeString(lastName)
        parcel.writeString(email)
        parcel.writeString(phoneNumber)
        parcel.writeString(password)
        parcel.writeString(role)
        parcel.writeByte(if (isNotificationsEnabled) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RegisterData> {
        override fun createFromParcel(parcel: Parcel): RegisterData {
            return RegisterData(parcel)
        }

        override fun newArray(size: Int): Array<RegisterData?> {
            return arrayOfNulls(size)
        }
    }
}

