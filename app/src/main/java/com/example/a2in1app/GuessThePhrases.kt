
package com.example.a2in1app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_guess_the_phrases.*

class GuessThePhrases : AppCompatActivity() {
    private lateinit var clRoot: ConstraintLayout
    private lateinit var guessEditText: EditText
    private lateinit var guessButton: Button
    private lateinit var messages: ArrayList<String>
    private lateinit var tvPhrase: TextView
    private lateinit var tvLetters: TextView

    private var answer = "android studio"
    private var myAnswerDictionary = mutableMapOf<Int, Char>()
    private var myAnswer = ""
    private var guessedLetters = ""
    private var count = 0
    private var guessPhrase = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guess_the_phrases)

        for(i in answer.indices){
            if(answer[i] == ' '){
                myAnswerDictionary[i] = ' '
                myAnswer += ' '
            }else{
                myAnswerDictionary[i] = '-'
                myAnswer += '-'
            }
        }

        clRoot = findViewById(R.id.clRootPhrase)
        messages = ArrayList()

        rvMessages.adapter = MessageAdapter(this, messages)
        rvMessages.layoutManager = LinearLayoutManager(this)

        guessEditText = findViewById(R.id.etGuessField)
        guessButton = findViewById(R.id.btGuessButton)
        guessButton.setOnClickListener { addMessage() }

        tvPhrase = findViewById(R.id.tvPhrase)
        tvLetters = findViewById(R.id.tvLetters)

        updateText()

        title = "Guess the Phrase"
    }

    override fun recreate() {
        super.recreate()
        answer = "android studio"
        myAnswerDictionary.clear()
        myAnswer = ""

        for(i in answer.indices){
            if(answer[i] == ' '){
                myAnswerDictionary[i] = ' '
                myAnswer += ' '
            }else{
                myAnswerDictionary[i] = '-'
                myAnswer += '-'
            }
        }

        guessedLetters = ""
        count = 0
        guessPhrase = true
        messages.clear()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString("answer", answer)

        val keys = myAnswerDictionary.keys.toIntArray()
        val values = myAnswerDictionary.values.toCharArray()
        outState.putIntArray("keys", keys)
        outState.putCharArray("values", values)

        outState.putString("myAnswer", myAnswer)
        outState.putString("guessedLetters", guessedLetters)
        outState.putInt("count", count)
        outState.putBoolean("guessPhrase", guessPhrase)
        outState.putStringArrayList("messages", messages)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        answer = savedInstanceState.getString("answer", "nothing here")

        val keys = savedInstanceState.getIntArray("keys")
        val values = savedInstanceState.getCharArray("values")
        if(keys != null && values != null){
            if(keys.size == values.size){
                myAnswerDictionary = mutableMapOf<Int, Char>().apply {
                    for (i in keys.indices) this [keys[i]] = values[i]
                }
            }
        }

        myAnswer = savedInstanceState.getString("myAnswer", "")
        guessedLetters = savedInstanceState.getString("guessedLetters", "")
        count = savedInstanceState.getInt("count", 0)
        guessPhrase = savedInstanceState.getBoolean("guessPhrase", true)
        messages.addAll(savedInstanceState.getStringArrayList("messages")!!)
        updateText()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_game, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val item: MenuItem = menu!!.getItem(1)
        if(item.title == "Other Game"){ item.title = "Numbers Game" }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.mi_new_game -> {
                CustomAlertDialog(this,"Are you sure you want to abandon the current game?")
                return true
            }
            R.id.mi_other_game -> {
                changeScreen(NumbersGame())
                return true
            }
            R.id.mi_back -> {
                changeScreen(MainActivity())
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun changeScreen(activity: Activity){
        val intent = Intent(this, activity::class.java)
        startActivity(intent)
    }

    private fun addMessage() {
       val msg = guessEditText.text.toString()

        if (msg.isNotEmpty() && msg.length == 1) {
            myAnswer = ""
            guessPhrase = false
            checkLetters(msg[0])
        } else {
            Snackbar.make(clRoot, "Please enter one letter only", Snackbar.LENGTH_LONG).show()
        }


        guessEditText.text.clear()
        guessEditText.clearFocus()
        rvMessages.adapter?.notifyDataSetChanged()
    }

    private fun disableEntry(){
        guessButton.isEnabled = false
        guessButton.isClickable = false
        guessEditText.isEnabled = false
        guessEditText.isClickable = false
    }

    private fun updateText(){
        tvPhrase.text = "Phrase:  " + myAnswer.toUpperCase()
        tvLetters.text = "Guessed Letters:  " + guessedLetters
        if(guessPhrase){
            guessEditText.hint = "Guess the full phrase"
        }else{
            guessEditText.hint = "Guess a letter"
        }
    }

    private fun checkLetters(guessedLetter: Char){
        var found = 0
        for(i in answer.indices){
            if(answer[i] == guessedLetter){
                myAnswerDictionary[i] = guessedLetter
                found++
            }
        }
        for(i in myAnswerDictionary){myAnswer += myAnswerDictionary[i.key]}
        if(myAnswer==answer){
            disableEntry()
            CustomAlertDialog(this,"You win!\n\nPlay again?")
        }
        if(guessedLetters.isEmpty()){guessedLetters+=guessedLetter}else{guessedLetters+=", "+guessedLetter}
        if(found>0){
            messages.add("Found $found ${guessedLetter.toUpperCase()}(s)")
        }else{
            messages.add("No ${guessedLetter.toUpperCase()}s found")
        }
        count++
        val guessesLeft = 10 - count
        if(count<10){messages.add("$guessesLeft guesses remaining")}
        updateText()
        rvMessages.scrollToPosition(messages.size - 1)
    }
}