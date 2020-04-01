package cn.xzj.agent.util

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AlertDialog
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import cn.xzj.agent.R
import cn.xzj.agent.entity.common.ShareMenuInfo
import cn.xzj.agent.ui.adapter.common.ShareMenuListAdapter
import com.channey.utils.ShapeUtil
import com.channey.utils.StringUtils
import com.channey.utils.ToastUtils

object DialogUtil {

    private var mNoticeDialog: Dialog? = null
    fun showNoticeDialog(context: Context,title: String, content: String?=null, listener: View.OnClickListener?=null) {
        mNoticeDialog = AlertDialog.Builder(context).create()
        mNoticeDialog!!.show()
        mNoticeDialog!!.setCanceledOnTouchOutside(false)
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_notice, null)
        mNoticeDialog!!.window!!.setContentView(view)
        mNoticeDialog!!.window!!.setBackgroundDrawable(ColorDrawable())
        val container = view.findViewById<LinearLayout>(R.id.dialog_notice_container)
        val confirmBtn = view.findViewById<AppCompatTextView>(R.id.notice_dialog_confirm_btn)
        val titleTv = view.findViewById<AppCompatTextView>(R.id.notice_dialog_title_tv)
        val contentTv = view.findViewById<AppCompatTextView>(R.id.notice_dialog_content_tv)
        ShapeUtil.setShape(container,radius = context.resources.getDimension(R.dimen.dp_12),solidColor = context.resources.getColor(R.color.white))
        if (StringUtils.isEmpty(title)) {
            titleTv.visibility = View.GONE
        } else {
            titleTv.visibility = View.VISIBLE
            titleTv.text = title
        }
        if (StringUtils.isEmpty(content)) {
            contentTv.visibility = View.GONE
        } else {
            contentTv.visibility = View.VISIBLE
            contentTv.text = content
        }

