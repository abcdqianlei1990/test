package cn.xzj.agent.i

import android.view.View

/**
 * Created by channey on 2018/3/22.
 */
interface  DateSelectedListener {
    fun onSelected(year:String,month:String,day:String,hour:String,min:String)
}