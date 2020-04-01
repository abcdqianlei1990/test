package cn.xzj.agent.ui.newyear

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import cn.xzj.agent.R
import cn.xzj.agent.constants.Constants
import cn.xzj.agent.constants.EnumValue
import cn.xzj.agent.core.common.QuickActivity
import cn.xzj.agent.entity.DefaultEventMessage
import cn.xzj.agent.util.CommonUtils
import cn.xzj.agent.util.KeyBoardUtil
import cn.xzj.agent.util.StatusBarUtil
import cn.xzj.agent.widget.SimpleToast
import kotlinx.android.synthetic.main.activity_newyear_reservation_search.*
import org.greenrobot.eventbus.EventBus

/**
 * @Author MarkYe
 * @Email yrmao9893@163.com
 * @Date 2018/12/29
 * @Des
 */
class NewYearReservationSearchActivity : QuickActivity() {
    companion object {
        val SEARCH_REQUEST=1001
    }
    override fun getLayoutId(): Int {
        return R.layout.activity_newyear_reservation_search
    }

    override fun initViews() {
        hideToolbar()
        StatusBarUtil.setPadding(this, ll_newyear_search_top)
    }

    override fun onResume() {
       super.onResume()
        CommonUtils.statistics(this, Constants.STATISTICS_chineseNewYearReserveCustomerSearch_ID, EnumValue.STATISTICS_TYPE_PAGE, EnumValue.STATISTICS_METRIC_VIEW)

    }

    override fun setListeners() {
        super.setListeners()
        iv_newyear_back.setOnClickListener {
            finish()
        }
        ic_search_delete.setOnClickListener {
            //输入框删除数据
            et_search.setText("")
        }
        et_search.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val value=p0!!.trim()
                if (value.isEmpty()){
                    ic_search_delete.visibility=View.INVISIBLE
                }else{
                    ic_search_delete.visibility=View.VISIBLE
                }
            }

        })
        et_search.setOnEditorActionListener(TextView.OnEditorActionListener { p0, p1, p2 ->
            if (p1 == EditorInfo.IME_ACTION_SEARCH) {
               var term=p0.text.toString().trim()
                //监听搜索动作
                if (TextUtils.isEmpty(p0.text.toString().trim())) {
                    SimpleToast.showShort(p0.hint.toString())
                    return@OnEditorActionListener true
                }
                KeyBoardUtil.close(this)
                EventBus.getDefault().post(DefaultEventMessage(SEARCH_REQUEST,"搜索数据",term))
                CommonUtils.statistics(this, Constants.STATISTICS_chineseNewYearReserveCustomerSearch_BTN_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
                finish()
                return@OnEditorActionListener true
            }
            false
        })
    }

}
