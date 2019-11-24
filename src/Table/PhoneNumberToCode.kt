package com.zlz2000.Table

import me.liuwj.ktorm.schema.Table
import me.liuwj.ktorm.schema.long
import me.liuwj.ktorm.schema.varchar

object PhoneNumberToCode :Table<Nothing>("phone_number_to_code"){
    val phoneNumber by varchar("phone_number").primaryKey()
    val code by varchar("code")
    val time by long("time")
}