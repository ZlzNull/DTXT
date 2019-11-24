package com.zlz2000.Dao

import com.zlz2000.Bean.UserDataBean
import com.zlz2000.Table.UserTable
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.*

val db = Database.connect(
    "jdbc:mysql://localhost:3306/dtxt?serverTimezone=UTC",
    "com.mysql.cj.jdbc.Driver",
    "zlz",
    "Whyzshngrd1@db"
)

class UserDao {
    fun saveUser(data: UserDataBean): Boolean =
        try {
            UserTable.insert {
                it.userName to data.name
                it.userPassword to data.password
                it.userPhoneNumber to data.phoneNumber
                it.userWorkId to data.workId
                it.userType to 0
            }
            true
        } catch (e: Exception) {
            println("插入用户出错：$e")
            false
        }

    fun searchUserPassword(type: Boolean, number: String): String? =
        try {
            var result: String? = null
            for (row in UserTable.select(UserTable.userPassword).where {
                if (type) {
                    UserTable.userPhoneNumber eq number
                } else {
                    UserTable.userWorkId eq number
                }
            }) {
                result = row[UserTable.userPassword]
            }
            result
        } catch (e: Exception) {
            println("查找用户密码出错：$e")
            null
        }

    fun searchUser(type: Boolean, number: String): Boolean =
        try {
            var result = false
            for (row in UserTable.select(UserTable.userName).where {
                if (type) {
                    UserTable.userPhoneNumber eq number
                } else {
                    UserTable.userWorkId eq number
                }
            }) {
                result = true
            }
            result
        } catch (e: Exception) {
            println("查找用户名出错：$e")
            false
        }

    fun findUserPhoneByWordId(workId: String): String? =
        try {
            var result: String? = null
            for (row in UserTable.select(UserTable.userPhoneNumber).where {
                UserTable.userWorkId eq workId
            }) {
                result = row[UserTable.userPhoneNumber]
            }
            result
        } catch (e: Exception) {
            println("根据用户工号查找对应手机号出错:$e")
            null
        }

    fun updatePassword(type: Boolean, number: String, password: String): Boolean =
        try {
            UserTable.update {
                it.userPassword to password
                if (type) {
                    where {
                        it.userPhoneNumber eq number
                    }
                } else {
                    where { it.userWorkId eq number }
                }
            }
            true
        } catch (e: Exception) {
            println("更新用户密码出错:$e")
            false
        }

}