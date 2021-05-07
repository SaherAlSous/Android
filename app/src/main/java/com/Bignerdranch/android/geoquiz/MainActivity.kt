package com.Bignerdranch.android.geoquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

//logging app life cycles
private const val TAG = "MainActivity" //no spaces better for filter in logcat

class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var questionTextView: TextView
    private lateinit var prevButton : ImageButton
    private lateinit var answersMap : MutableMap<Int, Boolean>

    private val questionBank = listOf( //We added objects of data class constructor
                                        // linked with the questions in R.String with answer.
        Question(R.string.question_australia,true),
        Question(R.string.question_africa,false),
        Question(R.string.question_oceans,true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )

    //creating an index to be used for questions in list
    private var currentIndex = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        questionTextView = findViewById(R.id.question_text_view)
        prevButton = findViewById(R.id.prev_button)

        updateQuestion()

        questionTextView.setOnClickListener{
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
        }

        trueButton.setOnClickListener {

            checkAnswer(true)
//            Snackbar.make(it, R.string.correct_toast, Snackbar.LENGTH_LONG)
//                .setAction("Show Toast") {
//                    val toastTrue = Toast.makeText(this, R.string.correct_toast, Toast.LENGTH_SHORT)
//                    toastTrue.setGravity(Gravity.TOP,0,0)
//                    toastTrue.show()
//                }
//                .show()
            }

        falseButton.setOnClickListener {
           checkAnswer(false)
        }

        prevButton.setOnClickListener {
            if (::answersMap.isInitialized && answersMap.size == questionBank.size){
                val score = answersMap.count { it.value == true }
                val snackbar : Snackbar = Snackbar.make(it, "Your score is $score of ${questionBank.size}", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Reset?"){
                        currentIndex =0
                        updateQuestion()
                        answersMap.clear()
                    }
                    snackbar.show()
            }else {
                if (currentIndex >= 1) {
                    currentIndex = (currentIndex - 1) % questionBank.size
                    updateQuestion()
                } else {
                    currentIndex = questionBank.size - 1
                    updateQuestion()
                }
            }
        }

        nextButton.setOnClickListener {
            if (::answersMap.isInitialized && answersMap.size == questionBank.size){
                val score = answersMap.count { it.value == true }
                val snackbar : Snackbar = Snackbar.make(it, "Your score is $score of ${questionBank.size}", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Reset?"){
                        currentIndex =0
                        answersMap.clear()
                        updateQuestion()
                    }
                snackbar.show()
            }else {
                currentIndex = (currentIndex + 1) % questionBank.size
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

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "OnStop(BUndle?) Called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "OnDestroyed(Bundle?) called")
    }
    private fun updateQuestion() {
        //We create a value that access and save the text in
        // the String file using the Question class template.
        val questionTextResId = questionBank[currentIndex].textResId

        //updating the text view field with the next question text from string.
        questionTextView.setText(questionTextResId)
        if (::answersMap.isInitialized) {
            if (answersMap.containsKey(key = currentIndex)) {
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
    private fun checkAnswer(userAnswer : Boolean){
        val correctAnswer = questionBank[currentIndex].answer
        val messageResId = if (userAnswer == correctAnswer){
            if (::answersMap.isInitialized){
                answersMap.put(currentIndex,true)
            }else{
                answersMap = mutableMapOf(currentIndex to true)
            }
            R.string.correct_toast

        } else {
            if (::answersMap.isInitialized){
                answersMap.put(currentIndex,false)
            }else{
                answersMap = mutableMapOf(currentIndex to false)
            }
            R.string.false_toast
        }
        Toast.makeText(this,messageResId ,Toast.LENGTH_SHORT).show()

        //create a mutableMap to save the answers with their K: index & V: UserAnswer

    }
}