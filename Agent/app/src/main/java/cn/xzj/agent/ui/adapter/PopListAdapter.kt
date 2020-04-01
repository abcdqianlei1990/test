package cn.xzj.agent.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cn.xzj.agent.R
import cn.xzj.agent.entity.task.TaskDateInfo
import cn.xzj.agent.i.OnRecyclerViewItemClickListener

class PopListAdapter(var context:Context, var list:List<TaskDateInfo>): RecyclerView.Adapter<PopListAdapter.Holder>() {
    private var listener:OnRecyclerViewItemClickListener ?= null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopListAdapter.Holder {
        var view = LayoutInflater.from(context).inflate(R.layout.item_single_tv,parent,false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: PopListAdapter.Holder, position: Int) {
        val info = list[position]
        holder.tv.text = info.title
        if (position == list.size -1) holder.divider.visibility = View.INVISIBLE else holder.divider.visibility = View.VISIBLE
        holder.itemView.setOnClickListener {
            if (listener != null) listener!!.onClick(holder.itemView,position)
        }
    }

    class Holder(itemView:View):RecyclerView.ViewHolder(itemView){
        var tv = itemView.findViewById<TextView>(R.id.item_single_tv_content)
        var divider = itemView.findViewById<TextView>(R.id.item_single_tv_divider)
    }

    fun setOnRecyclerViewItemClickListener(listener: OnRecyclerViewItemClickListener){
        this.listener = listener
    }
}