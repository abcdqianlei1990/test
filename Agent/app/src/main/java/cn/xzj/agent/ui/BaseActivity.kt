package cn.xzj.agent.ui

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.*
import cn.xzj.agent.MyApplication
import cn.xzj.agent.R
import cn.xzj.agent.ui.adapter.MultiSelectorAdapter
import cn.xzj.agent.ui.adapter.SingleSelectorAdapter
import cn.xzj.agent.constants.Keys
import cn.xzj.agent.i.*
import cn.xzj.agent.util.SharedPreferencesUtil
import cn.xzj.agent.util.Util
import com.aigestudio.wheelpicker.WheelPicker
import com.channey.utils.SharedPreferencesUtils
import com.channey.utils.StringUtils
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by channey on 2018/3/15.
 */
abstract class BaseActivity():AppCompatActivity(){
    private lateinit var mContentView:View
   lateinit var mApp:MyApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mApp= application as MyApplication
        var layoutId = initLayout()
        if (layoutId <= 0) throw Exception("invalid layout wechatId")
        mContentView = LayoutInflater.from(this).inflate(layoutId,null)
        setContentView(mContentView)
        initParams()
        initViews()
        initTitle()
        setListeners()
        initData()
        //此方法与统计分析sdk中统计日活的方法无关！如果不调用此方法，不仅会导致按照"几天不活跃"条件来推送失效，
        // 还将导致广播发送不成功以及设备描述红色等问题发生。可以只在应用的主Activity中调用此方法，但是由于SDK的日志发送策略，
        // 有可能由于主activity的日志没有发送成功，而导致未统计到日活数据。
//        PushAgent.getInstance(this).onAppStart();
    }

    fun getContentView():View{
        return mContentView
    }
    abstract fun initLayout():Int
    abstract fun initParams()
    abstract fun initViews()
    abstract fun initData()

    open fun setListeners(){}

    private fun initTitle(){
        try {
            var titleView:View = findViewById(R.id.title)
            if (titleView != null){
                var arrowBackTv = titleView.findViewById<AppCompatImageView>(R.id.title_back)
                arrowBackTv.setOnClickListener { finish() }
            }
        }catch (e:IllegalStateException){

        }
    }

    open fun setTitle(title:String){
        try {
            var titleView:View = findViewById(R.id.title)
            if (titleView != null){
                var labelTv = titleView.findViewById<TextView>(R.id.title_label)
                labelTv.text = title
            }
        }catch (e:IllegalStateException){

        }
    }

    fun showTitleLine(shown:Boolean){
        try {
            var titleView:View = findViewById(R.id.title)
            if (titleView != null){
                var lineTv = titleView.findViewById<TextView>(R.id.title_line)
                lineTv.visibility = if (shown) View.VISIBLE else View.INVISIBLE
            }
        }catch (e:IllegalStateException){

        }
    }
    fun toast(msg:String){
        if (!StringUtils.isEmpty(msg)){
            Toast.makeText(this,msg,Toast.LENGTH_SHORT).show()
//            val snackbar = Snackbar.make(mContentView, msg, Snackbar.LENGTH_SHORT)
//            var view = snackbar.view
//            view.setBackgroundResource(R.color.green29AC3E)
//            var contentView = view.findViewById<TextView>(android.support.design.R.wechatId.snackbar_text)
//            contentView.setTextColor(resources.getColor(R.color.white))
//            snackbar.show()
        }
    }

    fun setCustomerColor(s: String): SpannableString {
        val sp = SpannableString(s)
        val index = s.indexOf("后重发")
        sp.setSpan(ForegroundColorSpan(resources.getColor(R.color.redFF7731)), 0, index, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        return sp
    }

    fun Log(msg: String){
        android.util.Log.d("qian",msg)
    }

    //=================================== NOTICE DIALOG ===================================
    private var mNoticeDialog: Dialog? = null
    /**
     * 屏幕中间显示dialog
     * @param title 内容
     * @param listener  底部按钮点击事件
     */
    fun showNoticeDialog(title: String) {
        if (mNoticeDialog == null) {
            mNoticeDialog = AlertDialog.Builder(this).create()
        }
        mNoticeDialog!!.show()
        mNoticeDialog!!.setCanceledOnTouchOutside(false)
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_notice, null)
        mNoticeDialog!!.window!!.setContentView(view)
        mNoticeDialog!!.window!!.setBackgroundDrawable(ColorDrawable())
        var confirmBtn = view.findViewById<TextView>(R.id.notice_dialog_confirm_btn)
        var titleTv = view.findViewById<TextView>(R.id.notice_dialog_title_tv)
        var contentTv = view.findViewById<TextView>(R.id.notice_dialog_content_tv)
        if (StringUtils.isEmpty(title)) {
            titleTv.setVisibility(View.GONE)
        } else {
            titleTv.setVisibility(View.VISIBLE)
            titleTv.setText(title)
        }
        contentTv.visibility = View.GONE
        confirmBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                mNoticeDialog!!.dismiss()
            }
        })
    }

    /**
     * @param title Spanned类型，有些文案需要设置不同颜色
     */
    fun showNoticeDialog(title: Spanned) {
        if (mNoticeDialog == null) {
            mNoticeDialog = AlertDialog.Builder(this).create()
        }
        mNoticeDialog!!.show()
        mNoticeDialog!!.setCanceledOnTouchOutside(false)
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_notice, null)
        mNoticeDialog!!.window!!.setContentView(view)
        mNoticeDialog!!.window!!.setBackgroundDrawable(ColorDrawable())
        var confirmBtn = view.findViewById<TextView>(R.id.notice_dialog_confirm_btn)
        var titleTv = view.findViewById<TextView>(R.id.notice_dialog_title_tv)
        var contentTv = view.findViewById<TextView>(R.id.notice_dialog_content_tv)
        titleTv.setVisibility(View.VISIBLE)
        titleTv.setText(title)
        contentTv.visibility = View.GONE
        confirmBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                mNoticeDialog!!.dismiss()
            }
        })
    }

    fun showNoticeDialog(title: String,content: String,listener:View.OnClickListener) {
        if (mNoticeDialog == null) {
            mNoticeDialog = AlertDialog.Builder(this).create()
        }
        mNoticeDialog!!.show()
        mNoticeDialog!!.setCanceledOnTouchOutside(false)
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_notice, null)
        mNoticeDialog!!.window!!.setContentView(view)
        mNoticeDialog!!.window!!.setBackgroundDrawable(ColorDrawable())
        var confirmBtn = view.findViewById<TextView>(R.id.notice_dialog_confirm_btn)
        var titleTv = view.findViewById<TextView>(R.id.notice_dialog_title_tv)
        var contentTv = view.findViewById<TextView>(R.id.notice_dialog_content_tv)
        if (StringUtils.isEmpty(title)) {
            titleTv.setVisibility(View.GONE)
        } else {
            titleTv.setVisibility(View.VISIBLE)
            titleTv.setText(title)
        }
        if (StringUtils.isEmpty(content)) {
            contentTv.setVisibility(View.GONE)
        } else {
            contentTv.setVisibility(View.VISIBLE)
            contentTv.setText(content)
        }

        if(listener == null){
            confirmBtn.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    mNoticeDialog!!.dismiss()
                }
            })
        }else{
            confirmBtn.setOnClickListener(listener)
        }
    }

    fun dismissNoticeDialog(){
        if(mNoticeDialog != null) mNoticeDialog!!.dismiss()
    }


    private var mNoticeDialogWithCancelConfirm: Dialog? = null
    /**
     * 带取消和确认按钮的提示框
     * @param title
     * @param content
     * @param confirmText
     * @param cancelListener  listener for cancel btn
     * @param confirmListener  listener for confirm btn
     */
    fun showNoticeDialogWithCancelConfirm(title: String, content:String, confirmText:String,cancelListener: View.OnClickListener?, confirmListener: View.OnClickListener) {
        if (mNoticeDialogWithCancelConfirm == null) {
            mNoticeDialogWithCancelConfirm = AlertDialog.Builder(this).create()
        }
        mNoticeDialogWithCancelConfirm!!.show()
        mNoticeDialogWithCancelConfirm!!.setCanceledOnTouchOutside(false)
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_notice_with_cancelconfirm, null)
        mNoticeDialogWithCancelConfirm!!.window!!.setContentView(view)
        mNoticeDialogWithCancelConfirm!!.window!!.setBackgroundDrawable(ColorDrawable())
        var confirmBtn = view.findViewById<TextView>(R.id.dialog_with_cancel_confirm_confirm_btn)
        var cancelBtn = view.findViewById<TextView>(R.id.dialog_with_cancel_confirm_cancel_btn)
        var titleTv = view.findViewById<TextView>(R.id.dialog_with_cancel_confirm_title_tv)
        var contentTv = view.findViewById<TextView>(R.id.dialog_with_cancel_confirm_content_tv)
        if (StringUtils.isEmpty(title)) {
            titleTv.setVisibility(View.GONE)
        } else {
            titleTv.setVisibility(View.VISIBLE)
            titleTv.setText(title)
        }
        if (StringUtils.isEmpty(content)) {
            contentTv.setVisibility(View.GONE)
        } else {
            contentTv.setVisibility(View.VISIBLE)
            contentTv.setText(content)
        }
        if (!StringUtils.isEmpty(confirmText)) {
            confirmBtn.text = confirmText
        }
        confirmBtn.setOnClickListener(confirmListener)
        if (cancelListener == null){
            cancelBtn.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    mNoticeDialogWithCancelConfirm!!.dismiss()
                }
            })
        }else{
            cancelBtn.setOnClickListener(cancelListener)
        }
    }


    fun showNoticeDialogWithCancelConfirm(title: String,content:String,cancelText:String,confirmText:String,cancelListener: View.OnClickListener,confirmListener: View.OnClickListener) {
        if (mNoticeDialogWithCancelConfirm == null) {
            mNoticeDialogWithCancelConfirm = AlertDialog.Builder(this).create()
        }
        mNoticeDialogWithCancelConfirm!!.show()
        mNoticeDialogWithCancelConfirm!!.setCanceledOnTouchOutside(false)
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_notice_with_cancelconfirm, null)
        mNoticeDialogWithCancelConfirm!!.window!!.setContentView(view)
        mNoticeDialogWithCancelConfirm!!.window!!.setBackgroundDrawable(ColorDrawable())
        var confirmBtn = view.findViewById<TextView>(R.id.dialog_with_cancel_confirm_confirm_btn)
        var cancelBtn = view.findViewById<TextView>(R.id.dialog_with_cancel_confirm_cancel_btn)
        var titleTv = view.findViewById<TextView>(R.id.dialog_with_cancel_confirm_title_tv)
        var contentTv = view.findViewById<TextView>(R.id.dialog_with_cancel_confirm_content_tv)
        if (StringUtils.isEmpty(title)) {
            titleTv.setVisibility(View.GONE)
        } else {
            titleTv.setVisibility(View.VISIBLE)
            titleTv.setText(title)
        }
        if (StringUtils.isEmpty(content)) {
            contentTv.setVisibility(View.GONE)
        } else {
            contentTv.setVisibility(View.VISIBLE)
            contentTv.setText(content)
        }
        if (!StringUtils.isEmpty(cancelText)) {
            cancelBtn.text = cancelText
        }
        if (!StringUtils.isEmpty(confirmText)) {
            confirmBtn.text = confirmText
        }
        confirmBtn.setOnClickListener(confirmListener)
        if (cancelListener == null){
            cancelBtn.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    mNoticeDialogWithCancelConfirm!!.dismiss()
                }
            })
        }else{
            cancelBtn.setOnClickListener(cancelListener)
        }
    }

    fun dismissCancelConfirmDialog(){
        if (mNoticeDialogWithCancelConfirm != null) mNoticeDialogWithCancelConfirm!!.dismiss()
    }

    fun hasLogin():Boolean{
        var token = SharedPreferencesUtil.getToken(this)
        return !StringUtils.isEmpty(token)
    }
