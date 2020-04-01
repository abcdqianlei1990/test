package cn.xzj.agent.entity.task

import android.os.Parcel
import android.os.Parcelable

//{
//    agentId (string, optional): 经纪人ID ,
//    agentName (string, optional): 经纪人名 ,
//    completeTime (string, optional): 完成时间 ,
//    createTime (string, optional): 创建时间 ,
//    dueTime (string, optional): 到期时间 ,
//    startTime (string, optional): 开始时间 ,
//    status (string, optional): 任务状态: 0-待处理，19-已完成，29-已关闭 ,
//    taskDescription (string, optional): 任务描述 ,
//    taskId (string, optional): 任务ID ,
//    taskNo (string, optional): 任务编号 ,
//    taskTypeId (string, optional): 任务类型ID ,
//    taskTypeName (string, optional): 任务类型名 ,
//    updateTime (string, optional): 更新时间 ,
//    userId (string, optional): 客户ID ,
//    userName (string, optional): 客户名 ,
//    nickname (string, optional): 客户常用名 ,
//    userPhone (string, optional): 客户手机
//    userRank (string, optional): 客户等级
//}
data class TaskInfo(var agentId:String
                    ,var agentName:String
                    ,var completeTime:String?
                    ,var createTime:String?
                    ,var dueTime:String?
                    ,var startTime:String
                    ,var status:String
                    ,var taskDescription:String
                    ,var taskId:String
                    ,var taskNo:String
                    ,var taskTypeId:String
                    ,var taskTypeName:String
                    ,var updateTime:String?
                    ,var userId:String
                    ,var userName:String?
                    ,var nickname:String?
                    ,var userPhone:String
                    ,var userRank:String?):Parcelable{
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(agentId)
        parcel.writeString(agentName)
        parcel.writeString(completeTime)
        parcel.writeString(createTime)
        parcel.writeString(dueTime)
        parcel.writeString(startTime)
        parcel.writeString(status)
        parcel.writeString(taskDescription)
        parcel.writeString(taskId)
        parcel.writeString(taskNo)
        parcel.writeString(taskTypeId)
        parcel.writeString(taskTypeName)
        parcel.writeString(updateTime)
        parcel.writeString(userId)
        parcel.writeString(userName)
        parcel.writeString(nickname)
        parcel.writeString(userPhone)
        parcel.writeString(userRank)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TaskInfo> {
        override fun createFromParcel(parcel: Parcel): TaskInfo {
            return TaskInfo(parcel)
        }

        override fun newArray(size: Int): Array<TaskInfo?> {
            return arrayOfNulls(size)
        }
    }

}