package com.Bignerdranch.android.geoquiz

import android.util.Log
import androidx.lifecycle.ViewModel
private const val TAG = "QuizViewModel"

class QuizViewModel: ViewModel() {

    val questionBank = listOf( //We added objects of data class constructor
        // linked with the questions in R.String with answer.
        Question(R.string.question_australia,true),
        Question(R.string.question_africa,false),
        Question(R.string.question_oceans,true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )

    //creating an index to be used for questions in list
    var currentIndex = 0

    //adding the values of current question and functions of next & prev.
    val currentQuestionAnswer : Boolean
    get() = questionBank[currentIndex].answer

    val currentQuestionText : Int
    get() = questionBank[currentIndex].textResId

    fun moveToNext(){
        currentIndex = (currentIndex + 1) % questionBank.size
    }
    fun moveToPrev(){
        if (currentIndex == 0){
            currentIndex = questionBank.size - 1  //(currentIndex - 1) % questionBank.size
        }else{
            currentIndex -= 1
        }
    }



}