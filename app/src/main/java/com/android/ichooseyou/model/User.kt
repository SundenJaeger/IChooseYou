package com.android.ichooseyou.model

import android.os.Parcel
import android.os.Parcelable

class User : Parcelable {
    var name: String?
    var password: String?
    var email: String?
    var profileImageUri // Add this to store the profile image URI
            : String?

    constructor(name: String?, password: String?, email: String?) {
        this.name = name
        this.password = password
        this.email = email
        profileImageUri = null // Default to no image
    }

    protected constructor(`in`: Parcel) {
        name = `in`.readString()
        password = `in`.readString()
        email = `in`.readString()
        profileImageUri = `in`.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(name)
        dest.writeString(password)
        dest.writeString(email)
        dest.writeString(profileImageUri)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<User?> = object : Parcelable.Creator<User?> {
            override fun createFromParcel(source: Parcel): User {
                return User(source)
            }

            override fun newArray(size: Int): Array<User?> {
                return arrayOfNulls(size)
            }
        }
    }
}