package cn.xzj.agent.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import cn.xzj.agent.R
import cn.xzj.agent.entity.RobotChatInfo
import cn.xzj.agent.ui.adapter.common.BaseHolder

/**
 *
 * @Author MarkYe
 * @Email yrmao9893@163.com
 * @Date 2018/12/18
 * @Des
 */
class RobotChatAdapter(var context: Context, var data: ArrayList<RobotChatInfo>) : RecyclerView.Adapter<BaseHolder>() {
    private val CHAT_TYPE_1 = 1001
    private val CHAT_TYPE_2 = 1002

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder {
        val mBaseHolder: BaseHolder
        if (CHAT_TYPE_1 == viewType) {
            mBaseHolder = BaseHolder(LayoutInflater.from(context).inflate(R.layout.item_robot_chat_type2, null, false))
        } else {
            mBaseHolder = BaseHolder(LayoutInflater.from(context).inflate(R.layout.item_robot_chat_type1, null, false))
        }
        return mBaseHolder
    }

    override fun getItemViewType(position: Int): Int {
        if (data[position].objectType == 0) {
            return CHAT_TYPE_1
        } else {
            return CHAT_TYPE_2
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        holder.setText(R.id.tvRobotChatContent, Html.fromHtml(data[position].content.trim()))
        if (data[position].objectType == 0) {
            //自己显示头像
        }
    }
}