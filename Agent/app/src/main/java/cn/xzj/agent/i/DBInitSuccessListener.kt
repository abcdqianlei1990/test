package cn.xzj.agent.i

import net.sqlcipher.database.SQLiteDatabase

interface DBInitSuccessListener {
    fun onSuccess(db:SQLiteDatabase)
}