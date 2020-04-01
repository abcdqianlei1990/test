package cn.xzj.agent.ui.company

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.ExpandableListView
import android.widget.PopupWindow
import android.widget.TextView
import cn.xzj.agent.R
import cn.xzj.agent.constants.Code
import cn.xzj.agent.constants.Constants
import cn.xzj.agent.constants.EnumValue
import cn.xzj.agent.constants.Keys
import cn.xzj.agent.core.common.mvp.MVPBaseActivity
import cn.xzj.agent.entity.CityInfo
import cn.xzj.agent.entity.CommonListBody
import cn.xzj.agent.entity.customer.PositionRequestPermissionInfo
import cn.xzj.agent.entity.job.JobFeature
import cn.xzj.agent.entity.job.JobInfo
import cn.xzj.agent.entity.job.JobSearchSuggestion
import cn.xzj.agent.entity.job.RewardConditionInfo
import cn.xzj.agent.i.OnRecyclerViewItemClickListener
import cn.xzj.agent.iview.ICompanyJobSearchView
import cn.xzj.agent.presenter.CompanyJobSearchPresenter
import cn.xzj.agent.ui.adapter.CityFilterConditionAdapter
import cn.xzj.agent.ui.adapter.CompanyJobListAdapter
import cn.xzj.agent.ui.adapter.JobFeatureAdapter
import cn.xzj.agent.ui.adapter.common.BaseHolder
import cn.xzj.agent.ui.adapter.common.CommonListAdapter
import cn.xzj.agent.ui.adapter.common.CommonViewHolder
import cn.xzj.agent.ui.adapter.common.QuickAdapter
import cn.xzj.agent.ui.adapter.common.decoration.SimpleItemDecoration
import cn.xzj.agent.ui.job.JobDetailActivity
import cn.xzj.agent.util.*
import cn.xzj.agent.widget.SimpleToast
import com.alibaba.fastjson.JSON
import com.channey.utils.StringUtils
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.activity_company_job_search.*

/**
 * @ Author MarkYe
 * @ Email yrmao9893@163.com
 * @ Date 2018/9/10
 * @ Des 企业岗位岗位搜索页面
 */
class CompanyJobSearchActivity : MVPBaseActivity<CompanyJobSearchPresenter>(), ICompanyJobSearchView {

    private lateinit var mACache: ACache
    private var term: String? = null
    private var mCurrentPage=1
    private lateinit var mFeatureAdapter: JobFeatureAdapter
    private var mCityList = ArrayList<CityInfo>()
    private var mSelectedCity: CityInfo? = null
    private lateinit var mFeaturePopupWindow: PopupWindow//岗位特征popupWindow
    private var mJobFeatures = ArrayList<JobFeature>()//岗位特征数据
    private var mSelectedFeatures: ArrayList<String>? = null
    private var mTotalCount = 0
    private var mHistoryAdapter = object : CommonListAdapter<String>(this, R.layout.item_job_search_history) {
        override fun convert(viewHolder: CommonViewHolder, item: String?, position: Int) {
            viewHolder.setText(R.id.tv_value, item)
        }
    }
    private var mQuickResultAdapter = object : QuickAdapter<JobSearchSuggestion>(R.layout.item_job_search_quick_result) {
        override fun convert(holder: BaseHolder, item: JobSearchSuggestion, position: Int) {
            holder.setText(R.id.tv_value, item.positionName)
        }
    }
    private var mCompanyJobAdapter = CompanyJobListAdapter()

