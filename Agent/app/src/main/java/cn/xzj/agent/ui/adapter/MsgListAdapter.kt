package cn.xzj.agent.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cn.xzj.agent.R
import cn.xzj.agent.entity.agentinfo.MsgInfo
import cn.xzj.agent.constants.EnumValue
import cn.xzj.agent.i.OnRecyclerViewItemClickListener
import com.channey.utils.FormatUtils

class MsgListAdapter(var context:Context, var list:List<MsgInfo>): RecyclerView.Adapter<MsgListAdapter.Holder>() {
    private var listener: OnRecyclerViewItemClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MsgListAdapter.Holder {
        var view = LayoutInflater.from(context).inflate(R.layout.item_msg_list,parent,false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MsgListAdapter.Holder, position: Int) {
        val info = list[position]
        holder.type.text = info.taskTypeName
        holder.content.text = "用户${info.userName},电话${info.userPhone}，${info.taskDescription}"
//        if (position == list.size-1){
//            holder.line.visibility = View.INVISIBLE
//        }else{
//            holder.line.visibility = View.VISIBLE
//        }
        holder.itemView.setOnClickListener { v ->
            if (listener != null) listener!!.onClick(v,position)
         }
    }


    fun setOnItemLongClickListener(listener: OnRecyclerViewItemClickListener) {
        this.listener = listener
    }
    class Holder(itemView:View):RecyclerView.ViewHolder(itemView){
        var type = itemView.findViewById<TextView>(R.id.item_msg_type_tv)
        var date = itemView.findViewById<TextView>(R.id.item_msg_date_tv)
        var content = itemView.findViewById<TextView>(R.id.item_msg_content_tv)
        var line = itemView.findViewById<TextView>(R.id.item_msg_line_tv)
    }
}