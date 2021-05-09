package com.hearx.responseGame.ui

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.ImageViewCompat
import com.hearx.responseGame.model.Question
import com.hearx.responseGame.R
import com.hearx.responseGame.R.drawable
import com.hearx.responseGame.component.Gyroscope
import com.hearx.responseGame.constants.Constants
import kotlinx.android.synthetic.main.activity_quiz_questions.*
import java.util.*
import kotlin.concurrent.schedule

class QuizQuestionsActivity : AppCompatActivity(), View.OnClickListener {

    private var mCurrentPosition: Int = 1
    private var mQuestionsList: ArrayList<Question>? = null
    private var mSelectedOptionPosition: Int = 0
    private var mCorrectAnswers: Int = 0
    private var mUserName: String? = null
    private var mArrowColor: String? = null
    private var mFirstRound = true
    private lateinit var mGyroscope: Gyroscope
    private var mHasAnswered = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_questions)

        mUserName = intent.getStringExtra(Constants.USER_NAME)
        mArrowColor = intent.getStringExtra(Constants.ARROW_COLOUR)
        mQuestionsList = Constants.getQuestions()
        mGyroscope = Gyroscope(this)

        setQuestion()

        btn_submit.setOnClickListener(this)

        ImageViewCompat.setImageTintList(
            iv_image,
            ColorStateList.valueOf(Color.parseColor(mArrowColor))
        );
    }

    private fun setQuestion() {
        val question = mQuestionsList!![mCurrentPosition - 1]
        mHasAnswered = false

        mGyroscope.register()

        runOnUiThread {
            //reset arrow to transparent image after each round
            iv_image.setImageResource(drawable.ic_transparent)
            btn_submit.visibility = View.GONE

            if (mFirstRound) {
                val timer = object : CountDownTimer(4000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        if (millisUntilFinished > 0.toLong())
                            tv_start_timer.text = (millisUntilFinished / 1000).toString()
                    }

                    override fun onFinish() {
                        setArrowDirection(question)
                        ll_start_timer.visibility = View.GONE
                        mFirstRound = false
                    }
                }
                timer.start()
            }
        }

        //on first round don't set arrow direction at start,
        //it will start the random timer premature, need to do countdown for game begin on first round
        if (!mFirstRound)
            setArrowDirection(question)

        if (mCurrentPosition == mQuestionsList!!.size) {
            btn_submit.text = getString(R.string.finish)
        } else {
            btn_submit.text = getString(R.string.next)
        }

        progressBar.progress = mCurrentPosition
        tv_progress.text = getString(R.string.progress, mCurrentPosition, progressBar.max)
    }

    private fun setArrowDirection(question: Question) {
        var directionArrow: Int

        Timer("WaitTimeBeforeArrowDisplay", false).schedule(question.randomDisplayTime.toLong()) {
            runOnUiThread {
                directionArrow = when (question.direction) {
                    1 -> drawable.ic_arrow_left
                    2 -> drawable.ic_arrow_right
                    3 -> drawable.ic_arrow_up
                    else -> drawable.ic_arrow_down
                }
                iv_image.setImageResource(directionArrow)
            }
            setGyroscopeListener(question)
        }
    }

    private fun setGyroscopeListener(question: Question) {
        mGyroscope.setListener { rx, ry, rz ->
            if (!mHasAnswered) {
                //x axis is for left and right rotations
                if (rx > 2.5f) {
                    checkAnswer(question, 1, 2)
                }
                //y axis is for up and down rotations
                else if (ry > 2.5f) {
                    checkAnswer(question, 3, 4)
                }
            }
        }
    }

    private fun checkAnswer(question: Question, optionOne: Int, optionTwo: Int) {
        if (question.direction == optionOne || question.direction == optionTwo) {
            Toast.makeText(this, getString(R.string.toast_correct), Toast.LENGTH_SHORT).show()
            mCorrectAnswers++
            btn_submit.visibility = View.VISIBLE
        } else {
            Toast.makeText(this, getString(R.string.toast_incorrect), Toast.LENGTH_SHORT).show()
            mCorrectAnswers--
            btn_submit.visibility = View.VISIBLE
        }
        tv_total_score.text = getString(R.string.score_in_game, mCorrectAnswers)
        mHasAnswered = true
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_submit -> {
                if (mSelectedOptionPosition == 0) {
                    mCurrentPosition++

                    when {
                        mCurrentPosition <= mQuestionsList!!.size -> {
                            mGyroscope.unregister()
                            setQuestion()
                        }
                        else -> {
                            val intent = Intent(this, ResultActivity::class.java)
                            intent.putExtra(Constants.USER_NAME, mUserName)
                            intent.putExtra(Constants.TOTAL_SCORE, mCorrectAnswers)
                            intent.putExtra(Constants.TOTAL_ROUNDS, mQuestionsList!!.size)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        mGyroscope.unregister()
    }
}