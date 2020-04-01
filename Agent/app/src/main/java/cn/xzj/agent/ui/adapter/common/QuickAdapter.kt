package cn.xzj.agent.ui.adapter.common

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 *
 * @ Author yemao
 * @ Email yrmao9893@163.com
 * @ Date 2018/8/13
 * @ Des
 */
abstract class QuickAdapter<T>(mLayoutId: Int) : RecyclerView.Adapter<BaseHolder>() {
    var data: MutableList<T>? = null
    var layoutId: Int = mLayoutId
    var context: Context? = null
    var mOnItemClickListener: OnItemClickListener<T>? = null
    var mOnItemLongClickListener: OnItemLongClickListener<T>? = null

    init {
        data = ArrayList<T>()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder {
        context = parent.context
        val holder = BaseHolder(LayoutInflater.from(parent.context).inflate(layoutId, parent,false))
        return holder
    }

    override fun getItemCount(): Int {
        if (data == null)
            return 0
        return data!!.size
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        convert(holder, data!![position], position)
        try {
            setListener(holder, position)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun setNewData(list: List<T>) {
        data!!.clear()
        data!!.addAll(list)
        notifyDataSetChanged()
    }

    fun addItem(item: T) {
        data!!.add(item)
    }

    fun addAll(list: List<T>) {
        data!!.addAll(list)
    }
    fun remove(position: Int){
        data!!.removeAt(position)
    }

    fun clearData() {
        data!!.clear()
        notifyDataSetChanged()
    }

    private fun setListener(holder: BaseHolder, position: Int) {
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(object : View.OnClickListener {
                override fun onClick(p0: View?) {
                    mOnItemClickListener!!.onItemClick(p0!!, data!![position], position)
                }
            })
        }
        if (mOnItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(object : View.OnLongClickListener {
                override fun onLongClick(p0: View?): Boolean {
                    mOnItemLongClickListener!!.onItemLongClick(p0!!, data!![position], position)
                    return true
                }
            })
        }
    }

    abstract fun convert(holder: BaseHolder, item: T, position: Int)
    fun setOnItemClickListener(listener: OnItemClickListener<T>) {
        this.mOnItemClickListener = listener
    }

    fun setOnItemLongClickListener(listener: OnItemLongClickListener<T>) {
        this.mOnItemLongClickListener = listener
    }

    interface OnItemClickListener<T> {
        fun onItemClick(view: View, itemData: T, i: Int)
    }

    interface OnItemLongClickListener<T> {
        fun onItemLongClick(view: View, itemData: T, i: Int)
    }
}

