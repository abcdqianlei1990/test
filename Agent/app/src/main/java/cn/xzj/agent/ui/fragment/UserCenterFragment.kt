package cn.xzj.agent.ui.fragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.view.View
import cn.xzj.agent.R
import cn.xzj.agent.constants.Code
import cn.xzj.agent.constants.Constants
import cn.xzj.agent.constants.EnumValue
import cn.xzj.agent.core.common.mvp.MVPBaseFragment
import cn.xzj.agent.entity.agentinfo.AgentInfo
import cn.xzj.agent.entity.common.ShareInfo
import cn.xzj.agent.entity.common.ShareMenuInfo
import cn.xzj.agent.entity.share.WechatHongbaoShareInfo
import cn.xzj.agent.iview.IMineView
import cn.xzj.agent.net.Client
import cn.xzj.agent.presenter.MinePresenter
import cn.xzj.agent.ui.LoginActivity
import cn.xzj.agent.ui.MainActivity
import cn.xzj.agent.ui.UserRegisterActivity
import cn.xzj.agent.ui.adapter.common.ShareMenuListAdapter
import cn.xzj.agent.ui.agent.AgentProfileActivity
import cn.xzj.agent.ui.customerres.ResPurchaseActivity
import cn.xzj.agent.ui.goldenbeans.MyGoldenBeansActivity
import cn.xzj.agent.ui.mine.*
import cn.xzj.agent.ui.upload_wx_screenshot.UploadWxScreenshotActivity
import cn.xzj.agent.util.*
import cn.xzj.agent.widget.CommonDialog
import cn.xzj.agent.widget.GlideCircleTransform
import com.bumptech.glide.Glide
import com.channey.utils.StringUtils
import kotlinx.android.synthetic.main.fragment_usercenter.*

class UserCenterFragment : MVPBaseFragment<MinePresenter>(), View.OnClickListener, IMineView {

    private lateinit var mActivity: MainActivity
    private var shareDialog: CommonDialog? = null
    private var mWechatHongbaoShareInfo: WechatHongbaoShareInfo? = null
    private val REPOSITORY_URL="http://wiki.xiaozhijie.com/wordpress"//小职姐知识库地址
    private var mAgentInfo:AgentInfo ?= null
    override fun context(): Context {
        return context!!
    }

    override fun initLayout(): Int {
        return R.layout.fragment_usercenter
    }

    override fun initParams() {
        mActivity = activity as MainActivity
    }

    override fun initViews() {
        fragment_usercenter_register_group.setOnClickListener(this)
        fragment_usercenter_upload_wx_number_btn.setOnClickListener(this)
        fragment_usercenter_qrcode_group.setOnClickListener(this)
        fragment_usercenter_avatar_img.setOnClickListener(this)
        btn_scan.setOnClickListener(this)
        fragment_usercenter_HongBao_btn.setOnClickListener(this)
        fragment_usercenter_chat_btn.setOnClickListener(this)
        smartRefreshLayout.setOnRefreshListener {
            requestApi()
        }
        fragment_usercenter_repository_btn.setOnClickListener(this)
        fragment_usercenter_feedback_btn.setOnClickListener(this)
        fragment_usercenter_my_team_btn.setOnClickListener(this)
        fragment_usercenter_my_reward_btn.setOnClickListener(this)
        fragment_usercenter_entryInvite_btn.setOnClickListener(this)
        fragment_usercenter_my_beans_btn.setOnClickListener(this)
        fragment_usercenter_buy_res_btn.setOnClickListener(this)
        rl_top_parent.setOnClickListener(this)
        fragment_usercenter_logout_btn.setOnClickListener(this)
    }

