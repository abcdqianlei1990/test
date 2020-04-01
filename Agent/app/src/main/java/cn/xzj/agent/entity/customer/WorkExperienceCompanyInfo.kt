package cn.xzj.agent.entity.customer

import android.os.Parcel
import android.os.Parcelable

//applyDate (string, optional): 报名时间 ,
//id (string, optional): ID ,
//positionName (string, optional): 申请职位名称
class WorkExperienceCompanyInfo(var applyDate:String,var id:String,var positionName:String):Parcelable{
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(applyDate)
        parcel.writeString(id)
        parcel.writeString(positionName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WorkExperienceCompanyInfo> {
        override fun createFromParcel(parcel: Parcel): WorkExperienceCompanyInfo {
            return WorkExperienceCompanyInfo(parcel)
        }

        override fun newArray(size: Int): Array<WorkExperienceCompanyInfo?> {
            return arrayOfNulls(size)
        }
    }

}