package com.hearx.responseGame.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hearx.responseGame.R
import com.hearx.responseGame.constants.Constants
import dev.sasikanth.colorsheet.ColorSheet
import dev.sasikanth.colorsheet.utils.ColorSheetUtils
import kotlinx.android.synthetic.main.activity_main.*

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_start.setOnClickListener {
            if (edt_name.text.toString().isEmpty()) {
                Toast.makeText(this, getString(R.string.enter_name), Toast.LENGTH_SHORT).show()
            } else {
                ColorSheet().colorPicker(
                    colors = resources.getIntArray(R.array.colourPickerArray),
                    listener = { color ->
                        val intent = Intent(this, QuizQuestionsActivity::class.java)
                        intent.putExtra(Constants.USER_NAME, edt_name.text.toString())
                        intent.putExtra(Constants.ARROW_COLOUR, ColorSheetUtils.colorToHex(color))
                        startActivity(intent)
                        finish()
                    })
                    .show(supportFragmentManager)
            }
        }
    }
}