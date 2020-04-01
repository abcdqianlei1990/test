package cn.xzj.agent.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import cn.xzj.agent.R
import cn.xzj.agent.constants.Constants
import cn.xzj.agent.constants.EnumValue
import cn.xzj.agent.core.common.mvp.MVPBaseFragment
import cn.xzj.agent.entity.agentinfo.AgentInfo
import cn.xzj.agent.entity.task.HomeRefreshTaskMessage
import cn.xzj.agent.entity.task.TaskDateInfo
import cn.xzj.agent.entity.task.TaskTypeInfo
import cn.xzj.agent.i.OnRecyclerViewItemClickListener
import cn.xzj.agent.iview.IHomeView
import cn.xzj.agent.presenter.HomePresenter
import cn.xzj.agent.ui.adapter.PopListAdapter
import cn.xzj.agent.ui.adapter.ViewAdapter
import cn.xzj.agent.ui.message.MsgListActivity
import cn.xzj.agent.util.CommonUtils
import cn.xzj.agent.util.SharedPreferencesUtil
import cn.xzj.agent.util.StatusBarUtil
import com.channey.utils.FormatUtils
import kotlinx.android.synthetic.main.fragment_home.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*


class HomeFragment : MVPBaseFragment<HomePresenter>(), View.OnClickListener, IHomeView {


    private var mPopupWindow: PopupWindow? = null
    private lateinit var mSelectedTaskDateInfo: TaskDateInfo
    private var mTaskTypes = ArrayList<TaskTypeInfo>()
    private var mFragmentList = ArrayList<TaskListFragment>()
    private lateinit var mViewAdapter: ViewAdapter
    private var mAgentInfo: AgentInfo? = null
    private var mTaskDateInfoList = ArrayList<TaskDateInfo>()

    companion object {
        const val TASK_TYPE = "typeInfo"
        const val TASK_DATE_INFO = "TASK_DATE_INFO"
        private const val YESTERDAY_INDEX = 5//昨天下标
        private const val TODAY_INDEX = 6//今天下标
    }

    override fun context(): Context {
        return context!!
    }

    override fun initLayout(): Int {
        return R.layout.fragment_home
    }

    override fun initParams() {
    }

    override fun initViews() {
        statusLayoutFragmentHome.showLoading()
        statusLayoutFragmentHome.setOnRetryListener {
            statusLayoutFragmentHome.showLoading()
            getTaskTypes()
        }
        smartRefreshLayout.setOnRefreshListener {
            getTaskTypes()
            mPresenter.getUnreadMsg()
        }
        initViewPager()


    }

    private fun initTaskDateData() {
        val tomorrowTimestamp = System.currentTimeMillis() + 24 * 3600 * 1000//明天的时间戳
        for (i in 7 downTo 0) {
            Log.w("tag", "$i")
            val itemTimestamp = tomorrowTimestamp - 24 * 3600 * 1000 * i
            val date = FormatUtils.timeStamp2String(itemTimestamp, EnumValue.DATE_FORMAT_3)
            val from = FormatUtils.string2TimeStamp(EnumValue.DATE_FORMAT_4, "$date 00:00:00").toString()
            val to = FormatUtils.string2TimeStamp(EnumValue.DATE_FORMAT_4, "$date 23:59:59").toString()
            val shortDate = FormatUtils.timeStamp2String(itemTimestamp, EnumValue.DATE_FORMAT_2)
            when (i) {
                7 ->
                    mTaskDateInfoList.add(TaskDateInfo(date, shortDate, from, to, itemTimestamp, shortDate + "任务"))

                6 ->
                    mTaskDateInfoList.add(TaskDateInfo(date, shortDate, from, to, itemTimestamp, shortDate + "任务"))

                5 ->
                    mTaskDateInfoList.add(TaskDateInfo(date, shortDate, from, to, itemTimestamp, shortDate + "任务"))

                4 ->
                    mTaskDateInfoList.add(TaskDateInfo(date, shortDate, from, to, itemTimestamp, shortDate + "任务"))

                3 ->
                    mTaskDateInfoList.add(TaskDateInfo(date, shortDate, from, to, itemTimestamp, shortDate + "任务"))

                2 -> {
                    mTaskDateInfoList.add(TaskDateInfo(date, shortDate, from, to, itemTimestamp, "昨日任务"))
                }
                1 -> {
                    mTaskDateInfoList.add(TaskDateInfo(date, shortDate, from, to, itemTimestamp, "今日任务"))
                }
                0 -> {
                    mTaskDateInfoList.add(TaskDateInfo(date, shortDate, from, to, itemTimestamp, "明日任务"))
                }
            }

        }
    }


    override fun initData() {
        initTaskDateData()
        mSelectedTaskDateInfo = mTaskDateInfoList[TODAY_INDEX]
        setTaskDateTitle(TODAY_INDEX)
    }

