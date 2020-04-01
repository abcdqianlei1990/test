package cn.xzj.agent.ui.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.EditText
import android.widget.TextView
import cn.xzj.agent.R
import cn.xzj.agent.constants.Code
import cn.xzj.agent.constants.Constants
import cn.xzj.agent.constants.EnumValue
import cn.xzj.agent.constants.Keys
import cn.xzj.agent.core.common.mvp.MVPBaseFragment
import cn.xzj.agent.entity.CommonListBody
import cn.xzj.agent.entity.customer.CustomerDetailInfo
import cn.xzj.agent.entity.task.*
import cn.xzj.agent.iview.ITaskListView
import cn.xzj.agent.presenter.TaskListPresenter
import cn.xzj.agent.ui.adapter.TaskAdapter
import cn.xzj.agent.ui.adapter.common.QuickAdapter
import cn.xzj.agent.ui.adapter.common.decoration.TaskItemDecoration
import cn.xzj.agent.ui.customer.CustomerDetailActivity
import cn.xzj.agent.ui.customer.remark.NoteRemarkActivity
import cn.xzj.agent.ui.customer.remark.RemarkActivity
import cn.xzj.agent.util.CommonUtils
import cn.xzj.agent.util.SharedPreferencesUtil
import cn.xzj.agent.widget.CommonDialog
import cn.xzj.agent.widget.SimpleToast
import kotlinx.android.synthetic.main.fragment_task_list.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class TaskListFragment : MVPBaseFragment<TaskListPresenter>(), View.OnClickListener, ITaskListView {

    private lateinit var mAdapter: TaskAdapter
    private var mCurrentPage = 1
    private var mTaskTypeId: String? = null
    private val ACTION_PHONECALL = 0
    private val ACTION_GOTO_DETAIL = 1
    private var mContactStatus: Int = TasksRequestBody2.CONTACT_STATUS_0
    private val COMMUNICATION_TYPE_PHONE = 0
    private val COMMUNICATION_TYPE_WX = 1
    private val COMMUNICATION_TYPE_TEL = 3

    private lateinit var mTaskDateInfo: TaskDateInfo
    private lateinit var typeInfo: TaskTypeInfo

    override fun context(): Context {
        return context!!
    }

    override fun initLayout(): Int {
        return R.layout.fragment_task_list
    }

    override fun initParams() {
    }

    override fun initViews() {
        statusLayout.setOnRetryListener {
            smartRefreshLayout.autoRefresh()
        }
        mAdapter = TaskAdapter()
        fragment_task_list_recyclerview.layoutManager = LinearLayoutManager(context)
        fragment_task_list_recyclerview.adapter = mAdapter
        fragment_task_list_recyclerview.addItemDecoration(TaskItemDecoration())
        mAdapter.setOnItemClickListener(object : QuickAdapter.OnItemClickListener<TaskInfo> {
            override fun onItemClick(view: View, itemData: TaskInfo, i: Int) {
                getCustomerDetailInfo(i, ACTION_GOTO_DETAIL)
                CommonUtils.statistics(context, Constants.STATISTICS_TASK_customerDetail_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
            }
        })

        mAdapter.setOnMarkClickListener(object : TaskAdapter.OnNormalClickListener {
            override fun onClick(view: View, taskInfo: TaskInfo, position: Int) {
                gotoRemark(mAdapter.data!![position].userId)
                CommonUtils.statistics(context, Constants.STATISTICS_TASK_historyNote_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
            }
        })
        mAdapter.setOnDoneClickListener(object : TaskAdapter.OnNormalClickListener {
            override fun onClick(view: View, taskInfo: TaskInfo, position: Int) {
                gotoNoteRemarkActivity(mAdapter.data!![position])
//                if (taskInfo.taskTypeId == EnumValue.TASK_TYPE_REGIST) {
//                    //新注册任务
//                    showCompleteNoticeDialog(taskInfo)
//                } else {
//                    //普通任务
//                    showCommonCompleteNoticeDialog(taskInfo)
//                }
            }
        })
        mAdapter.setOnPhonecallClickListener(object : TaskAdapter.OnNormalClickListener {
            override fun onClick(view: View, taskInfo: TaskInfo, position: Int) {
                getCustomerDetailInfo(position, ACTION_PHONECALL)
                CommonUtils.statistics(context, Constants.STATISTICS_TASK_CALL_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)

            }
        })
        smartRefreshLayout.setOnRefreshListener {
            mCurrentPage = 1
            getTasksInPeriod()
        }
        smartRefreshLayout.setOnLoadMoreListener {
            mCurrentPage++
            getTasksInPeriod()
        }
    }

    private fun gotoNoteRemarkActivity(taskInfo: TaskInfo?) {
        var intent = Intent(context, NoteRemarkActivity::class.java)
        if (taskInfo != null) {
            intent.putExtra(NoteRemarkActivity.key_taskInfo,taskInfo)
            intent.putExtra(NoteRemarkActivity.key_userId,taskInfo.userId)
        }
        startActivityForResult(intent,Code.RequestCode.NoteRemarkActivity)
    }

    override fun initData() {
    }

    override fun onResume() {
        super.onResume()
        if (arguments != null) {
            typeInfo = arguments!!.getSerializable(HomeFragment.TASK_TYPE) as TaskTypeInfo
            mTaskDateInfo = arguments!!.getSerializable(HomeFragment.TASK_DATE_INFO) as TaskDateInfo
            mTaskTypeId = typeInfo.id
            if (mTaskTypeId == EnumValue.TASK_TYPE_REGIST) {
                if (fragment_task_list_contactstatus_group.visibility != View.VISIBLE)
                    fragment_task_list_contactstatus_group.visibility = View.VISIBLE
            }
            getTasksInPeriod()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun onDefaultEvent(event: TaskListFragmentEventMessage) {
        mTaskDateInfo = event.taskDateInfo
        mTaskTypeId = typeInfo.id
        getTasksInPeriod()
    }


    override fun setListeners() {
        fragment_task_list_contactnotyet_btn.setOnClickListener(this)
        fragment_task_list_notcontact_btn.setOnClickListener(this)
    }

    private fun getCustomerDetailInfo(position: Int, action: Int) {
        val task = mAdapter.data!![position]
        val map = HashMap<String, String>()
        map.put("task_type_id", task.taskTypeId)
        map.put("user_id", task.userId)
        mPresenter.getCustomerDetailInfo(map, task.userPhone, action)
    }


    private fun loadingComplete() {
        if (mCurrentPage == 1) {
            smartRefreshLayout.finishRefresh()
        } else {
            smartRefreshLayout.finishLoadMore()
        }
    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.fragment_task_list_contactnotyet_btn -> {
                invalidateContactStatusUI(R.id.fragment_task_list_contactnotyet_btn)
            }
            R.id.fragment_task_list_notcontact_btn -> {
                invalidateContactStatusUI(R.id.fragment_task_list_notcontact_btn)
            }
        }
    }

    private fun invalidateContactStatusUI(viewId: Int) {
        when (viewId) {
            R.id.fragment_task_list_contactnotyet_btn -> {
                fragment_task_list_contactnotyet_btn.setTextColor(resources.getColor(R.color.white))
                fragment_task_list_notcontact_btn.setTextColor(resources.getColor(R.color.black333333))
                fragment_task_list_contactnotyet_btn.setBackgroundResource(R.drawable.bg_green_solid)
                fragment_task_list_notcontact_btn.setBackgroundResource(R.drawable.bg_gray_solid)
                mContactStatus = TasksRequestBody2.CONTACT_STATUS_0
            }
            R.id.fragment_task_list_notcontact_btn -> {
                fragment_task_list_notcontact_btn.setTextColor(resources.getColor(R.color.white))
                fragment_task_list_contactnotyet_btn.setTextColor(resources.getColor(R.color.black333333))
                fragment_task_list_notcontact_btn.setBackgroundResource(R.drawable.bg_green_solid)
                fragment_task_list_contactnotyet_btn.setBackgroundResource(R.drawable.bg_gray_solid)
                mContactStatus = TasksRequestBody2.CONTACT_STATUS_1
            }
        }
        getTasksInPeriod()
    }

    override fun onTasksGetSuccess(content: CommonListBody<TaskInfo>) {
        if (mCurrentPage == 1) {
            mAdapter.setNewData(content.items)
        } else {
            mAdapter.addAll(content.items)
            mAdapter.notifyDataSetChanged()
        }
        if (mAdapter.data!!.isEmpty()) {
            statusLayout.showEmpty()
        } else {
            statusLayout.showContent()
        }
        loadingComplete()
        smartRefreshLayout.setNoMoreData(content.totalCount <= mAdapter.data!!.size)
    }

    override fun onTasksGetFailure(s: String) {
        statusLayout.showLoadError()
        loadingComplete()
    }

    override fun makeTaskDoneSuccess(content: Boolean) {
        smartRefreshLayout.autoRefresh()
        EventBus.getDefault().post(HomeRefreshTaskMessage())
    }

    override fun makeTaskDoneFailure() {
    }

    fun showCommonCompleteNoticeDialog(taskInfo: TaskInfo) {
        val mDialog = CommonDialog.newBuilder(context)
                .setTitle("确认完成任务？")
                .setView(R.layout.dialog_task_normal_done_notice)
                .setPositiveButton("完成任务") { dialog ->
                    val tvPhone = dialog.getView<TextView>(R.id.tv_phone)
                    val tvWx = dialog.getView<TextView>(R.id.tv_wx)
                    val tvTel = dialog.getView<TextView>(R.id.tv_tel)
                    if (!tvPhone.isSelected && !tvWx.isSelected && !tvTel.isSelected) {
                        SimpleToast.showNormal("请选择沟通方式")
                        return@setPositiveButton
                    }
                    val mEtNote = dialog.getView<EditText>(R.id.et_note)
                    if (mEtNote.text.toString().trim().isEmpty()) {
                        SimpleToast.showNormal(mEtNote.hint.toString())
                        return@setPositiveButton
                    }

                    val body = TaskCompleteDTO()
                    body.taskId = taskInfo.taskId
                    body.taskTypeId = taskInfo.taskTypeId
                    body.userId = taskInfo.userId

                    if (tvPhone.isSelected) {
                        body.communicateMethod = COMMUNICATION_TYPE_PHONE
                    }
                    if (tvWx.isSelected) {
                        body.communicateMethod = COMMUNICATION_TYPE_WX
                    }
                    if (tvTel.isSelected) {
                        body.communicateMethod = COMMUNICATION_TYPE_TEL
                    }
                    body.comment = mEtNote.text.toString().trim()
                    mPresenter.makeTaskDone(body)
                    CommonUtils.statistics(context, Constants.STATISTICS_TASK_COMPLETE_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
                    dialog.cancel()
                }
                .setNegativeButton("取消", null)
                .create()
        val tvPhone = mDialog.getView<TextView>(R.id.tv_phone)
        val tvWx = mDialog.getView<TextView>(R.id.tv_wx)
        val tvTel = mDialog.getView<TextView>(R.id.tv_tel)
        tvPhone.setOnClickListener {
            tvPhone.setTextColor(context!!.resources.getColor(R.color.white))
            tvPhone.setBackgroundResource(R.drawable.shape_job_feature_item_selected)
            tvWx.setTextColor(context!!.resources.getColor(R.color.black808080))
            tvWx.setBackgroundResource(R.drawable.shape_job_feature_item_unselected)
            tvTel.setTextColor(context!!.resources.getColor(R.color.black808080))
            tvTel.setBackgroundResource(R.drawable.shape_job_feature_item_unselected)
            tvPhone.isSelected = true
            tvWx.isSelected = false
            tvTel.isSelected = false
        }
        tvWx.setOnClickListener {
            tvPhone.setTextColor(context!!.resources.getColor(R.color.black808080))
            tvPhone.setBackgroundResource(R.drawable.shape_job_feature_item_unselected)
            tvWx.setTextColor(context!!.resources.getColor(R.color.white))
            tvWx.setBackgroundResource(R.drawable.shape_job_feature_item_selected)
            tvTel.setTextColor(context!!.resources.getColor(R.color.black808080))
            tvTel.setBackgroundResource(R.drawable.shape_job_feature_item_unselected)
            tvPhone.isSelected = false
            tvWx.isSelected = true
            tvTel.isSelected = false
        }
        tvTel.setOnClickListener {
            tvPhone.setTextColor(context!!.resources.getColor(R.color.black808080))
            tvPhone.setBackgroundResource(R.drawable.shape_job_feature_item_unselected)
            tvWx.setTextColor(context!!.resources.getColor(R.color.black808080))
            tvWx.setBackgroundResource(R.drawable.shape_job_feature_item_unselected)
            tvTel.setTextColor(context!!.resources.getColor(R.color.white))
            tvTel.setBackgroundResource(R.drawable.shape_job_feature_item_selected)
            tvPhone.isSelected = false
            tvWx.isSelected = false
            tvTel.isSelected = true
        }
        mDialog.show()
    }

    fun showCompleteNoticeDialog(taskInfo: TaskInfo) {
        val taskCompleteDTO = TaskCompleteDTO()
        val mCompleteNoticeDialog = CommonDialog.newBuilder(context)
                .setTitle("确认完成任务？")
                .setView(R.layout.dialog_task_done_notice)
                .setPositiveButton("确定") { dialog ->
                    val mEv = dialog!!.getView<EditText>(R.id.dialog_task_done_notice_ed)
                    if (mEv.text.toString().trim().isEmpty()) {
                        SimpleToast.showNormal(mEv.hint.toString())
                        return@setPositiveButton
                    }
                    taskCompleteDTO.comment = mEv.text.toString().trim()
                    taskCompleteDTO.userId = taskInfo.userId
                    taskCompleteDTO.taskTypeId = taskInfo.taskTypeId
                    taskCompleteDTO.taskId = taskInfo.taskId
                    mPresenter.makeTaskDone(taskCompleteDTO)
                    CommonUtils.statistics(context, Constants.STATISTICS_TASK_COMPLETE_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
                    mEv.text.clear()
                    dialog.cancel()
                }
                .setNegativeButton("取消", null)
                .create()
        mCompleteNoticeDialog!!.show()

        val phoneTraceBtn = mCompleteNoticeDialog!!.getView<TextView>(R.id.dialog_task_done_notice_phone_content)
        val wechatTraceBtn = mCompleteNoticeDialog!!.getView<TextView>(R.id.dialog_task_done_notice_wechat_content)
        val telTraceBtn = mCompleteNoticeDialog!!.getView<TextView>(R.id.dialog_task_done_notice_tel_content)
        val followTraceBtn = mCompleteNoticeDialog!!.getView<TextView>(R.id.dialog_task_done_notice_follow_content)
        val contactNotYetBtn = mCompleteNoticeDialog!!.getView<TextView>(R.id.dialog_task_done_notice_contactnotyet_content)
        fun setStatus(viewId: Int) {
            when (viewId) {
                R.id.dialog_task_done_notice_phone_content -> {
                    phoneTraceBtn.setTextColor(context!!.resources.getColor(R.color.white))
                    phoneTraceBtn.setBackgroundResource(R.drawable.shape_job_feature_item_selected)
                    wechatTraceBtn.setTextColor(context!!.resources.getColor(R.color.black808080))
                    wechatTraceBtn.setBackgroundResource(R.drawable.shape_job_feature_item_unselected)
                    telTraceBtn.setTextColor(context!!.resources.getColor(R.color.black808080))
                    telTraceBtn.setBackgroundResource(R.drawable.shape_job_feature_item_unselected)
                    followTraceBtn.setTextColor(context!!.resources.getColor(R.color.black808080))
                    followTraceBtn.setBackgroundResource(R.drawable.shape_job_feature_item_unselected)
                    contactNotYetBtn.setTextColor(context!!.resources.getColor(R.color.black808080))
                    contactNotYetBtn.setBackgroundResource(R.drawable.shape_job_feature_item_unselected)
                    taskCompleteDTO.communicateMethod = COMMUNICATION_TYPE_PHONE
                    taskCompleteDTO.isLineConnected = true
                    taskCompleteDTO.isWxAdded = false
                    taskCompleteDTO.isTelConnected = false
                    taskCompleteDTO.isContactFailed = false
                }
                R.id.dialog_task_done_notice_wechat_content -> {
                    wechatTraceBtn.setTextColor(context!!.resources.getColor(R.color.white))
                    wechatTraceBtn.setBackgroundResource(R.drawable.shape_job_feature_item_selected)
                    phoneTraceBtn.setTextColor(context!!.resources.getColor(R.color.black808080))
                    phoneTraceBtn.setBackgroundResource(R.drawable.shape_job_feature_item_unselected)
                    telTraceBtn.setTextColor(context!!.resources.getColor(R.color.black808080))
                    telTraceBtn.setBackgroundResource(R.drawable.shape_job_feature_item_unselected)
                    followTraceBtn.setTextColor(context!!.resources.getColor(R.color.black808080))
                    followTraceBtn.setBackgroundResource(R.drawable.shape_job_feature_item_unselected)
                    contactNotYetBtn.setTextColor(context!!.resources.getColor(R.color.black808080))
                    contactNotYetBtn.setBackgroundResource(R.drawable.shape_job_feature_item_unselected)
                    taskCompleteDTO.communicateMethod = COMMUNICATION_TYPE_WX
                    taskCompleteDTO.isLineConnected = false
                    taskCompleteDTO.isWxAdded = true
                    taskCompleteDTO.isTelConnected = false
                    taskCompleteDTO.isContactFailed = false
                }
                R.id.dialog_task_done_notice_tel_content -> {
                    telTraceBtn.setTextColor(context!!.resources.getColor(R.color.white))
                    telTraceBtn.setBackgroundResource(R.drawable.shape_job_feature_item_selected)
                    wechatTraceBtn.setTextColor(context!!.resources.getColor(R.color.black808080))
                    wechatTraceBtn.setBackgroundResource(R.drawable.shape_job_feature_item_unselected)
                    phoneTraceBtn.setTextColor(context!!.resources.getColor(R.color.black808080))
                    phoneTraceBtn.setBackgroundResource(R.drawable.shape_job_feature_item_unselected)
                    followTraceBtn.setTextColor(context!!.resources.getColor(R.color.black808080))
                    followTraceBtn.setBackgroundResource(R.drawable.shape_job_feature_item_unselected)
                    contactNotYetBtn.setTextColor(context!!.resources.getColor(R.color.black808080))
                    contactNotYetBtn.setBackgroundResource(R.drawable.shape_job_feature_item_unselected)
                    taskCompleteDTO.communicateMethod = COMMUNICATION_TYPE_TEL
                    taskCompleteDTO.isLineConnected = false
                    taskCompleteDTO.isWxAdded = false
                    taskCompleteDTO.isTelConnected = true
                    taskCompleteDTO.isContactFailed = false
                }
                R.id.dialog_task_done_notice_follow_content -> {
                    followTraceBtn.setTextColor(context!!.resources.getColor(R.color.white))
                    followTraceBtn.setBackgroundResource(R.drawable.shape_job_feature_item_selected)
                    wechatTraceBtn.setTextColor(context!!.resources.getColor(R.color.black808080))
                    wechatTraceBtn.setBackgroundResource(R.drawable.shape_job_feature_item_unselected)
                    telTraceBtn.setTextColor(context!!.resources.getColor(R.color.black808080))
                    telTraceBtn.setBackgroundResource(R.drawable.shape_job_feature_item_unselected)
                    phoneTraceBtn.setTextColor(context!!.resources.getColor(R.color.black808080))
                    phoneTraceBtn.setBackgroundResource(R.drawable.shape_job_feature_item_unselected)
                    contactNotYetBtn.setTextColor(context!!.resources.getColor(R.color.black808080))
                    contactNotYetBtn.setBackgroundResource(R.drawable.shape_job_feature_item_unselected)
                    taskCompleteDTO.communicateMethod = COMMUNICATION_TYPE_PHONE
                    taskCompleteDTO.isLineConnected = false
                    taskCompleteDTO.isWxAdded = false
                    taskCompleteDTO.isTelConnected = false
                    taskCompleteDTO.isContactFailed = false
                }
                R.id.dialog_task_done_notice_contactnotyet_content -> {
                    contactNotYetBtn.setTextColor(context!!.resources.getColor(R.color.white))
                    contactNotYetBtn.setBackgroundResource(R.drawable.shape_job_feature_item_selected)
                    wechatTraceBtn.setTextColor(context!!.resources.getColor(R.color.black808080))
                    wechatTraceBtn.setBackgroundResource(R.drawable.shape_job_feature_item_unselected)
                    telTraceBtn.setTextColor(context!!.resources.getColor(R.color.black808080))
                    telTraceBtn.setBackgroundResource(R.drawable.shape_job_feature_item_unselected)
                    followTraceBtn.setTextColor(context!!.resources.getColor(R.color.black808080))
                    followTraceBtn.setBackgroundResource(R.drawable.shape_job_feature_item_unselected)
                    phoneTraceBtn.setTextColor(context!!.resources.getColor(R.color.black808080))
                    phoneTraceBtn.setBackgroundResource(R.drawable.shape_job_feature_item_unselected)
                    taskCompleteDTO.communicateMethod = COMMUNICATION_TYPE_PHONE
                    taskCompleteDTO.isLineConnected = false
                    taskCompleteDTO.isWxAdded = false
                    taskCompleteDTO.isTelConnected = false
                    taskCompleteDTO.isContactFailed = true
                }
            }
        }
        setStatus(phoneTraceBtn.id)
        phoneTraceBtn.setOnClickListener {
            setStatus(it.id)
        }
        wechatTraceBtn.setOnClickListener {
            setStatus(it.id)
        }
        telTraceBtn.setOnClickListener {
            setStatus(it.id)
        }
        followTraceBtn.setOnClickListener {
            setStatus(it.id)
        }
        contactNotYetBtn.setOnClickListener {
            setStatus(it.id)
        }

    }

    /**
     * 请求某个时间段的任务
     */
    private fun getTasksInPeriod() {
        val contactStatus = if (mTaskTypeId == EnumValue.TASK_TYPE_REGIST) mContactStatus else null
        val agentInfo = SharedPreferencesUtil.getCurrentAgentInfo(context)
        val body = TasksRequestBody2(agentInfo.agentId, mCurrentPage,
                EnumValue.PAGE_SIZE.toInt(), 0, contactStatus, mTaskTypeId!!, mTaskDateInfo.from, mTaskDateInfo.to)
        mPresenter.getTasks(body)
    }


    override fun onCustomerDetailInfoGetSuccess(content: CustomerDetailInfo, number: String, action: Int) {
        when (action) {
            ACTION_PHONECALL -> CommonUtils.makePhoneCall(activity, number, numberFormat(content.phone))
            ACTION_GOTO_DETAIL -> CustomerDetailActivity.jump(context!!, content)
        }
    }

    override fun onCustomerDetailInfoGetFailure() {
    }

    private fun numberFormat(number: String): String {
        if (number.contains("-")) {
            val strings = number.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val sb = StringBuilder()
            for (s in strings) {
                sb.append(s)
            }
            return sb.toString()
        } else
            return number
    }

    private fun gotoRemark(userId: String) {
        val intent = Intent(context, RemarkActivity::class.java)
        intent.putExtra(Keys.USER_ID, userId)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            Code.RequestCode.NoteRemarkActivity -> {
                if (resultCode == Code.ResultCode.OK){
                    mCurrentPage = 1
                    getTasksInPeriod()
                }
            }
        }
    }
}