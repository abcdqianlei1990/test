package cn.xzj.agent.ui.customer.remark

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.*
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.TextView
import cn.xzj.agent.R
import cn.xzj.agent.constants.Code
import cn.xzj.agent.constants.Constants
import cn.xzj.agent.constants.EnumValue
import cn.xzj.agent.core.common.mvp.MVPBaseFragment
import cn.xzj.agent.entity.common.KeyValue
import cn.xzj.agent.entity.customer.MarkJsonInfo
import cn.xzj.agent.entity.customer.NoteRemarkBodyNew
import cn.xzj.agent.entity.task.TaskCompleteDTO
import cn.xzj.agent.entity.task.TaskInfo
import cn.xzj.agent.iview.INoteRemarkView
import cn.xzj.agent.presenter.NoteRemarkPresenter
import cn.xzj.agent.ui.adapter.AppointmentFailReasonAdapter
import cn.xzj.agent.ui.adapter.ScenePopListAdapter
import cn.xzj.agent.ui.adapter.common.QuickAdapter
import cn.xzj.agent.util.CommonUtils
import cn.xzj.agent.util.Util
import cn.xzj.agent.widget.CommonDialog
import com.alibaba.fastjson.JSON
import com.channey.utils.ShapeUtil
import com.channey.utils.StringUtils
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import kotlinx.android.synthetic.main.fragment_note_remark.*

class NoteRemarkFragment : MVPBaseFragment<NoteRemarkPresenter>(), View.OnClickListener, INoteRemarkView {

    private val COMMUNICATE_PHONE = 0
    private val COMMUNICATE_WECHAT = 1
    private val COMMUNICATE_QQ = 2
    private val COMMUNICATE_TEL = 3
    private val COMMUNICATE_RESULT_NAV = 0 //"未接通"
    private val COMMUNICATE_RESULT_POS = 1 //"已接通"
    private var mCurrentCommunicate = -1
    private lateinit var mActivity: RemarkActivity
    private var taskCompleteDTO = TaskCompleteDTO()
    private var taskInfo: TaskInfo? = null//是否在新注册任务中
    private lateinit var mMarkJsonInfo:MarkJsonInfo
    private lateinit var mReasonAdapter: AppointmentFailReasonAdapter

    override fun context(): Context {
        return context!!
    }


    override fun initLayout(): Int {
        return R.layout.fragment_note_remark
    }

    override fun initParams() {
        mActivity = activity as RemarkActivity
    }


    override fun initViews() {
//        initReasonRecyclerView()
        initReasonFlow()
    }

//    private fun initReasonRecyclerView(){
//        mReasonAdapter = AppointmentFailReasonAdapter()
//        mReasonAdapter.setNewData(mSelectedReason)
//        var layoutManager = FlexboxLayoutManager(context)
//        layoutManager.flexDirection = FlexDirection.ROW
//        layoutManager.alignItems = AlignItems.STRETCH;
//        layoutManager.justifyContent = JustifyContent.FLEX_START
//        activity_appointmentfail_list.adapter = mReasonAdapter
//    }
    private fun initReasonFlow(){
        activity_appointmentfail_flowlayout.removeAllViews()
        activity_appointmentfail_flowlayout.adapter = object : TagAdapter<KeyValue>(mSelectedReason) {
            override fun getView(parent: FlowLayout, position: Int, o: KeyValue): View {
//                val layout = LayoutInflater.from(context).inflate(R.layout.item_appointment_fail_reason2,
//                        activity_appointmentfail_flowlayout, false) as ConstraintLayout
//                var tv = layout.findViewById<AppCompatTextView>(R.id.item_appointment_fail_tv)
//                var delBtn = layout.findViewById<AppCompatImageView>(R.id.item_appointment_fail_del)
//                tv.text = o.value
//                setSelectedStyle(tv)
//                return tv
                val tv = LayoutInflater.from(context).inflate(R.layout.item_appointment_fail_reason,
                        activity_appointmentfail_flowlayout, false) as AppCompatTextView
                tv.text = o.value
                setSelectedStyle(tv)
                return tv
            }

            override fun onSelected(position: Int, view: View) {
//                setSelectedStyle(view)
                mSelectedReason.removeAt(position)
                activity_appointmentfail_flowlayout.adapter.notifyDataChanged()
            }

            private fun setSelectedStyle(view: View){
                var tv = view as AppCompatTextView
                tv.setTextColor(resources.getColor(R.color.green29AC3E))
                var radius = resources.getDimension(R.dimen.dp_4)
                var strokeWidth = resources.getDimension(R.dimen.dp_1)
                var strokeColor = resources.getColor(R.color.green29AC3E)
                var solidColor = resources.getColor(R.color.greenF5FFF6)
                ShapeUtil.setShape(view,radius = radius,strokeWidth = strokeWidth.toInt(),strokeColor = strokeColor,solidColor = solidColor)
            }
        }
    }

