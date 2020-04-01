package cn.xzj.agent.ui.adapter.common.decoration

import android.content.Context
import android.graphics.Rect
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View

class LineSplitDecoration(var context:Context, var splitSize:Int, var bottomMargin:Int = 0):RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
//        super.getItemOffsets(outRect, view, parent, state)
        var positon = parent.getChildLayoutPosition(view)
        //设置行间距
        if (!isFirstLine(parent,positon)){
            outRect.top = splitSize
        }
        //设置底部margin
        if (positon == parent.adapter!!.itemCount - 1){
            outRect.bottom = bottomMargin
        }
    }

    private fun isFirstLine(parent: RecyclerView,position:Int):Boolean{
        var layoutManager = parent.layoutManager
        if (layoutManager is GridLayoutManager){
            return position < layoutManager.spanCount
        }else if (layoutManager is LinearLayoutManager){
            return position == 0
        }
        return false
    }
}