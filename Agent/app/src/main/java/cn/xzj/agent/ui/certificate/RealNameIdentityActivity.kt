package cn.xzj.agent.ui.certificate

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import cn.xzj.agent.R
import cn.xzj.agent.constants.Code
import cn.xzj.agent.constants.Constants
import cn.xzj.agent.core.common.mvp.MVPBaseActivity
import cn.xzj.agent.entity.FileUploadDTO
import cn.xzj.agent.entity.FileUploadVO
import cn.xzj.agent.entity.certificate.*
import cn.xzj.agent.iview.IRealNameIdentity
import cn.xzj.agent.presenter.RealNameIdentityPresenter
import cn.xzj.agent.util.CommonUtils
import cn.xzj.agent.util.DialogUtil
import com.channey.utils.FormatUtils
import com.channey.utils.StringUtils
import com.megvii.demo.activity.IDCardDetectActivity
import com.megvii.demo.utils.Configuration
import com.megvii.idcardquality.IDCardQualityLicenseManager
import com.megvii.licensemanager.Manager

import kotlinx.android.synthetic.main.activity_realname_identity.*

import java.util.ArrayList
import java.util.HashMap

/**
 * Created by channey on 2016/11/8.
 * version:1.0
 * desc:
 */

class RealNameIdentityActivity : MVPBaseActivity<RealNameIdentityPresenter>(), IRealNameIdentity,View.OnClickListener{

    val TITLE = "实名认证"
    private val INTO_IDCARDSCAN_PAGE = 100
    private var uuid: String? = null
    private var mFrontCardImgGet = false   //已拍摄身份证正面照片
    private var mBackCardImgGet = false    //已拍摄身份证背面照片
    internal var mImgsMap: MutableMap<Int, Bitmap> = HashMap()
    private var mPosPath: String? = null
    private var mNegPath: String? = null
    private var mFrontLegality: LegalityInfo? = null
    private var mBackLegality: LegalityInfo? = null
    private var mFrontImgPath: String? = null
    private var mBackImgPath: String? = null
    private val FRONT_SIDE = 1
    private val BACK_SIDE = 2
    private val ID_PHOTO_VALUE = 0.80f

    private val FRONT_SIDE_NAME = "front_side.png"
    private val BACK_SIDE_NAME = "BACK_side.png"
    private var mIdCardInfo:IDCardInfo = IDCardInfo()
    companion object{
        val key_idCard = "idCard"
        val key_realname = "realname"
        fun jump(context: AppCompatActivity, requestCode: Int = Code.RequestCode.RealNameIdentityActivity) {
            val intent = Intent(context, RealNameIdentityActivity::class.java)
            context.startActivityForResult(intent, requestCode)
        }
    }

    override fun context(): Context {
        return this
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_realname_identity
    }

    override fun initParams() {
        uuid = CommonUtils.getUUIDString(this)
    }

    override fun initViews() {
        setTitle(TITLE)
    }

    override fun initData() {
        startGetLicense()
        checkCameraPermission()
    }

    private lateinit var mIdCardLicenseManager:IDCardQualityLicenseManager
    private fun startGetLicense() {
        mIdCardLicenseManager = IDCardQualityLicenseManager(this)
        var status: Long = 0
        try {
            status = mIdCardLicenseManager.checkCachedLicense()
        } catch (e: Throwable) {
            e.printStackTrace()
        }

        if (status > 0) {//大于0，已授权或者授权未过期

        } else { //需要重新授权
            Thread(Runnable {
                try {
                    getLicense()
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }).start()


        }

    }

    private fun getLicense() {
        val manager = Manager(this)
        manager.registerLicenseManager(mIdCardLicenseManager)
        val uuid = Configuration.getUUID(this)
        val authMsg = mIdCardLicenseManager.getContext(uuid)
        manager.takeLicenseFromNetwork(authMsg)
    }

    private var array: Array<String?> ?= null
    private fun checkCameraPermission() {
        val list = ArrayList<String>()
        val cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        val storagePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            list.add(Manifest.permission.CAMERA)
        }
        if (storagePermission != PackageManager.PERMISSION_GRANTED) {
            list.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        array = DialogUtil.listToArray(list)
        if (list.size > 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(array, 1)
            } else {
                ActivityCompat.requestPermissions(this, array!!, 1)
            }
        }
    }

    override fun setListeners() {
        card_verify_pos_img.setOnClickListener(this)
        card_verify_nag_img.setOnClickListener(this)
        card_verify_next_btn.setOnClickListener(this)
        card_verify_name.setOnClickListener(this)
        card_verify_number.setOnClickListener(this)
        card_verify_number_view_group.setOnClickListener(this)
        card_verify_name_view_group.setOnClickListener(this)
    }

