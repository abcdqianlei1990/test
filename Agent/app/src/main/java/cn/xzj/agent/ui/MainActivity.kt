package cn.xzj.agent.ui

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.support.annotation.RequiresApi
import android.support.design.widget.TabLayout
import android.view.KeyEvent
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import cn.xzj.agent.R
import cn.xzj.agent.constants.Code
import cn.xzj.agent.constants.Keys
import cn.xzj.agent.core.common.BaseFragment
import cn.xzj.agent.core.common.mvp.MVPBaseActivity
import cn.xzj.agent.entity.agentinfo.AgentInfo
import cn.xzj.agent.entity.privacy.InstalledAppUploadBody
import cn.xzj.agent.iview.IMainView
import cn.xzj.agent.presenter.MainPresenter
import cn.xzj.agent.service.PhoneListenerService
import cn.xzj.agent.ui.customerres.ResPurchaseActivity
import cn.xzj.agent.ui.fragment.*
import cn.xzj.agent.ui.message.MsgListActivity
import cn.xzj.agent.util.APPUpdateUtil
import cn.xzj.agent.util.LogLevel
import cn.xzj.agent.util.SharedPreferencesUtil
import cn.xzj.agent.widget.CommonDialog
import cn.xzj.agent.widget.SimpleToast
import com.channey.utils.SharedPreferencesUtils
import com.channey.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : MVPBaseActivity<MainPresenter>(), TabLayout.OnTabSelectedListener, IMainView {
    private val TAG = MainActivity::class.java.simpleName
    private lateinit var mHomeFragment: HomeFragment
    private lateinit var mCustomerFragment: CustomerListFragment
    private lateinit var mMsgListFragment: MsgListActivity
    private lateinit var mUserCenterFragment: UserCenterFragment
//    private lateinit var mNewYearJobFragment: NewYearReserveFragment
    private lateinit var mJobFragment: CompanyJobFragment
    private lateinit var mResPurchaseFragment: ResPurchaseActivity
    private var mFragments = ArrayList<BaseFragment>()

    private var agentInfo: AgentInfo? = null
    private var mCurrentTab: Int = 0
    private val TAB_HOME = 0
    private val TAB_MSG = 1
    private val TAB_USER = 2
    private var IMEI: String? = null
    private var UIN: String? = null
    private var CUR_DB_PATH: String = ""

    companion object {
        fun jump(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun context(): Context {
        return this
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initParams() {
        mHomeFragment = HomeFragment()
        mCustomerFragment = CustomerListFragment()
        mMsgListFragment = MsgListActivity()
        mUserCenterFragment = UserCenterFragment()
//        mNewYearJobFragment = NewYearReserveFragment()
        mJobFragment = CompanyJobFragment()
        mResPurchaseFragment = ResPurchaseActivity()
//        mFragments.add(mHomeFragment)
        mFragments.add(mCustomerFragment)
        mFragments.add(mMsgListFragment)
//        mFragments.add(mResPurchaseFragment)
//        mFragments.add(mJobFragment)
        mFragments.add(mUserCenterFragment)
    }

    override fun initViews() {
        hideToolbar()
        if (SharedPreferencesUtil.getCurrentAgentInfo(this) != null) {
            initNavigation()
            startPhoneListenService()
        } else {
            CommonDialog.newBuilder(this)
                    .setCancelable(false)
                    .setMessage("登录失效，请重新登录")
                    .setPositiveButton(this.resources.getString(R.string.confirm)) { dialog ->
                        dialog.dismiss()
                        SharedPreferencesUtil.clear(this)
                        LoginActivity.jump(this)
                    }
                    .create()
                    .show()
        }
        com.jaeger.library.StatusBarUtil.setColor(this,resources.getColor(R.color.green29AC3E),0)
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun initData() {
//        initWxMonitor()
//        WxMonitorUtils.execCmd("chmod -R 777 $WX_ROOT_PATH", object : OnCmdExecSuccessListener {
//            override fun onFailure() {
//                startUserPrivacyService()
//            }
//
//            override fun onSuccess() {
//                //get wechat uin
//                WxMonitorUtils.copyFile("${Constants.WX_DB_DIR_PATH}/${WxMonitorUtils.systemInfoFileName}"
//                        , "/data/data/$packageName/${WxMonitorUtils.systemInfoFileName}", object : DBFileInitSuccessListener {
//                    override fun onSuccess() {
//                        UIN = WxMonitorUtils.getCfgInfo("/data/data/$packageName/${WxMonitorUtils.systemInfoFileName}")
//                        SharedPreferencesUtils.saveString(this@MainActivity, Keys.UIN, UIN!!, Activity.MODE_MULTI_PROCESS)
////                        Log("UIN = $UIN")
//                        CUR_DB_PATH = "/data/data/$packageName/$COPY_WX_DATA_DB"
//                        if (!StringUtils.isEmpty(IMEI) && !StringUtils.isEmpty(UIN)) {
//                            startHeartBeatService()
//                        }
//                    }
//                })
//            }
//        })
    }

    private fun initNavigation() {
        activity_main_tab_layout.addTab(activity_main_tab_layout.newTab()
                .setCustomView(LayoutInflater.from(context()).inflate(R.layout.tablayout_main, null)))

        activity_main_tab_layout.addTab(activity_main_tab_layout.newTab()
                .setCustomView(LayoutInflater.from(context()).inflate(R.layout.tablayout_main, null)))

        activity_main_tab_layout.addTab(activity_main_tab_layout.newTab()
                .setCustomView(LayoutInflater.from(context()).inflate(R.layout.tablayout_main, null)))
        setTabLayoutSelect(0)
        activity_main_tab_layout.addOnTabSelectedListener(this)
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        setTabLayoutSelect(tab!!.position)
    }

    private fun setTabLayoutSelect(position: Int) {
        for (tabIndex in 0 until activity_main_tab_layout.tabCount) {
            val mView = activity_main_tab_layout.getTabAt(tabIndex)!!.customView
            val mTextView = mView!!.findViewById<TextView>(R.id.tv_tab_layout_title)
            val mImageIcon = mView.findViewById<ImageView>(R.id.iv_tab_layout_icon)
            when (tabIndex) {

                0 -> {
                    mTextView.text = resources.getString(R.string.tab_customer)
                    if (tabIndex == position) {
                        mImageIcon.setImageResource(R.mipmap.tab_customer_on)
                        mTextView.setTextColor(resources.getColor(R.color.green29AC3E))
                    } else {
                        mImageIcon.setImageResource(R.mipmap.tab_customer_off)
                        mTextView.setTextColor(resources.getColor(R.color.black969696))
                    }
                }
                1 -> {
                    mTextView.text = resources.getString(R.string.tab_msg)
                    if (tabIndex == position) {
                        mImageIcon.setImageResource(R.mipmap.home_tab_msg_active)
                        mTextView.setTextColor(resources.getColor(R.color.green29AC3E))
                    } else {
                        mImageIcon.setImageResource(R.mipmap.home_tab_msg_unactive)
                        mTextView.setTextColor(resources.getColor(R.color.black969696))
                    }
                }

                2 -> {
                    mTextView.text = resources.getString(R.string.tab_usercenter)
                    if (tabIndex == position) {
                        mImageIcon.setImageResource(R.mipmap.tab_user_on)
                        mTextView.setTextColor(resources.getColor(R.color.green29AC3E))
                    } else {
                        mImageIcon.setImageResource(R.mipmap.tab_user_off)
                        mTextView.setTextColor(resources.getColor(R.color.black969696))
                    }
                }
            }
        }
        mCurrentTab = position
        val transaction = supportFragmentManager.beginTransaction()
        for (i  in 0 until mFragments.size){
            var fragment = mFragments[i]
            if (i == position){
                if (!fragment.isAdded){
                    transaction.add(R.id.activity_main_content,fragment,"fragment$i")
                }
                transaction.show(fragment)
            }else{
                transaction.hide(mFragments[i])
            }
        }
        transaction.commitNowAllowingStateLoss()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Code.RequestCode.LOGIN -> {
                if (Code.ResultCode.OK == resultCode) {
//                    activity_main_navigation.selectTab(TAB_MSG)
                }
            }
        }
    }

    private var exitTime: Long = 0
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun exit() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            SimpleToast.showNormal("再按一次退出程序")
            exitTime = System.currentTimeMillis()
        } else {
            finish()
            System.exit(0)
        }
    }

    override fun onAgentInfoGetSuccess(agentInfo: AgentInfo) {
        //保存小职姐信息
        SharedPreferencesUtil.setCurrentAgentInfo(this, agentInfo)
    }

    override fun onAgentInfoGetFailure() {
    }

    private fun startPhoneListenService() {
        val token = SharedPreferencesUtil.getToken(this)
        val mPhoneListenerServiceIntent = Intent(this, PhoneListenerService::class.java)
        mPhoneListenerServiceIntent.putExtra(Keys.TOKEN,token)
        bindService(mPhoneListenerServiceIntent, mPhoneListenServiceConnection, BIND_AUTO_CREATE)
    }

    private var mPhoneListenServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(p0: ComponentName?) {
            LogLevel.i(TAG, "电话监听服务-断开")
        }

        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            LogLevel.i(TAG, "电话监听服务绑定成功")
        }
    }


    @SuppressLint("SdCardPath")
    override fun onResume() {
        super.onResume()
        APPUpdateUtil.checkUpdate(this)
        mPresenter.getAgentInfo()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        agentInfo = SharedPreferencesUtil.getCurrentAgentInfo(this)
        if (agentInfo != null) {
            val installedAppUploadBody = InstalledAppUploadBody()
            val mDeviceInfo = SharedPreferencesUtil.getDeviceInfo(this)
            if (mDeviceInfo != null)
                installedAppUploadBody.deviceId = mDeviceInfo.id
            installedAppUploadBody.apps = getAllInstallApp()
            mPresenter.uploadAllInstallApp(agentInfo!!.agentId, installedAppUploadBody)
        }
    }

    private fun getAllInstallApp(): List<String> {
        val apps = packageManager.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES)
        val appNames = ArrayList<String>()
        if (apps != null) {
            for (app in apps) {
                if (app.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM == 0) {
                    appNames.add(app.applicationInfo.loadLabel(packageManager).toString())
                }
            }
        }
        return appNames
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(mPhoneListenServiceConnection)
    }
}