//
    private var mMyLoadingDialog: AlertDialog? = null
    /**
     * 显示用户绑定银行卡列表popwindow
     */
    fun showLoading() {
        if (mMyLoadingDialog == null) {
            mMyLoadingDialog = AlertDialog.Builder(this).create()
            mMyLoadingDialog!!.show()
            mMyLoadingDialog!!.setCanceledOnTouchOutside(false)
            val view = LayoutInflater.from(this).inflate(R.layout.dialog_loading, null)
            mMyLoadingDialog!!.window!!.setContentView(view)
            mMyLoadingDialog!!.window!!.setBackgroundDrawable(ColorDrawable())
        } else {
            mMyLoadingDialog!!.show()
        }
    }

    fun dismissLoading() {
        if (mMyLoadingDialog != null) mMyLoadingDialog!!.dismiss()
    }

    /**
     * 设置添加屏幕的背景透明度

     * @param bgAlpha
     * *            屏幕透明度0.0-1.0 1表示完全不透明
     */
    fun setBackgroundAlpha(bgAlpha: Float) {
        val lp = window.attributes
        lp.alpha = bgAlpha
        window.attributes = lp
    }

    fun networkUnavailable() {
        toast("当前网络不可用，请检查网络设置")
    }

//    /**
//     * 设置倒计时按钮是否可点击
//     * @param btn
//     * *
//     * @param clickable
//     */
//    fun setTimerBtnClickable(btn: TimerButton, clickable: Boolean) {
//        if (clickable) {
////            btn.setBackgroundDrawable(resources.getDrawable(R.drawable.background_authbtn_normal))
//            btn.setTextColor(resources.getColor(R.color.yellow_FF6200))
//        } else {
////            btn.setBackgroundDrawable(resources.getDrawable(R.drawable.background_authbtn_unclickable))
//            btn.setTextColor(resources.getColor(R.color.commonTextGrayColor))
//        }
//    }
//
//    /**
//     * 收起软键盘
//     */
//    fun hideSoftInput() {
//        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        val isOpen = imm.isActive//isOpen若返回true，则表示输入法打开
//        if (isOpen) {
//            imm.hideSoftInputFromWindow(mContentView.getWindowToken(), 0)
//        }
//    }
//
//    private var mBindingCardsPopupWindow: PopupWindow? = null
//    private var mBindingCardsPopAdapter: PopCardsListAdapter? = null
//    /**
//     * 显示用户绑定银行卡列表popwindow
//     * @param cards 用户绑定的银行卡
//     * *
//     * @param listener item点击事件
//     */
//    fun showBindingCardsWindow(activity: BaseActivity, cards: List<BankCardInfo>, @Nullable defaultInfo: BankCardInfo, listener: RecyclerViewItemClickListener, titleStr: String) {
//        if (mBindingCardsPopupWindow == null) {
//            mBindingCardsPopupWindow = PopupWindow(this)
//            val view = LayoutInflater.from(this).inflate(R.layout.pop_binding_cards, null)
//            mBindingCardsPopupWindow!!.contentView = view
//            val recyclerView = view.findViewById<RecyclerView>(R.wechatId.pop_bindingcards_list)
//            val close = view.findViewById<TextView>(R.wechatId.pop_bindingcards_exit)
//            val title = view.findViewById<TextView>(R.wechatId.pop_bindingcards_title)
//            val btn = view.findViewById<RelativeLayout>(R.wechatId.pop_bindingcards_btn)
//            title.text = titleStr
//            btn.setOnClickListener {
//                BindCardStep1Activity.jumpForResult(activity, Code.RequestCode.BIND_CARD)
//                mBindingCardsPopupWindow!!.dismiss()
//            }
//            mBindingCardsPopAdapter = PopCardsListAdapter(this, cards)
//            mBindingCardsPopAdapter!!.setInfo(defaultInfo)
//            mBindingCardsPopAdapter!!.setRecyclerViewItemClickListener(listener)
//            recyclerView.layoutManager = LinearLayoutManager(this)
//            recyclerView.adapter = mBindingCardsPopAdapter
//            close.setOnClickListener { mBindingCardsPopupWindow!!.dismiss() }
//            mBindingCardsPopupWindow!!.width = LinearLayout.LayoutParams.MATCH_PARENT
//            mBindingCardsPopupWindow!!.height = UIUtil.dip2px(this, 270)
//            mBindingCardsPopupWindow!!.setBackgroundDrawable(ColorDrawable())
//            mBindingCardsPopupWindow!!.isOutsideTouchable = false
//            mBindingCardsPopupWindow!!.isTouchable = true
//            mBindingCardsPopupWindow!!.animationStyle = R.style.popWindowAnim
//            mBindingCardsPopupWindow!!.setOnDismissListener {
//                setBackgroundAlpha(1.0f)
//            }
//        }
//        mBindingCardsPopAdapter!!.info = defaultInfo
//        setBackgroundAlpha(0.5f);
//        val height = mBindingCardsPopupWindow!!.contentView.height
//        val w = mBindingCardsPopupWindow!!.contentView.width
//        mBindingCardsPopupWindow!!.showAtLocation(mContentView, Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, w, -height) //设置layout在PopupWindow中显示的位置
//        mBindingCardsPopAdapter!!.notifyDataSetChanged()
//
//    }
//
//    fun dismissBindingCardsPop(){
//        if (mBindingCardsPopupWindow != null) mBindingCardsPopupWindow!!.dismiss()
//    }
//
    fun setBtnClickable(btn:TextView,clickable:Boolean){
        if (clickable){
            btn.setBackgroundResource(R.drawable.bg_submit_btn_clickable)
            btn.isClickable = true
            btn.isEnabled = true
        }else{
            btn.setBackgroundResource(R.drawable.bg_submit_btn_unclickable)
            btn.isClickable = false
            btn.isEnabled = false
        }
    }
