package cn.xzj.agent.ui.customer

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.os.Message
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.view.View
import cn.xzj.agent.R
import cn.xzj.agent.constants.Code
import cn.xzj.agent.core.common.BaseActivity
import cn.xzj.agent.core.common.mvp.MVPBaseActivity
import cn.xzj.agent.entity.FileUploadDTO
import cn.xzj.agent.entity.FileUploadVO
import cn.xzj.agent.entity.customer.BadgeUploadRequestBody
import cn.xzj.agent.entity.customer.WorkExperienceCompanyInfo
import cn.xzj.agent.iview.IAddWorkExp
import cn.xzj.agent.presenter.AddWorkExpPresenter
import cn.xzj.agent.ui.CompanySelectActivity
import cn.xzj.agent.util.CommonUtils
import cn.xzj.agent.util.DialogUtil
import com.channey.utils.ImageUtils
import kotlinx.android.synthetic.main.activity_add_workrecord.*
import java.io.*

/**
 * 添加工作经历,上传工牌
 */
class AddWorkExperienceActivity():MVPBaseActivity<AddWorkExpPresenter>(),IAddWorkExp,View.OnClickListener {

    override fun getLayoutId(): Int {
        return R.layout.activity_add_workrecord
    }

    override fun context(): Context {
        return this
    }

    private val tag = "AddWorkRecordActivity"
    private val title = "上传工牌"
    private val WORKCARD_NAME = "workcard.png"
    private var mUserId:String? = null
    companion object {
        const val REQUESTCODE_CAMERA_PERMISSION = 0x001
        const val REQUESTCODE_TAKEPHOTO = 0x002
        const val key_userId = "userId"
        fun jump(activity: BaseActivity,userId:String,requestCode:Int = Code.RequestCode.AddWorkExperienceActivity){
            var intent = Intent(activity, AddWorkExperienceActivity::class.java)
            intent.putExtra(key_userId,userId)
            activity.startActivityForResult(intent,requestCode)
        }
    }

    override fun initParams() {
        mUserId = intent.getStringExtra(key_userId)
    }

    override fun initViews() {
        setTitle(title)
        com.jaeger.library.StatusBarUtil.setColor(this,resources.getColor(R.color.green29AC3E),1)
    }

    override fun initData() {

    }

