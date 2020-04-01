package cn.xzj.agent.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cn.xzj.agent.R
import cn.xzj.agent.i.OnRecyclerViewItemClickListener

class IndexTabsAdapter(var context:Context, var list:List<String>): RecyclerView.Adapter<IndexTabsAdapter.Holder>() {
    private var listener:OnRecyclerViewItemClickListener ?= null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndexTabsAdapter.Holder {
        var view = LayoutInflater.from(context).inflate(R.layout.item_index_tabs,parent,false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: IndexTabsAdapter.Holder, position: Int) {
        val info = list[position]
        holder.tv.text = info
        holder.itemView.setOnClickListener {
            if (listener != null) listener!!.onClick(holder.itemView,position)
        }
    }

    class Holder(itemView:View):RecyclerView.ViewHolder(itemView){
        var tv = itemView.findViewById<TextView>(R.id.item_index_tabs_tv)
    }

    fun setOnRecyclerViewItemClickListener(listener: OnRecyclerViewItemClickListener){
        this.listener = listener
    }
}