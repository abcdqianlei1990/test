package cn.xzj.agent.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cn.xzj.agent.R

class SingleSelectorAdapter(var context:Context, var list:List<String>,var recyclerView: RecyclerView): RecyclerView.Adapter<SingleSelectorAdapter.Holder>() {
    private var selection:String ?= null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleSelectorAdapter.Holder {
        var view = LayoutInflater.from(context).inflate(R.layout.item_pop_selector,parent,false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: SingleSelectorAdapter.Holder, position: Int) {
        val info = list[position]
        holder.tv.text = info
        if (info == selection){
            holder.tv.setTextColor(context.resources.getColor(R.color.green29AC3E))
        }else{
            holder.tv.setTextColor(context.resources.getColor(R.color.black333333))
        }
        if (position == list.size-1) holder.line.visibility = View.INVISIBLE else holder.line.visibility = View.VISIBLE
        holder.itemView.setOnClickListener {
            var str = list[position]
            clearStatus()
            if (str == selection){
                selection = null
                holder.tv.setTextColor(context.resources.getColor(R.color.black333333))
            }else{
                selection = str
                holder.tv.setTextColor(context.resources.getColor(R.color.green29AC3E))
            }
        }
    }

    class Holder(itemView:View):RecyclerView.ViewHolder(itemView){
        var tv = itemView.findViewById<TextView>(R.id.item_selector_tv)
        var line = itemView.findViewById<TextView>(R.id.item_selector_line)
    }

    fun setDefaultSelection(selection:String?){
        this.selection = selection
    }

    fun getSelection():String?{
        return selection
    }

    private fun clearStatus(){
        for (i in 0 until itemCount){
            val holder = recyclerView.findViewHolderForLayoutPosition(i) as Holder
            holder.tv.setTextColor(context.resources.getColor(R.color.black333333))
        }
    }
}