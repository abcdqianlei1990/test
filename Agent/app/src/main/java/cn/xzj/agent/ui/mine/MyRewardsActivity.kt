package cn.xzj.agent.ui.mine

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AlertDialog
import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import cn.xzj.agent.R
import cn.xzj.agent.constants.Code
import cn.xzj.agent.constants.EnumValue
import cn.xzj.agent.core.common.BaseActivity
import cn.xzj.agent.core.common.mvp.MVPBaseActivity
import cn.xzj.agent.entity.CommonListBody
import cn.xzj.agent.entity.agentinfo.AgentInfo
import cn.xzj.agent.entity.reward.FirstRewardInfo
import cn.xzj.agent.entity.reward.HierarchicalRewardInfo
import cn.xzj.agent.iview.IMyRewardView
import cn.xzj.agent.presenter.MyRewardPresenter
import cn.xzj.agent.ui.adapter.CurLvRewardListAdapter
import cn.xzj.agent.ui.adapter.EntryRewardListAdapter
import cn.xzj.agent.ui.adapter.FirstRewardListAdapter
import cn.xzj.agent.ui.adapter.LowerLvRewardListAdapter
import cn.xzj.agent.ui.adapter.common.decoration.LineSplitDecoration
import cn.xzj.agent.util.CommonUtils
import cn.xzj.agent.widget.MyWheelView
import com.bigkoo.pickerview.adapter.ArrayWheelAdapter
import com.channey.utils.FormatUtils
import com.channey.utils.ShapeUtil
import com.channey.utils.StringUtils
import com.contrarywind.listener.OnItemSelectedListener
import kotlinx.android.synthetic.main.activity_my_rewards.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MyRewardsActivity:MVPBaseActivity<MyRewardPresenter>(),IMyRewardView,View.OnClickListener {

    private var mCurrentPage = 1
    private var mCurrentPageLowerList = 1
    private var mCurrentPageFirstList = 1
    private var mCurrentPageEntryList = 1
    private var mCurLvTotalCount = 0
    private var mLowerLvTotalCount = 0
    private var mFirstTotalCount = 0
    private var mEntryTotalCount = 0
    private lateinit var mCurLvRewardListAdapter: CurLvRewardListAdapter
    private lateinit var mLowerLvRewardListAdapter: LowerLvRewardListAdapter
    private lateinit var mFirstRewardListAdapter: FirstRewardListAdapter
    private lateinit var mEntryRewardListAdapter: EntryRewardListAdapter
    private var mCurRewardList = ArrayList<HierarchicalRewardInfo>()
    private var mLowerRewardList = ArrayList<HierarchicalRewardInfo>()
    private var mFirstRewardList = ArrayList<FirstRewardInfo>()
    private var mEntryRewardList = ArrayList<HierarchicalRewardInfo>()
    private lateinit var mAgentInfo:AgentInfo
    private var mSelectedDate:String? = null
    private var mCurrentRewardType = rewardType_cur  //提成类型: 0 - 服务报酬, 1 - 培训津贴
    private var selectedYear:Int? = null
    private var selectedMonth:Int? = null

    companion object{
        val key_agentInfo = "agentInfo"
        val rewardType_cur = 0
        val rewardType_lower = 1
        val rewardType_first = 2
        val rewardType_entry = 3
        fun jump(context:BaseActivity,agentInfo:AgentInfo,requestCode:Int = Code.RequestCode.MyRewardsActivity){
            var intent = Intent(context,MyRewardsActivity::class.java)
            intent.putExtra(key_agentInfo,agentInfo)
            context.startActivityForResult(intent,requestCode)
        }
    }

    override fun context(): Context {
        return this
    }

    override fun initParams() {
        super.initParams()
        mAgentInfo = intent.getSerializableExtra(key_agentInfo) as AgentInfo
        //默认显示当前月份
        selectedYear = Calendar.getInstance().get(Calendar.YEAR)
        selectedMonth = Calendar.getInstance().get(Calendar.MONTH) + 1   //因为Calendar month从0开始
        mSelectedDate = "${selectedYear}年${selectedMonth}月"
        activity_my_rewards_month_filter_tv.text = mSelectedDate
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_my_rewards
    }

    override fun setListeners() {
        super.setListeners()
        activity_my_rewards_month_filter_group.setOnClickListener(this)
        activity_my_rewards_curLvReward_title_tv.setOnClickListener(this)
        activity_my_rewards_lowerLvReward_title_tv.setOnClickListener(this)
        activity_my_rewards_firstReward_title_tv.setOnClickListener(this)
        activity_my_rewards_entryReward_title_tv.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v != null){
            when(v.id){
                R.id.activity_my_rewards_month_filter_group -> {
                    CommonUtils.closeKeyBoard(this)
                    showDatePickPop()
                }
                R.id.activity_my_rewards_curLvReward_title_tv -> {
                    mCurrentRewardType = rewardType_cur
                    activity_my_rewards_totalMonthReward_tv.visibility = View.VISIBLE
                    activity_my_rewards_curLvReward_indicator_tv.visibility = View.VISIBLE
                    activity_my_rewards_lowerLvReward_indicator_tv.visibility = View.GONE
                    activity_my_rewards_firstReward_indicator_tv.visibility = View.GONE
                    activity_my_rewards_entryReward_indicator_tv.visibility = View.GONE

                    activity_my_rewards_entrySmartRefreshLayout.visibility = View.VISIBLE
                    activity_my_rewards_curSmartRefreshLayout.visibility = View.VISIBLE
                    activity_my_rewards_lowerSmartRefreshLayout.visibility = View.GONE
                    activity_my_rewards_firstSmartRefreshLayout.visibility = View.GONE
                    mCurrentPage = 1
                    getRewardInfo(rewardType_cur)
                }
                R.id.activity_my_rewards_lowerLvReward_title_tv -> {
                    mCurrentRewardType = rewardType_lower
                    activity_my_rewards_totalMonthReward_tv.visibility = View.VISIBLE
                    activity_my_rewards_curLvReward_indicator_tv.visibility = View.GONE
                    activity_my_rewards_lowerLvReward_indicator_tv.visibility = View.VISIBLE
                    activity_my_rewards_firstReward_indicator_tv.visibility = View.GONE
                    activity_my_rewards_entryReward_indicator_tv.visibility = View.GONE

                    activity_my_rewards_entrySmartRefreshLayout.visibility = View.GONE
                    activity_my_rewards_curSmartRefreshLayout.visibility = View.GONE
                    activity_my_rewards_lowerSmartRefreshLayout.visibility = View.VISIBLE
                    activity_my_rewards_firstSmartRefreshLayout.visibility = View.GONE
                    mCurrentPageLowerList = 1
                    getRewardInfo(rewardType_lower)
                }
                R.id.activity_my_rewards_firstReward_title_tv -> {
                    mCurrentRewardType = rewardType_first
                    activity_my_rewards_totalMonthReward_tv.visibility = View.VISIBLE
                    activity_my_rewards_curLvReward_indicator_tv.visibility = View.GONE
                    activity_my_rewards_lowerLvReward_indicator_tv.visibility = View.GONE
                    activity_my_rewards_firstReward_indicator_tv.visibility = View.VISIBLE
                    activity_my_rewards_entryReward_indicator_tv.visibility = View.GONE

                    activity_my_rewards_curSmartRefreshLayout.visibility = View.GONE
                    activity_my_rewards_lowerSmartRefreshLayout.visibility = View.GONE
                    activity_my_rewards_entrySmartRefreshLayout.visibility = View.GONE
                    activity_my_rewards_firstSmartRefreshLayout.visibility = View.VISIBLE
                    mCurrentPageFirstList = 1
                    getRewardInfo(mCurrentRewardType)
                }
                R.id.activity_my_rewards_entryReward_title_tv -> {
                    mCurrentRewardType = rewardType_entry
                    activity_my_rewards_totalMonthReward_tv.visibility = View.GONE
                    activity_my_rewards_curLvReward_indicator_tv.visibility = View.GONE
                    activity_my_rewards_lowerLvReward_indicator_tv.visibility = View.GONE
                    activity_my_rewards_entryReward_indicator_tv.visibility = View.VISIBLE
                    activity_my_rewards_firstReward_indicator_tv.visibility = View.GONE

                    activity_my_rewards_curSmartRefreshLayout.visibility = View.GONE
                    activity_my_rewards_lowerSmartRefreshLayout.visibility = View.GONE
                    activity_my_rewards_firstSmartRefreshLayout.visibility = View.GONE
                    activity_my_rewards_entrySmartRefreshLayout.visibility = View.VISIBLE
                    mCurrentPageEntryList = 1
                    getRewardInfo(mCurrentRewardType)
                }
            }
        }
    }

    override fun initViews() {
        setTitle("我的提成")
        var indicatorBg = resources.getColor(R.color.tabIndicatorColor)
        var indicatorRadius = resources.getDimension(R.dimen.dp_2)
        var monthBtnRadius = resources.getDimension(R.dimen.dp_14)
        ShapeUtil.setShape(activity_my_rewards_curLvReward_indicator_tv,radius = indicatorRadius,solidColor = indicatorBg)
        ShapeUtil.setShape(activity_my_rewards_lowerLvReward_indicator_tv,radius = indicatorRadius,solidColor = indicatorBg)
        ShapeUtil.setShape(activity_my_rewards_entryReward_indicator_tv,radius = indicatorRadius,solidColor = indicatorBg)
        ShapeUtil.setShape(activity_my_rewards_firstReward_indicator_tv,radius = indicatorRadius,solidColor = indicatorBg)
        ShapeUtil.setShape(activity_my_rewards_month_filter_group,radius = monthBtnRadius,solidColor = resources.getColor(R.color.white))
        initEntryRewardListRecyclerView()
        initCurListRecyclerView()
        initLowerListRecyclerView()
        initFirstRewardListRecyclerView()
    }

    private fun initEntryRewardListRecyclerView() {
        mEntryRewardListAdapter = EntryRewardListAdapter()
        mEntryRewardListAdapter.data = mEntryRewardList
        activity_my_rewards_list_entry.layoutManager = LinearLayoutManager(this)
        activity_my_rewards_list_entry.adapter = mEntryRewardListAdapter
        activity_my_rewards_entrySmartRefreshLayout.isEnableLoadMore = true
        var decoration = LineSplitDecoration(this,splitSize = resources.getDimension(R.dimen.dp_10).toInt())
        activity_my_rewards_list_entry.addItemDecoration(decoration)
        activity_my_rewards_entrySmartRefreshLayout.setOnRefreshListener {
            mCurrentPageEntryList = 1
            getRewardInfo(rewardType_entry)
        }
        activity_my_rewards_entrySmartRefreshLayout.setOnLoadMoreListener {
            mCurrentPageEntryList++
            getRewardInfo(rewardType_entry)
        }
    }

    private fun initCurListRecyclerView() {
        mCurLvRewardListAdapter = CurLvRewardListAdapter()
        mCurLvRewardListAdapter.data = mCurRewardList
        activity_my_rewards_list_cur.layoutManager = LinearLayoutManager(this)
        activity_my_rewards_list_cur.adapter = mCurLvRewardListAdapter
        activity_my_rewards_curSmartRefreshLayout.isEnableLoadMore = true
        var decoration = LineSplitDecoration(this,splitSize = resources.getDimension(R.dimen.dp_10).toInt())
        activity_my_rewards_list_cur.addItemDecoration(decoration)
        activity_my_rewards_curSmartRefreshLayout.setOnRefreshListener {
            mCurrentPage = 1
            getRewardInfo(rewardType_cur)
        }
        activity_my_rewards_curSmartRefreshLayout.setOnLoadMoreListener {
            mCurrentPage++
            getRewardInfo(rewardType_cur)
        }
    }

    private fun initLowerListRecyclerView() {
        mLowerLvRewardListAdapter = LowerLvRewardListAdapter()
        mLowerLvRewardListAdapter.data = mLowerRewardList
        activity_my_rewards_list_lower.layoutManager = LinearLayoutManager(this)
        activity_my_rewards_list_lower.adapter = mLowerLvRewardListAdapter
        activity_my_rewards_lowerSmartRefreshLayout.isEnableLoadMore = true
        var decoration = LineSplitDecoration(this,splitSize = resources.getDimension(R.dimen.dp_10).toInt())
        activity_my_rewards_list_lower.addItemDecoration(decoration)
        activity_my_rewards_lowerSmartRefreshLayout.setOnRefreshListener {
            mCurrentPageLowerList = 1
            getRewardInfo(rewardType_lower)
        }
        activity_my_rewards_lowerSmartRefreshLayout.setOnLoadMoreListener {
            mCurrentPageLowerList++
            getRewardInfo(rewardType_lower)
        }
    }

    /**
     * 初始化首单提成列表
     */
    private fun initFirstRewardListRecyclerView() {
        mFirstRewardListAdapter = FirstRewardListAdapter()
        mFirstRewardListAdapter.data = mFirstRewardList
        activity_my_rewards_list_first.layoutManager = LinearLayoutManager(this)
        activity_my_rewards_list_first.adapter = mFirstRewardListAdapter
        activity_my_rewards_firstSmartRefreshLayout.isEnableLoadMore = true
        var decoration = LineSplitDecoration(this,splitSize = resources.getDimension(R.dimen.dp_10).toInt())
        activity_my_rewards_list_first.addItemDecoration(decoration)
        activity_my_rewards_firstSmartRefreshLayout.setOnRefreshListener {
            mCurrentPageFirstList = 1
            getRewardInfo(rewardType_first)
        }
        activity_my_rewards_firstSmartRefreshLayout.setOnLoadMoreListener {
            mCurrentPageFirstList++
            getRewardInfo(rewardType_first)
        }
    }

    private fun loadingComplete() {
        if (mCurrentPage <= 1) {
            activity_my_rewards_curSmartRefreshLayout.finishRefresh()
        } else {
            activity_my_rewards_curSmartRefreshLayout.finishLoadMore()
        }

        activity_my_rewards_curSmartRefreshLayout.setNoMoreData(mCurLvTotalCount <= mCurLvRewardListAdapter.data!!.size)
    }

    private fun lowerListLoadingComplete() {
        if (mCurrentPageLowerList <= 1) {
            activity_my_rewards_lowerSmartRefreshLayout.finishRefresh()
        } else {
            activity_my_rewards_lowerSmartRefreshLayout.finishLoadMore()
        }

        activity_my_rewards_lowerSmartRefreshLayout.setNoMoreData(mLowerLvTotalCount <= mLowerLvRewardListAdapter.data!!.size)
    }

    private fun firstRewardListLoadingComplete() {
        if (mCurrentPageFirstList <= 1) {
            activity_my_rewards_firstSmartRefreshLayout.finishRefresh()
        } else {
            activity_my_rewards_firstSmartRefreshLayout.finishLoadMore()
        }

        activity_my_rewards_firstSmartRefreshLayout.setNoMoreData(mFirstTotalCount <= mFirstRewardListAdapter.data!!.size)
    }
    private fun entryRewardListLoadingComplete() {
        if (mCurrentPageEntryList <= 1) {
            activity_my_rewards_entrySmartRefreshLayout.finishRefresh()
        } else {
            activity_my_rewards_entrySmartRefreshLayout.finishLoadMore()
        }

        activity_my_rewards_entrySmartRefreshLayout.setNoMoreData(mEntryTotalCount <= mEntryRewardListAdapter.data!!.size)
    }

    override fun initData() {
        super.initData()
        getRewardInfo(rewardType_cur)
    }

    private fun getLastDayOfDate(year:Int,month:Int):Long{
        var dateFormat = "yyyy年MM月dd日 HH:mm:ss"
        var cal = Calendar.getInstance()
        cal.set(Calendar.YEAR,year)
        cal.set(Calendar.MONTH,month - 1)
        var lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        return FormatUtils.string2TimeStamp(dateFormat,"${year}年${month}月${lastDay}日 23:59:59")
    }

    private fun getRewardInfo(rewardType:Int){
        var dateFormat = "yyyy年MM月dd日 HH:mm:ss"
        var from = FormatUtils.string2TimeStamp(dateFormat,"${mSelectedDate!!}1日 00:00:00")
        //获取月份最后一天
        var to = getLastDayOfDate(selectedYear!!,selectedMonth!!)
        var map = HashMap<String,String>()
        map["reward_date_from"] = "$from"
        map["reward_date_to"] = "$to"
        map["agent_id"] = mAgentInfo.agentId
        map["page_size"] = EnumValue.PAGE_SIZE
        when(rewardType){
            rewardType_cur -> {
                map["page_no"] = "$mCurrentPage"
                mPresenter.getCurLvRewardInfo(map)
                mPresenter.getTotalRewardAmount(rewardType_cur,from,to)
            }
            rewardType_lower -> {
                map["page_no"] = "$mCurrentPageLowerList"
                mPresenter.getLowerLvRewardInfo(map)
                mPresenter.getTotalRewardAmount(rewardType_lower,from,to)
            }
            rewardType_first -> {
                map["page_no"] = "$mCurrentPageFirstList"
                mPresenter.getFirstRewardInfo(map)
                mPresenter.getTotalRewardAmount(rewardType_first,from,to)
            }
            rewardType_entry -> {
                map["page_no"] = "$mCurrentPageFirstList"
                map["reward_type"] = "0"    //提成类型: 0 - 入职提成, 1 - 在职提成, 2 - 邀请提成
                mPresenter.getEntryRewardInfo(map)
            }
        }
    }

    override fun onCurLvRewardGetSuccess(body: CommonListBody<HierarchicalRewardInfo>) {
        mCurLvTotalCount = body.totalCount
        if (mCurrentPage == 1) mCurRewardList.clear()
        mCurRewardList.addAll(body.items)
        mCurLvRewardListAdapter.notifyDataSetChanged()
        loadingComplete()
    }

    override fun onCurLvRewardGetFailure() {
        loadingComplete()
    }

    override fun onLowerLvRewardGetSuccess(body: CommonListBody<HierarchicalRewardInfo>) {
        mLowerLvTotalCount = body.totalCount
        if (mCurrentPageLowerList == 1) mLowerRewardList.clear()
        mLowerRewardList.addAll(body.items)
        mLowerLvRewardListAdapter.notifyDataSetChanged()
        lowerListLoadingComplete()
    }

    override fun onLowervRewardGetFailure() {
        lowerListLoadingComplete()
    }

    override fun onFirstRewardGetSuccess(body: CommonListBody<FirstRewardInfo>) {
        mFirstTotalCount = body.totalCount
        if (mCurrentPageFirstList == 1) mFirstRewardList.clear()
        mFirstRewardList.addAll(body.items)
        mFirstRewardListAdapter.notifyDataSetChanged()
        firstRewardListLoadingComplete()
    }

    override fun onFirstRewardGetFailure() {
        firstRewardListLoadingComplete()
    }

    override fun onEntryRewardGetSuccess(body: CommonListBody<HierarchicalRewardInfo>) {
        mEntryTotalCount = body.totalCount
        if (mCurrentPageEntryList == 1) mEntryRewardList.clear()
        mEntryRewardList.addAll(body.items)
        mEntryRewardListAdapter.notifyDataSetChanged()
        entryRewardListLoadingComplete()
    }

    override fun onEntryRewardGetFailure() {
        entryRewardListLoadingComplete()
    }

    private var mDatePickPop: Dialog? = null
    fun showDatePickPop() {
        if (mDatePickPop == null) {
            mDatePickPop = AlertDialog.Builder(this).create()
        }
        mDatePickPop!!.show()
        mDatePickPop!!.setCanceledOnTouchOutside(false)
        val view = LayoutInflater.from(this).inflate(R.layout.pop_reward_datepicker, null)
        mDatePickPop!!.window!!.setContentView(view)
        mDatePickPop!!.window!!.setBackgroundDrawable(ColorDrawable())
        var confirmBtn = view.findViewById<AppCompatButton>(R.id.pop_reward_datepick_submit_btn)
        var cancelBtn = view.findViewById<AppCompatButton>(R.id.pop_reward_datepick_cancel_btn)
        var wheelView1 = view.findViewById<MyWheelView>(R.id.pop_reward_datepick_yearWheelView)
        var wheelView2 = view.findViewById<MyWheelView>(R.id.pop_reward_datepick_monthWheelView)

        var mOptionsItems1 = ArrayList<String>()
        var mOptionsItems2 = ArrayList<String>()
        var curYear = Calendar.getInstance().get(Calendar.YEAR)
        var curMonth = Calendar.getInstance().get(Calendar.MONTH) + 1   //因为Calendar month从0开始
        mOptionsItems1.add("${curYear}年")

        for (i in 0 until curMonth){
            mOptionsItems2.add("${i+1}月")
        }
        wheelView1.setCyclic(false)
        wheelView2.setCyclic(false)
        wheelView1.adapter = ArrayWheelAdapter(mOptionsItems1)
        wheelView2.adapter = ArrayWheelAdapter(mOptionsItems2)
        wheelView2.initPosition = mOptionsItems2.size-1
        selectedYear = curYear
        selectedMonth = mOptionsItems2[mOptionsItems2.size-1].replace("月","").toInt()

        wheelView1.setOnItemSelectedListener(object :OnItemSelectedListener{
            override fun onItemSelected(index: Int) {
                selectedYear = mOptionsItems1[index].replace("年","").toInt()
//                showToast("$selectedYear")
            }

        })
        wheelView2.setOnItemSelectedListener(object :OnItemSelectedListener{
            override fun onItemSelected(index: Int) {
                selectedMonth = mOptionsItems2[index].replace("月","").toInt()
//                showToast("$selectedMonth")
            }

        })
        confirmBtn.setOnClickListener {
            mDatePickPop!!.dismiss()
            mSelectedDate = "${selectedYear}年${selectedMonth}月"
            activity_my_rewards_month_filter_tv.text = mSelectedDate
            getRewardInfo(mCurrentRewardType)
        }
        cancelBtn.setOnClickListener { mDatePickPop!!.dismiss() }
    }

    override fun onTotalRewardGetSuccess(amount: Double) {
        activity_my_rewards_totalMonthReward_tv.text = "收入：${StringUtils.doubleFormat(amount)}元"
    }

    override fun onTotalRewardGetFailure() {
        activity_my_rewards_totalMonthReward_tv.text = "收入：￥元"
    }
}