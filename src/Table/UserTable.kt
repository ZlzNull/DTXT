package com.zlz2000.Table

import me.liuwj.ktorm.schema.Table
import me.liuwj.ktorm.schema.int
import me.liuwj.ktorm.schema.varchar

object UserTable :Table<Nothing>("user") {
    val userId by int("user_id").primaryKey()
    val userName by varchar("user_name")
    val userWorkId by varchar("user_work_id").primaryKey()
    val userPhoneNumber by varchar("user_phone_number").primaryKey()
    val userType by int("user_type")
    val userPassword by varchar("user_password")
    val userPost by varchar("user_post")
}