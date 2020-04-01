package cn.xzj.agent.ui.customer

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import cn.xzj.agent.R
import cn.xzj.agent.constants.Code
import cn.xzj.agent.constants.Constants
import cn.xzj.agent.constants.EnumValue
import cn.xzj.agent.constants.Keys
import cn.xzj.agent.core.common.BaseFragment
import cn.xzj.agent.core.common.mvp.MVPBaseActivity
import cn.xzj.agent.entity.CityInfo
import cn.xzj.agent.entity.CommonListBody
import cn.xzj.agent.entity.common.ActionInfo
import cn.xzj.agent.entity.common.KeyValue
import cn.xzj.agent.entity.customer.*
import cn.xzj.agent.i.OnRecyclerViewItemClickListener
import cn.xzj.agent.iview.ICustomerDetailView
import cn.xzj.agent.presenter.AddWorkingRecordPresenter
import cn.xzj.agent.presenter.CustomerDetailPresenter
import cn.xzj.agent.ui.AddWorkingRecordActivity
import cn.xzj.agent.ui.adapter.ActionMenuAdapter
import cn.xzj.agent.ui.adapter.RecordsPageAdapter
import cn.xzj.agent.ui.adapter.WorkingRecordListAdapter
import cn.xzj.agent.ui.adapter.common.QuickAdapter
import cn.xzj.agent.ui.adapter.common.decoration.SimpleItemDecoration
import cn.xzj.agent.ui.customer.remark.RemarkActivity
import cn.xzj.agent.ui.customer.remark.RemarkHistoryActivity
import cn.xzj.agent.ui.job.JobListActivity
import cn.xzj.agent.util.CommonUtils
import cn.xzj.agent.util.StatusBarUtil
import cn.xzj.agent.util.Util
import cn.xzj.agent.widget.SimpleToast
import com.alibaba.fastjson.JSON
import com.channey.utils.FormatUtils
import com.channey.utils.ShapeUtil
import com.channey.utils.StringUtils
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import kotlinx.android.synthetic.main.activity_customer_detail.*

/**
 * 客户详情
 */
class CustomerDetailActivity : MVPBaseActivity<CustomerDetailPresenter>(), View.OnClickListener, ICustomerDetailView {

    private var mDetailInfo: CustomerDetailInfo? = null
    private lateinit var mUserInfoRemarkInfo: RemarkVO
    private lateinit var mMarkJsonInfo:MarkJsonInfo
    private var mSituationMap = HashMap<String,String>()

    companion object {
        fun jump(context: Context, info: CustomerDetailInfo) {
            var intent = Intent(context, CustomerDetailActivity::class.java)
            intent.putExtra(Keys.CUSTOMER_DETAIL_INFO, info)
            context.startActivity(intent)
        }
    }

    override fun context(): Context {
        return this
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_customer_detail
    }

    override fun initParams() {
        mDetailInfo = intent.getParcelableExtra(Keys.CUSTOMER_DETAIL_INFO)
    }

    override fun initViews() {
//        StatusBarUtil.darkMode(this)
//        StatusBarUtil.setPadding(this, rl_parent)
        com.jaeger.library.StatusBarUtil.setColor(this,resources.getColor(R.color.green29AC3E),0)
        hideToolbar()
        initRecyclerView()
    }

    override fun initData() {
        if (mDetailInfo != null) {
            injectViews()
            mPresenter.getHistories(mDetailInfo!!.userId,1)
            getWorkingRecords()
        }
        //加载注记数据
        val json = Util.getJsonFromAssets(this, "mark.json")
        mMarkJsonInfo = JSON.parseObject(json, MarkJsonInfo::class.java)
        for (i in 0 until mMarkJsonInfo.situations.size){
            var obj = mMarkJsonInfo.situations[i]
            mSituationMap[obj.key] = obj.value
        }
    }

    private fun getWorkingRecords(){
        mPresenter.getWorkingRecords(mDetailInfo!!.userId,mCurrentPage)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter.getRemarkInfo(mDetailInfo!!.userId)
    }

    override fun onResume() {
        super.onResume()
        CommonUtils.statistics(this, Constants.STATISTICS_customerDetail_ID, EnumValue.STATISTICS_TYPE_PAGE, EnumValue.STATISTICS_METRIC_VIEW)
        if (mDetailInfo != null)
            mPresenter.getCustomerDetailInfo(mDetailInfo!!.userId)
    }