        if (listener == null) {
            confirmBtn.setOnClickListener { dismissNoticeDialog() }
        } else {
            confirmBtn.setOnClickListener(listener)
        }
    }

    fun dismissNoticeDialog() {
        if (mNoticeDialog != null) mNoticeDialog!!.dismiss()
        if (mNoticeDialogWithCancelConfirm != null) mNoticeDialogWithCancelConfirm!!.dismiss()
    }

    private var mNoticeDialogWithCancelConfirm: Dialog? = null
    fun showNoticeDialogWithCancelConfirm(context:Context,title: String?=null, content:String?=null, cancelText:String?=null, confirmText:String?=null, cancelListener: View.OnClickListener?=null, confirmListener: View.OnClickListener) {
        mNoticeDialogWithCancelConfirm = AlertDialog.Builder(context).create()
        mNoticeDialogWithCancelConfirm!!.show()
        mNoticeDialogWithCancelConfirm!!.setCanceledOnTouchOutside(false)
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_notice_with_cancelconfirm, null)
        mNoticeDialogWithCancelConfirm!!.window!!.setContentView(view)
        mNoticeDialogWithCancelConfirm!!.window!!.setBackgroundDrawable(ColorDrawable())
        var confirmBtn = view.findViewById<TextView>(R.id.dialog_with_cancel_confirm_confirm_btn)
        var cancelBtn = view.findViewById<TextView>(R.id.dialog_with_cancel_confirm_cancel_btn)
        var titleTv = view.findViewById<TextView>(R.id.dialog_with_cancel_confirm_title_tv)
        var contentTv = view.findViewById<TextView>(R.id.dialog_with_cancel_confirm_content_tv)
        if (StringUtils.isEmpty(title)) {
            titleTv.visibility = View.GONE
        } else {
            titleTv.visibility = View.VISIBLE
            titleTv.text = title
        }
        if (StringUtils.isEmpty(content)) {
            contentTv.visibility = View.GONE
        } else {
            contentTv.visibility = View.VISIBLE
            contentTv.text = content
        }
        if (!StringUtils.isEmpty(cancelText)) {
            cancelBtn.text = cancelText
        }
        if (!StringUtils.isEmpty(confirmText)) {
            confirmBtn.text = confirmText
        }
        confirmBtn.setOnClickListener(confirmListener)
        if (cancelListener == null){
            cancelBtn.setOnClickListener { mNoticeDialogWithCancelConfirm!!.dismiss() }
        }else{
            cancelBtn.setOnClickListener(cancelListener)
        }
    }

    fun dismissCancelConfirmDialog(){
        if (mNoticeDialogWithCancelConfirm != null) {
            mNoticeDialogWithCancelConfirm!!.dismiss()
            mNoticeDialogWithCancelConfirm = null
        }
    }

    // ============================== 分享弹框 ==============================
    private var mShareMenuPopupWindow: AlertDialog? = null
    private var mShareMenuAdapter: ShareMenuListAdapter? = null
    fun showSharePopWindow(context: Context,platforms:ArrayList<ShareMenuInfo>,listener: ShareMenuListAdapter.ShareMenuItemClickListener) {
        if (mShareMenuPopupWindow == null) {
            mShareMenuPopupWindow = AlertDialog.Builder(context).create()
            mShareMenuPopupWindow!!.show()
            //设置显示位置和动画
            val window = mShareMenuPopupWindow!!.window
            val attributes = window.attributes
            attributes.width = window.getWindowManager().getDefaultDisplay().getWidth()
            attributes.height = WindowManager.LayoutParams.WRAP_CONTENT
            window.setGravity(Gravity.BOTTOM)
            window.setWindowAnimations(R.style.popWindowAnim)

            mShareMenuPopupWindow!!.setCanceledOnTouchOutside(false)
            val view = LayoutInflater.from(context).inflate(R.layout.pop_share, null)
            mShareMenuPopupWindow!!.window!!.setContentView(view)
            mShareMenuPopupWindow!!.window!!.setBackgroundDrawable(ColorDrawable())

            val recyclerView = view.findViewById<RecyclerView>(R.id.share_recyclerView)
            val cancel = view.findViewById<AppCompatTextView>(R.id.share_cancel_btn)
            initShareMenuRecyclerView(context,recyclerView, platforms,listener)
            cancel.setOnClickListener { mShareMenuPopupWindow!!.dismiss() }
            mShareMenuPopupWindow!!.setOnDismissListener {

            }
        }else{
            val window = mShareMenuPopupWindow!!.window
            window.setGravity(Gravity.BOTTOM)
            window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            window.setWindowAnimations(R.style.popWindowAnim)
            mShareMenuPopupWindow!!.show()
        }
        mShareMenuAdapter!!.notifyDataSetChanged()
    }

    fun dismissShareMenuDialog(){
        if (mShareMenuPopupWindow != null) mShareMenuPopupWindow!!.dismiss()
    }

    private fun initShareMenuRecyclerView(context: Context,recyclerView: RecyclerView,platforms:ArrayList<ShareMenuInfo>, listener: ShareMenuListAdapter.ShareMenuItemClickListener) {
        val manager = GridLayoutManager(context, platforms.size)
        recyclerView.layoutManager = manager
        mShareMenuAdapter = ShareMenuListAdapter(context)
        mShareMenuAdapter!!.setMenus(platforms)
        mShareMenuAdapter!!.setOnRecyclerViewItemClickListener(listener)
        recyclerView.adapter = mShareMenuAdapter
    }

    fun listToArray(list: java.util.ArrayList<String>): Array<String?> {
        val arr = arrayOfNulls<String>(list.size)
        for (i in list.indices) {
            arr[i] = list[i]
        }
        return arr
    }

    private var mNameEditDialog: Dialog? = null
    fun showNameEditDialog(context: Context,name: String,confirmListener: NameEditConfirmListener) {
        mNameEditDialog = AlertDialog.Builder(context).create()
        mNameEditDialog!!.show()
        mNameEditDialog!!.setCanceledOnTouchOutside(false)
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_name_edit, null)
        mNameEditDialog!!.window!!.setContentView(view)
        mNameEditDialog!!.window!!.setBackgroundDrawable(ColorDrawable())
        val editText = view.findViewById(R.id.edit) as EditText
        ShapeUtil.setShape(
                editText,
                radius = context.resources.getDimension(R.dimen.dp_4),
                solidColor = context.resources.getColor(R.color.white),
                strokeWidth = context.resources.getDimension(R.dimen.commonLineHeight).toInt(),
                strokeColor = context.resources.getColor(R.color.colorPrimary)
        )
        val negBtn = view.findViewById(R.id.cancel_btn) as AppCompatTextView
        val posBtn = view.findViewById(R.id.confirm_btn) as AppCompatTextView
        editText.setText(name)
        editText.setSelection(name.length)
        //调用系统输入法
        mNameEditDialog!!.window!!.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
        mNameEditDialog!!.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        //        //调用系统输入法
        //        //设置可获得焦点
        //        editText.setFocusable(true);
        //        editText.setFocusableInTouchMode(true);
        //        //请求获得焦点
        //        editText.requestFocus();
        val inputManager = editText
                .context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.showSoftInput(editText, 0)

        negBtn.setOnClickListener { mNameEditDialog!!.dismiss() }
        posBtn.setOnClickListener {
            val str = editText.text.toString()
            if (TextUtils.isEmpty(str)) {
                ToastUtils.showToast(context,"姓名不能为空")
            } else {
                confirmListener.onConfirm(str)
                mNameEditDialog!!.dismiss()
            }
        }

    }

    fun dismissNameEditDialog() {
        if (mNameEditDialog != null) mNameEditDialog!!.dismiss()
    }

    interface NameEditConfirmListener{
        fun onConfirm(str:String)
    }

    private var mFullScreenImgPop: PopupWindow? = null
    /**
     * 显示用户绑定银行卡列表popwindow
     */
    fun showFullScreenImgPop(activity: Context, anchor:View, bitmap: Bitmap?=null, url:String?=null) {
        if (bitmap == null && StringUtils.isEmpty(url)) {
//            ToastUtils.showToast(activity,"image res must not be null")
            return
        }
        if (bitmap != null && StringUtils.isNotEmpty(url)) {
//            ToastUtils.showToast(activity,"bitmap和url不能同时存在")
            return
        }
        if (mFullScreenImgPop != null) {
            dismissFullScreenImgPop()
        }
        mFullScreenImgPop = PopupWindow(activity)
        val view = LayoutInflater.from(activity).inflate(R.layout.pop_fullscreen_img, null)
        mFullScreenImgPop!!.contentView = view
        view.setOnClickListener{ mFullScreenImgPop!!.dismiss() }
        val imageView = view.findViewById<ImageView>(R.id.pop_fullscreen_img)
        if (bitmap != null) imageView.setImageBitmap(bitmap)

        if (StringUtils.isNotEmpty(url)) ImageLoader.loadImage(activity,url,imageView)

        mFullScreenImgPop!!.width = LinearLayout.LayoutParams.MATCH_PARENT
        mFullScreenImgPop!!.height = LinearLayout.LayoutParams.MATCH_PARENT
        mFullScreenImgPop!!.setBackgroundDrawable(ColorDrawable())
        mFullScreenImgPop!!.isOutsideTouchable = false
        mFullScreenImgPop!!.isTouchable = true
//        mFullScreenImgPop!!.isFocusable = true
        mFullScreenImgPop!!.animationStyle = R.style.popWindowAnim
        mFullScreenImgPop!!.showAsDropDown(anchor)
    }

    fun dismissFullScreenImgPop(){
        if (mFullScreenImgPop != null) {
            mFullScreenImgPop!!.dismiss()
            mFullScreenImgPop = null
        }
    }
}