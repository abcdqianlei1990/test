package cn.xzj.agent.ui.adapter

import android.text.Html
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import cn.xzj.agent.R
import cn.xzj.agent.constants.EnumValue
import cn.xzj.agent.entity.job.JobInfo
import cn.xzj.agent.ui.adapter.common.BaseHolder
import cn.xzj.agent.ui.adapter.common.QuickAdapter
import com.channey.utils.FormatUtils
import com.channey.utils.StringUtils

/**
 *
 * @Author MarkYe
 * @Email yrmao9893@163.com
 * @Date 2018/12/28
 * @Des 岗位列表adapter
 */
class JobListAdapter : QuickAdapter<JobInfo>(R.layout.item_job_list) {
    override fun convert(holder: BaseHolder, item: JobInfo, position: Int) {
        holder.setText(R.id.item_job_list_name_tv, item.positionName)
        if (item.positionTypes != null) {
            var mFeature = ""
            for (tagFeature in item.positionTypes) {
                mFeature += if (TextUtils.isEmpty(mFeature)) tagFeature
                else
                    ",$tagFeature"
            }
            holder.setText(R.id.item_job_list_zx_flag_tv, mFeature)
            if (item.positionTypes.isEmpty()) {
                holder.getView<TextView>(R.id.item_job_list_zx_flag_tv).visibility = View.INVISIBLE
            } else {
                holder.getView<TextView>(R.id.item_job_list_zx_flag_tv).visibility = View.VISIBLE
            }
        } else {
            holder.getView<TextView>(R.id.item_job_list_zx_flag_tv).visibility = View.INVISIBLE
        }
        holder.setText(R.id.item_job_list_salary_tv, "综合工资：" + "${item.minSalary.toInt()}-${item.maxSalary.toInt()}元")

        if (item.recruitmentNeeds.isNotEmpty()){
            val recuimentNeedInfo = item.recruitmentNeeds[0]
            //入职奖励
            var userMaleFee = StringUtils.doubleFormat(item.recruitmentNeeds[0].userMaleFee)
            var userFemaleFee = StringUtils.doubleFormat(item.recruitmentNeeds[0].userFemaleFee)
            holder.setText(R.id.item_job_list_reward_tv, "入职奖励：男-$userMaleFee 女-$userFemaleFee")
            holder.getView<View>(R.id.item_job_list_reward_tv).visibility=View.VISIBLE

            holder.setText(R.id.item_job_list_interviewdate_tv, "面试时间：" + FormatUtils.timeStamp2String(recuimentNeedInfo.interviewDate.toLong(), EnumValue.DATE_FORMAT_1))
            if (recuimentNeedInfo.userMaleToll>0||recuimentNeedInfo.userFemaleToll>0){
                holder.setText(R.id.item_job_list_toll_tv,"收费金额：男${recuimentNeedInfo.userMaleToll.toInt()} 女${recuimentNeedInfo.userFemaleToll.toInt()}")
                holder.getView<View>(R.id.item_job_list_toll_tv).visibility=View.VISIBLE
            }else{
                holder.getView<View>(R.id.item_job_list_toll_tv).visibility=View.GONE
            }
        }

//        holder.assembleDateTv.text = "集合时间：" + FormatUtils.timeStamp2String(recuimentNeedInfo.assembleTime.toLong(), EnumValue.DATE_FORMAT_1)
        holder.setText(R.id.item_job_list_city_tv, "${item.city}")
        val format = String.format(context!!.resources.getString(R.string.applied_count), item.applyCount)
        var spanned = Html.fromHtml(format)
        holder.getView<TextView>(R.id.item_job_list_count_tv).text = spanned
        //设置背景色，标识可预报名 无返费代表可预报名
        try {
            var configuredReturnFee = false
            for (recruitmentNeedItem in item.recruitmentNeeds) {
                if (recruitmentNeedItem.isConfiguredReturnFee) {
                    configuredReturnFee = true
                }
            }
            if (!configuredReturnFee) {
                holder.itemView.setBackgroundResource(R.color.greenC4E8CA)
            } else {
                holder.itemView.setBackgroundResource(R.color.white)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (item.applied) {
            holder.itemView.setBackgroundResource(R.color.commonBackground)
        } else {
            holder.itemView.setBackgroundResource(R.color.white)
        }

    }
}