    private fun uploadImage() {
        val list = ArrayList<FileUploadDTO>()
        val posBitmap = mImgsMap[FRONT_SIDE]
        val negBitmap = mImgsMap[BACK_SIDE]
        val posBase64 = CommonUtils.bitmapToBase64(posBitmap)
        val negBase64 = CommonUtils.bitmapToBase64(negBitmap)
        var posFile = FileUploadDTO()
        posFile.content = posBase64
        posFile.name = FRONT_SIDE_NAME
        posFile.size = posBase64!!.length

        var negFile = FileUploadDTO()
        negFile.content = negBase64
        negFile.name = BACK_SIDE_NAME
        negFile.size = negBase64!!.length
        list.add(posFile)
        list.add(negFile)
        mPresenter.uploadFile(list)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == INTO_IDCARDSCAN_PAGE && resultCode == RESULT_OK) {
            if (data != null){
                val side = Configuration.getCardType(this)
                val idcardImgData = data.getByteArrayExtra("idcardimg_bitmap")
                val idcardBmp = BitmapFactory.decodeByteArray(
                    idcardImgData, 0,
                    idcardImgData.size
                )
                mImgsMap[side] = idcardBmp
                val fileName = StringBuilder(Constants.PATH + "idcardImg_")
                if (side == FRONT_SIDE) {
                    mFrontCardImgGet = true
                    fileName.append("front")
                } else {
                    mBackCardImgGet = true
                    fileName.append("back")
                }
                fileName.append(".png")
                if (side == BACK_SIDE) {
                    mFrontImgPath = fileName.toString()
                } else {
                    mBackImgPath = fileName.toString()
                }
                //解析
                mPresenter.idCardOCR(fileName.toString(),idcardImgData)
//                getIDCardInfo(fileName.toString(), idcardImgData, side)
            }
        }
    }

    override fun onOCRSuccess(idCardInfo: IDCardInfo) {

        if (1 == idCardInfo.side) {
            mIdCardInfo.issued_by = idCardInfo.issued_by
            mIdCardInfo.valid_date_start = idCardInfo.valid_date_start
            mIdCardInfo.valid_date_end = idCardInfo.valid_date_end
            mBackLegality = idCardInfo.legality
            val frontBitmap = mImgsMap[FRONT_SIDE]
            if (frontBitmap == null) {
                showToast("请继续扫描身份证正面")
            }
            val drawable = BitmapDrawable(mImgsMap[BACK_SIDE])
            card_verify_nag_img.setBackgroundDrawable(drawable)
            card_verify_nag_restart_btn.visibility = View.VISIBLE
        } else {
            mIdCardInfo.name = idCardInfo.name
            mIdCardInfo.gender = idCardInfo.gender
            mIdCardInfo.nationality = idCardInfo.nationality
            mIdCardInfo.birth_year = idCardInfo.birth_year
            mIdCardInfo.birth_month = idCardInfo.birth_month
            mIdCardInfo.birth_day = idCardInfo.birth_day
            mIdCardInfo.address = idCardInfo.address
            mIdCardInfo.idcard_number = idCardInfo.idcard_number
            val backBitmap = mImgsMap[BACK_SIDE]
            if (backBitmap == null) {
                showToast("请继续扫描身份证背面")
            }
            val drawable = BitmapDrawable(mImgsMap[FRONT_SIDE])
            card_verify_pos_img.setBackgroundDrawable(drawable)
            card_verify_pos_restart_btn.visibility = View.VISIBLE
            mFrontLegality = idCardInfo.legality
        }
        if (mImgsMap.size == 2) {
            card_verify_name.setText(mIdCardInfo!!.name.result)
            card_verify_edit_tv.visibility = View.VISIBLE
            card_verify_number.setText(mIdCardInfo!!.idcard_number.result)
        }
    }

    override fun onOCRFailure(msg: String) {
        showToast(msg)
    }

    override fun onClick(view: View) {
        when(view.id){
            R.id.card_verify_pos_img -> {
                Configuration.setCardType(this, FRONT_SIDE)
                val intent = Intent(this, IDCardDetectActivity::class.java)
                startActivityForResult(intent, INTO_IDCARDSCAN_PAGE)
            }
            R.id.card_verify_nag_img -> {
                Configuration.setCardType(this, BACK_SIDE)
                val intent = Intent(this, IDCardDetectActivity::class.java)
                startActivityForResult(intent, INTO_IDCARDSCAN_PAGE)
            }
            R.id.card_verify_name -> {
                val name1 = card_verify_name.getText().toString()
                if (TextUtils.isEmpty(name1)) {
                    showToast("请先上传身份证照片")
                }
            }
            R.id.card_verify_number -> {
                val number1 = card_verify_number.getText().toString()
                if (TextUtils.isEmpty(number1)) {
                    showToast("请先上传身份证照片")
                }
            }
            R.id.card_verify_next_btn -> {
                if (!mFrontCardImgGet || !mBackCardImgGet) {
                    showToast("请先上传身份证照片")
                } else {
                    val b = cardParamsCheck()
                    if (b) {
                        val frontRealIdCard = isRealIdCard(mFrontLegality!!)
                        val backRealIdCard = isRealIdCard(mBackLegality!!)
                        if (!frontRealIdCard && !backRealIdCard) {
                            showToast("身份证非原件或身份证扫描未达到要求")
                            return
                        }
                        if (!frontRealIdCard) {
                            showToast("身份证正面非原件或身份证扫描未达到要求")
                            return
                        }
                        if (!backRealIdCard) {
                            showToast("身份证背面非原件或身份证扫描未达到要求")
                            return
                        }
                        uploadImage()
                    }
                }
            }
            R.id.card_verify_name_view_group -> {
                val name = card_verify_name.getText().toString()
                if (TextUtils.isEmpty(name)) {
                    showToast("请先上传身份证照片")
                } else {
                    DialogUtil.showNameEditDialog(this,name, object : DialogUtil.NameEditConfirmListener {
                        override fun onConfirm(str: String) {
                            card_verify_name.setText(str)
                            card_verify_name.setSelection(str.length)
                        }
                    })
                }
            }
            R.id.card_verify_number_view_group -> {
                val number = card_verify_number.getText().toString()
                if (TextUtils.isEmpty(number)) {
                    showToast("请先上传身份证照片")
                }

            }
        }
    }

    private fun cardParamsCheck(): Boolean {
        val name = card_verify_name.text.toString()
        val number = card_verify_number.text.toString()
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(number)) {
            showToast("缺少姓名或身份证号，请扫描身份证正面")
            return false
        }
        if (mIdCardInfo == null || mIdCardInfo!!.issued_by == null || StringUtils.isEmpty(mIdCardInfo!!.issued_by.result)) {
            showToast("缺少身份证签发机关，请扫描身份证背面")
            return false
        }
        if (mIdCardInfo == null || mIdCardInfo!!.valid_date_start == null || StringUtils.isEmpty(mIdCardInfo!!.valid_date_start.result)) {
            showToast("缺少身份证有效期，请扫描身份证背面")
            return false
        }
        if (!validDateVerify(mIdCardInfo!!.valid_date_end)) {
            showToast("该身份证已过期，无法完成实名认证")
            return false
        }
        if (number.length in 1..17) {
            showToast("不支持18位以下身份证号")
            return false
        }
        mIdCardInfo!!.name.result = name
        mIdCardInfo!!.idcard_number.result = number
        return true
    }

    /**
     * 判断是否是真实照片拍摄的
     * 大于阈值0.80即认为是原件
     * @param info
     * @return
     */
    private fun isRealIdCard(info: LegalityInfo): Boolean {
        val idPhoto = info.iD_Photo
        return idPhoto > ID_PHOTO_VALUE
    }

    /**
     * 判断身份证有效期截止日期是否有效
     * 有效期为5年，10年，20年和长期
     * @param validDate
     * @return
     * true => 1.证件上的截止时间 >= 当前日期 2.证件上的截止时间 <= 当前日期+20年
     * note:截止日期应该是当天都有效，所以 证件上的截止时间需要加上23小时59分59秒
     */
    private fun validDateVerify(validDate: IdCardOcrRetInfo): Boolean {
        if ("长期" == validDate.result) {
            return true
        }
        var validateStamp = FormatUtils.string2TimeStamp("yyyyMMdd", validDate.result)
        validateStamp += (23 * 3600 * 1000).toLong() + (59 * 60 * 1000).toLong() + (59 * 1000).toLong() + 999
        val currentTimeMillis = System.currentTimeMillis()
        return validateStamp > currentTimeMillis
    }

    override fun uploadFileSuccess(info: List<FileUploadVO>) {
        for (data in info) {
            val name = data.name
            when (name) {
                FRONT_SIDE_NAME -> mPosPath = data.url
                BACK_SIDE_NAME -> mNegPath = data.url
            }
        }
        var body = RealNamePostBody(
                idCardFrontImageUrl = mPosPath!!,
                idCardBackImageUrl = mNegPath!!,
                idNo = mIdCardInfo!!.idcard_number.result,
                name = mIdCardInfo!!.name.result
        )
        mPresenter.realNameCertificate(body)
    }

    override fun uploadFileFailure() {

    }

    override fun onCommitCardInfoSuccess(success:Boolean) {
        showToast("实名认证成功")
        val intent = Intent()
        intent.putExtra(key_idCard, mIdCardInfo!!.idcard_number)
        intent.putExtra(key_realname, mIdCardInfo!!.name)
        setResult(Code.ResultCode.OK, intent)
        finish()
    }

    override fun onCommitCardInfoFailure() {
    }

    override fun onDestroy() {
        super.onDestroy()
        CommonUtils.deleteFile(mFrontImgPath)
        CommonUtils.deleteFile(mBackImgPath)
    }
}

