package cn.xzj.agent.ui.mine

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import android.support.v7.widget.GridLayoutManager
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import cn.xzj.agent.R
import cn.xzj.agent.constants.Constants
import cn.xzj.agent.constants.EnumValue
import cn.xzj.agent.core.common.mvp.MVPBaseActivity
import cn.xzj.agent.entity.FeedbackBody
import cn.xzj.agent.entity.FileUploadVO
import cn.xzj.agent.iview.IFeedbackView
import cn.xzj.agent.presenter.FeedbackPresenter
import cn.xzj.agent.ui.adapter.common.BaseHolder
import cn.xzj.agent.ui.adapter.common.QuickAdapter
import cn.xzj.agent.util.BitmapUtil
import cn.xzj.agent.util.CommonUtils
import cn.xzj.agent.util.PermissionsUtil
import cn.xzj.agent.widget.CommonDialog
import cn.xzj.agent.widget.SimpleBottomSheetDialog
import cn.xzj.agent.widget.SimpleToast
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_feedback.*

/**
 *
 * @Author MarkYe
 * @Email yrmao9893@163.com
 * @Date 2019/3/7
 * @Des 问题反馈
 */
class FeedbackActivity : MVPBaseActivity<FeedbackPresenter>(), IFeedbackView {
    private var mSimpleBottomSheetDialog: SimpleBottomSheetDialog.Builder? = null
    private var mFeedbackTypeDialog: SimpleBottomSheetDialog.Builder? = null
    private var currentPhotoPath: String? = null//当前拍照照片的路径
    private val REQUEST_CODE_PHOTO_IMAGE = 1001
    private val IMAGE_REQUEST_CODE = 1002
    private val REQUEST_CODE_CAMERA = 1003
    private var feedbackType: String? = null
    private val mAdapter = object : QuickAdapter<String>(R.layout.item_feedback_image) {
        override fun convert(holder: BaseHolder, item: String, position: Int) {
            if (position == data!!.size - 1) {
                holder.setVisibility(R.id.ivItemFeedbackDelete, View.GONE)
                holder.setVisibility(R.id.llItemFeedbackPhoto, View.VISIBLE)
                holder.setVisibility(R.id.ivItemFeedback, View.GONE)
                holder.getView<View>(R.id.llItemFeedbackPhoto).setOnClickListener {
                    if (position >= 3) {
                        SimpleToast.showNormal("最多上传3张图片")
                    } else {
                        showPhotoDialog()
                    }
                }
            } else {
                holder.setVisibility(R.id.ivItemFeedbackDelete, View.VISIBLE)
                holder.setVisibility(R.id.llItemFeedbackPhoto, View.GONE)
                holder.setVisibility(R.id.ivItemFeedback, View.VISIBLE)
                holder.getView<View>(R.id.ivItemFeedbackDelete).setOnClickListener {
                    remove(position)
                    notifyDataSetChanged()
                }
                Glide.with(context!!).load(item)
                        .placeholder(R.mipmap.ic_feedback_default)
                        .into(holder.getView(R.id.ivItemFeedback))
            }
        }

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_feedback
    }

    override fun context(): Context {
        return this
    }

    override fun initViews() {
        setLifeBack()
        setTitle("问题反馈")
        recyclerViewActivityImage.layoutManager = GridLayoutManager(this, 4)
        recyclerViewActivityImage.adapter = mAdapter
        mAdapter.addItem("")
        mAdapter.notifyDataSetChanged()
    }

