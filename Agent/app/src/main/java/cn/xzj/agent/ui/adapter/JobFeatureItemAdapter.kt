package cn.xzj.agent.ui.adapter

import android.content.Context
import android.widget.TextView
import cn.xzj.agent.R
import cn.xzj.agent.entity.job.JobFeature
import cn.xzj.agent.ui.adapter.common.CommonListAdapter
import cn.xzj.agent.ui.adapter.common.CommonViewHolder

/**
 *
 * @ Author MarkYe
 * @ Email yrmao9893@163.com
 * @ Date 2018/9/18
 * @ Des
 */
class JobFeatureItemAdapter(var context: Context) : CommonListAdapter<JobFeature.FeaturesBean>(context, R.layout.item_job_feature_item) {

    private var selectedPosition = -1
    override fun convert(viewHolder: CommonViewHolder?, item: JobFeature.FeaturesBean, position: Int) {
        viewHolder!!.setText(R.id.tv_value, item.name)
        if (selectedPosition==position){
            viewHolder.getView<TextView>(R.id.tv_value).setTextColor(viewHolder.itemView.context.resources.getColor(R.color.white))
            viewHolder.getView<TextView>(R.id.tv_value).setBackgroundResource(R.drawable.shape_job_feature_item_selected)
        }else{
            viewHolder.getView<TextView>(R.id.tv_value).setTextColor(viewHolder.itemView.context.resources.getColor(R.color.black808080))
            viewHolder.getView<TextView>(R.id.tv_value).setBackgroundResource(R.drawable.shape_job_feature_item_unselected)

        }
    }

    fun setSelectedPosition(position: Int) {
        this.selectedPosition = position
        notifyDataSetChanged()
    }
    fun getSelectedPosition():Int{
        return selectedPosition
    }
}