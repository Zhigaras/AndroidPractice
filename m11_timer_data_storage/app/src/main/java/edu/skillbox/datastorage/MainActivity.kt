package edu.skillbox.datastorage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val repository = Repository(this)
        val saveButton = findViewById<Button>(R.id.save_button)
        val clearButton = findViewById<Button>(R.id.clear_button)
        val textInput = findViewById<TextInputEditText>(R.id.input_text)
        val textOutput = findViewById<TextView>(R.id.output_text)

        fun updateTextOutput() { textOutput.text = repository.getText() }

        updateTextOutput()

        saveButton.setOnClickListener {
            repository.saveText(textInput.text.toString())
            updateTextOutput()
        }

        clearButton.setOnClickListener {
            repository.clearText()
            updateTextOutput()
        }
    }
}