    override fun setListeners() {
        activity_add_workrecord_companySelectGroup.setOnClickListener(this)
        activity_add_workrecord_workCardGroup.setOnClickListener(this)
        activity_add_workrecord_submit_btn.setOnClickListener(this)
        activity_add_workrecord_workcard_img.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.activity_add_workrecord_companySelectGroup -> {
                mPresenter.getWorkExpCompany(mUserId!!)
            }
            R.id.activity_add_workrecord_workCardGroup -> {
                requestCameraPermission()
            }
            R.id.activity_add_workrecord_submit_btn -> {
                if (mApplyId == null){
                    showToast("请先选择入职企业")
                    return
                }
                if (mBitmap == null){
                    showToast("请选择工牌照片")
                    return
                }
                uploadWorkCardImg()
            }
            R.id.activity_add_workrecord_workcard_img -> viewFullScreen()
        }
    }

    private fun requestCameraPermission(){
        val permission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        val storagePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (permission == PackageManager.PERMISSION_GRANTED &&
                storagePermission == PackageManager.PERMISSION_GRANTED) {
            openCamera()
        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(arrayOf(Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUESTCODE_CAMERA_PERMISSION)
            }else{
                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUESTCODE_CAMERA_PERMISSION)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUESTCODE_CAMERA_PERMISSION){
            for (i in 0 until  permissions.size){
                if (permissions[i] == Manifest.permission.CAMERA && grantResults[i] == PackageManager.PERMISSION_GRANTED){
                    openCamera()
                }
            }
        }
    }

    private var mImageUri: Uri?= null
    private var mImagePath: String?= null
    private fun openCamera(){
        //启动相机程序
        mImageUri = getUri()
        var intent = Intent()
        intent.action = MediaStore.ACTION_IMAGE_CAPTURE
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri)
        startActivityForResult(intent, REQUESTCODE_TAKEPHOTO)
    }

    private fun getUri(): Uri {
        var uri:Uri
        var file = getFile()
        mImagePath = file.absolutePath
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){//24
            val authority = "$packageName.fileProvider"
            uri = FileProvider.getUriForFile(this,authority,file)
        }else {
            uri = Uri.fromFile(file)
        }
        return uri
    }

    /**
     * 该方法用于获取指定路径 和 名字 的file
     * @return
     */
    private fun getFile(): File {
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).absolutePath + "/xzj/"
        val filePath = File(path)
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val file = File(filePath.path + File.separator + WORKCARD_NAME)
        if (file.exists())
            file.delete()
        return file

    }

    private var mSourceBitmap:Bitmap ?= null
    private var mBitmap:Bitmap ?= null
    private var mApplyId:String ?= null
    private val MSG_SET_BITMAP = 0
    private val MSG_SHOW_LOADING = 1
    private var mHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when(msg.what){
                MSG_SET_BITMAP -> {
                    closeProgressDialog()
                    if (mBitmap != null){
                        activity_add_workrecord_camera_img.visibility = View.GONE
                        activity_add_workrecord_workcard_img.visibility = View.VISIBLE
                        activity_add_workrecord_workcard_img.setImageBitmap(mBitmap)
                    }
                }
                MSG_SHOW_LOADING -> {
                    showProgressDialog("",false)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        bitmapRecycle()
    }

    private fun bitmapRecycle(){
        if (mSourceBitmap != null) {
            mSourceBitmap!!.recycle()
            mSourceBitmap = null
        }
        if (mBitmap != null) {
            mBitmap!!.recycle()
            mBitmap = null
        }
        System.gc()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            REQUESTCODE_TAKEPHOTO -> {
                try {
                    //如果不先设置null，那么在bitmap执行recycle()时，会因为view重绘已被回收的bitmap而异常
                    //Canvas: trying to use a recycled bitmap android.graphics.Bitmap
                    activity_add_workrecord_workcard_img.setImageBitmap(null)
                    bitmapRecycle()//回收bitmap，不然会内存溢出
//                    var bm = BitmapFactory.decodeStream(contentResolver.openInputStream(mImageUri))
                    var bm = BitmapFactory.decodeFile(mImagePath)
                    if (bm == null){
                        return
                        showToast("图片解析异常，请重试")
                    }
                    val degree = ImageUtils.getImageDegree(mImagePath!!)
                    mSourceBitmap = ImageUtils.rotateImage(degree, bm)
                    compressImage(mImagePath!!, object : ImageCompressListener {
                        override fun onCompressFinished() {
                            mBitmap = BitmapFactory.decodeFile(mImagePath!!)
                            mHandler.sendEmptyMessage(MSG_SET_BITMAP)
                        }
                    })
                } catch (e:Exception) {
                    e.printStackTrace()
                    bitmapRecycle()
                    showToast("图片解析异常，请重试")
                } catch (e:OutOfMemoryError){
                    e.printStackTrace()
                    bitmapRecycle()
                    showToast("图片解析异常，请重试")
                }
            }
            Code.RequestCode.CompanySelectActivity -> {
                if (data != null){
                    var name = data.getStringExtra(CompanySelectActivity.PARAM_KEY_POSITIONNAME)
                    mApplyId = data.getStringExtra(CompanySelectActivity.PARAM_KEY_APPLYID)
//                    if(mApplyId == null) mApplyId = "12345"
                    activity_add_workrecord_company_tv.text = name
                }
            }
        }
    }

    interface ImageCompressListener{
        fun onCompressFinished()
    }
    private fun compressImage(path: String, listener:ImageCompressListener) {
        mHandler.sendEmptyMessage(MSG_SHOW_LOADING)
        var file = File(path)
        if (!file.exists()){
            showToast("文件不存在")
            return
        }
        Thread(Runnable {
            var image = BitmapFactory.decodeFile(path)
            var baos = ByteArrayOutputStream()
            image.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            var options = 80
            while (baos.toByteArray().size / 1024 > 1024) {
                baos.reset()
                image.compress(Bitmap.CompressFormat.JPEG, options, baos)
                options -= 5
            }
            //保存并覆盖源文件
            file.delete()
            var out = FileOutputStream(file)
            out.write(baos.toByteArray())
            out.flush()
            out.close()
            listener.onCompressFinished()
        }).start()
    }

    /**
     * 上传工牌照片
     */
    private fun uploadWorkCardImg() {
        var l = ArrayList<FileUploadDTO>()
        var entity = FileUploadDTO()
        var base64Str = CommonUtils.bitmapToBase64(mBitmap)
        entity.content = base64Str
        entity.size = base64Str.length
        entity.name = "badge${System.currentTimeMillis()}.png"
        l.add(FileUploadDTO())
        mPresenter.uploadWorkCardImg(l)
    }

    private fun viewFullScreen(){
        DialogUtil.showFullScreenImgPop(this,activity_add_workrecord_workcard_img,mSourceBitmap)
    }


    override fun onWorkExpCompanyGetSuccess(list: ArrayList<WorkExperienceCompanyInfo>) {
        if (list.isNotEmpty()){
            CompanySelectActivity.jump(this,list)
        }else{
            showToast("无入职企业")
        }
    }

    override fun onWorkExpCompanyGetFailure() {

    }

    override fun uploadFileSuccess(content: List<FileUploadVO>) {
        mPresenter.uploadBadge(BadgeUploadRequestBody(mApplyId!!,content[0].url,mUserId!!))
    }

    override fun uploadFileFailure() {

    }

    override fun uploadBadgeSuccess(success: Boolean) {
        if (success){
            showToast("工牌上传成功")
            finish()
        }
    }

    override fun uploadBadgeFailure() {

    }
}