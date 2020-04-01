package cn.xzj.agent.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cn.xzj.agent.R
import cn.xzj.agent.entity.CityInfo
import cn.xzj.agent.i.OnRecyclerViewItemClickListener

class CityFilterConditionAdapter(var context:Context, var list:List<CityInfo>, private var defaultSelection:CityInfo?): RecyclerView.Adapter<CityFilterConditionAdapter.Holder>() {
    private var listener:OnRecyclerViewItemClickListener ?= null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityFilterConditionAdapter.Holder {
        var view = LayoutInflater.from(context).inflate(R.layout.item_filter_conditions,parent,false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CityFilterConditionAdapter.Holder, position: Int) {
        val info = list[position]
        holder.contentTv.text = info.name
        if (defaultSelection != null && defaultSelection!!.id == info.id){
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