    override fun setListeners() {
        activity_customer_detail_nav_back.setOnClickListener(this)
        activity_customer_detail_mark_history_btn.setOnClickListener(this)
        activity_customer_detail_phonecall_btn.setOnClickListener(this)
        activity_customer_detail_suggest_btn.setOnClickListener(this)
        activity_customer_detail_follow_btn.setOnClickListener(this)
        activity_customer_detail_remark_btn.setOnClickListener(this)
        activity_customer_detail_to_certification_btn.setOnClickListener(this)
        activity_customer_detail_markHistory_seeAll_tv.setOnClickListener(this)
        activity_customer_detail_markHistory_seeAll_ic.setOnClickListener(this)
//        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
//            override fun onTabReselected(tab: TabLayout.Tab?) {
//            }
//
//            override fun onTabUnselected(tab: TabLayout.Tab?) {
//            }
//
//            override fun onTabSelected(tab: TabLayout.Tab?) {
//                selectFragment(tab!!.position)
//            }
//        })
        activity_customer_detail_member_suggest_tv.setOnClickListener(this)
    }

    private lateinit var mWorkingRecordAdapter:WorkingRecordListAdapter
    private var mWorkingRecordList = ArrayList<WorkingRecordInfo>()
    private var mTotalCount = 0
    private var mCurrentPage = 1
    private fun initRecyclerView(){
        mWorkingRecordAdapter = WorkingRecordListAdapter()
        mWorkingRecordAdapter.setDeleteBtnOnClickListener(object : QuickAdapter.OnItemClickListener<WorkingRecordInfo> {
            override fun onItemClick(view: View, itemData: WorkingRecordInfo, position: Int) {
                var info = mWorkingRecordList[position]
                if (info.removable){
                    mPresenter.deleteWorkingRecord(mDetailInfo!!.userId,info.id)
                }else{
                    showToast("该记录不可删除")
                }
            }
        })
        mWorkingRecordAdapter.data = mWorkingRecordList
        var linearLayoutManager = LinearLayoutManager(this)
        activity_customer_detail_workingRecord_list.adapter = mWorkingRecordAdapter
        activity_customer_detail_workingRecord_list.layoutManager = linearLayoutManager
        activity_customer_detail_workingRecord_smartRefreshLayout.isEnableLoadMore = true
        activity_customer_detail_workingRecord_smartRefreshLayout.setOnRefreshListener {
            mCurrentPage = 1
            getWorkingRecords()
        }
        activity_customer_detail_workingRecord_smartRefreshLayout.setOnLoadMoreListener {
            mCurrentPage++
            getWorkingRecords()
        }
    }

