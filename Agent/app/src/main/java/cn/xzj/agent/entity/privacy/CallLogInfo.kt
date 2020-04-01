package cn.xzj.agent.entity.privacy

/**
 * 通话记录
 * @param duration 通话时长
 * @param name 通话记录姓名
 * @param date 通话时间
 * @param type 通话类型
 * @param number 号码
 */
class CallLogInfo(var duration:Long
                  , var name:String
                  , var date:Long
                  , var type:Int
                  , var number:String){
//    /** Call log type for incoming calls.  */
//    val INCOMING_TYPE = 1
//    /** Call log type for outgoing calls.  */
//    val OUTGOING_TYPE = 2
//    /** Call log type for missed calls.  */
//    val MISSED_TYPE = 3
//    /** Call log type for voicemails.  */
//    val VOICEMAIL_TYPE = 4
//    /** Call log type for calls rejected by direct user action.  */
//    val REJECTED_TYPE = 5
//    /** Call log type for calls blocked automatically.  */
//    val BLOCKED_TYPE = 6
//    /**
//     * Call log type for a call which was answered on another device.  Used in situations where
//     * a call rings on multiple devices simultaneously and it ended up being answered on a
//     * device other than the current one.
//     */
//    val ANSWERED_EXTERNALLY_TYPE = 7

    override fun toString(): String {
        return "name:$name,number:$number,date:$date,duration:$duration,type:$type"
    }
}