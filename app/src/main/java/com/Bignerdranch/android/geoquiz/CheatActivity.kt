package com.Bignerdranch.android.geoquiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

private const val EXTRA_ANSWER_IS_TRUE = "com.Bignerdranch.android.geoquiz.answer_is_true"
const val EXTRA_ANSWER_IS_SHOWN = "com.Bignerdranch.android.geoquiz.answer_shown"


class CheatActivity : AppCompatActivity() {

    private lateinit var answerTextView: TextView
    private lateinit var showAnswerButton: Button

    //creating a variable that would save the extra from the intent.
    // it should be inside onCreate area to be retrieved once activity is created,
    // and has a default value (false) if extra was empty.

    private var answerIsTrue = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)
        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)
        answerTextView = findViewById(R.id.answer_text_view)
        showAnswerButton= findViewById(R.id.show_answer_bottom)

    showAnswerButton.setOnClickListener {
        val answerText = when{
            answerIsTrue -> R.string.true_button
            else -> R.string.false_button
        }
        answerTextView.setText(answerText)
        //create a function to return the result to MainActivity
        setAnswerShownResult(true)
    }

}

    private fun setAnswerShownResult(isAnswerShown: Boolean) {
    val data = Intent().apply {
        putExtra(EXTRA_ANSWER_IS_SHOWN, isAnswerShown)
    }
        setResult(Activity.RESULT_OK, data)
        /*
        when we use startActivityForResult to sent intent, we need to send result back
        sending result of an activity require us to use setResult(ActivityResult, data to be returned)
        since the button was pressed. then result is okay, is the activity was canceled with no action,
        then result_canceled. once the user press Back, the result of the activity will be sent to
        main activity.
         */
}

    companion object{
        fun newIntent(packageContext: Context, answerIsTrue: Boolean) :Intent{
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
                /*
                We called this function from MainActivity to prepare the Intent of this activity
                with its extras.
                 */
            }
        }
    }
}