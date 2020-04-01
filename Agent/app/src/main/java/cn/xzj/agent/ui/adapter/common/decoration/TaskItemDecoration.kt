package cn.xzj.agent.ui.adapter.common.decoration

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

class TaskItemDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        var itemCount = parent!!.adapter!!.itemCount
        var position = parent!!.getChildAdapterPosition(view)
        if (position != itemCount - 1) {
            outRect!!.bottom = 10
        }
    }
}