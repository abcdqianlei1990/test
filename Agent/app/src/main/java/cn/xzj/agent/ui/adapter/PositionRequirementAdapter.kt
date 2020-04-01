package cn.xzj.agent.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cn.xzj.agent.R
import cn.xzj.agent.constants.EnumValue
import cn.xzj.agent.entity.job.RecruitmentNeedsInfo
import cn.xzj.agent.i.OnRecyclerViewItemClickListener
import com.channey.utils.FormatUtils
import com.channey.utils.StringUtils
import me.codeboy.android.aligntextview.AlignTextView

class PositionRequirementAdapter(var context: Context, var list: List<RecruitmentNeedsInfo>) : RecyclerView.Adapter<PositionRequirementAdapter.Holder>() {
    private var listener: OnRecyclerViewItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PositionRequirementAdapter.Holder {
        var view = LayoutInflater.from(context).inflate(R.layout.item_position_requirement, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: PositionRequirementAdapter.Holder, position: Int) {
        val info = list[position]
        holder.interviewTv.text = "面试时间：" + FormatUtils.timeStamp2String(info.interviewDate.toLong(), EnumValue.DATE_FORMAT_1)
        //是否停招
        holder.stopRecruitmentTv.visibility = if (info.status == 7) View.VISIBLE else View.GONE
        //是否主推
        holder.majorTv.visibility = if (info.isMajor) View.VISIBLE else View.GONE
        holder.assembleTv.text = "集合时间：" + FormatUtils.timeStamp2String(info.assembleTime.toLong(), EnumValue.DATE_FORMAT_1)
        holder.quantityTv.text = "男女需求：${info.maleQuantity.toInt()}:${info.femaleQuantity.toInt()}"
        holder.countTv.text = "招聘人数：${info.headCount}"
        //****入职奖励start****//
        var sb = StringBuilder()

        if (info.incumbencyStatus !=null||info.namesDays!=null) {
            //出名单需在职，出名单预计天数X天   。如果没有则不显示。
//            sb.append("出名单需在职，出名单预计天数${info.namesDays}天\n")
            sb.append("<font color=\"#FF0000\">")
            if (info.incumbencyStatus==1){
                sb.append("出名单需在职，")
            }
            if (info.namesDays!=null){
                sb.append("出名单预计天数${info.namesDays}天")
            }
            sb.append("</font>")
            sb.append("<br/>")
        }
        for (i in 0 until info.returnConditions.size) {
            sb.append(info.returnConditions[i].condition + " 男:${info.returnConditions[i].userMaleFee}元 女:${info.returnConditions[i].userFemaleFee}元")
            if (i != info.returnConditions.size - 1) sb.append("<br/>")
        }
        holder.rewardTv.text = Html.fromHtml(sb.toString())
        //*****入职奖励end****//

        if ("HOUR".equals(info.hourlyWageUnit)) {
            holder.hourlyTv.text = "工时单价：${info.hourlyWage.toInt()}元/小时"
        } else {
            holder.hourlyTv.text = "工时单价：${info.hourlyWage.toInt()}元/天"
        }
        if ("HOUR".equals(info.platformSubsidyUnit)) {
            holder.plantformTv.text = "平台补贴：${info.platformSubsidy.toInt()}元/小时"
        } else {
            holder.plantformTv.text = "平台补贴：${info.platformSubsidy.toInt()}元/天"
        }
        if (info.platformSubsidyDeadline != null) {
            holder.platformSubsidyDeadlineTv.text = "差价&补贴截止日期：${FormatUtils.timeStamp2String(info.platformSubsidyDeadline.toLong(), EnumValue.DATE_FORMAT_3)}"
        }

        holder.itemView.setOnClickListener {
            if (listener != null) listener!!.onClick(holder.itemView, position)
        }
        //收费金额
        if (info.userMaleToll != null || info.userFemaleToll != null) {
            val payContentMan = StringBuffer()
            //判断有无收费
            payContentMan.append("男${info.userMaleToll.toInt()} : ")
            if (info.laborMaleToll != null) {
                payContentMan.append("代收劳务公司管理费${info.laborMaleToll.toInt()}, ")
            }
            if (info.platformMaleToll != null) {
                payContentMan.append("平台收费${info.platformMaleToll.toInt()}, ")
            }
            if (info.platformMaleSubsidyToll != null) {
                payContentMan.append("平台补贴${info.platformMaleSubsidyToll.toInt()}")
            }
            val payContentWuman = StringBuffer()
            payContentWuman.append("女${info.userFemaleToll.toInt()} : ")
            if (info.laborFemaleToll != null) {
                payContentWuman.append("代收劳务公司管理费${info.laborFemaleToll.toInt()}, ")
            }
            if (info.platformFemaleToll != null) {
                payContentWuman.append("平台收费${info.platformFemaleToll.toInt()}, ")
            }
            if (info.platformFemaleSubsidyToll != null) {
                payContentWuman.append("平台补贴${info.platformFemaleSubsidyToll.toInt()}")
            }
            holder.payMoneyManTv.text = payContentMan.toString()
            holder.payMoneyWumanTv.text = payContentWuman.toString()
            holder.payMoneyParent.visibility = View.VISIBLE
        } else {
            holder.payMoneyParent.visibility = View.GONE
        }
        //提成
        if (info.rewards != null){
            var rewardCondition = StringBuilder("小职姐提成:")
            for (i in 0 until info.rewards.size){
                var reward = info.rewards[i]
                if (!(rewardCondition.contains("正常返费") || rewardCondition.contains("小时工"))){
                    var value = StringBuilder()
                    var commissionValueMaleStr = StringUtils.doubleFormat(reward.commissionValueMale)
                    var commissionValueFemaleStr = StringUtils.doubleFormat(reward.commissionValueFemale)
                    if (StringUtils.isNotEmpty(commissionValueMaleStr)){
                        value.append("男$commissionValueMaleStr ")
                    }
                    if (StringUtils.isNotEmpty(commissionValueFemaleStr)){
                        value.append("女$commissionValueFemaleStr")
                    }
                    when(reward.commissionType){
                        "REWARD" -> {
                            rewardCondition.append("正常返费 ")
                            when(reward.commissionKey){
                                "REWARD_DAYS_PUNCH" -> rewardCondition.append("出勤打卡满${reward.condition}天 $value")
                                "REWARD_DAYS_INSERVICE" -> rewardCondition.append("在职满${reward.condition}天 $value")
                                "REWARD_DAYS_WORK" -> rewardCondition.append("工作满${reward.condition}天 $value")
                                "REWARD_HOURS_WORK" -> rewardCondition.append("出勤满${reward.condition}小时 $value")
                            }
                        }
                        "HW" -> {
                            rewardCondition.append("小时工 ")
                            when(reward.commissionKey){
                                "HW_SERVICE" -> rewardCondition.append("${StringUtils.doubleFormat(reward.commissionValueMale)}元/小时")
                            }
                        }
                    }
                }
                if (i < info.rewards.size-1) rewardCondition.append(";")
            }
            holder.rewardConditionTv.visibility = View.VISIBLE
            holder.rewardConditionTv.text = rewardCondition
        }else{
            holder.rewardConditionTv.text = ""
            holder.rewardConditionTv.visibility = View.GONE
        }

    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var interviewTv = itemView.findViewById<TextView>(R.id.item_requirement_interviewdate)
        var majorTv = itemView.findViewById<TextView>(R.id.item_requirement_major)
        var assembleTv = itemView.findViewById<TextView>(R.id.item_requirement_assembledate)
        var quantityTv = itemView.findViewById<TextView>(R.id.item_requirement_quantity)
        var countTv = itemView.findViewById<TextView>(R.id.item_requirement_requirecount)
        var rewardTv = itemView.findViewById<TextView>(R.id.item_requirement_reward)
        var hourlyTv = itemView.findViewById<TextView>(R.id.item_requirement_hourlywage)
        var plantformTv = itemView.findViewById<TextView>(R.id.item_requirement_platformSubsidy)
        var payMoneyParent = itemView.findViewById<View>(R.id.item_requirement_shoufei_monery_parent)
        var payMoneyManTv = itemView.findViewById<AlignTextView>(R.id.item_requirement_shoufei_monery_man)
        var payMoneyWumanTv = itemView.findViewById<AlignTextView>(R.id.item_requirement_shoufei_monery_wuman)
        var platformSubsidyDeadlineTv = itemView.findViewById<TextView>(R.id.item_requirement_platformSubsidyDeadline)
        val stopRecruitmentTv = itemView.findViewById<TextView>(R.id.item_requirement_stopRecruitment)
        val rewardConditionTv = itemView.findViewById<AppCompatTextView>(R.id.item_requirement_rewardCondition_tv)
    }

    fun setOnRecyclerViewItemClickListener(listener: OnRecyclerViewItemClickListener) {
        this.listener = listener
    }
}