package com.Bignerdranch.android.geoquiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider

private const val EXTRA_ANSWER_IS_TRUE = "com.Bignerdranch.android.geoquiz.answer_is_true"
const val EXTRA_ANSWER_IS_SHOWN = "com.Bignerdranch.android.geoquiz.answer_shown"
private const val BOOLEAN_KEY = "BOO"
private var cheaterStatus = false
const val INDEX_INTENT = "INDEX_INTENT"


class CheatActivity : AppCompatActivity() {

    private lateinit var answerTextView: TextView
    private lateinit var showAnswerButton: Button


    //creating a variable that would save the extra from the intent.
    // it should be inside onCreate area to be retrieved once activity is created,
    // and has a default value (false) if extra was empty.

    private var answerIsTrue = false
    private var currentIndex = 0
    private var cheatedAnswer = mutableListOf<Int>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)
        //saving the status on configuration change.
        val cheatStatus = savedInstanceState?.getBoolean(BOOLEAN_KEY, false) ?: false
        setAnswerShownResult(cheatStatus)

        //getting the details from MAINACTIVITY
        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)
        currentIndex = intent.getIntExtra(INDEX_INTENT, 0)
        answerTextView = findViewById(R.id.answer_text_view)
        showAnswerButton= findViewById(R.id.show_answer_bottom)

        //if answer is true, show the text again
        if (cheatStatus) {
            val answerText = when {
                answerIsTrue -> R.string.true_button
                else -> R.string.false_button
            }
            answerTextView.setText(answerText)
        }

    showAnswerButton.setOnClickListener {
        val answerText = when{
            answerIsTrue -> R.string.true_button
            else -> R.string.false_button
        }
        answerTextView.setText(answerText)
        //create a function to return the result to MainActivity
        setAnswerShownResult(true)
        cheaterStatus = true

        //cheatedAnswer.add(currentIndex)

        cheatedList.add(currentIndex)

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
        fun newIntent(packageContext: Context, answerIsTrue: Boolean, currentIndex: Int) :Intent{
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
                putExtra(INDEX_INTENT, currentIndex)
                /*
                We called this function from MainActivity to prepare the Intent of this activity
                with its extras.
                 */
            }
        }
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.d(BOOLEAN_KEY, "onSave called.")
        savedInstanceState.putBoolean(BOOLEAN_KEY,cheaterStatus)
    }

    override fun onDestroy() {
        super.onDestroy()
//        for (element in cheatedAnswer){
//            MainActivity.cheatedList
//            println(MainActivity.cheatedList)
//        }
    }
}