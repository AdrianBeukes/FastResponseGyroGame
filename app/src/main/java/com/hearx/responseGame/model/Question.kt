package com.hearx.responseGame.model

data class Question(
    val id: Int,
    val direction: Int,
    val randomDisplayTime: Int,
    val answer: Int
)