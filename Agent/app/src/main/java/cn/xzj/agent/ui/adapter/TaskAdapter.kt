package cn.xzj.agent.ui.adapter

import android.support.v7.widget.AppCompatImageView
import android.text.Html
import android.view.View
import android.widget.TextView
import cn.xzj.agent.R
import cn.xzj.agent.constants.EnumValue
import cn.xzj.agent.entity.task.TaskInfo
import cn.xzj.agent.ui.adapter.common.BaseHolder
import cn.xzj.agent.ui.adapter.common.QuickAdapter
import com.channey.utils.FormatUtils
import com.channey.utils.StringUtils

/**
 *
 * @Author MarkYe
 * @Email yrmao9893@163.com
 * @Date 2019/3/2
 * @Des 任务列表适配器
 */
class TaskAdapter : QuickAdapter<TaskInfo>(R.layout.item_task) {
    private var markClickListener: OnNormalClickListener? = null
    private var doneClickListener: OnNormalClickListener? = null
    private var phoneCallClickListener: OnNormalClickListener? = null
    override fun convert(holder: BaseHolder, item: TaskInfo, position: Int) {
        val baseInfoTv = holder.getView<TextView>(R.id.item_task_baseinfo_tv)
        val levelImg = holder.getView<AppCompatImageView>(R.id.item_task_level_img)
        val desc = holder.getView<TextView>(R.id.item_task_desc_tv)
        val noticeDate = holder.getView<TextView>(R.id.item_task_noticedate_tv)
        val markBtn = holder.getView<TextView>(R.id.item_task_mark_btn)
        val doneBtn = holder.getView<TextView>(R.id.item_task_done_btn)
        val phoneCallBtn = holder.getView<TextView>(R.id.item_task_phonecall_btn)
        var nickname = ""
        if (!StringUtils.isEmpty(item.nickname)) {
            nickname = item.nickname!!
        }
        var userName = ""
        if (!StringUtils.isEmpty(item.userName)) {
            userName = "(${item.userName})"
        }
        val str = String.format(context!!.resources.getString(R.string.user_base_info), item.userPhone, nickname, userName)
        val spanned = Html.fromHtml(str)
        baseInfoTv.text = spanned

        //客户等级
        if (StringUtils.isEmpty(item.userRank)) {
            levelImg.visibility = View.GONE
        } else {
            levelImg.visibility = View.VISIBLE
            when (item.userRank) {
                EnumValue.CUSTOMER_LEVEL_A -> levelImg.setImageResource(R.mipmap.ic_customer_level_a)
                EnumValue.CUSTOMER_LEVEL_B -> levelImg.setImageResource(R.mipmap.ic_customer_level_b)
                else -> levelImg.visibility = View.GONE
            }
        }
        desc.text = item.taskDescription
        val date = FormatUtils.timeStamp2String(item.startTime.toLong(), EnumValue.DATE_FORMAT_1)
        noticeDate.text = date
        markBtn.setOnClickListener {
            if (markClickListener != null) markClickListener!!.onClick(holder.itemView,item, position)
        }
        doneBtn.setOnClickListener {
            if (doneClickListener != null) doneClickListener!!.onClick(holder.itemView,item, position)
        }
        phoneCallBtn.setOnClickListener {
            if (phoneCallClickListener != null) phoneCallClickListener!!.onClick(holder.itemView,item, position)
        }
    }

    fun setOnMarkClickListener(listener: OnNormalClickListener) {
        markClickListener = listener
    }

    fun setOnDoneClickListener(listener: OnNormalClickListener) {
        doneClickListener = listener
    }

    fun setOnPhonecallClickListener(listener: OnNormalClickListener) {
        phoneCallClickListener = listener
    }

    public interface OnNormalClickListener {
        fun onClick(view: View, taskInfo: TaskInfo,position:Int)
    }
}