    private fun loadingComplete() {
        if (mCurrentPage <= 1) {
            activity_customer_detail_workingRecord_smartRefreshLayout.finishRefresh()
        } else {
            activity_customer_detail_workingRecord_smartRefreshLayout.finishLoadMore()
        }

        activity_customer_detail_workingRecord_smartRefreshLayout.setNoMoreData(mTotalCount <= mWorkingRecordAdapter.data!!.size)
        if (mWorkingRecordList.isEmpty()){
            activity_customer_detail_workingRecord_group.visibility = View.GONE
        }else{
            activity_customer_detail_workingRecord_group.visibility = View.VISIBLE
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.activity_customer_detail_nav_back -> finish()
            R.id.activity_customer_detail_mark_history_btn -> {
                RemarkHistoryActivity.jump(this, mDetailInfo!!.userId)
                CommonUtils.statistics(this, Constants.STATISTICS_customerDetail_historyNote_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
            }
            R.id.activity_customer_detail_phonecall_btn -> {
                CommonUtils.makePhoneCall(this, mDetailInfo!!.phone.replaceRange(3, 7, "****"), mDetailInfo!!.phone)
                CommonUtils.statistics(this, Constants.STATISTICS_customerDetail_CALL_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
            }
            R.id.activity_customer_detail_follow_btn -> {
                NoticeSettingActivity.jumpForResult(this, mDetailInfo!!.userId, Code.RequestCode.FOLLOW_NOTICE_SETTING)
                CommonUtils.statistics(this, Constants.STATISTICS_customerDetail_returnVisit_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
            }
            R.id.activity_customer_detail_suggest_btn -> {
                JobListActivity.jump(context = this, customerDetailInfo = mDetailInfo!!)
                CommonUtils.statistics(this, Constants.STATISTICS_customerDetail_recommendPosition_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)

            }
            R.id.activity_customer_detail_remark_btn -> {
                RemarkActivity.jumpForResult(this, mDetailInfo!!.userId, mUserInfoRemarkInfo, Code.RequestCode.REMARK)
                CommonUtils.statistics(this, Constants.STATISTICS_customerDetail_remarkAndNote_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)

            }
            R.id.activity_customer_detail_to_certification_btn -> {
                //去实名认证
                IDCardCertificationActivity.jump(this, mDetailInfo!!)
                CommonUtils.statistics(this, Constants.STATISTICS_customerDetail_realNameAuthentication_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
            }
            R.id.activity_customer_detail_member_suggest_tv -> {
//                MemberSuggestActivity.jump(this,mDetailInfo!!)
                AddWorkingRecordActivity.jump(this,mDetailInfo!!.userId)
            }
            R.id.activity_customer_detail_markHistory_seeAll_tv,R.id.activity_customer_detail_markHistory_seeAll_ic -> {
                RemarkHistoryActivity.jump(this@CustomerDetailActivity, mDetailInfo!!.userId)
                CommonUtils.statistics(this@CustomerDetailActivity, Constants.STATISTICS_customerDetail_historyNote_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun injectViews() {
        setBaseUserInfo(mDetailInfo!!.nickname)
        //客户等级
        if (StringUtils.isEmpty(mDetailInfo!!.rank)) {
            activity_customer_detail_level_img.visibility = View.GONE
        } else {
            activity_customer_detail_level_img.visibility = View.VISIBLE
            when (mDetailInfo!!.rank) {
                EnumValue.CUSTOMER_LEVEL_A -> activity_customer_detail_level_img.setImageResource(R.mipmap.ic_customer_level_a)
                EnumValue.CUSTOMER_LEVEL_B -> activity_customer_detail_level_img.setImageResource(R.mipmap.ic_customer_level_b)
                else -> activity_customer_detail_level_img.visibility = View.GONE
            }
        }
        //意向
        var solidColor = resources.getColor(R.color.redF83232)
        var radius = resources.getDimension(R.dimen.dp_12)
        var intentStr = ""
        when(mDetailInfo!!.wish){
            OriginalCustomerInfo.INTENT_EXTREMELY_HIGH -> {
                solidColor = resources.getColor(R.color.redF83232)
                intentStr = "意向：极高"
            }
            OriginalCustomerInfo.INTENT_HIGH -> {
                solidColor = resources.getColor(R.color.yellowFF5800)
                intentStr = "意向：高"
            }
            OriginalCustomerInfo.INTENT_MEDIUM -> {
                solidColor = resources.getColor(R.color.yellowFF8500)
                intentStr = "意向：中"
            }
            OriginalCustomerInfo.INTENT_LOW -> {
                solidColor = resources.getColor(R.color.yellowFFB600)
                intentStr = "意向：低"
            }
            OriginalCustomerInfo.INTENT_NONE -> {
                solidColor = resources.getColor(R.color.green90D600)
                intentStr = "意向：无"
            }
            else -> activity_customer_detail_intentLv_tv.visibility = View.GONE
        }
        ShapeUtil.setShape(activity_customer_detail_intentLv_tv,solidColor = solidColor,radius = radius)
        activity_customer_detail_intentLv_tv.text = intentStr

        val registerDate = FormatUtils.timeStamp2String(mDetailInfo!!.registerTime.toLong(), EnumValue.DATE_FORMAT_1)
        val sourceName = if (StringUtils.isEmpty(mDetailInfo!!.sourceName)) "" else mDetailInfo!!.sourceName
        val source = if (StringUtils.isEmpty(mDetailInfo!!.source)) "" else "(${mDetailInfo!!.source})"
        val referrer= if (TextUtils.isEmpty(mDetailInfo!!.referrer))"" else mDetailInfo!!.referrer
        activity_customer_detail_regist_tv.text = "$registerDate $referrer $sourceName$source"
        showRealNameViews(mDetailInfo!!.isIdCardVerified)

    }

    @SuppressLint("SetTextI18n")
    private fun showRealNameViews(show: Boolean) {
        if (show) {
            var sex = ""
            when (mDetailInfo!!.sex) {
                "0" -> sex = "保密"
                "1" -> sex = "男"
                "2" -> sex = "女"
            }
            activity_customer_detail_realname_tv.text = "已实名认证 $sex ${mDetailInfo!!.age} ${mDetailInfo!!.constellation}"
            activity_customer_detail_to_certification_btn.visibility = View.GONE
        } else {
            //未实名认证
            activity_customer_detail_realname_tv.text = "未实名认证"
            activity_customer_detail_to_certification_btn.visibility = View.VISIBLE
        }
        if (show && !StringUtils.isEmpty(mDetailInfo!!.jiguan)) {
            activity_customer_detail_locicon.visibility = View.VISIBLE
            activity_customer_detail_loc_tv.visibility = View.VISIBLE
            activity_customer_detail_loc_tv.text = "${mDetailInfo!!.jiguan}"
        } else {
            activity_customer_detail_locicon.visibility = View.GONE
            activity_customer_detail_loc_tv.visibility = View.GONE
        }
    }


    fun getDetailInfoForFragment(): CustomerDetailInfo {
        return mDetailInfo!!
    }

    override fun onRemarkGetFailure(msg: String) {
        SimpleToast.showNormal(msg)
    }

    override fun onRemarkGetSuccess(info: RemarkVO) {
        mUserInfoRemarkInfo = info
        setBaseUserInfo(info.nickname)
    }

    override fun onCustomerDetailInfoGetSuccess(content: CustomerDetailInfo) {
        mDetailInfo = content
        injectViews()

    }

    override fun onCustomerDetailInfoGetFailure() {
    }

    private fun setBaseUserInfo(nickName: String?) {
        var phone = mDetailInfo!!.phone.replaceRange(3, 7, "****")
        var name = ""
        if (!StringUtils.isEmpty(nickName)) {
            name = nickName!!
        }
        var userName = ""
        if (!StringUtils.isEmpty(mDetailInfo!!.userName)) {
            userName = "(${mDetailInfo!!.userName})"
        }
        var str = String.format(resources.getString(R.string.user_base_info), phone, name, userName)
        val spanned = Html.fromHtml(str)
        activity_customer_detail_userbase_tv.text = spanned
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Code.RequestCode.REMARK -> {
                mPresenter.getRemarkInfo(mDetailInfo!!.userId)
                mPresenter.getHistories(mDetailInfo!!.userId,1)
            }
            Code.RequestCode.FOLLOW_NOTICE_SETTING -> {
                if (resultCode == Code.ResultCode.OK) {

                }
            }
            Code.RequestCode.AddWorkingRecordActivity -> {
                mCurrentPage = 1
                getWorkingRecords()
            }
        }
    }

    override fun onHistoryGetSuccess(info: CommonListBody<RemarkHistoryInfo>) {
        if (info.items.isNotEmpty()) showMarkHistory(info.items[0],info.totalCount)
    }

    override fun onHistoryGetFailure() {

    }

    private fun showMarkHistory(info:RemarkHistoryInfo,count:Int){
        activity_customer_detail_markHistory_group.visibility = View.VISIBLE
        activity_customer_detail_markHistory_tv.text = "历史注记 ($count)"
        activity_customer_detail_mark_date_tv.text = FormatUtils.timeStamp2String(info.updateTime.toLong(),EnumValue.DATE_FORMAT_1)
        activity_customer_detail_mark_agentname_tv.text = info.operatorName
        activity_customer_detail_mark_situation_tv.text = "${info.getCommunicate()}-${info.getCommunicateResult()}-${info.getAppointmentResult()}"
        if (StringUtils.isEmpty(info.comment)){
            activity_customer_detail_mark_comment_tv.visibility = View.INVISIBLE
        }else{
            activity_customer_detail_mark_comment_tv.visibility = View.VISIBLE
            activity_customer_detail_mark_comment_tv.text = "沟通记录：${info.comment}"
        }
        //显示注记
        var values = ArrayList<String>()
        for (key in info.communicateSituation){
            if (key != "RESERVED"){
                var value = mSituationMap[key]
                if (value != null) values.add(value)
            }
        }
        initReasonFlow(values)
    }

    private fun initReasonFlow(reasons:ArrayList<String>){
        activity_customer_detail_mark_flowlayout.adapter = object : TagAdapter<String>(reasons) {
            override fun getView(parent: FlowLayout, position: Int, s: String): View {
                val tv = LayoutInflater.from(this@CustomerDetailActivity).inflate(R.layout.item_appointment_fail_reason,
                        activity_customer_detail_mark_flowlayout, false) as AppCompatTextView
                tv.text = s
                tv.setTextColor(resources.getColor(R.color.redFF7731))
                ShapeUtil.setShape(tv,radius = resources.getDimension(R.dimen.dp_15),solidColor = resources.getColor(R.color.yellowFFFAB8))
                return tv
            }
        }
    }

    override fun onWorkingRecordsGetSuccess(info: CommonListBody<WorkingRecordInfo>) {
        mTotalCount  = info.totalCount
        if (mCurrentPage == 1) mWorkingRecordList.clear()
        mWorkingRecordList.addAll(info.items)
        mWorkingRecordAdapter.notifyDataSetChanged()
        loadingComplete()
    }

    override fun onWorkingRecordsGetFailure() {
        loadingComplete()
    }

    override fun onDeleteWorkingRecordSuccess(boolean: Boolean) {
        if (boolean){
            mCurrentPage = 1
            getWorkingRecords()
        }
    }

    override fun onDeleteWorkingRecordFailure() {

    }
}