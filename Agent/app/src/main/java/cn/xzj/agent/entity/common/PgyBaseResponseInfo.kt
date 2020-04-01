package cn.xzj.agent.entity.common

data class PgyBaseResponseInfo<T>(val code: String, val data: T, val message: String){
    fun isSuccess():Boolean {
        return code.equals("0")
    }
}