    override fun initData() {
//        mPresenter.getRegisterTask(mActivity.getUserId()!!)
        val json = Util.getJsonFromAssets(context, "mark.json")
        mMarkJsonInfo = JSON.parseObject(json, MarkJsonInfo::class.java)
    }

    private var mRadioBtns = ArrayList<AppCompatRadioButton>()
    override fun setListeners() {
        fragment_note_remark_btn.setOnClickListener(this)
        fragment_note_communicate_scene_group.setOnClickListener(this)
        fragment_note_appointment_fail_title.setOnClickListener(this)
        fragment_note_appointment_fail_arrow.setOnClickListener(this)

        mRadioBtns.add(fragment_note_communicate_telephone_rb)
        mRadioBtns.add(fragment_note_communicate_phone_rb)
        mRadioBtns.add(fragment_note_communicate_wx_rb)
        fragment_note_communicate_radiogroup.setOnCheckedChangeListener { group, checkedId ->
            fragment_note_communicate_scene_tv.text = ""
            mSelectedScene = null
            when(checkedId){
                R.id.fragment_note_communicate_telephone_rb -> {
                    mCurrentCommunicate = COMMUNICATE_PHONE
                }
                R.id.fragment_note_communicate_phone_rb -> {
                    mCurrentCommunicate = COMMUNICATE_TEL
                }
                R.id.fragment_note_communicate_wx_rb -> {
                    mCurrentCommunicate = COMMUNICATE_WECHAT
                }
            }
        }
        fragment_note_communicate_result_radiogroup.setOnCheckedChangeListener { group, checkedId ->
            fragment_note_communicate_scene_tv.text = ""
            mSelectedScene = null
            when(checkedId){
                R.id.fragment_note_communicate_result_nav_rb -> {
                    mCommunicateResult = COMMUNICATE_RESULT_NAV
                    showFailReasonGroup(false)
                }
                R.id.fragment_note_communicate_result_pos_rb -> {
                    mCommunicateResult = COMMUNICATE_RESULT_POS
                    var situation = fragment_note_communicate_scene_tv.text
                    if (situation != null && situation.toString() == "没约好"){
                        showFailReasonGroup(true)
                    }
                }
            }
        }
        fragment_note_communicate_scene_tv.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s != null && s.toString() == "没约好"){
                    showFailReasonGroup(true)
                }else{
                    showFailReasonGroup(false)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
    }

    /**
     * 显示/隐藏没约好的原因view group
     * 只有是已接通且没约好的情况才显示
     */
    private fun showFailReasonGroup(shown:Boolean){
        if (shown){
            fragment_note_appointment_fail_group.visibility = View.VISIBLE
        }else{
            fragment_note_appointment_fail_group.visibility = View.GONE
        }
    }