    override fun initData() {

    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rl_top_parent -> {
                if (mAgentInfo != null) AgentProfileActivity.jump(mActivity,mAgentInfo!!)
            }
            R.id.fragment_usercenter_logout_btn -> {
                loginOut()
            }
            R.id.fragment_usercenter_register_group -> {
                UserRegisterActivity.jump(context!!)
                CommonUtils.statistics(context, Constants.STATISTICS_MY_REGISTER_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)

            }
            R.id.fragment_usercenter_entryInvite_btn -> {
                var list = ArrayList<ShareMenuInfo>()
                list.add(ShareMenuInfo(R.mipmap.ic_share_wechat, "微信", ShareUtil.SHARE_TYPE_WECHAT))
                list.add(ShareMenuInfo(R.mipmap.ic_share_moments, "微信朋友圈", ShareUtil.SHARE_TYPE_MOMENTS))
                DialogUtil.showSharePopWindow(context!!,list, ShareMenuListAdapter.ShareMenuItemClickListener { view, position, platform ->
                    var shareInfo = ShareInfo()
                    shareInfo.title = "加入兼职小职姐"
                    shareInfo.content = "加入兼职小职姐，带你赚钱带你飞"
                    shareInfo.imgUrl = "https://assets.xiaozhijie.com/xzj/logo.png"
                    shareInfo.targetUrl = "${Client.H5_RES_HOST}activity/ziyoujianzhi"
                    shareInfo.targetPlatform = platform
                    ShareUtil.share(context!!,shareInfo,object :ShareUtil.ShareSuccessListener{
                        override fun onShareSuccess(shareType:String) {
                            DialogUtil.dismissShareMenuDialog()
                        }
                    })
                })
            }
            R.id.fragment_usercenter_my_team_btn -> {
                if (mAgentInfo != null) MyTeamActivity.jump(mActivity,mAgentInfo!!.agentId,mAgentInfo!!.upperLevelAgentId)
            }
            R.id.fragment_usercenter_my_reward_btn -> {
                if (mAgentInfo != null) MyRewardsActivity.jump(mActivity,mAgentInfo!!)
            }
            R.id.fragment_usercenter_upload_wx_number_btn -> {
                startActivity(Intent(context, UploadWxScreenshotActivity::class.java))
                CommonUtils.statistics(context, Constants.STATISTICS_MY_uploadWechat_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
            }
            R.id.fragment_usercenter_qrcode_group -> {
                startActivity(Intent(context, MyQRCodeActivity::class.java))
                CommonUtils.statistics(context, Constants.STATISTICS_MY_QRCODE_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
            }
            R.id.fragment_usercenter_avatar_img -> {
            }
            R.id.btn_scan -> {
                startActivity(Intent(context, ScanActivity::class.java))
                CommonUtils.statistics(context, Constants.STATISTICS_MY_SCAN_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
            }
            R.id.fragment_usercenter_HongBao_btn -> {
                if (mWechatHongbaoShareInfo == null)
                    mPresenter.getWechatHongbaoInfo()
                else {
                    showShareDialog()
                }
            }
            R.id.fragment_usercenter_chat_btn ->{
                startActivity(Intent(context, RobotChatActivity::class.java))
                CommonUtils.statistics(context, Constants.STATISTICS_MY_problemReply_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
            }
            R.id.fragment_usercenter_repository_btn ->{
                toLookRepository()
                CommonUtils.statistics(context, Constants.STATISTICS_MY_blogRepository_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
            }
            R.id.fragment_usercenter_feedback_btn->{
                startActivity(Intent(context, FeedbackActivity::class.java))
                CommonUtils.statistics(context, Constants.STATISTICS_MY_problemFeedback_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)

            }
            R.id.fragment_usercenter_my_beans_btn -> {
                MyGoldenBeansActivity.jump(mActivity,mBeansCount)
                CommonUtils.statistics(context, Constants.STATISTICS_MY_GOLDENBEANS_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
            }
            R.id.fragment_usercenter_buy_res_btn -> {
                ResPurchaseActivity.jump(mActivity)
                CommonUtils.statistics(context, Constants.STATISTICS_RES_PURCHASE_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
            }
        }
    }

    private fun toLookRepository() {
        val mIntent=Intent(Intent.ACTION_VIEW)
        val mUri=Uri.parse(REPOSITORY_URL)
        mIntent.data = mUri
        context!!.startActivity(mIntent)

    }

    /**
     * 显示分享红包dialog
     */
    private fun showShareDialog() {
        if (shareDialog == null) {
            shareDialog = CommonDialog.newBuilder(context)
                    .setView(R.layout.dialog_share_hongbao)
                    .setCancelable(true)
                    .create()
            shareDialog!!.getView<View>(R.id.btn_dialog_share_hongbao_cancel).setOnClickListener {
                shareDialog!!.dismiss()
            }
            shareDialog!!.getView<View>(R.id.btn_dialog_share_hongbao_wechat_share).setOnClickListener {
                val url=if (!TextUtils.isEmpty(mWechatHongbaoShareInfo!!.targetUrl))
                        mWechatHongbaoShareInfo!!.targetUrl else mWechatHongbaoShareInfo!!.wxImage
                ShareUtils.shareXiaoChengXuToWeChat(context,mWechatHongbaoShareInfo!!.title,
                        mWechatHongbaoShareInfo!!.content,
                        mWechatHongbaoShareInfo!!.wxUserName,
                        mWechatHongbaoShareInfo!!.wxPath,
                        url,
                        null,
                        mWechatHongbaoShareInfo!!.wxImage)
                CommonUtils.statistics(context, Constants.STATISTICS_MY_REDPACKED_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
                shareDialog!!.dismiss()
            }
        }
        shareDialog!!.setBottomShow()
    }

    private fun loginOut() {

        CommonDialog.newBuilder(context)
                .setMessage("确定要退出登录？")
                .setPositiveButton(context!!.resources.getString(R.string.confirm)) { dialog ->
                    dialog.dismiss()
                    SharedPreferencesUtil.clear(context)
                    LoginActivity.jump(context!!)
                }.setNegativeButton(context!!.resources.getString(R.string.cancel), null)
                .create()
                .show()
    }

    override fun onAgentInfoGetSuccess(agentInfo: AgentInfo) {
        smartRefreshLayout.isRefreshing = false
        injectViews(agentInfo)
        SharedPreferencesUtil.setCurrentAgentInfo(context, agentInfo)
    }

    override fun onAgentInfoGetFailure() {
        smartRefreshLayout.isRefreshing = false
        if (SharedPreferencesUtil.getCurrentAgentInfo(context) != null) {
            injectViews(SharedPreferencesUtil.getCurrentAgentInfo(context))
        }
    }

    override fun onGetWechatHongbaoFail() {
    }

    override fun onGetWechatHongbaoSuccess(wechatHongbaoShareInfo: WechatHongbaoShareInfo) {
        mWechatHongbaoShareInfo = wechatHongbaoShareInfo
        showShareDialog()
    }

    private fun injectViews(info: AgentInfo) {
        mAgentInfo = info
        fragment_usercenter_name_tv.text = if (!StringUtils.isEmpty(info.nick)) info.nick else info.name + ""
        if (info.phone != null)
            fragment_usercenter_phone_tv.text = "手机：${info.phone}"
        if (StringUtils.isNotEmpty(info.wechat)) {
            fragment_usercenter_wx_tv.text = "微信：${info.wechat}"
            fragment_usercenter_wx_tv.visibility = View.VISIBLE
        }else{
            fragment_usercenter_wx_tv.visibility = View.GONE
        }
        if (info.headImages != null) {
            for (img in info.headImages) {
                if (img.isSelected) {
                    Glide.with(this)
                            .load(img.url)
                            .placeholder(R.mipmap.usercenter_avatar)
                            .transform(GlideCircleTransform())
                            .error(R.mipmap.usercenter_avatar)
                            .into(fragment_usercenter_avatar_img)
                    break
                }
            }
        }
        //是否上传微信好友截图
        tv_is_upload_wx_scrrentshot.text = if (info.isDailyWXFriendCountUploaded) "今日已上传微信数" else "今日未上传微信数"
    }

    override fun onResume() {
        super.onResume()
        requestApi()
        CommonUtils.statistics(context, Constants.STATISTICS_MY_ID, EnumValue.STATISTICS_TYPE_PAGE, EnumValue.STATISTICS_METRIC_VIEW)
    }

    private fun requestApi(){
        mPresenter.getAgentInfo()
        if (fragment_usercenter_my_beans_btn.visibility == View.VISIBLE)mPresenter.getGoldenBeansCount()
    }

    private var mBeansCount:Int = 0
    override fun onGoldenBeansCountGetSuccess(count: Int) {
        if (count > 0) {
            mBeansCount = count
            iv_my_beans_count_tv.text = "${count}金豆"
        }
    }

    override fun onGoldenBeansCountGetFail() {

    }

}