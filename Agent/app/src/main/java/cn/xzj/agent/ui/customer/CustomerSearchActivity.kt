package cn.xzj.agent.ui.customer

import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import cn.xzj.agent.R
import cn.xzj.agent.constants.Constants
import cn.xzj.agent.constants.EnumValue
import cn.xzj.agent.constants.Keys
import cn.xzj.agent.entity.event.ActiveCustomerEventMessage
import cn.xzj.agent.entity.event.CoreCustomerEventMessage
import cn.xzj.agent.entity.event.NormalCustomerEventMessage
import cn.xzj.agent.i.OnRecyclerViewItemClickListener
import cn.xzj.agent.ui.BaseActivity
import cn.xzj.agent.ui.adapter.SearchHistoryAdapter
import cn.xzj.agent.util.CommonUtils
import com.channey.utils.SharedPreferencesUtils
import com.channey.utils.StringUtils
import kotlinx.android.synthetic.main.activity_customer_search.*
import org.greenrobot.eventbus.EventBus

/**
 * 搜索客户
 */
class CustomerSearchActivity : BaseActivity(), View.OnClickListener {
    private var mOriginalHistory = ArrayList<String>()  //所有历史
    private var mVisiableHistory = ArrayList<String>()  //当前显示的历史
    private var mHistorySet = HashSet<String>()
    private lateinit var mHistoryAdapter: SearchHistoryAdapter
    private var type: Int = TYPE_NOMARL

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.activity_customer_search_back_tv -> {
                finish()
            }
            R.id.activity_customer_search_view_delete -> {
                activity_customer_search_view.setText(null)
            }
        }
    }

    companion object {
        val TYPE_NOMARL = 0
        val TYPE_CORE = 1
        val TYPE_ACTIVE = 2
        fun jump(context: Context, type: Int) {
            var intent = Intent(context, CustomerSearchActivity::class.java)
            intent.putExtra(Keys.DATA_KEY_1, type)
            context.startActivity(intent)
        }
    }

    override fun initLayout(): Int {
        return R.layout.activity_customer_search
    }

    override fun initParams() {
        mHistoryAdapter = SearchHistoryAdapter(this, mVisiableHistory)
        type = intent.getIntExtra(Keys.DATA_KEY_1, 0)
    }

    override fun initViews() {
        initSearchView()
        initHistoryList()
    }

    override fun onResume() {
        super.onResume()
        when (type) {
            TYPE_NOMARL -> {
                CommonUtils.statistics(this, Constants.STATISTICS_customerSearch_ID, EnumValue.STATISTICS_TYPE_PAGE, EnumValue.STATISTICS_METRIC_VIEW)
            }
            TYPE_CORE -> {
                CommonUtils.statistics(this, Constants.STATISTICS_coreCustomerSearch_ID, EnumValue.STATISTICS_TYPE_PAGE, EnumValue.STATISTICS_METRIC_VIEW)
            }
            TYPE_ACTIVE -> {
                CommonUtils.statistics(this, Constants.STATISTICS_activeCustomerSearch_ID, EnumValue.STATISTICS_TYPE_PAGE, EnumValue.STATISTICS_METRIC_VIEW)
            }
        }
    }

    override fun initData() {
        mHistorySet = SharedPreferencesUtils.getStringSet(this, Keys.SEARCH_HISTORY) as HashSet<String>
        for (str in mHistorySet) {
            mOriginalHistory.add(str)
            mVisiableHistory.add(str)
        }
        mHistoryAdapter.notifyDataSetChanged()
    }

    override fun setListeners() {
        activity_customer_search_back_tv.setOnClickListener(this)
        activity_customer_search_view_delete.setOnClickListener(this)
    }

    private fun initSearchView() {
        activity_customer_search_view.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(newText: CharSequence?, start: Int, before: Int, count: Int) {
                if (newText != null) {
                    activity_customer_search_view_delete.visibility = View.VISIBLE
                    mVisiableHistory.clear()
                    for (str in mOriginalHistory) {
                        if (str.contains(newText)) {
                            mVisiableHistory.add(str)
                        }
                    }
                    mHistoryAdapter.notifyDataSetChanged()
                } else {
                    activity_customer_search_view_delete.visibility = View.GONE
                }
            }

        })
        activity_customer_search_view.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {//搜索按键action
//                    SystemUtil.hideKeyboard(this,activity_customer_search_view);

                    var str = activity_customer_search_view.text.toString().trim();
                    if (StringUtils.isEmpty(str)) {
                        return true;
                    }
                    mHistorySet.add(str)
                    if (!mOriginalHistory.contains(str)) mOriginalHistory.add(str)
                    back2List(str)
                    return true;
                }
                return false;
            }

        })
    }

    private fun initHistoryList() {
        var layoutManager = LinearLayoutManager(this)
        mHistoryAdapter.setOnRecyclerViewItemClickListener(object : OnRecyclerViewItemClickListener {
            override fun onClick(view: View, position: Int) {
                back2List(mVisiableHistory[position])
            }
        })
        activity_customer_search_history.adapter = mHistoryAdapter
        activity_customer_search_history.layoutManager = layoutManager
    }

    override fun onDestroy() {
        super.onDestroy()
        SharedPreferencesUtils.saveStringSet(this, Keys.SEARCH_HISTORY, mHistorySet)
    }

    private fun back2List(keyWord: String) {
//        var intent = Intent()
//        intent.putExtra(Keys.KEY_WORD,keyWord)
//        setResult(Code.ResultCode.OK,intent)
        when (type) {
            TYPE_NOMARL -> {
                val eventMessage = NormalCustomerEventMessage()
                eventMessage.content = keyWord
                EventBus.getDefault().postSticky(eventMessage)
            }
            TYPE_CORE -> {
                val eventMessage = CoreCustomerEventMessage()
                eventMessage.content = keyWord
                EventBus.getDefault().postSticky(eventMessage)
                CommonUtils.statistics(this, Constants.STATISTICS_coreCustomerSearch_BTN_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
            }
            TYPE_ACTIVE -> {
                val eventMessage = ActiveCustomerEventMessage()
                eventMessage.content = keyWord
                EventBus.getDefault().postSticky(eventMessage)
                CommonUtils.statistics(this, Constants.STATISTICS_activeCustomerSearch_BTN_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
            }
        }

        finish()
    }
}