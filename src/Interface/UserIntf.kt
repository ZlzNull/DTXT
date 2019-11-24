package com.zlz2000.Interface

import com.zlz2000.Bean.LoginBean
import com.zlz2000.Bean.UserDataBean
import com.zlz2000.Server.State

interface UserIntf {
    fun saveUser(data:UserDataBean):Boolean
    fun verifyUser(data:LoginBean):State
    fun searchUser(type:Boolean,number: String):Boolean
    fun findUserPhoneByWordId(workId:String):String?
}