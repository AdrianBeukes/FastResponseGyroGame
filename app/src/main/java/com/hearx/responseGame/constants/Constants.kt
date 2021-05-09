package com.hearx.responseGame.constants

import com.hearx.responseGame.model.Question

object Constants {

    const val USER_NAME: String = "user_name"
    const val TOTAL_ROUNDS: String = "total_rounds"
    const val TOTAL_SCORE: String = "total_score"
    const val ARROW_COLOUR: String = "arrow_colour"

    fun getQuestions(): ArrayList<Question> {
        val questionsList = ArrayList<Question>()

        //creating a for loop to populate a list of 10 random questions, each having unique direction and start time
        for (question in 1..10) {
            val direction: Int = (1..4).random()
            val displayTime: Int = (2..5).random() * 1000

            val que = Question(
                question,
                direction,
                displayTime,
                direction
            )
            questionsList.add(que)
        }

        return questionsList
    }
}