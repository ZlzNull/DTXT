package com.zlz2000.Interface

import com.zlz2000.Bean.LoginBean

interface LoginIntf {
    fun login(data:LoginBean):Map<String,Any>
}