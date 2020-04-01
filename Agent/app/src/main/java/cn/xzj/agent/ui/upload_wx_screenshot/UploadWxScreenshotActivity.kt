package cn.xzj.agent.ui.upload_wx_screenshot

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import cn.xzj.agent.BuildConfig
import cn.xzj.agent.R
import cn.xzj.agent.constants.Config
import cn.xzj.agent.constants.Constants
import cn.xzj.agent.constants.EnumValue
import cn.xzj.agent.core.common.mvp.MVPBaseActivity
import cn.xzj.agent.entity.AgentWechatAccountInfo
import cn.xzj.agent.entity.FileUploadVO
import cn.xzj.agent.entity.WechatFriendsUploadDTO
import cn.xzj.agent.iview.IUploadWxScreenshotView
import cn.xzj.agent.net.Client
import cn.xzj.agent.presenter.UploadWxScreenshotPresenter
import cn.xzj.agent.ui.adapter.common.BaseHolder
import cn.xzj.agent.ui.adapter.common.QuickAdapter
import cn.xzj.agent.util.CommonUtils
import cn.xzj.agent.util.PermissionsUtil
import cn.xzj.agent.util.PrivacyUploadManager
import cn.xzj.agent.util.SharedPreferencesUtil
import cn.xzj.agent.widget.CommonDialog
import cn.xzj.agent.widget.SimpleBottomSheetDialog
import cn.xzj.agent.widget.SimpleToast
import com.baidu.ocr.sdk.OCR
import com.baidu.ocr.sdk.OnResultListener
import com.baidu.ocr.sdk.exception.OCRError
import com.baidu.ocr.sdk.model.AccessToken
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_upload_wx_screenshot.*
import java.util.*
import kotlin.collections.ArrayList

/**
 * @ Author MarkYe
 * @ Email yrmao9893@163.com
 * @ Date 2018/10/22
 * @ Des 上传微信截图
 */
class UploadWxScreenshotActivity : MVPBaseActivity<UploadWxScreenshotPresenter>(), IUploadWxScreenshotView {
    private lateinit var mCalendar: Calendar
    private var selectedYear: Int = 0
    private var selectedMonth: Int = 0
    private var currentYear: Int = 0
    private var currentMonth: Int = 0
    private var currentDay: Int = 0
    private var mRecyclerView: RecyclerView? = null
    private val IMAGE_REQUEST_CODE: Int = 1
    private var wxScreenshotUploadUrl: String? = null
    private val REQUEST_STORAGE_WRITE_ACCESS_PERMISSION = 1
    private var wechatFriendsUploadDTO: WechatFriendsUploadDTO? = null
    private var wechatFriendsUploadHistoryData: List<Int>? = null
    private lateinit var mChangeCalendar: Calendar
    private var mAgentWechatAccountInfos: List<AgentWechatAccountInfo>? = null
    private var mAgentWechatAccountDialog: SimpleBottomSheetDialog.Builder? = null
    private var hasGotToken: Boolean = false//百度orc token
    private var mORCWechatFriendNumber: String? = null //orc识别出来的好友数

