package com.Bignerdranch.android.geoquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var questionTextView: TextView
    private lateinit var prevButton : ImageButton

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
        setContentView(R.layout.activity_main)

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        questionTextView = findViewById(R.id.question_text_view)
        prevButton = findViewById(R.id.prev_button)

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
            if (currentIndex >= 1){
                currentIndex = (currentIndex - 1) % questionBank.size
                updateQuestion()
            } else {
                currentIndex = questionBank.size - 1
                updateQuestion()
            }
        }

        nextButton.setOnClickListener {
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
        }

        updateQuestion()
    }
    private fun updateQuestion() {
        //We create a value that access and save the text in
        // the String file using the Question class template.
        val questionTextResId = questionBank[currentIndex].textResId

        //updating the text view field with the next question text from string.
        questionTextView.setText(questionTextResId)
    }
    private fun checkAnswer(userAnser : Boolean){
        val correctAnser = questionBank[currentIndex].answer
        val messageResId = if (userAnser == correctAnser) R.string.correct_toast else R.string.false_toast
        Toast.makeText(this,messageResId ,Toast.LENGTH_SHORT).show()
    }
}