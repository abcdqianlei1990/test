package cn.xzj.agent.ui.adapter

import android.content.Context
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cn.xzj.agent.R
import cn.xzj.agent.entity.task.TaskInfo
import cn.xzj.agent.constants.EnumValue
import cn.xzj.agent.i.OnRecyclerViewItemClickListener
import com.channey.utils.FormatUtils
import com.channey.utils.StringUtils

class TaskListAdapter(var context:Context, var list:List<TaskInfo>): RecyclerView.Adapter<TaskListAdapter.Holder>() {
    private var listener:OnRecyclerViewItemClickListener ?= null
    private var markClicklistener:OnRecyclerViewItemClickListener ?= null
    private var doneClicklistener:OnRecyclerViewItemClickListener ?= null
    private var phonecallClicklistener:OnRecyclerViewItemClickListener ?= null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskListAdapter.Holder {
        var view = LayoutInflater.from(context).inflate(R.layout.item_task,parent,false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: TaskListAdapter.Holder, position: Int) {
        val info = list[position]
        var nickname = ""
        if (!StringUtils.isEmpty(info.nickname)){
            nickname = info.nickname!!
        }
        var userName = ""
        if (!StringUtils.isEmpty(info!!.userName)){
            userName = "(${info!!.userName})"
        }
        var str = String.format(context.resources.getString(R.string.user_base_info),info.userPhone,nickname,userName)
        val spanned = Html.fromHtml(str)
        holder.baseInfoTv.text = spanned

        //客户等级
        if (StringUtils.isEmpty(info.userRank)){
            holder.levelImg.visibility = View.GONE
        }else{
            holder.levelImg.visibility = View.VISIBLE
            when(info.userRank){
                EnumValue.CUSTOMER_LEVEL_A -> holder.levelImg.setImageResource(R.mipmap.ic_customer_level_a)
                EnumValue.CUSTOMER_LEVEL_B -> holder.levelImg.setImageResource(R.mipmap.ic_customer_level_b)
                else -> holder.levelImg.visibility = View.GONE
            }
        }

        holder.desc.text = info.taskDescription
        var date = FormatUtils.timeStamp2String(info.startTime!!.toLong(),EnumValue.DATE_FORMAT_1)
        holder.noticeDate.text = date
        holder.markBtn.setOnClickListener {
            if (markClicklistener != null) markClicklistener!!.onClick(holder.itemView,position)
        }
        holder.doneBtn.setOnClickListener {
            if (doneClicklistener != null) doneClicklistener!!.onClick(holder.itemView,position)
        }
        holder.phoneCallBtn.setOnClickListener {
            if (phonecallClicklistener != null) phonecallClicklistener!!.onClick(holder.itemView,position)
        }
        holder.itemView.setOnClickListener {
            if (list != null) listener!!.onClick(holder.itemView,position)
        }
    }


    class Holder(itemView:View):RecyclerView.ViewHolder(itemView){
        var baseInfoTv = itemView.findViewById<TextView>(R.id.item_task_baseinfo_tv)
        var levelImg = itemView.findViewById<AppCompatImageView>(R.id.item_task_level_img)
        var desc = itemView.findViewById<TextView>(R.id.item_task_desc_tv)
        var noticeDate = itemView.findViewById<TextView>(R.id.item_task_noticedate_tv)
        var markBtn = itemView.findViewById<TextView>(R.id.item_task_mark_btn)
        var doneBtn = itemView.findViewById<TextView>(R.id.item_task_done_btn)
        var phoneCallBtn = itemView.findViewById<TextView>(R.id.item_task_phonecall_btn)
    }

    fun setOnRecyclerViewItemClickListener(listener: OnRecyclerViewItemClickListener){
        this.listener = listener
    }

    fun setOnMarkClickListener(listener:OnRecyclerViewItemClickListener){
        markClicklistener = listener
    }
    fun setOnDoneClickListener(listener:OnRecyclerViewItemClickListener){
        doneClicklistener = listener
    }
    fun setOnPhonecallClickListener(listener:OnRecyclerViewItemClickListener){
        phonecallClicklistener = listener
    }
}