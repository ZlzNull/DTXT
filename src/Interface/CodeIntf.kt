package com.zlz2000.Interface


interface CodeIntf {
    fun verifyCode(phoneNumber:String,code:String,time:Long):Boolean
    fun sendCode(numberType: Boolean, number: String,type:String):Map<String,Any>
    fun sendCode(phoneNumber:String):Boolean
}