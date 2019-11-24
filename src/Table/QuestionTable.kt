package com.zlz2000.Table

import me.liuwj.ktorm.schema.Table
import me.liuwj.ktorm.schema.int
import me.liuwj.ktorm.schema.varchar

object Question : Table<Nothing>("question"){
    val questionTitle by varchar("question_title")
    val questionId by int("question_id").primaryKey()
    val questionType by int("question_type")
    val questionOptionA by varchar("question_option_a")
    val questionOptionB by varchar("question_option_b")
    val questionOptionC by varchar("question_option_c")
    val questionOptionD by varchar("question_option_d")
    val questionAnswer by varchar("question_answer")
}