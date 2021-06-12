package com.cookiejarapps.android.notes

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.cookiejarapps.notes.Note
import com.cookiejarapps.notes.database.DatabaseHelper
import com.google.gson.Gson
import java.util.*


class NoteEditorActivity : AppCompatActivity() {
    var noteID = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)
        val editText = findViewById<View>(R.id.editTextTitle) as EditText
        val editTextContent = findViewById<View>(R.id.editText) as EditText

        val database = DatabaseHelper(this)

        val intent = intent

        noteID = intent.getIntExtra("noteID", -1)
        if (noteID != -1) {
            editTextContent.setText(MainActivity.notes[noteID].content)
            editText.setText(MainActivity.notes[noteID].title)
        } else {
            noteID = MainActivity.notes.size
            MainActivity.notes.add(Note(noteID, "", "", getMatColor("500")))
            database.insertNote("", "", getMatColor("500"))

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
                database.updateNote(noteID + 1, s.toString(), null, null)
                MainActivity.arrayAdapter!!.notifyDataSetChanged()
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
                database.updateNote(noteID + 1, null, s.toString(), null)
                MainActivity.arrayAdapter!!.notifyDataSetChanged()
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
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.note, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.getItemId()
        if (id == R.id.close) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

}
