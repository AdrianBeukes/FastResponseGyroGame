package com.hearx.responseGame.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hearx.responseGame.R
import com.hearx.responseGame.constants.Constants
import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val username = intent.getStringExtra(Constants.USER_NAME)
        val totalQuestions = intent.getIntExtra(Constants.TOTAL_ROUNDS, 0)
        val correctAnswers = intent.getIntExtra(Constants.TOTAL_SCORE, 0)

        tv_name.text = username
        tv_score.text = getString(R.string.score, correctAnswers, totalQuestions)

        btn_finsih.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }
}