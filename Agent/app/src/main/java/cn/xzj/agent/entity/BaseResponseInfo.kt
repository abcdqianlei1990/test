package cn.xzj.agent.entity

/**
 * Created by channey on 2016/10/28.
 * version:1.0
 * desc:

 * {
 * "code": "string",
 * "content": {
 * "name": "phone", "value":"13524612345"
 * ; *      },
 * "error": {
 * "message": "string",
 * "source": "string",
 * "parameter": {}
 * }
 * }
 */
data class BaseResponseInfo<T>(val code: String,val content: T,val error: ErrorInfo){
    fun isSuccess():Boolean {
        return code.equals("0")
    }

    /**
     * token失效
     */
    fun isTokenInvalid():Boolean{
        return code.startsWith("4")
    }

    override fun toString(): String {
        return "BaseResponseInfo(code='$code', content=$content, error=$error)"
    }

}