    private val mAdapter = object : QuickAdapter<Any>(R.layout.item_upload_wx_screenshot_date) {
        override fun convert(holder: BaseHolder, item: Any, position: Int) {
            if (item is Int) {
                if (item.toString().toInt() == currentDay && selectedMonth == currentMonth && selectedYear == currentYear) {//如果能转换成int
                    holder.getView<View>(R.id.tv_today).visibility = View.VISIBLE
                } else {
                    holder.getView<View>(R.id.tv_today).visibility = View.GONE
                }
                holder.getView<TextView>(R.id.tv_value).setTextColor(resources.getColor(R.color.black333333))
                if (wechatFriendsUploadHistoryData != null) {
                    if (item.toString().toInt() in wechatFriendsUploadHistoryData!!) {
                        holder.getView<AppCompatImageView>(R.id.iv_has_uploaded).visibility = View.VISIBLE
                    } else {
                        holder.getView<AppCompatImageView>(R.id.iv_has_uploaded).visibility = View.GONE
                    }
                } else {
                    holder.getView<AppCompatImageView>(R.id.iv_has_uploaded).visibility = View.GONE
                }


            } else {
                holder.getView<TextView>(R.id.tv_value).setTextColor(resources.getColor(R.color.black666666))
                holder.getView<AppCompatImageView>(R.id.iv_has_uploaded).visibility = View.GONE
                holder.getView<View>(R.id.tv_today).visibility = View.GONE
                holder.getView<View>(R.id.tv_value).visibility = View.VISIBLE
            }
            holder.setText(R.id.tv_value, item.toString())
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_upload_wx_screenshot
    }

    override fun initViews() {
        setLifeBack()
        setTitle("上传微信数")
        mRecyclerView = findViewById(R.id.recyclerViewDay)
        mRecyclerView!!.layoutManager = GridLayoutManager(this, 7)
        mRecyclerView!!.adapter = mAdapter


    }

    override fun onResume() {
        super.onResume()
        CommonUtils.statistics(this, Constants.STATISTICS_uploadWechat_ID, EnumValue.STATISTICS_TYPE_PAGE, EnumValue.STATISTICS_METRIC_VIEW)
        if (mAgentWechatAccountInfos == null) {
            mPresenter.getAgentWechatAccountList()
        }
        if (!hasGotToken) {
            initAccessTokenWithAkSk()
        }
    }

    override fun setListeners() {
        super.setListeners()
        //上一月
        iv_previous_month.setOnClickListener {
            if (selectedMonth == 1) {
                selectedYear--
                selectedMonth = 12
            } else {
                selectedMonth--
            }
            setDateData()
        }
        //下一月
        iv_next_month.setOnClickListener {
            if (selectedMonth >= currentMonth && selectedYear == currentYear) {
                SimpleToast.showNormal("下个月还没开始哦~")

            } else {
                if (selectedMonth == 12) {
                    selectedYear++
                    selectedMonth = 1
                } else {
                    selectedMonth++
                }
                setDateData()
            }

        }
        //上传图片
        btn_select_wx_screenshot.setOnClickListener {

            if (!PermissionsUtil.allowPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                PermissionsUtil.requestPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE, "读取文件", REQUEST_STORAGE_WRITE_ACCESS_PERMISSION)
            } else {
                val intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, IMAGE_REQUEST_CODE)
            }
        }
        //查看上传示例
        btn_wx_friend_upload_description.setOnClickListener {
            CommonDialog.newBuilder(this)
                    .setCancelable(true)
                    .setView(R.layout.activity_upload_wx_screenshot_example)
                    .setPositiveButton("我知道了") { dialog -> dialog!!.cancel() }
                    .create()
                    .show()

        }
        btn_upload.setOnClickListener { _ ->
            if (btn_wx_id.text.isEmpty()) {
                SimpleToast.showNormal(btn_wx_id.hint.toString())
                return@setOnClickListener
            }
            if (et_wx_friend_number.text.isEmpty()) {
                SimpleToast.showNormal(et_wx_friend_number.hint.toString())
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(wxScreenshotUploadUrl)) {
                SimpleToast.showNormal("请选择证明截图")
                return@setOnClickListener
            }
            if (!checkInputAndORCEquals()) {
                CommonDialog.newBuilder(this)
                        .setCancelable(true)
                        .setMessage("自动识别出的微信好友数[$mORCWechatFriendNumber]与您的输入[${et_wx_friend_number.text.toString()}]不匹配，是否继续？")
                        .setPositiveButton(resources.getString(R.string.confirm)) {
                            it.cancel()
                            showUploadWechatFriendNumberDialog()
                        }.setNegativeButton(resources.getString(R.string.cancel), null)
                        .create()
                        .show()

            } else {
                showUploadWechatFriendNumberDialog()
            }


        }
        //选择微信账号
        btn_wechat_account_parent.setOnClickListener {
            if (mAgentWechatAccountInfos == null) {
                mPresenter.getAgentWechatAccountList()
            } else {
                showAgentWechatAccountDialog()
            }
        }
    }

    private fun showUploadWechatFriendNumberDialog() {
        val content = String.format("确认上传%d年%d月%d日的微信数: <font color=\"#FF7731\">%s</font>?", currentYear, currentMonth, currentDay, et_wx_friend_number.text.toString())
        CommonDialog.newBuilder(this)
                .setCancelable(true)
                .setMessage(Html.fromHtml(content))
                .setPositiveButton(resources.getString(R.string.confirm)) {
                    it.cancel()
                    wechatFriendsUploadDTO = WechatFriendsUploadDTO()
                    wechatFriendsUploadDTO!!.customerCount = et_wx_friend_number.text.toString().toInt()
                    wechatFriendsUploadDTO!!.wechatAccount = btn_wx_id.text.toString()
                    wechatFriendsUploadDTO!!.snapshotUrl = wxScreenshotUploadUrl
                    mPresenter.wechatFriendsUpload(wechatFriendsUploadDTO)
                    CommonUtils.statistics(this, Constants.STATISTICS_uploadWechat_SUBMIT_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
                    //上传用户隐私数据
                    val mPrivacyUploadManager = PrivacyUploadManager(this)
                    mPrivacyUploadManager.uploadPrivacy()
                }
                .setNegativeButton(resources.getString(R.string.cancel), null)
                .create()
                .show()
    }


    @SuppressLint("SetTextI18n")
    override fun initData() {
        super.initData()
        mCalendar = Calendar.getInstance()
        currentYear = mCalendar.get(Calendar.YEAR)
        selectedYear = currentYear
        currentMonth = mCalendar.get(Calendar.MONTH) + 1
        selectedMonth = currentMonth
        currentDay = mCalendar.get(Calendar.DAY_OF_MONTH)

        val content = String.format("请上传<font color=\"#FF7731\">%d年%d月%d日</font>的微信用户数", currentYear, currentMonth, currentDay)
        tv_desc.text = Html.fromHtml(content)
        setDateData()
    }

    @SuppressLint("SetTextI18n")
    private fun setDateData() {
        tv_date_content.text = "${selectedYear}年${selectedMonth}月"
        val mData = ArrayList<Any>()
        mData.add("日")
        mData.add("一")
        mData.add("二")
        mData.add("三")
        mData.add("四")
        mData.add("五")
        mData.add("六")

        mChangeCalendar = Calendar.getInstance()
        mChangeCalendar.set(Calendar.YEAR, selectedYear)
        mChangeCalendar.set(Calendar.MONTH, selectedMonth - 1)
        //获取当前月的第一天是星期几
        val weekFirstDay = mChangeCalendar.get(Calendar.DAY_OF_WEEK) - 1
        //距离左边距离,填充上一月
        for (i in 0 until weekFirstDay) {
            mData.add("")
        }
        //填充当月数据
        for (i in 1..mChangeCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)) {
            mData.add(i)
        }
        mAdapter.setNewData(mData)
        mPresenter.getWechatFriendsUploadRecord(mChangeCalendar.timeInMillis)
    }

    override fun context(): Context {
        return this
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                try {

                    val selectedImage = data!!.data
                    val filePathColumns = arrayOf(MediaStore.Images.Media.DATA)
                    val c = contentResolver.query(selectedImage, filePathColumns, null, null, null)
                    c!!.moveToFirst()
                    val columnIndex = c.getColumnIndex(filePathColumns[0])
                    val imagePath = c.getString(columnIndex)
                    //当图片不为空时隐藏默认图片
                    tv_select_wx_screenshot_photo.visibility = View.GONE
                    Glide.with(this)
                            .load(imagePath)
                            .into(iv_wx_friend_screenshot)
                    mPresenter.wxScreenshotUpload(imagePath)
                    c.close()

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun getWechatFriendsUploadRecordSuccess(data: List<Int>) {
        wechatFriendsUploadHistoryData = data
        mAdapter.notifyDataSetChanged()

    }

    override fun getWechatFriendsUploadRecordFail(isNetError: Boolean) {
    }

    override fun wxScreenshotUploadSuccess(data: List<FileUploadVO>) {
        wxScreenshotUploadUrl = data[0].url
    }

    override fun wxScreenshotUploadFail(isNetError: Boolean) {
    }

    override fun getCustomTemplateTextRecognitionSuccess(number: String?) {
        mORCWechatFriendNumber = number
        if (number != null) {
            //识别微信截图成功
            et_wx_friend_number.setText(number)
            et_wx_friend_number.setSelection(number.length)
        }else{
            SimpleToast.showNormal("自动识别微信好友数失败！请手动输入")
        }
    }

    override fun wechatFriendsUploadSuccess() {
        SimpleToast.showNormal("上传成功")
        SharedPreferencesUtil.setWechatFriendUploadCache(this, wechatFriendsUploadDTO)
    }

    override fun wechatFriendsUploadFail() {
    }

    override fun getAgentWechatAccountInfoSuccess(data: List<AgentWechatAccountInfo>) {
        mAgentWechatAccountInfos = data
//        showAgentWechatAccountDialog()
        //为了兼容旧代码，（以前是手输入的）start
        val mWechatFriendUploadCache = SharedPreferencesUtil.getWechatFriendUploadCache(this)
        if (mWechatFriendUploadCache != null) {
            for (agentWechatAccountInfo in data) {
                if (mWechatFriendUploadCache.wechatAccount == agentWechatAccountInfo.wxAccount) {
                    btn_wx_id.text = mWechatFriendUploadCache.wechatAccount
                    break
                }
            }
        }
        //为了兼容旧代码，（以前是手输入的）end
    }

    private fun showAgentWechatAccountDialog() {
        if (mAgentWechatAccountInfos == null)
            return
        if (mAgentWechatAccountDialog == null) {
            val mStrAccount = ArrayList<String>()
            for (itemData in mAgentWechatAccountInfos!!) {
                mStrAccount.add(itemData.wxAccount + "")
            }
            mAgentWechatAccountDialog = SimpleBottomSheetDialog.newBuilder(this)
                    .setData(mStrAccount)
                    .setItemClicklistener { v, s, position ->
                        btn_wx_id.text = s
                    }
        }
        mAgentWechatAccountDialog!!.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        // 释放内存资源
        OCR.getInstance(this).release()
    }

    /**
     * 初始化百度orc token
     */
    private fun initAccessTokenWithAkSk() {
        var mAK = ""
        var mSK = ""
        when (BuildConfig.ENVIRONMENT) {
            Client.ENV_DEV, Client.ENV_TEST -> {
                mAK = Config.BAIDU_API_KEY_TEST
                mSK = Config.BAIDU_SECRET_KEY_TEST
            }
            else -> {
                mAK = Config.BAIDU_API_KEY
                mSK = Config.BAIDU_SECRET_KEY
            }
        }
        OCR.getInstance(this).initAccessTokenWithAkSk(object : OnResultListener<AccessToken> {
            override fun onResult(p0: AccessToken?) {
            }

            override fun onError(p0: OCRError?) {
                p0!!.printStackTrace()
                SimpleToast.showNormal("AK，SK方式获取token失败")
            }
        }, this, mAK, mSK)
    }

    /**
     * 检测输入的微信好友数与识别出来的微信好友数是否相同
     */
    private fun checkInputAndORCEquals(): Boolean {
        var mEquals = false
        if (mORCWechatFriendNumber != null && mORCWechatFriendNumber!="") {
            mEquals = mORCWechatFriendNumber!! == et_wx_friend_number.text.toString().trim()
        } else {
            mEquals = true
        }
        return mEquals
    }
}
