package cn.xzj.agent.ui.customer.remark

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.util.TypedValue
import android.view.View
import cn.xzj.agent.R
import cn.xzj.agent.constants.Constants
import cn.xzj.agent.constants.EnumValue
import cn.xzj.agent.constants.Keys
import cn.xzj.agent.core.common.QuickActivity
import cn.xzj.agent.entity.customer.RemarkVO
import cn.xzj.agent.ui.adapter.RemarkAdapter
import cn.xzj.agent.util.CommonUtils
import cn.xzj.agent.util.StatusBarUtil
import kotlinx.android.synthetic.main.activity_customer_remark.*

/**
 * 添加备注
 */
class RemarkActivity : QuickActivity(),View.OnClickListener{

    private lateinit var mUserInfoFragment: RemarkFragment
    private lateinit var mNoteFragment: NoteRemarkFragment
    open val TAB_USERINFO = 0
    open val TAB_NOTE = 1
    private var mCurrentTab:Int = TAB_USERINFO
    private var mUserId:String ?= null
    private var mUserRemarkInfo: RemarkVO?= null

    companion object {
        fun jumpForResult(activity: Activity,userId:String,userRemarkInfo:RemarkVO?,requestCode:Int){
            var intent = Intent(activity, RemarkActivity::class.java)
            intent.putExtra(Keys.USER_ID,userId)
            if (userRemarkInfo != null) intent.putExtra(Keys.USER_REMARK_INFO,userRemarkInfo)
            activity.startActivityForResult(intent,requestCode)
        }
    }
    override fun getLayoutId(): Int {
        return R.layout.activity_customer_remark
    }

    override fun initParams() {
        mUserInfoFragment = RemarkFragment()
        mNoteFragment = NoteRemarkFragment()
        mUserId = intent.getStringExtra(Keys.USER_ID)
        mUserRemarkInfo = intent.getSerializableExtra(Keys.USER_REMARK_INFO) as RemarkVO?
        if (mUserRemarkInfo != null){
            var bundle = Bundle()
            bundle.putSerializable(Keys.USER_REMARK_INFO,mUserRemarkInfo)
            mUserInfoFragment.arguments = bundle
        }
    }

    override fun initViews() {
        hideToolbar()
        initViewPager()
    }

    override fun initData() {

    }

    override fun onResume() {
        super.onResume()
        CommonUtils.statistics(this, Constants.STATISTICS_remarkAndNote_ID, EnumValue.STATISTICS_TYPE_PAGE, EnumValue.STATISTICS_METRIC_VIEW)
    }

    override fun setListeners() {
        title_tab_1.setOnClickListener(this)
        title_tab_2.setOnClickListener(this)
        title_back.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.title_tab_1 -> {
                selectTab(TAB_USERINFO)
            }
            R.id.title_tab_2 -> {
                selectTab(TAB_NOTE)
            }
            R.id.title_back ->{
                finish()
            }
        }
    }

    private fun initViewPager(){
        var list = ArrayList<Fragment>()
        list.add(mUserInfoFragment)
        list.add(mNoteFragment)
        var adapter = RemarkAdapter(supportFragmentManager,list)
        activity_customer_remark_vp.adapter = adapter
        activity_customer_remark_vp.addOnPageChangeListener(object :ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                mCurrentTab = position
                invalidateTabStyle(position)
            }

        })
        activity_customer_remark_vp.currentItem = TAB_USERINFO
    }

    private fun invalidateTabStyle(tab:Int){
        when(tab){
            TAB_USERINFO -> {
                title_tab_1.setTextSize(TypedValue.COMPLEX_UNIT_SP,18.0f)
                title_tab_2.setTextSize(TypedValue.COMPLEX_UNIT_SP,16.0f)
                title_tab_1.setTextColor(resources.getColor(R.color.green29AC3E))
                title_tab_2.setTextColor(resources.getColor(R.color.green9DE5A9))
            }
            TAB_NOTE -> {
                title_tab_2.setTextSize(TypedValue.COMPLEX_UNIT_SP,18.0f)
                title_tab_1.setTextSize(TypedValue.COMPLEX_UNIT_SP,16.0f)
                title_tab_2.setTextColor(resources.getColor(R.color.green29AC3E))
                title_tab_1.setTextColor(resources.getColor(R.color.green9DE5A9))
            }
        }
    }

    private fun selectTab(tab:Int){
        mCurrentTab = tab
        activity_customer_remark_vp.currentItem = tab
        invalidateTabStyle(tab)
    }

    fun getUserId():String?{ return mUserId}
}
