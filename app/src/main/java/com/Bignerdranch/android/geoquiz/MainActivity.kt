package com.Bignerdranch.android.geoquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar

//logging app life cycles
private const val TAG = "MainActivity" //no spaces better for filter in logcat
private const val KEY_INDEX = "index" //creating a key index to pair it with currentindex
                                     // for onSaveInstanceState (bundle?) to save data once
                                    // app is killed.

private const val KEYS = "key"
private const val VALUE = "value"

class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var questionTextView: TextView
    private lateinit var prevButton : ImageButton
    private lateinit var answersMap : MutableMap<Int, Boolean>

    /*linking viewmodel class to main activity
             ViewModelProvider acts like a registry of VIewModel, once called
              it creates and returns a new instance of quizViewModel, when the activity
              queries for the QuizViewModel after configuration change, it returns the instance
              it created first.

          val provider : ViewModelProvider = ViewModelProviders.of(this)
          val quizViewModel = provider.get(QuizViewModel::class.java)
          Log.d(TAG, "Got a QuizViewModel: $quizViewModel")
          ***BELOW***
           */
    private val quizViewModel : QuizViewModel by lazy {
        ViewModelProviders.of(this).get(QuizViewModel::class.java)
    }

// this is forbidden, since it initialize the ViewModel before onCreate, and crash the app.
//    val currentIndex = quizViewModel.currentIndex
//    val questionBank = quizViewModel.questionBank


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)
        //checking for saved data and getting them if not null
        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        quizViewModel.currentIndex = currentIndex
        val keysArray = savedInstanceState?.getIntArray(KEYS) ?: IntArray(0)
        val valuesArray = savedInstanceState?.getBooleanArray(VALUE) ?: BooleanArray(0)
        answersMap = mutableMapOf<Int, Boolean>().apply {
            for (i in keysArray.indices) this [keysArray[i]] = valuesArray[i]
        }

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        questionTextView = findViewById(R.id.question_text_view)
        prevButton = findViewById(R.id.prev_button)

        updateQuestion()

        questionTextView.setOnClickListener{
            quizViewModel.moveToNext()
            updateQuestion()
        }

        trueButton.setOnClickListener {
            checkAnswer(true)
            }

        falseButton.setOnClickListener {
           checkAnswer(false)
        }

        prevButton.setOnClickListener {
            if (::answersMap.isInitialized && answersMap.size == quizViewModel.questionBank.size){
                val score = answersMap.count { it.value == true }
                val snackbar : Snackbar = Snackbar.make(it, "Your score is $score of ${quizViewModel.questionBank.size}", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Reset?"){
                        quizViewModel.currentIndex =0
                        updateQuestion()
                        answersMap.clear()
                    }
                snackbar.show()
            }else {
                quizViewModel.moveToPrev()
                    updateQuestion()
                }
            }

        nextButton.setOnClickListener {
            if (::answersMap.isInitialized && answersMap.size == quizViewModel.questionBank.size){
                val score = answersMap.count { it.value == true }
                val snackbar : Snackbar = Snackbar.make(it, "Your score is $score of ${quizViewModel.questionBank.size}", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Reset?"){
                        quizViewModel.currentIndex =0
                        answersMap.clear()
                        updateQuestion()
                    }
                snackbar.show()
            }else {
                quizViewModel.moveToNext()
                updateQuestion()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart(Bundle?) called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "OnResume(Bundle?) called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "OnPause(Bundle?) called")
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putInt(KEY_INDEX, quizViewModel.currentIndex) //<-- saving currentindex
        Log.d(TAG, "OnSave(BUndle?) Called")
        if (::answersMap.isInitialized){
            val keys = (answersMap.keys).toIntArray()
            val values = (answersMap.values).toBooleanArray()
            savedInstanceState.putIntArray(KEYS, keys)
            savedInstanceState.putBooleanArray(VALUE, values)
        }
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "OnStop(BUndle?) Called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "OnDestroyed(Bundle?) called")
    }

    private fun checkAnswer(userAnswer : Boolean){
        val correctAnswer = quizViewModel.questionBank[quizViewModel.currentIndex].answer
        val messageResId = if (userAnswer == correctAnswer){
            if (::answersMap.isInitialized){
                answersMap.put(quizViewModel.currentIndex,true)
            }else{
                answersMap = mutableMapOf(quizViewModel.currentIndex to true)
            }
            R.string.correct_toast

        } else {
            if (::answersMap.isInitialized){
                answersMap.put(quizViewModel.currentIndex,false)
            }else{
                answersMap = mutableMapOf(quizViewModel.currentIndex to false)
            }
            R.string.false_toast
        }
        Toast.makeText(this,messageResId ,Toast.LENGTH_SHORT).show()

        //create a mutableMap to save the answers with their K: index & V: UserAnswer

    }
    private fun updateQuestion() {
        //We create a value that access and save the text in
        // the String file using the Question class template.
        val questionTextResId = quizViewModel.questionBank[quizViewModel.currentIndex].textResId

        //updating the text view field with the next question text from string.
        questionTextView.setText(questionTextResId)
        if (::answersMap.isInitialized) {
            if (answersMap.containsKey(key = quizViewModel.currentIndex)) {
                trueButton.isClickable = false
                falseButton.isClickable = false
                Toast.makeText(
                    this,
                    "you answered this question before, move to other questions",
                    Toast.LENGTH_LONG
                ).show()
            }else {
                trueButton.isClickable = true
                falseButton.isClickable = true
            }
        }
    }
}