//
//    fun showPopupWindow(url: String) {
//        // 一个自定义的布局，作为显示的内容
//        val contentView = LayoutInflater.from(this).inflate(
//                R.layout.pop_img, null)
//        val imageView = contentView.findViewById<ImageView>(R.wechatId.pop_img)
//        val popupWindow = PopupWindow(contentView,
//                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true)
//        contentView.setOnClickListener{ popupWindow.dismiss() }
//        ImageLoader.loadImage(this,url,imageView,0)
//        val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
//        val metrics = DisplayMetrics()
//        wm.defaultDisplay.getMetrics(metrics)
//
//        //        int w = (int) (metrics.widthPixels*0.7);
//        //        int h = (int) (metrics.heightPixels*0.7);
//        popupWindow.width = metrics.widthPixels
//        popupWindow.height = metrics.heightPixels
//        popupWindow.isFocusable = true
//        popupWindow.isTouchable = true
//        popupWindow.isOutsideTouchable = false
//        popupWindow.setOnDismissListener { setBackgroundAlpha(1f) }
//        setBackgroundAlpha(0.5f)
//        popupWindow.showAtLocation(mContentView, Gravity.CENTER, 0, 0)
//    }
//
//    // ============================== 分享弹框 ==============================
//    private var mShareMenuPopupWindow: AlertDialog? = null
//    private var mShareMenuAdapter: ShareMenuListAdapter? = null
//    fun showSharePopWindow(listener: RecyclerViewItemClickListener) {
//        if (mShareMenuPopupWindow == null) {
//            mShareMenuPopupWindow = AlertDialog.Builder(this).create()
//            mShareMenuPopupWindow!!.show()
//            //设置显示位置和动画
//            val window = mShareMenuPopupWindow!!.window
//            val attributes = window.attributes
//            attributes.width = window.getWindowManager().getDefaultDisplay().getWidth()
//            attributes.height = WindowManager.LayoutParams.WRAP_CONTENT
//            window.setGravity(Gravity.BOTTOM)
//            window.setWindowAnimations(R.style.popWindowAnim)
//
//            mShareMenuPopupWindow!!.setCanceledOnTouchOutside(false)
//            val view = LayoutInflater.from(this).inflate(R.layout.pop_share, null)
//            mShareMenuPopupWindow!!.getWindow()!!.setContentView(view)
//            mShareMenuPopupWindow!!.getWindow()!!.setBackgroundDrawable(ColorDrawable())
//
//            val recyclerView = view.findViewById<RecyclerView>(R.wechatId.share_recyclerView)
//            val cancel = view.findViewById<TextView>(R.wechatId.share_cancel_btn)
//            initShareMenuRecyclerView(recyclerView, listener)
//            cancel.setOnClickListener { mShareMenuPopupWindow!!.dismiss() }
//            mShareMenuPopupWindow!!.setOnDismissListener {
//
//            }
//        }else{
//            val window = mShareMenuPopupWindow!!.window
//            window.setGravity(Gravity.BOTTOM)
//            window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//            window.setWindowAnimations(R.style.popWindowAnim)
//            mShareMenuPopupWindow!!.show()
//        }
//        mShareMenuAdapter!!.notifyDataSetChanged()
//    }
//
//    fun dismissShareMenuDialog(){
//        if (mShareMenuPopupWindow != null) mShareMenuPopupWindow!!.dismiss()
//    }
//
//    fun initShareMenuRecyclerView(recyclerView: RecyclerView, listener: RecyclerViewItemClickListener) {
//        val manager = GridLayoutManager(this, 5)
//        recyclerView.layoutManager = manager
//        mShareMenuAdapter = ShareMenuListAdapter(this)
//        mShareMenuAdapter!!.setOnRecyclerViewItemClickListener(listener)
//        recyclerView.adapter = mShareMenuAdapter
//    }
//
//    /**
//     * 根据参数分享到指定平台
//     * @param position
//     */
//    private val SHARE_CONTENT_TITLE = "来呀！来呀！"
//    private val SHARE_CONTENT = "小职姐小职姐小职姐小职姐小职姐小职姐"
//    private val targetUrl = "http://www.baidu.com/"
//    private val IMG_URL = "https://img3.doubanio.com/view/photo/l/public/p926291454.webp"
//    fun doShare(position:Int,shareInfo: ShareInfo,listener: ShareUtil.ShareSuccessListener) {
//        when (position) {
//            0 -> {
//                ShareUtil.shareWeChat(this, shareInfo, listener)
//            }
//            1 -> {
//                ShareUtil.shareWexinMoments(this, shareInfo, listener)
//            }
//            2 -> {
//                ShareUtil.shareQQ(this, shareInfo, listener)
//            }
//            3 -> {
//                ShareUtil.shareQZone(this, shareInfo, listener)
//            }
//            4 -> {
//                ShareUtil.shareSina(this, shareInfo, listener)
//            }
//            else -> {
//            }
//        }
//    }
//
    fun tokenInvalid(){
        logout()
        showNoticeDialog("登录信息已失效,请重新登录","",object :View.OnClickListener{
            override fun onClick(v: View?) {
                dismissNoticeDialog()
                finish()
                LoginActivity.jump(this@BaseActivity)
            }
        })
    }

    fun logout() {
        SharedPreferencesUtil.clear(this)
    }