    companion object {
        fun jump(context: Context) {
            val intent = Intent(context, CompanyJobSearchActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_company_job_search
    }

    override fun initParams() {
        super.initParams()
    }

    override fun initViews() {
        hideToolbar()
        StatusBarUtil.setPadding(this, rl_top_parent)
        tcl_history.adapter = mHistoryAdapter
        rv_quick_result.layoutManager = LinearLayoutManager(this)
        rv_quick_result.adapter = mQuickResultAdapter
        rv_quick_result.addItemDecoration(SimpleItemDecoration.builder(this)
                .build())
        dropDownTabLayoutCompanyJob.setSumItemSize(2)
        dropDownTabLayoutCompanyJob.addItem("工作城市")
        dropDownTabLayoutCompanyJob.addItem("岗位特征")
        initFeaturePopupWindow()
        //RecyclerView
        recyclerViewCompanyJob.layoutManager = LinearLayoutManager(this)
        mCompanyJobAdapter.setOnRewardConditionBtnClickListener(object : QuickAdapter.OnItemClickListener<JobInfo> {
            override fun onItemClick(view: View, itemData: JobInfo, i: Int) {
                if (itemData.recruitmentNeeds.isEmpty()){
                    DialogUtil.showNoticeDialog(context(),title = "无招聘需求",listener = View.OnClickListener {
                        DialogUtil.dismissNoticeDialog()
                    })
                }else{
                    mPresenter.getRewardCondition(itemData.recruitmentNeeds[0].id)
                }
            }
        })
        recyclerViewCompanyJob.adapter = mCompanyJobAdapter
    }

    override fun onResume() {
        super.onResume()
        CommonUtils.statistics(this, Constants.STATISTICS_enterpriseThinkSearch_ID, EnumValue.STATISTICS_TYPE_PAGE, EnumValue.STATISTICS_METRIC_VIEW)
    }

    override fun initData() {
        super.initData()
        val mHistoryData: List<String>
        mACache = ACache.get(this)
        val jobSearchHistory = mACache.getAsString(Keys.JOB_SEARCH_HISTORY)
        if (!TextUtils.isEmpty(jobSearchHistory)) {
            mHistoryData = JSON.parseArray(jobSearchHistory, String::class.java)
            mHistoryAdapter.clear()
            mHistoryAdapter.addAllItem(mHistoryData)
        } else {
            mHistoryData = ArrayList<String>()
            mHistoryAdapter.addAllItem(mHistoryData)
        }
    }

    override fun setListeners() {
        super.setListeners()
        tv_search_cancel.setOnClickListener {
            finish()
        }
        ic_search_delete.setOnClickListener {
            et_search.setText("")
        }
        ic_search_history_delete.setOnClickListener {
            mHistoryAdapter.clear()
            mHistoryAdapter.notifyDataSetChanged()
        }
        tcl_history.setItemClickListener {
            term=mHistoryAdapter.data[it]
            et_search.setText(term)
            et_search.setSelection(term!!.length)
            setSearchStatus(term)
            getJobs()
        }
        mQuickResultAdapter.setOnItemClickListener(object : QuickAdapter.OnItemClickListener<JobSearchSuggestion> {
            override fun onItemClick(view: View, itemData: JobSearchSuggestion, i: Int) {
                //跳转到详情
                JobDetailActivity.jumpForResult(view.context as Activity,
                        jobId = itemData.positionId,
                        userId = null,
                        requestCode = Code.RequestCode.POSITION_DETAIL,
                        userName = null)
                mHistoryAdapter.addItem(itemData.positionName)
                mHistoryAdapter.notifyDataSetChanged()
                CommonUtils.statistics(this@CompanyJobSearchActivity, Constants.STATISTICS_enterpriseThinkSearch_positionDetail_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
            }
        })
        et_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //隐藏job view
                if (llCompanyJobParent.visibility!=View.GONE){
                    llCompanyJobParent.visibility=View.GONE
                    mCompanyJobAdapter.clearData()
                }
                if (p0!!.isEmpty()) {
                    ic_search_delete.visibility = View.GONE
                    rl_search_history.visibility = View.VISIBLE
                    rv_quick_result.visibility = View.GONE
                    mQuickResultAdapter.clearData()
                } else {
                    ic_search_delete.visibility = View.VISIBLE
                    rl_search_history.visibility = View.GONE
                    rv_quick_result.visibility = View.VISIBLE
                    mPresenter.getSearchSuggestions(p0.toString())
                    CommonUtils.statistics(this@CompanyJobSearchActivity, Constants.STATISTICS_enterpriseThinkSearch_INPUT_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
                }
            }
        })
        et_search.setOnEditorActionListener(TextView.OnEditorActionListener { p0, p1, p2 ->
            if (p1 == EditorInfo.IME_ACTION_SEARCH) {
                term=p0.text.toString().trim()
                //监听搜索动作
                if (TextUtils.isEmpty(p0.text.toString().trim())) {
                    SimpleToast.showShort(p0.hint.toString())
                    return@OnEditorActionListener true
                }
                KeyBoardUtil.close(this)
                addHistoryAdapter(term!!)
                //开始搜索
                setSearchStatus(term)
                getJobs()
                CommonUtils.statistics(this, Constants.STATISTICS_enterpriseThinkSearch_BTN_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
                return@OnEditorActionListener true
            }
            false
        })
        statusLayoutManager.rootLayout.setOnTouchListener { view, motionEvent ->
            KeyBoardUtil.close(this)
            return@setOnTouchListener false
        }
        mCompanyJobAdapter.setOnItemClickListener(object : QuickAdapter.OnItemClickListener<JobInfo> {
            override fun onItemClick(view: View, itemData: JobInfo, i: Int) {
                gotoPositionDetail(i)
            }
        })
        dropDownTabLayoutCompanyJob.setOnItemOnClickListener { view, position ->
            when (position) {
                0 -> {
                    if (mCityList.isNotEmpty()) {
                        showCityFilterCondition(mCityList)
                    } else {
                        mPresenter.getCities()
                    }
                }
                1 -> {
                    //弹出岗位特征ui
                    if (mJobFeatures == null || mJobFeatures.isEmpty()) {
                        mPresenter.getJobFeatures()
                    } else {
                        mFeaturePopupWindow.showAtLocation(dropDownTabLayoutCompanyJob, Gravity.CENTER, 0, 0)
                    }

                }
            }
        }
        //刷新控件
        smartRefreshLayoutCompanyJob.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                mCurrentPage++
                getJobs()
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                mCurrentPage = 1
                getJobs()
            }
        })
    }

    private fun addHistoryAdapter(term: String) {
        for ( i in 0 until mHistoryAdapter.data.size){
            if (mHistoryAdapter.data[i] == term){
                mHistoryAdapter.removeItem(i)
                break
            }
        }

        if (mHistoryAdapter.data.size == 10) {
            mHistoryAdapter.removeItem(9)
            mHistoryAdapter.addItem(0, term)
        } else {
            mHistoryAdapter.addItem(0, term)
        }
        mHistoryAdapter.notifyDataSetChanged()
    }

    override fun onPause() {
        super.onPause()
        //关闭软键盘
        KeyBoardUtil.close(this)
        try {
            //保存岗位搜索缓存
            mACache.put(Keys.JOB_SEARCH_HISTORY, JSON.toJSONString(mHistoryAdapter.data))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getSearchSuggestionsSuccess(data: List<JobSearchSuggestion>, term: String) {
        LogLevel.w("getSearchSuggestionsSuccess", "" + et_search.text.toString() + "mSearchIndex $term")
        if (et_search.text.toString() == term) {
            //最新一次索引
            mQuickResultAdapter.setNewData(data)

        }
    }

    override fun context(): Context {
        return this
    }
    override fun onCityListGetSuccess(list: List<CityInfo>) {
        mCityList.clear()
        mCityList.addAll(list)
        mCityList.add(0, CityInfo("-1", "不限"))
        showCityFilterCondition(mCityList)
    }

    override fun onJobListGetSuccess(info: CommonListBody<JobInfo>) {
        mTotalCount = info.totalCount
        if (mCurrentPage == 1) {
            mCompanyJobAdapter.setNewData(info.items)
        } else {
            mCompanyJobAdapter.addAll(info.items)
            mCompanyJobAdapter.notifyDataSetChanged()
        }
        if (mTotalCount != 0) {
            statusLayoutCompanyJob.showContent()
        } else {
            statusLayoutCompanyJob.showEmpty()
        }
        if (mCurrentPage == 1) {
            smartRefreshLayoutCompanyJob.finishRefresh()
        } else {
            smartRefreshLayoutCompanyJob.finishLoadMore()
        }
        smartRefreshLayoutCompanyJob.setNoMoreData(mTotalCount <= mCompanyJobAdapter.data!!.size)
        mCompanyJobAdapter.notifyDataSetChanged()
    }

    override fun onJobListGetFailure() {
        if (mCompanyJobAdapter.data!!.size == 0) {
            statusLayoutCompanyJob.showLoadError()
        }
        if (mCurrentPage == 1) {
            smartRefreshLayoutCompanyJob.finishRefresh()
        } else {
            smartRefreshLayoutCompanyJob.finishLoadMore()
        }
    }

    override fun onPermissionGetSuccess(info: PositionRequestPermissionInfo, position: Int) {
    }

    override fun onGetJobFeaturesSuceess(jobFeatures: List<JobFeature>) {
        mJobFeatures.clear()
        mJobFeatures.addAll(jobFeatures)
        mFeatureAdapter.notifyDataSetChanged()
        mFeaturePopupWindow.showAtLocation(dropDownTabLayoutCompanyJob, Gravity.CENTER, 0, 0)
    }

    override fun onGetJobFeaturesFailure() {
    }

    private fun getJobs() {
        val cityId: String?
        if (mSelectedCity == null || mSelectedCity!!.name.equals("不限")) {
            cityId = null
        } else {
            cityId = mSelectedCity!!.id
        }
        mPresenter.getJobList(cityId = cityId, pageNo = mCurrentPage, term = term, features = mSelectedFeatures)
    }

    private var mCityPopupWindow: PopupWindow? = null
    private fun showCityFilterCondition(conditions: List<CityInfo>) {

        if (mCityPopupWindow == null) {
            val popupView = LayoutInflater.from(this).inflate(R.layout.pop_filter_condition, null)
            val mRecyclerView = popupView.findViewById<RecyclerView>(R.id.pop_filter_condition_list)
            val adapter = CityFilterConditionAdapter(this, conditions, mSelectedCity)
            adapter.setOnRecyclerViewItemClickListener(object : OnRecyclerViewItemClickListener {
                override fun onClick(view: View, position: Int) {
                    mCityPopupWindow!!.dismiss()
                    mSelectedCity = conditions[position]
                    dropDownTabLayoutCompanyJob.itemTitle = mSelectedCity!!.name
                    getJobs()
                }
            })
            val layoutManager = LinearLayoutManager(this)
            mRecyclerView.adapter = adapter
            mRecyclerView.layoutManager = layoutManager
            mCityPopupWindow = PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            mCityPopupWindow!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#ffffff")))
            mCityPopupWindow!!.isFocusable = true
            mCityPopupWindow!!.isOutsideTouchable = true
            mCityPopupWindow!!.setOnDismissListener {
                dropDownTabLayoutCompanyJob.closeMenu()
            }
            mCityPopupWindow!!.update()
        }
        mCityPopupWindow!!.showAsDropDown(viewTabLayoutCompanyJob, 0, 0)
    }

    private fun initFeaturePopupWindow() {
        val mJobFeatureView = LayoutInflater.from(this).inflate(R.layout.popupwindow_job_feature, null, false)
        val mLvJobFeature = mJobFeatureView.findViewById<ExpandableListView>(R.id.lv_job_feature)
        mLvJobFeature.setGroupIndicator(null)
        mFeatureAdapter = JobFeatureAdapter(this, mJobFeatures)
        mLvJobFeature.setAdapter(mFeatureAdapter)
        mFeatureAdapter.setOnSelectedFeatureListener {
            mSelectedFeatures = it
            getJobs()
        }

        mFeaturePopupWindow = PopupWindow(mJobFeatureView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        mFeaturePopupWindow.isOutsideTouchable = true
        mFeaturePopupWindow.isFocusable = true
        mFeaturePopupWindow.animationStyle = R.style.popupWindowRightInRightOut
        mFeaturePopupWindow.setOnDismissListener {
            dropDownTabLayoutCompanyJob.closeMenu()
        }
        mFeaturePopupWindow.update()
        mJobFeatureView.setOnClickListener {
            mFeaturePopupWindow.dismiss()
        }
        //重置按钮
        mJobFeatureView.findViewById<Button>(R.id.btn_reset).setOnClickListener {
            mFeatureAdapter.removeSelectedFeature()
        }
        //关闭按钮
        mJobFeatureView.findViewById<Button>(R.id.btn_confirm).setOnClickListener {
            mFeaturePopupWindow.dismiss()
        }
    }
    private fun gotoPositionDetail(position: Int) {
        JobDetailActivity.jumpForResult(this,
                jobId = mCompanyJobAdapter.data!![position].positionId,
                userId = null,
                requestCode = Code.RequestCode.POSITION_DETAIL, userName = null)
    }
    private fun setSearchStatus(text:String?){
        if (TextUtils.isEmpty(text)) {
            //隐藏job view
            if (llCompanyJobParent.visibility!=View.GONE){
                llCompanyJobParent.visibility=View.GONE
                mCompanyJobAdapter.clearData()
            }
        } else {
            ic_search_delete.visibility = View.VISIBLE
            rl_search_history.visibility = View.GONE
            rv_quick_result.visibility = View.GONE
            llCompanyJobParent.visibility=View.VISIBLE
        }
    }

    override fun onRewardConditionGetSuccess(condition: List<RewardConditionInfo>?) {
        if (condition == null || condition.isEmpty()){
            DialogUtil.showNoticeDialog(context(),title = "无提成条件",listener = View.OnClickListener {
                DialogUtil.dismissNoticeDialog()
            })
        }else{
            var sb = StringBuilder()
            for (i in 0 until condition.size){
                var info = condition[i]
                if (!(sb.contains("正常返费") || sb.contains("小时工"))){
                    var value = StringBuilder()
                    var commissionValueMaleStr = StringUtils.doubleFormat(info.commissionValueMale)
                    var commissionValueFemaleStr = StringUtils.doubleFormat(info.commissionValueFemale)
                    if (StringUtils.isNotEmpty(commissionValueMaleStr)){
                        value.append("男$commissionValueMaleStr ")
                    }
                    if (StringUtils.isNotEmpty(commissionValueFemaleStr)){
                        value.append("女$commissionValueFemaleStr")
                    }
                    when(info.commissionType){
                        "REWARD" -> {
                            sb.append("正常返费 ")
                            when(info.commissionKey){
                                "REWARD_DAYS_PUNCH" -> sb.append("出勤打卡满${info.condition}天 $value")
                                "REWARD_DAYS_INSERVICE" -> sb.append("在职满${info.condition}天 $value")
                                "REWARD_DAYS_WORK" -> sb.append("工作满${info.condition}天 $value")
                                "REWARD_HOURS_WORK" -> sb.append("出勤满${info.condition}小时 $value")
                            }
                        }
                        "HW" -> {
                            sb.append("小时工 ")
                            when(info.commissionKey){
                                "HW_SERVICE" -> sb.append("${StringUtils.doubleFormat(info.commissionValueMale)}元/小时")
                            }
                        }
                    }
                }
                if (i < condition.size-1) sb.append(";")
            }
            DialogUtil.showNoticeDialog(context(),title = "提成条件",content = sb.toString(),listener = View.OnClickListener {
                DialogUtil.dismissNoticeDialog()
            })
        }
    }

    override fun onRewardConditionGetFailure() {

    }
}