    private var mCommunicateResult:Int = -1 //沟通结果

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.fragment_note_remark_btn -> {
                if (mCurrentCommunicate == -1) {
                    showToast(msg = "请选择沟通方式")
                    return
                }
                if (mCommunicateResult == -1) {
                    showToast(msg = "请选择沟通结果")
                    return
                }
                if (mSelectedScene == null) {
                    showToast(msg = "请选择具体情况")
                    return
                }
                if (mSelectedScene!!.key == "NOT_RESERVED" && mSelectedReason.isEmpty()){
                    showToast("请选择没约好的原因")
                    return
                }
                if (taskInfo != null) {
                    //有新注册任务，完成注记并完成任务
                    showDoneTaskAndRemarkDialog()
                } else {
                    //普通注记
                    showRemarkDialog()
                }

            }
            R.id.fragment_note_communicate_scene_group -> {
                var situations = ArrayList<KeyValue>()
                if (COMMUNICATE_RESULT_NAV == mCommunicateResult){
                    when(mCurrentCommunicate){
                        COMMUNICATE_PHONE,COMMUNICATE_TEL -> {
                            situations = mMarkJsonInfo.communicateSituation
                        }
                        COMMUNICATE_WECHAT -> {
                            situations = mMarkJsonInfo.wxCommunicateSituation
                        }
                    }
                }else{
                    situations = mMarkJsonInfo.appointmentResult
                }
                showScenePopWindow(situations)
            }
            R.id.fragment_note_appointment_fail_title,R.id.fragment_note_appointment_fail_arrow -> {
                gotoAppointmentFailReason()
            }
        }
    }

    private fun gotoAppointmentFailReason(){
        var intent = Intent(context, AppointmentFailReasonActivity::class.java)
        startActivityForResult(intent,Code.RequestCode.AppointmentFailReason)
    }

    private var mSelectedScene:KeyValue? = null   //具体情况

    override fun showNetworkError() {
        super.showNetworkError()
        getStatusLayoutManager().showNetWorkError()
    }

    override fun showError() {
        super.showError()
        getStatusLayoutManager().showError()
    }

    override fun showLoading() {
        super.showLoading()
        getStatusLayoutManager().showLoading()
    }

    override fun showContent() {
        super.showContent()
        getStatusLayoutManager().showContent()
    }

    override fun onGetRegisterTaskSuccess(taskInfo: TaskInfo?) {
        this.taskInfo = taskInfo
        if (taskInfo != null) {
            //此客户有新注册任务
            fragment_note_remark_btn.text = "保存注记并完成新注册任务"
        } else {
            fragment_note_remark_btn.text = "提交"
        }
    }

    override fun onGetRegisterTaskFailure(msg: String) {
        fragment_note_remark_btn.text = "提交"
        getStatusLayoutManager().showContent()
    }

    override fun onRemarkSuccess(success: Boolean) {
        if (success) {
            showToast("注记添加成功")
            CommonUtils.statistics(context, Constants.STATISTICS_remarkAndNote_NOTE_SUBMIT_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
            activity!!.finish()
        }
    }

    override fun onRemarkFailure(msg: String) {
        showToast(msg)
    }

    override fun makeTaskDoneSuccess(content: Boolean) {
        if (content) {
            showToast("完成任务成功")
            CommonUtils.statistics(context, Constants.STATISTICS_remarkAndNote_NOTE_SUBMIT_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
            activity!!.finish()
//            val userId = mActivity.getUserId()
//            mPresenter.commitNoteRemark(fragment_note_remark_ed.text.toString().trim(), mCurrentCommunicate, userId!!)
        }
    }

    override fun makeTaskDoneFailure() {
    }

    private fun showRemarkDialog() {
        CommonDialog.newBuilder(mActivity)
                .setMessage("确认添加注记？")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定") { dialog ->
                    dialog!!.dismiss()
                    val userId = mActivity.getUserId()
                    if (userId != null){
                        var comment = fragment_note_communicate_record_ed.text.toString().trim()
                        var reason = ArrayList<String>()
                        for (obj in mSelectedReason){
                            reason.add(obj.key)
                        }
                        var body = NoteRemarkBodyNew(
                                comment = comment,
                                communicateMethod = mCurrentCommunicate,
                                communicateResult = mCommunicateResult,
                                communicateSituation = mSelectedScene!!.key,
                                reason = reason,
                                userId = userId
                        )
                        mPresenter.commitNoteRemark(body)
                    }
                }
                .create()
                .show()
    }

    private fun showDoneTaskAndRemarkDialog() {
        CommonDialog.newBuilder(mActivity)
                .setMessage("保存注记并完成新注册任务？")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定") { dialog ->
                    dialog!!.dismiss()
                    val userId = mActivity.getUserId()
                    taskCompleteDTO.comment = fragment_note_communicate_record_ed.text.toString().trim()
                    taskCompleteDTO.communicateMethod = mCurrentCommunicate
                    taskCompleteDTO.communicateResult = mCommunicateResult
                    taskCompleteDTO.isContactFailed = (mCommunicateResult == COMMUNICATE_RESULT_NAV)
                    taskCompleteDTO.isLineConnected = (mCurrentCommunicate == COMMUNICATE_PHONE && mCommunicateResult == COMMUNICATE_RESULT_POS)
                    taskCompleteDTO.isTelConnected = (mCurrentCommunicate == COMMUNICATE_TEL && mCommunicateResult == COMMUNICATE_RESULT_POS)
                    taskCompleteDTO.communicateSituation = mSelectedScene!!.key
                    var reason = ArrayList<String>()
                    for (obj in mSelectedReason){
                        reason.add(obj.key)
                    }
                    taskCompleteDTO.reason = reason
                    taskCompleteDTO.userId = userId
                    taskCompleteDTO.taskTypeId = taskInfo!!.taskTypeId
                    taskCompleteDTO.taskId = taskInfo!!.taskId
                    taskCompleteDTO.isWxAdded = (mCurrentCommunicate == COMMUNICATE_WECHAT)    //后端在true的情况下会检测是否有微信
                    mPresenter.makeTaskDone(taskCompleteDTO)
                }
                .create()
                .show()
    }

    private var mScenePopWindow: PopupWindow? = null
    private var mScenePopAdapter: ScenePopListAdapter? = null
    private fun showScenePopWindow(scenes: List<KeyValue>) {
        if (mScenePopWindow != null && mScenePopWindow!!.isShowing) return
        mScenePopWindow = PopupWindow(context)
        val view = LayoutInflater.from(context).inflate(R.layout.pop_mark_scene, null)
        mScenePopWindow!!.contentView = view
        val relativeLayout = view.findViewById<RelativeLayout>(R.id.pop_mark_scene_parent)
        val recyclerView = view.findViewById<RecyclerView>(R.id.pop_mark_scene_list)
        val close = view.findViewById<AppCompatImageView>(R.id.pop_mark_scene_close_btn)
        var radius = resources.getDimension(R.dimen.dp_12)
        ShapeUtil.setShape(relativeLayout,leftTopRadius = radius,rightTopRadius = radius,solidColor = resources.getColor(R.color.white))
        close.setOnClickListener {
            mScenePopWindow!!.dismiss()
        }
        mScenePopAdapter = ScenePopListAdapter()
        mScenePopAdapter!!.setNewData(scenes)
        mScenePopAdapter!!.setDefaultSelect(mSelectedScene)
        mScenePopAdapter!!.setOnItemClickListener(object : QuickAdapter.OnItemClickListener<KeyValue> {
            override fun onItemClick(view: View, itemData: KeyValue, position: Int) {
                mSelectedScene = itemData
                mScenePopWindow!!.dismiss()
            }
        })
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = mScenePopAdapter
        mScenePopWindow!!.width = LinearLayout.LayoutParams.MATCH_PARENT
        mScenePopWindow!!.height = LinearLayout.LayoutParams.WRAP_CONTENT
        mScenePopWindow!!.setBackgroundDrawable(ColorDrawable())
        mScenePopWindow!!.isOutsideTouchable = false
        mScenePopWindow!!.isTouchable = true
        mScenePopWindow!!.animationStyle = R.style.popWindowAnim
        mScenePopWindow!!.setOnDismissListener {
            setBackgroundAlpha(1.0f)
            mScenePopWindow = null
            mScenePopAdapter = null
            if (mSelectedScene != null) fragment_note_communicate_scene_tv.text = mSelectedScene!!.value
        }
        mScenePopAdapter!!.setDefaultSelect(mSelectedScene)
        setBackgroundAlpha(0.5f)
        val height = mScenePopWindow!!.contentView.height
        val w = mScenePopWindow!!.contentView.width
        mScenePopWindow!!.showAtLocation(view, Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, w, -height) //设置layout在PopupWindow中显示的位置
        mScenePopAdapter!!.notifyDataSetChanged()
    }

    private var mSelectedReason = ArrayList<KeyValue>()
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            Code.RequestCode.AppointmentFailReason -> {
                if (Code.ResultCode.OK == resultCode){
                    if (data != null){
                        //获取选中的position
                        var selectedPositionArray = data.getIntegerArrayListExtra(AppointmentFailReasonActivity.key_selected_reason)
                        for (i in selectedPositionArray){
                            var reason = mMarkJsonInfo.reason[i]
                            mSelectedReason.add(reason)
                        }
                        //显示reasons
                        if (mSelectedReason.isNotEmpty()){
                            activity_appointmentfail_flowlayout.visibility = View.VISIBLE
//                            mReasonAdapter.notifyDataSetChanged()
//                            initReasonFlow()
//                            activity_appointmentfail_flowlayout.removeAllViews()
                            activity_appointmentfail_flowlayout.adapter.notifyDataChanged()
                        }
                    }
                }
            }
        }
    }
}