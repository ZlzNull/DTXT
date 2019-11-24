package com.zlz2000.Dao

import com.zlz2000.Table.PhoneNumberToCode
import me.liuwj.ktorm.dsl.*
import java.sql.SQLIntegrityConstraintViolationException
import java.util.*

val dbc = db

class CodeDao {
    fun saveCode(code: String, phoneNumber: String, date: Long) {
        try {
            PhoneNumberToCode.insert {
                it.phoneNumber to phoneNumber
                it.code to code
                it.time to date
            }
        } catch (e: SQLIntegrityConstraintViolationException) {
            PhoneNumberToCode.update {
                it.code to code
                it.time to date
                where {
                    it.phoneNumber eq phoneNumber
                }
            }
        } catch (e: Exception) {
            println("保存验证码出错：$e")
        }
    }

    fun verifyCodeByPhoneNumber(phoneNumber: String, code: String, time: Long): Boolean =
        try {
            var result: String? = null
            var tempTime: Long? = null
            for (row in PhoneNumberToCode.select(PhoneNumberToCode.code, PhoneNumberToCode.time).where {
                PhoneNumberToCode.phoneNumber eq phoneNumber
            }) {
                result = row[PhoneNumberToCode.code]
                tempTime = row[PhoneNumberToCode.time]
            }
            tempTime != null && (time - tempTime) < 300000 && result != null && code == result
        } catch (e: Exception) {
            println("在数据库中验证验证码出错:$e")
            false
        }
}

