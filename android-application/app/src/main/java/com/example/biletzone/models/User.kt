package com.example.biletzone.models

import android.os.Parcel
import android.os.Parcelable

data class User(
    val uuid: String?,
    val email: String?,
    val password: String?,
    val role: String?,
    val confirmationToken: String?,
    val isConfirmed: Boolean,
    val isNotificationsEnabled: Boolean
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(uuid)
        parcel.writeString(email)
        parcel.writeString(password)
        parcel.writeString(role)
        parcel.writeString(confirmationToken)
        parcel.writeByte(if (isConfirmed) 1 else 0)
        parcel.writeByte(if (isNotificationsEnabled) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}
