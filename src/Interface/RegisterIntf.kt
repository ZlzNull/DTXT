package com.zlz2000.Interface

import com.zlz2000.Bean.RegisterBean

interface RegisterIntf {
    fun register(data:RegisterBean):Map<String,Any>
}