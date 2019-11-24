package com.zlz2000.Dao

import com.zlz2000.Bean.QuestionChoose
import com.zlz2000.Bean.QuestionFill
import com.zlz2000.Bean.QuestionTOrF
import com.zlz2000.Table.Question
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.insert

inline fun <reified T> saveQuestion(data: T):Boolean{
    when(T::class.java){
        QuestionChoose::class.java -> {
            data as QuestionChoose
            Question.insert {
                it.questionType to 1
                it.questionTitle to data.title
                it.questionOptionA to data.a
                it.questionOptionB to data.b
                it.questionOptionC to data.c
                it.questionOptionD to data.d
                it.questionAnswer to data.answer
            }
            return true
        }
        QuestionTOrF::class.java -> {
            data as QuestionTOrF
            Question.insert {
                it.questionType to 2
                it.questionTitle to data.title
                it.questionAnswer to data.answer
            }
            return true
        }
        QuestionFill::class.java -> {
            data as QuestionFill
            Question.insert {
                it.questionType to 3
                it.questionTitle to data.title
                it.questionAnswer to data.answer
            }
            return true
        }
    }
    return false
}