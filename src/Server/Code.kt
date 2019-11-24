package com.zlz2000.Server

import com.zlz2000.Dao.CodeDao
import com.zlz2000.Interface.CodeIntf
import java.util.*
import kotlin.Exception

class Code : CodeIntf {
    override fun verifyCode(phoneNumber: String, code: String, time: Long): Boolean =
        CodeDao().verifyCodeByPhoneNumber(phoneNumber, code, time)

    override fun sendCode(
        numberType: Boolean,
        number: String,
        type: String
    ): Map<String, Any> =
        when (User().searchUser(numberType, number)) {
            true -> {
                if (type == "register") {
                    mapOf("flag" to false, "msg" to "该手机号已被注册，请尝试登录。如忘记密码，请找回密码。")
                } else {
                    //发送验证码并保存
                    if (numberType) {
                        sendCode(number)
                        mapOf("flag" to true)
                    } else {
                        //先根据工号找到手机号
                        when (val phoneNumber = User().findUserPhoneByWordId(number)) {
                            null -> mapOf("flag" to false, "msg" to "该工号不存在对应账号，请检查后重试！")
                            else -> {
                                sendCode(phoneNumber)
                                mapOf("flag" to true)
                            }
                        }
                    }
                    mapOf("flag" to true)
                }
            }
            false -> {
                if (type == "register") {
                    //发送验证码并保存
                    sendCode(number)
                    mapOf("flag" to true)
                } else {
                    if (numberType) {
                        mapOf("flag" to false, "msg" to "该手机号未注册，请检查后重试！")
                    } else {
                        mapOf("flag" to false, "msg" to "该工号不存在对应账号，请检查后重试！")
                    }
                }
            }
        }

    override fun sendCode(phoneNumber: String): Boolean =
        try {
            //生成验证码
            val code by lazy {
                var a = ""
                for (i in 1..6) {
                    a += (0..9).random()
                }
                a
            }
            //发送验证码

            //保存验证码到数据库
            CodeDao().saveCode(code, phoneNumber, Date().time)
            true
        } catch (e: Exception) {
            println("发送验证码出错：$e")
            false
        }

}