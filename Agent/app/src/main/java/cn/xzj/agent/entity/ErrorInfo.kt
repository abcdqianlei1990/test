package cn.xzj.agent.entity

import java.util.ArrayList

/**
 * Created by channey on 2016/10/28.
 * version:1.0
 * desc:
 */

data class ErrorInfo(val message: String,val details: String,val errors: ArrayList<ErrorsInfo>)