    @SuppressLint("SetTextI18n")
    private fun setTaskDateTitle(selectPosition: Int) {
        if (selectPosition >= YESTERDAY_INDEX)
            fragment_home_date_tv.text = "${mSelectedTaskDateInfo.shortDate} ${mSelectedTaskDateInfo.title}"
        else {
            fragment_home_date_tv.text = mSelectedTaskDateInfo.title
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
    }

    override fun onResume() {
        super.onResume()
        getTaskTypes()
        mPresenter.getUnreadMsg()
        CommonUtils.statistics(context, Constants.STATISTICS_TASK_ID, EnumValue.STATISTICS_TYPE_PAGE, EnumValue.STATISTICS_METRIC_VIEW)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }

    override fun setListeners() {
        fragment_home_date_tv.setOnClickListener(this)
        fragment_home_msgIcon_tv.setOnClickListener(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onDefaultEvent(event: HomeRefreshTaskMessage) {
        getTaskTypes()
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.fragment_home_date_tv -> {
                showPop()
            }
            R.id.fragment_home_msgIcon_tv -> {
//                fragment_home_msgIconPoint_tv.visibility = View.GONE
//                MsgListActivity.jump(context!!)
//                CommonUtils.statistics(context, Constants.STATISTICS_TASK_MESSAGE_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showPop() {
        val popupView = LayoutInflater.from(context).inflate(R.layout.pop_list, null)
        val mRecyclerView = popupView.findViewById<RecyclerView>(R.id.pop_list_recyclerview)
        val adapter = PopListAdapter(context!!, mTaskDateInfoList)
        adapter.setOnRecyclerViewItemClickListener(object : OnRecyclerViewItemClickListener {
            override fun onClick(view: View, position: Int) {
                mSelectedTaskDateInfo = mTaskDateInfoList[position]
                setTaskDateTitle(position)
                dismissPopList()
                //重新请求所有任务
                getTaskTypes()
                CommonUtils.statistics(context, Constants.STATISTICS_TASK_changeDate_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
            }
        })
        mRecyclerView.adapter = adapter
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        mPopupWindow = PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        mPopupWindow!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#ffffff")))
        mPopupWindow!!.isFocusable = true
        mPopupWindow!!.isOutsideTouchable = true
        mPopupWindow!!.update()
        mPopupWindow!!.showAsDropDown(fragment_home_date_tv, 0, 20)
    }

    fun dismissPopList() {
        if (mPopupWindow != null) {
            mPopupWindow!!.dismiss()
        }
    }

    private fun initViewPager() {

        mViewAdapter = ViewAdapter(fragmentManager!!, mFragmentList, mTaskTypes)
        viewpager.adapter = mViewAdapter
        tablayout.setViewPager(viewpager)
    }

    /**
     * 获取到类型后，根据类型初始化任务列表
     */
    private fun initChildTaskFragment() {
        mFragmentList.clear()
        for (i in 0 until mTaskTypes.size) {
            val itemFragment = TaskListFragment()
            val bundle = Bundle()
            bundle.putSerializable(TASK_TYPE, mTaskTypes[i])
            bundle.putSerializable(TASK_DATE_INFO, mSelectedTaskDateInfo)
            itemFragment.arguments = bundle
            mFragmentList.add(itemFragment)
        }
        mViewAdapter.notifyDataSetChanged()
        viewpager.offscreenPageLimit = mFragmentList.size - 1//这段代码是解决viewpager和第三方tablayout嵌套问题
        tablayout.notifyDataSetChanged()
    }

    override fun onTaskTypesGetSuccess(content: List<TaskTypeInfo>) {
        smartRefreshLayout.isRefreshing = false
        mTaskTypes.clear()
        mTaskTypes.addAll(content)
        initChildTaskFragment()
        statusLayoutFragmentHome.showContent()
    }

    override fun onTaskTypesGetFailure(isNetWorkError: Boolean) {
        smartRefreshLayout.isRefreshing = false
        if (isNetWorkError)
            statusLayoutFragmentHome.showNetworkError()
        else
            statusLayoutFragmentHome.showLoadError()
    }

    override fun onUnreadMsgGetSuccess(content: Int) {
        if (content > 0) {
            fragment_home_msgIconPoint_tv.visibility = View.VISIBLE
        } else {
            fragment_home_msgIconPoint_tv.visibility = View.GONE
        }
    }

    override fun onUnreadMsgGetFailure() {
    }

    /**
     * 请求某个时间段的任务
     */
    private fun getTaskTypes() {
        if (mAgentInfo == null)
            mAgentInfo = SharedPreferencesUtil.getCurrentAgentInfo(context)
        val from = mSelectedTaskDateInfo.from
        val to = mSelectedTaskDateInfo.to
        mPresenter.getTaskTypes(from, to)
    }

}