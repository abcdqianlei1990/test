package cn.xzj.agent.entity.agentinfo

import android.os.Parcel
import android.os.Parcelable

class ImgSelectInfo(var selected:Boolean,var url:String):Parcelable{
    constructor(parcel: Parcel) : this(
            parcel.readByte() != 0.toByte(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (selected) 1 else 0)
        parcel.writeString(url)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ImgSelectInfo> {
        override fun createFromParcel(parcel: Parcel): ImgSelectInfo {
            return ImgSelectInfo(parcel)
        }

        override fun newArray(size: Int): Array<ImgSelectInfo?> {
            return arrayOfNulls(size)
        }
    }

}