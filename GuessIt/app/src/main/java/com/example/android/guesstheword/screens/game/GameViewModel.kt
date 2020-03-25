package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {

    companion object {
        const val ONE_SECOND = 1000L
        const val COUNTDOWN_PANIC = 10000L
        const val COUNTDOWN_TIME = 60000L
    }

    private val timer: CountDownTimer

    private val _time = MutableLiveData<Long>()
    val time = Transformations.map(_time) { t ->
        DateUtils.formatElapsedTime(t / ONE_SECOND)
    }

    private val _word = MutableLiveData<String>()
    val word: LiveData<String> get() = _word

    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int> get() = _score

    private val _eventGameFinish = MutableLiveData<Boolean>()
    val eventGameFinish: LiveData<Boolean> get() = _eventGameFinish

    private val _buzz = MutableLiveData<BuzzType>()
    val buzz: LiveData<BuzzType> get() = _buzz

    private lateinit var wordList: MutableList<String>

    init {
        resetList()
        _score.value = 0
        _eventGameFinish.value = false

        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {

            override fun onTick(millisUntilFinished: Long) {
                _time.value = millisUntilFinished

                if (millisUntilFinished <= COUNTDOWN_PANIC) {
                    _buzz.value = BuzzType.COUNTDOWN_PANIC
                }
            }

            override fun onFinish() {
                _buzz.value = BuzzType.GAME_OVER
                _eventGameFinish.value = true
            }

        }.start()

        nextWord()
    }

    /** Moves to the next word in the list */
    private fun nextWord() {
        if (wordList.isEmpty()) {
            resetList()
        }

        _word.value = wordList.removeAt(0)
    }

    /** Methods for buttons presses **/
    fun onSkip() {
        _score.value = _score.value?.minus(1)
        nextWord()
    }

    fun onCorrect() {
        _score.value = _score.value?.plus(1)
        _buzz.value = BuzzType.CORRECT
        nextWord()
    }

    fun onGameFinishComplete() {
        _eventGameFinish.value = false
    }

    fun onBuzzComplete() {
        _buzz.value = BuzzType.NO_BUZZ
    }

    override fun onCleared() {
        super.onCleared()
        timer.cancel()
    }

    /** Resets the list of words and randomizes the order */
    private fun resetList() {
        wordList = mutableListOf("queen", "hospital", "basketball", "cat", "change", "snail", "soup",
                "calendar", "sad", "desk", "guitar", "home", "railway", "zebra", "jelly", "car", "crow",
                "trade", "bag", "roll", "bubble"
        )
        wordList.shuffle()
    }
}

