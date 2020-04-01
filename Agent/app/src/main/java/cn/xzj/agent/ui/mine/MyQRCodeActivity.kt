package cn.xzj.agent.ui.mine

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.support.v4.content.FileProvider
import android.view.View
import cn.xzj.agent.BuildConfig
import cn.xzj.agent.R
import cn.xzj.agent.constants.Config.ROOT_FILE_NAME
import cn.xzj.agent.constants.Constants
import cn.xzj.agent.constants.EnumValue
import cn.xzj.agent.core.common.QuickActivity
import cn.xzj.agent.core.common.mvp.MVPBaseActivity
import cn.xzj.agent.entity.agentinfo.AgentInfo
import cn.xzj.agent.entity.common.QrCodeTemplateInfo
import cn.xzj.agent.iview.IMyQRCode
import cn.xzj.agent.net.Client
import cn.xzj.agent.presenter.MyQRCodePresenter
import cn.xzj.agent.ui.SplashActivity
import cn.xzj.agent.util.CommonUtils
import cn.xzj.agent.util.ImageLoader
import cn.xzj.agent.util.QRCodeUtil
import cn.xzj.agent.util.SharedPreferencesUtil
import cn.xzj.agent.widget.CommonDialog
import cn.xzj.agent.widget.SimpleBottomSheetDialog
import cn.xzj.agent.widget.SimpleToast
import com.channey.utils.ShapeUtil
import com.channey.utils.StringUtils
import kotlinx.android.synthetic.main.activity_my_qrcode.*
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream

/**
 * 我的二维码
 */
class MyQRCodeActivity : MVPBaseActivity<MyQRCodePresenter>(),IMyQRCode {

    private lateinit var agentInfo: AgentInfo
    private lateinit var nickName: String
    private var h5_url="https://h5activity.xiaozhijie.com"
    private var mTemplates = ArrayList<QrCodeTemplateInfo>()
    private var mCurTemplate = 0

    override fun context(): Context {
        return applicationContext
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_my_qrcode
    }

    override fun initParams() {
        super.initParams()
        agentInfo = SharedPreferencesUtil.getCurrentAgentInfo(this)
        nickName = if (!StringUtils.isEmpty(agentInfo.nick)) agentInfo.nick else agentInfo.name + ""
    }

    override fun initData() {
        super.initData()
        mPresenter.getQRCodeTemplates()
    }

