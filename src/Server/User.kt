package com.zlz2000.Server

import com.zlz2000.Bean.ForgetBean
import com.zlz2000.Bean.LoginBean
import com.zlz2000.Bean.RegisterBean
import com.zlz2000.Bean.UserDataBean
import com.zlz2000.Dao.UserDao
import com.zlz2000.Interface.ForgetIntf
import com.zlz2000.Interface.LoginIntf
import com.zlz2000.Interface.RegisterIntf
import com.zlz2000.Interface.UserIntf
import java.util.*

class User : RegisterIntf, LoginIntf, ForgetIntf, UserIntf {
    override fun register(data: RegisterBean): Map<String, Any> {
        if (!Code().verifyCode(data.phoneNumber, data.code, Date().time)) {
            return mapOf("flag" to false, "msg" to "验证码错误")
        }
        val result = findUserPhoneByWordId(data.workId)
        if (result != null) {
            return mapOf("flag" to false, "msg" to "注册出错，请检查工号")
        }
        if (!saveUser(UserDataBean(data.name, data.phoneNumber, data.workId, data.password))) {
            return mapOf("flag" to false, "msg" to "注册出错，请稍后再试")
        }
        return mapOf("flag" to true)
    }

    override fun login(data: LoginBean): Map<String, Any> =
        when (verifyUser(data)) {
            State.NONE -> mapOf("flag" to false, "msg" to "用户不存在，请检查工号或手机号是否输入正确")
            State.ERROR -> mapOf("flag" to false, "msg" to "密码错误")
            State.SUCCESS -> mapOf("flag" to true)
        }

    override fun forget(data: ForgetBean): Map<String, Any> {
        if (data.type) {
            if (!Code().verifyCode(data.number, data.code, Date().time)) {
                return mapOf("flag" to false, "msg" to "验证码错误")
            }
        } else {
            val result = findUserPhoneByWordId(data.number) ?: return mapOf("flag" to false, "msg" to "请检查工号是否正确!")
            if (!Code().verifyCode(result, data.code, Date().time)) {
                return mapOf("flag" to false, "msg" to "验证码错误")
            }
        }
        if (!UserDao().updatePassword(data.type, data.number, data.password)){
            return mapOf("flag" to false, "msg" to "修改密码失败，请稍后再试")
        }
        return mapOf("flag" to true)
    }

    override fun saveUser(data: UserDataBean): Boolean = UserDao().saveUser(data)

    override fun verifyUser(data: LoginBean): State {
        return when (val temp = UserDao().searchUserPassword(data.type, data.number)) {
            null -> State.NONE
            else -> {
                if (data.password == temp) {
                    State.SUCCESS
                } else {
                    State.ERROR
                }
            }
        }
    }

    override fun searchUser(type: Boolean, number: String): Boolean = UserDao().searchUser(type, number)

    override fun findUserPhoneByWordId(workId: String): String? = UserDao().findUserPhoneByWordId(workId)
}

enum class State {
    NONE,
    ERROR,
    SUCCESS
}