    override fun setListeners() {
        super.setListeners()
        btnActivityFeedbackCommit.setOnClickListener {
            val feedbackContent = editActivityFeedbackDetail.text.toString().trim()
            if (feedbackContent.isEmpty()) {
                SimpleToast.showNormal(editActivityFeedbackDetail.hint.toString())
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(feedbackType)) {
                SimpleToast.showNormal(getString(R.string.please_select_issue_type))
                return@setOnClickListener
            }
            if (mAdapter.data!!.size <= 1) {
                mPresenter.commit(FeedbackBody(feedbackContent, feedbackType, null))
            } else {
                val mImagePath = ArrayList<String>()
                for (i in 0 until mAdapter.data!!.size - 1) {
                    mImagePath.add(mAdapter.data!![i])
                }
                mPresenter.imagesUpload(mImagePath)
            }
        }
        btnActivityFeedbackSelectIssueType.setOnClickListener {
            showFeedbackTypeDialog()
        }
        editActivityFeedbackDetail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            @SuppressLint("SetTextI18n")
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tvActivityFeedbackMaxLength.text = "可输入${s!!.length}/200字"
            }
        })
    }

    override fun onFeedbackSuccess() {
        CommonDialog.newBuilder(this)
                .setCancelable(false)
                .setMessage("您的反馈提交成功")
                .setPositiveButton("我知道了") {
                    it.cancel()
                    finish()
                }
                .create()
                .show()
        CommonUtils.statistics(this, Constants.STATISTICS_problemFeedback_SUBMIT_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
    }

    override fun onImagesUploadSuccess(data: List<FileUploadVO>) {
        val feedbackContent = editActivityFeedbackDetail.text.toString().trim()
        val imageUrls = ArrayList<String>()
        for (itemData in data) {
            imageUrls.add(itemData.url)
        }
        mPresenter.commit(FeedbackBody(feedbackContent, feedbackType, imageUrls))
    }


    override fun onImagesUploadFail() {
    }

    private fun showPhotoDialog() {
        if (mSimpleBottomSheetDialog == null) {
            mSimpleBottomSheetDialog = SimpleBottomSheetDialog.newBuilder(this)
                    .setCancelable(true)
                    .setData("拍照", "图片库")
                    .setItemClicklistener { v, s, position ->
                        when (position) {
                            0 -> {
                                if (!PermissionsUtil.allowPermissions(this, Manifest.permission.CAMERA)) {
                                    PermissionsUtil.requestPermission(this, Manifest.permission.CAMERA, "打开相机", REQUEST_CODE_CAMERA)
                                } else {
                                    currentPhotoPath = BitmapUtil.toPhoto(this, REQUEST_CODE_PHOTO_IMAGE)
                                }
                            }
                            1 -> {
                                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                                startActivityForResult(intent, IMAGE_REQUEST_CODE)
                            }
                        }
                    }
        }
        mSimpleBottomSheetDialog!!.show()

    }

    private fun showFeedbackTypeDialog() {
        /**
         * type (string, optional): 问题类型: APP - 用户端APP, AGENT_PC - 小职姐PC端, AGENT_APP - 小职姐服务版, STORE - 门店系统
         */
        if (mFeedbackTypeDialog == null) {
            mFeedbackTypeDialog = SimpleBottomSheetDialog.newBuilder(this)
                    .setCancelable(true)
                    .setData("小职姐服务版APP", "PC版CRM服务系统", "其他问题或建议")
                    .setItemClicklistener { v, s, position ->
                        tvActivityFeedbackSelectIssueType.text = s
                        when (position) {
                            0 -> {
                                feedbackType = "AGENT_APP"
                            }
                            1 -> {
                                feedbackType = "AGENT_PC"
                            }
                            2 -> {
                                feedbackType = "OTHERS"
                            }
                        }
                    }
        }
        mFeedbackTypeDialog!!.show()

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_PHOTO_IMAGE -> {
                if (resultCode == RESULT_OK) {
                    if (currentPhotoPath != null) {
                        addAdapterItem(currentPhotoPath!!)
                    }
                }
            }
            IMAGE_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        val selectedImage = data!!.data
                        val filePathColumns = arrayOf(MediaStore.Images.Media.DATA)
                        val c = contentResolver.query(selectedImage, filePathColumns, null, null, null)
                        c!!.moveToFirst()
                        val columnIndex = c.getColumnIndex(filePathColumns[0])
                        val imagePath = c.getString(columnIndex)
                        c.close()
                        addAdapterItem(imagePath)

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    private fun addAdapterItem(imagePath: String) {
        mAdapter.remove(mAdapter.data!!.size - 1)
        mAdapter.addItem(imagePath)
        mAdapter.addItem("")
        mAdapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        CommonUtils.statistics(this, Constants.STATISTICS_problemFeedback_ID, EnumValue.STATISTICS_TYPE_PAGE, EnumValue.STATISTICS_METRIC_VIEW)

    }
}