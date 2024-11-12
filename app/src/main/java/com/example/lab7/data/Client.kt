package com.example.lab7.data

import android.os.Parcel
import android.os.Parcelable

data class Client(
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val price: Int = 0,
    val orders: Int = 0,
    val discounts: String = "",
    val contactInfo: String = ""
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(firstName)
        parcel.writeString(lastName)
        parcel.writeString(email)
        parcel.writeInt(price)
        parcel.writeInt(orders)
        parcel.writeString(discounts)
        parcel.writeString(contactInfo)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Client> {
        override fun createFromParcel(parcel: Parcel): Client {
            return Client(parcel)
        }

        override fun newArray(size: Int): Array<Client?> {
            return arrayOfNulls(size)
        }
    }
}
