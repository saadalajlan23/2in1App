package com.example.a2in1app


import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var numbersGameButton: Button
    private lateinit var guessThePhraseButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        numbersGameButton = findViewById(R.id.btNumbersGame)
        numbersGameButton.setOnClickListener {
            val intent = Intent(this, NumbersGame::class.java)
            startActivity(intent)
        }
        guessThePhraseButton = findViewById(R.id.btGuessThePhrase)
        guessThePhraseButton.setOnClickListener {
            val intent = Intent(this, GuessThePhrases::class.java)
            startActivity(intent)
        }

        title = "Main Activity"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mi_numbers_game -> {
                val intent = Intent(this, NumbersGame::class.java)
                startActivity(intent)
                return true
            }
            R.id.mi_guess_the_phrase -> {
                val intent = Intent(this, GuessThePhrases::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)



    }
}