    @SuppressLint("SetTextI18n")
    override fun initViews() {
        setLifeBack()
        setTitle("我的二维码")
        setRightBtn(R.mipmap.nav_sort, View.OnClickListener {
            showActionsDialog()
            CommonUtils.statistics(this, Constants.STATISTICS_QRCODE_saveImg_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
        })
        //设置昵称
        tv_nike_name.text = nickName
        //微信号
        wechat.text = agentInfo.wechat
        //设置头像
        var avatar = "" //头像
        if (agentInfo.headImages != null) {
//            for (img in agentInfo.headImages) {
//                if (img.isSelected) {
//                    Glide.with(this)
//                            .load(img.url)
//                            .placeholder(R.mipmap.usercenter_avatar)
//                            .bitmapTransform(GlideCircleTransform(this))
//                            .error(R.mipmap.ic_rqcode_default_avatar)
//                            .into(iv_photo)
//                    avatar=img.url
//                    break
//                }
//            }
        }
        //设置url
        when(BuildConfig.ENVIRONMENT){
            Client.ENV_DEV->{
                h5_url="http://h5activity-dev.lxworker.com"
            }
            Client.ENV_TEST->{
                h5_url="http://h5activity-test.lxworker.com"
            }
            Client.ENV_PRO->{
                h5_url="https://h5activity.xiaozhijie.com"
            }
        }
        //生成二维码
        var mBitmap = QRCodeUtil.createQRCodeBitmap("$h5_url/register/xzj.html?agent-id=${agentInfo.agentId}&agent-name=$nickName&avatar=$avatar", 400)
        val logo = BitmapFactory.decodeResource(resources,R.mipmap.logo)
        mBitmap = QRCodeUtil.createQRCodeBitmap("$h5_url/register/xzj.html?agent-id=${agentInfo.agentId}&agent-name=$nickName&avatar=$avatar", 400,logo,0.2f)
        iv_qr_code.setImageBitmap(mBitmap)
        ShapeUtil.setShape(iv_qr_code,radius = resources.getDimension(R.dimen.dp_4),solidColor = resources.getColor(R.color.white))
    }

    private fun showActionsDialog() {
        SimpleBottomSheetDialog.newBuilder(this)
                .setData("换个样式", "保存图片并分享")
                .setItemClicklistener { v, s, position ->
                    when(position){
                        0 -> {
                            if (mTemplates.isNotEmpty()){
                                if (mCurTemplate < mTemplates.size-1){
                                    mCurTemplate++
                                }else{
                                    mCurTemplate = 0
                                }
                                var template = mTemplates[mCurTemplate]
                                ImageLoader.loadImage(this,template.url,activity_qrcode_img,R.mipmap.qrcode_bg)
                            }else{
                                showToast("未配置样式")
                            }
                        }
                        1 -> savePhoto()
                    }
                }
                .show()
    }
    override fun onResume() {
        super.onResume()
        CommonUtils.statistics(this, Constants.STATISTICS_QRCODE_ID, EnumValue.STATISTICS_TYPE_PAGE, EnumValue.STATISTICS_METRIC_VIEW)
    }

    private fun savePhoto() {
        //**********将layout转换成bitmap************/
        rl_qrcode_parent.isDrawingCacheEnabled = true
        rl_qrcode_parent.buildDrawingCache()
        val bmp = rl_qrcode_parent.drawingCache

        //**********保存图片到本地************/
        try {
            //创建文件夹
            val rootFile = CommonUtils.getAgentImageRootFile()
            val fileName = "$nickName.png"
            //创建图片文件
            val myCaptureFile = File(rootFile, fileName)
            fun save() {
                val bos = BufferedOutputStream(FileOutputStream(myCaptureFile))
                bmp.compress(Bitmap.CompressFormat.PNG, 90, bos)
                bos.flush()
                bos.close()
            }
            if (!myCaptureFile.exists()) {
                //文件不存在,新建文件
                myCaptureFile.createNewFile()
                save()
                showShareDialog(myCaptureFile)
            } else {
                //图片已存在提示是否重新保存
                CommonDialog.newBuilder(this)
                        .setCancelable(true)
                        .setMessage("图片已存在是否替换")
                        .setNegativeButton("取消") {
                            showShareDialog(myCaptureFile)
                        }.setPositiveButton("替换") {
                            it.cancel()
                            save()
                            showShareDialog(myCaptureFile)
                        }
                        .create()
                        .show()
                return
            }


        } catch (e: Exception) {
            e.printStackTrace()
            SimpleToast.showLong(e.message)
        }

    }

    private fun showShareDialog(myCaptureFile: File) {
        CommonDialog.newBuilder(this)
                .setCancelable(true)
                .setTitle("保存成功")
                .setMessage("查看图片去 '文件管理' > '$ROOT_FILE_NAME' ,是否去分享?")
                .setNegativeButton("取消") {
                }
                .setPositiveButton("去分享") {
                    it.cancel()
                    val imageUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                        val authority = "$packageName.fileProvider"
                        FileProvider.getUriForFile(this, authority, myCaptureFile)
                    }
                    else Uri.fromFile(myCaptureFile)
                    val shareIntent = Intent()
                    shareIntent.action = Intent.ACTION_SEND
                    shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri)
                    shareIntent.type = "image/*"
                    startActivity(Intent.createChooser(shareIntent, "分享图片"))
                }
                .create()
                .show()
    }


    override fun onQRCodeTemplateGetSuccess(list: ArrayList<QrCodeTemplateInfo>) {
        mTemplates.clear()
        mTemplates.addAll(list)
    }

    override fun onQRCodeTemplateGetFailure() {

    }
}