//
//    /**
//     * 显示空白页(不显示按钮)
//     */
//    fun showEmptyView(imgId:Int,title:Int){
//        empty_view_img.setBackgroundResource(imgId)
//        empty_view_title.text = getString(title)
//        empty_view_content.visibility = View.GONE
//    }
//
//    /**
//     * 显示空白页(不显示按钮)
//     */
//    fun showEmptyView(imgId:Int,title:Int,content:Int){
//        empty_view_img.setBackgroundResource(imgId)
//        empty_view_title.text = getString(title)
//        empty_view_content.text = getString(content)
//        empty_view_content.visibility = View.VISIBLE
//    }
//
//    /**
//     * 显示空白页(显示按钮)
//     */
//    fun showEmptyView(imgId:Int,title:Int,content:Int,showBtn:Boolean,btnText:Int,listener: View.OnClickListener){
//        empty_view_img.setBackgroundResource(imgId)
//        empty_view_title.text = getString(title)
//        empty_view_content.text = getString(content)
//        empty_view_content.visibility = View.VISIBLE
//        if (showBtn){
//            empty_view_btn.visibility = View.VISIBLE
//            empty_view_btn.text = getString(btnText)
//            empty_view_btn.setOnClickListener(listener)
//        }else{
//            empty_view_btn.visibility = View.GONE
//        }
//    }


    /**
     * 打电话
     * @param number1 用于显示在弹框中（脱敏）
     * @param number2 真实号码（未脱敏）
     */
    fun makePhoneCall(number1:String,number2:String){
        showNoticeDialogWithCancelConfirm("确认拨打电话$number1","","确认",
                View.OnClickListener {
                    dismissCancelConfirmDialog()
                },
                View.OnClickListener {
                    phonePermissionCheck(number2)
                    dismissCancelConfirmDialog()
                })
    }

    private fun phonePermissionCheck(number: String) {
        val list = java.util.ArrayList<String>()
        val permission = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
        if (permission == PackageManager.PERMISSION_GRANTED) {
            val number = numberFormat(number)
            call(number)
        } else {
            list.add(Manifest.permission.CALL_PHONE)
            val array = Util.listToArray(list)
            if (list.size > 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(array, 1)
                }
            }
        }
    }

    private fun numberFormat(number: String): String {
        if (number.contains("-")) {
            val strings = number.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val sb = StringBuilder()
            for (s in strings) {
                sb.append(s)
            }
            return sb.toString()
        } else
            return number
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            for (i in permissions.indices) {
                if (permissions[i] == Manifest.permission.CALL_PHONE && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    toast("请重新拨打电话")
                } else {
                    toast("已禁用拨打电话权限")
                }
            }
        }
    }

    /**
     * 调用拨号界面
     * @param phone 电话号码
     */
    private fun call(phone: String) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone))
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }


    //============================= 时间选择弹框 =============================
    private var mDatePickerPopupWindow: AlertDialog? = null
    fun showDatePickerPopWindow(listener: DateSelectedListener?) {
        var selectYear = ""
        var selectMonth = ""
        var selectDay = ""
        var selectHour = ""
        var selectMin = ""
        mDatePickerPopupWindow = AlertDialog.Builder(this).create()
        mDatePickerPopupWindow!!.show()
        //设置显示位置和动画
        val window = mDatePickerPopupWindow!!.window
        val attributes = window.attributes
        attributes.width = window.getWindowManager().getDefaultDisplay().getWidth()
        attributes.height = WindowManager.LayoutParams.WRAP_CONTENT
        window.setGravity(Gravity.BOTTOM)
        window.setWindowAnimations(R.style.popWindowAnim)

        mDatePickerPopupWindow!!.setCanceledOnTouchOutside(false)
        val view = LayoutInflater.from(this).inflate(R.layout.view_date_select, null)
        mDatePickerPopupWindow!!.getWindow()!!.setContentView(view)
        mDatePickerPopupWindow!!.getWindow()!!.setBackgroundDrawable(ColorDrawable())

        val negBtn = view.findViewById<TextView>(R.id.date_select_neg_btn)
        val posBtn = view.findViewById<TextView>(R.id.date_select_pos_btn)
        val yearWheel = view.findViewById<WheelPicker>(R.id.WheelView1)
        val monthWheel = view.findViewById<WheelPicker>(R.id.WheelView2)
        val dayWheel = view.findViewById<WheelPicker>(R.id.WheelView3)
        val hourWheel = view.findViewById<WheelPicker>(R.id.WheelView4)
        val minWheel = view.findViewById<WheelPicker>(R.id.WheelView5)

        //设置年份数据，从今年开始,从今年开始往后推10年
//            TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
        var c = Calendar.getInstance()
        c.timeZone = java.util.TimeZone.getTimeZone("GMT+8")
        var currentYear = c.get(Calendar.YEAR); // 获取当前年份
        var currentMonth = c.get(Calendar.MONTH) + 1;// 获取当前月份
        var currentDay = c.get(Calendar.DAY_OF_MONTH);// 获取当日期
        var currentHour = c.get(Calendar.HOUR_OF_DAY);//时
        var currentMin = c.get(Calendar.MINUTE);//分
        selectYear = "${currentYear}年"
        selectMonth = if (currentMonth > 9) "${currentMonth}月" else "0${currentMonth}月"
        selectDay = if (currentDay > 9) "${currentDay}日" else "0${currentDay}日"
        selectHour= if (currentHour > 9) "${currentHour}时" else "0${currentHour}时"
        selectMin = if (currentMin > 9) "${currentMin}分" else "0${currentMin}分"
        var yeas:ArrayList<String> = ArrayList<String>()
        for (i in currentYear..currentYear+10){
            yeas.add("${i}年")
        }
        yearWheel.data = yeas
        yearWheel.selectedItemPosition = 0
        yearWheel.setOnItemSelectedListener(object : WheelPicker.OnItemSelectedListener {
            override fun onItemSelected(picker: WheelPicker?, data: Any?, position: Int) {
//                    Log("当前年份选择为 $data")
                selectYear = yeas[position]
            }
        })
        //设置月份
        var months:ArrayList<String> = ArrayList<String>()
        var selectedMonthPos = 0
        for (i in 1..31){
            var temp = if(i > 9) "${i}月" else "0${i}月"
            months.add("$temp")
            if (i == currentMonth) selectedMonthPos = i-1
        }
        monthWheel.data = months
        monthWheel.selectedItemPosition = selectedMonthPos
        monthWheel.setOnItemSelectedListener(object : WheelPicker.OnItemSelectedListener {
            override fun onItemSelected(picker: WheelPicker?, data: Any?, position: Int) {
//                    Log("当前月份选择为 $data")
                selectMonth = months[position]
            }
        })
        //设置天
        var days:ArrayList<String> = ArrayList<String>()
        var selectedDayPos = 0
        for (i in 1..31){
            var temp = if(i > 9) "${i}日" else "0${i}日"
            days.add("$temp")
            if (i == currentDay) selectedDayPos = i-1
        }
        dayWheel.data = days
        dayWheel.selectedItemPosition = selectedDayPos
        dayWheel.setOnItemSelectedListener(object : WheelPicker.OnItemSelectedListener {
            override fun onItemSelected(picker: WheelPicker?, data: Any?, position: Int) {
//                    Log("当前日期选择为 $data")
                selectDay = days[position]
            }
        })
        //设置小时
        var hours:ArrayList<String> = ArrayList<String>()
        var selectedHourPos = 2
        for (i in 0..23){
            var temp = if(i > 9) "${i}时" else "0${i}时"
            hours.add("$temp")
            if (i == currentHour) selectedHourPos = i
        }
        hourWheel.data = hours
        hourWheel.selectedItemPosition = selectedHourPos
        hourWheel.setOnItemSelectedListener(object : WheelPicker.OnItemSelectedListener {
            override fun onItemSelected(picker: WheelPicker?, data: Any?, position: Int) {
//                    Log("当前小时选择为 $data")
                selectHour = hours[position]
            }
        })
        //设置分
        var mins:ArrayList<String> = ArrayList<String>()
        var selectedMinPos = 2
        for (i in 0..59){
            var temp = if(i > 9) "${i}分" else "0${i}分"
            mins.add("$temp")
            if (i == currentMin) selectedMinPos = i
        }
        minWheel.data = mins
        minWheel.selectedItemPosition = selectedMinPos
        minWheel.setOnItemSelectedListener(object : WheelPicker.OnItemSelectedListener {
            override fun onItemSelected(picker: WheelPicker?, data: Any?, position: Int) {
//                    Log("当前分选择为 $data")
                selectMin = mins[position]
            }
        })


        negBtn.setOnClickListener { mDatePickerPopupWindow!!.dismiss() }
        posBtn.setOnClickListener {
            mDatePickerPopupWindow!!.dismiss()
//                if (listener != null) listener.onSelected("$selectYear$selectMonth$selectDay$selectHour$selectMin")
            if (listener != null) listener.onSelected(selectYear,selectMonth,selectDay,selectHour,selectMin)
        }

        mDatePickerPopupWindow!!.setOnDismissListener {

        }
//        if (mDatePickerPopupWindow == null) {
//
//        }else{
//            val window = mDatePickerPopupWindow!!.window
//            window.setGravity(Gravity.BOTTOM)
//            window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//            window.setWindowAnimations(R.style.popWindowAnim)
//            mDatePickerPopupWindow!!.show()
//        }
    }

    fun dismissDatePickerDialog(){
        if (mDatePickerPopupWindow != null) mDatePickerPopupWindow!!.dismiss()
    }

    //============================= 单选框 =============================
    private var mPickerPopupWindow: AlertDialog? = null

    /**
     * 显示单选框
     * @param list 数据源
     * @param defaultSelectPos  默认选中项position
     * @param listener  确认选择后回调
     */
    fun showPickerPopWindow(title: String,list:List<String>,defaultSelection:String?,listener: PickerSelectedListener?) {
        mPickerPopupWindow = AlertDialog.Builder(this).create()
        mPickerPopupWindow!!.show()
        //设置显示位置和动画
        val window = mPickerPopupWindow!!.window
        val attributes = window.attributes
        attributes.width = window.windowManager.defaultDisplay.getWidth()
        attributes.height = WindowManager.LayoutParams.WRAP_CONTENT
        window.setGravity(Gravity.BOTTOM)
        window.setWindowAnimations(R.style.popWindowAnim)

        mPickerPopupWindow!!.setCanceledOnTouchOutside(false)
        val view = LayoutInflater.from(this).inflate(R.layout.view_wheel_picker, null)
        mPickerPopupWindow!!.getWindow()!!.setContentView(view)
        mPickerPopupWindow!!.getWindow()!!.setBackgroundDrawable(ColorDrawable())

        val titleTv = view.findViewById<TextView>(R.id.view_picker_title_tv)
        val negBtn = view.findViewById<TextView>(R.id.view_picker_neg_btn)
        val posBtn = view.findViewById<TextView>(R.id.view_picker_pos_btn)
        val wheel = view.findViewById<WheelPicker>(R.id.wheel)
        titleTv.text = title
        wheel.data = list
        var defaultSelectPos = 0
        if (list.contains(defaultSelection)){
            defaultSelectPos = list.indexOf(defaultSelection)
        }
        wheel.selectedItemPosition = defaultSelectPos
        var selectedPosition = defaultSelectPos
        var selection = list[defaultSelectPos]
        wheel.setOnItemSelectedListener(object : WheelPicker.OnItemSelectedListener {
            override fun onItemSelected(picker: WheelPicker?, data: Any?, position: Int) {
                selection = list[position]
                selectedPosition = position
            }
        })


        negBtn.setOnClickListener { mPickerPopupWindow!!.dismiss() }
        posBtn.setOnClickListener {
            mPickerPopupWindow!!.dismiss()
            if (listener != null) listener.onSelected(selectedPosition,"$selection")
        }

        mPickerPopupWindow!!.setOnDismissListener {

        }

//        if (mPickerPopupWindow == null) {
//
//        }else{
//            val window = mPickerPopupWindow!!.window
//            window.setGravity(Gravity.BOTTOM)
//            window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//            window.setWindowAnimations(R.style.popWindowAnim)
//            mPickerPopupWindow!!.show()
//        }
    }
    fun dismissPickerDialog(){
        if (mPickerPopupWindow != null) mPickerPopupWindow!!.dismiss()
    }


    //============================= 单选框 =============================
    private var mSingleSelectorPopupWindow: AlertDialog? = null

    /**
     * 显示单选框
     * @param title 弹框标题
     * @param defaultSelection
     * @param list 数据源
     * @param listener  确认选择后回调
     */
    fun showSingleSelectPopWindow(title: String, defaultSelection: String?,list:List<String>, listener: SingleSelectListener) {
        mSingleSelectorPopupWindow = AlertDialog.Builder(this).create()
        mSingleSelectorPopupWindow!!.show()
        //设置显示位置和动画
        val window = mSingleSelectorPopupWindow!!.window
        val attributes = window.attributes
        attributes.width = window.windowManager.defaultDisplay.getWidth()
        attributes.height = WindowManager.LayoutParams.WRAP_CONTENT
        window.setGravity(Gravity.BOTTOM)
        window.setWindowAnimations(R.style.popWindowAnim)

        mSingleSelectorPopupWindow!!.setCanceledOnTouchOutside(false)
        val view = LayoutInflater.from(this).inflate(R.layout.pop_selector, null)
        mSingleSelectorPopupWindow!!.getWindow()!!.setContentView(view)
        mSingleSelectorPopupWindow!!.getWindow()!!.setBackgroundDrawable(ColorDrawable())

        val confirmBtn = view.findViewById<TextView>(R.id.pop_single_selector_confirm_btn)
        val closeBtn = view.findViewById<TextView>(R.id.pop_single_selector_close_btn)
        val titleTv = view.findViewById<TextView>(R.id.pop_single_selector_title_tv)
        val recyclerView = view.findViewById<RecyclerView>(R.id.pop_single_selector_list)

        titleTv.text = title
        closeBtn.setOnClickListener { mSingleSelectorPopupWindow!!.dismiss() }

        var adapter = SingleSelectorAdapter(this,list,recyclerView)
        adapter.setDefaultSelection(defaultSelection)
        var layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager
        adapter.notifyDataSetChanged()
        confirmBtn.setOnClickListener {
            var selection = adapter.getSelection()
            listener.onSelect(selection)
        }
    }
    fun dismissSingleSelectPop(){
        if (mSingleSelectorPopupWindow != null) mSingleSelectorPopupWindow!!.dismiss()
    }

    //============================= 多选框 =============================
    private var mMultiSelectorPopupWindow: AlertDialog? = null

    /**
     * 显示单选框
     * @param title 弹框标题
     * @param defaultSelection 默认选中项
     * @param list 数据源
     * @param listener  确认选择后回调
     */
    fun showMultiSelectPopWindow(title: String, defaultSelection: List<String>?,list:List<String>, listener: MultiSelectListener) {
        mMultiSelectorPopupWindow = AlertDialog.Builder(this).create()
        mMultiSelectorPopupWindow!!.show()
        //设置显示位置和动画
        val window = mMultiSelectorPopupWindow!!.window
        val attributes = window.attributes
        attributes.width = window.windowManager.defaultDisplay.getWidth()
        attributes.height = WindowManager.LayoutParams.WRAP_CONTENT
        window.setGravity(Gravity.BOTTOM)
        window.setWindowAnimations(R.style.popWindowAnim)

        mMultiSelectorPopupWindow!!.setCanceledOnTouchOutside(false)
        val view = LayoutInflater.from(this).inflate(R.layout.pop_selector, null)
        mMultiSelectorPopupWindow!!.getWindow()!!.setContentView(view)
        mMultiSelectorPopupWindow!!.getWindow()!!.setBackgroundDrawable(ColorDrawable())

        val closeBtn = view.findViewById<TextView>(R.id.pop_single_selector_close_btn)
        val confirmBtn = view.findViewById<TextView>(R.id.pop_single_selector_confirm_btn)
        val titleTv = view.findViewById<TextView>(R.id.pop_single_selector_title_tv)
        val recyclerView = view.findViewById<RecyclerView>(R.id.pop_single_selector_list)

        titleTv.text = title
        closeBtn.setOnClickListener { mMultiSelectorPopupWindow!!.dismiss() }
        var adapter = MultiSelectorAdapter(this,list)
        adapter.setDefaultSelections(if (defaultSelection == null) ArrayList<String>() else defaultSelection)
        var layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager
        adapter.notifyDataSetChanged()
        confirmBtn.setOnClickListener {
            var selection = adapter.getSelection()
            listener.onSelect(selection)
        }
        mMultiSelectorPopupWindow!!.setOnDismissListener {

        }
    }
    fun dismissMultiSelectPop(){
        if (mMultiSelectorPopupWindow != null) mMultiSelectorPopupWindow!!.dismiss()
    }
}