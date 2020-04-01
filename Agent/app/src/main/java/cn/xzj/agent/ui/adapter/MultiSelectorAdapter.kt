package cn.xzj.agent.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cn.xzj.agent.R

class MultiSelectorAdapter(var context:Context, var list:List<String>): RecyclerView.Adapter<MultiSelectorAdapter.Holder>() {
    private var selection = ArrayList<String>()
//    private var defaultSelection = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultiSelectorAdapter.Holder {
        var view = LayoutInflater.from(context).inflate(R.layout.item_pop_selector,parent,false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MultiSelectorAdapter.Holder, position: Int) {
        val info = list[position]
        holder.tv.text = info
        if (selection.contains(info)){
            holder.tv.setTextColor(context.resources.getColor(R.color.green29AC3E))
        }else{
            holder.tv.setTextColor(context.resources.getColor(R.color.black333333))
        }
        if (position == list.size-1) holder.line.visibility = View.INVISIBLE else holder.line.visibility = View.VISIBLE
        holder.itemView.setOnClickListener {
            var str = list[position]
            if (selection.contains(str)){
                selection.remove(str)
                holder.tv.setTextColor(context.resources.getColor(R.color.black333333))
            }else{
                selection.add(str)
                holder.tv.setTextColor(context.resources.getColor(R.color.green29AC3E))
            }
        }
    }

    class Holder(itemView:View):RecyclerView.ViewHolder(itemView){
        var tv = itemView.findViewById<TextView>(R.id.item_selector_tv)
        var line = itemView.findViewById<TextView>(R.id.item_selector_line)
    }

    fun getSelection():List<String>{
        return selection
    }

    fun setDefaultSelections(defaultSelection:List<String>){
        this.selection.addAll(defaultSelection)
    }
}