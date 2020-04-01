package cn.xzj.agent.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cn.xzj.agent.R
import cn.xzj.agent.entity.job.JobTypeFilterInfo
import cn.xzj.agent.i.OnRecyclerViewItemClickListener

class FilterConditionAdapter(var context:Context, var list:List<JobTypeFilterInfo>, private var defaultSelection:String?): RecyclerView.Adapter<FilterConditionAdapter.Holder>() {
    private var listener:OnRecyclerViewItemClickListener ?= null
//    private var defaultSelection:Int ?= null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterConditionAdapter.Holder {
        var view = LayoutInflater.from(context).inflate(R.layout.item_filter_conditions,parent,false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: FilterConditionAdapter.Holder, position: Int) {
        val info = list[position]
        holder.contentTv.text = info.desc
        if (defaultSelection != null && defaultSelection!!.equals(info.type)){
            holder.contentTv.setTextColor(context.resources.getColor(R.color.green29AC3E))
        }else{
            holder.contentTv.setTextColor(context.resources.getColor(R.color.black333333))
        }
        if (position == list.size -1) holder.lineTv.visibility = View.INVISIBLE else holder.lineTv.visibility = View.VISIBLE
        holder.itemView.setOnClickListener {
            if (listener != null) listener!!.onClick(holder.itemView,position)
        }
    }

    class Holder(itemView:View):RecyclerView.ViewHolder(itemView){
        var contentTv = itemView.findViewById<TextView>(R.id.item_filter_condition_content_tv)
        var lineTv = itemView.findViewById<TextView>(R.id.item_filter_condition_line_tv)
    }

    fun setOnRecyclerViewItemClickListener(listener: OnRecyclerViewItemClickListener){
        this.listener = listener
    }
}