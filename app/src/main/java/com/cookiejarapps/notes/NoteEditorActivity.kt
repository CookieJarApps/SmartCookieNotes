package com.cookiejarapps.notes

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import java.util.*


class NoteEditorActivity : AppCompatActivity() {
    var noteID = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)
        val editText = findViewById<View>(R.id.editTextTitle) as EditText
        val editTextContent = findViewById<View>(R.id.editText) as EditText
        val intent = intent
        noteID = intent.getIntExtra("noteID", -1)
        if (noteID != -1) {
            editTextContent.setText(MainActivity.notes[noteID].content)
            editText.setText(MainActivity.notes[noteID].title)
        } else {
            MainActivity.notes.add(Note("", "", getMatColor("500")))
            noteID = MainActivity.notes.size - 1
            MainActivity.arrayAdapter!!.notifyDataSetChanged()
        }
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                MainActivity.notes[noteID].title = s.toString()
                updatePrefs()
            }

            override fun afterTextChanged(s: Editable) {}
        })
        editTextContent.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                MainActivity.notes[noteID].content = s.toString()
                updatePrefs()
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }

    fun updatePrefs(){
        MainActivity.arrayAdapter!!.notifyDataSetChanged()
        val sharedPreferences =
            applicationContext.getSharedPreferences(
                "com.cookiejarapps.notes",
                Context.MODE_PRIVATE
            )
        val set =
            HashSet(MainActivity.notes)

        val gson = Gson()
        val jsonString = gson.toJson(set)

        sharedPreferences.edit().putString("notes", jsonString).apply()
    }

    private fun getMatColor(typeColor: String): Int {
        var returnColor: Int = Color.BLACK
        val arrayId = resources.getIdentifier(
            "mdcolor_$typeColor",
            "array",
            applicationContext.packageName
        )
        if (arrayId != 0) {
            val colors = resources.obtainTypedArray(arrayId)
            val index = (Math.random() * colors.length()).toInt()
            returnColor = colors.getColor(index, Color.BLACK)
            colors.recycle()
        }
        return returnColor
    }
}
