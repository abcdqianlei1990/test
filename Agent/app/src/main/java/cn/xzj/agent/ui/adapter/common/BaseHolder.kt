package cn.xzj.agent.ui.adapter.common

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.View
import android.widget.TextView

/**
 *
 * @ Author yemao
 * @ Email yrmao9893@163.com
 * @ Date 2018/8/13
 * @ Des RecyclerView ViewHolder 基类
 */
class BaseHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var mContext: Context = itemView.context
    private var contentView: View = itemView
    private var idData: SparseArray<View> = SparseArray()

    @Suppress("UNCHECKED_CAST")
    fun <T : View> getView(viewId: Int): T {
        var mView: View? = idData.get(viewId)
        if (mView == null) {
            mView = contentView.findViewById(viewId)
            idData.put(viewId, mView)
        }
        return mView as T
    }

    fun setText(viewId: Int, text: CharSequence?): BaseHolder {
        val mTextView: TextView = getView(viewId)
        var txt = if (text == null) "" else text
        mTextView.text = txt
        return this
    }
    fun setVisibility(viewId: Int,visibility: Int){
        getView<View>(viewId).visibility=visibility
    }

}
