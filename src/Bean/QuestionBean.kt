package com.zlz2000.Bean

data class QuestionChoose(
    val title:String,
    val a:String,
    val b:String,
    val c:String,
    val d:String,
    val answer:String
)

data class QuestionTOrF(
    val title: String,
    val answer: String
)

data class QuestionFill(
    val title: String,
    val answer: String
)