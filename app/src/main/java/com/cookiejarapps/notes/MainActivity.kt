package com.cookiejarapps.notes

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView.OnItemLongClickListener
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import java.util.*


class MainActivity : AppCompatActivity() {
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.dropdown, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        if (item.itemId == R.id.add_note) {
            val intent = Intent(applicationContext, NoteEditorActivity::class.java)
            startActivity(intent)
            return true
        }
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val listView =
            findViewById<View>(R.id.listView) as ListView
        val sharedPreferences = applicationContext.getSharedPreferences(
            "com.cookiejarapps.notes",
            Context.MODE_PRIVATE
        )
        val set =
            sharedPreferences.getStringSet("notes", null) as HashSet<String>?
        if (set == null) {
            notes.add("Example Note")
        } else {
            notes =
                ArrayList(set) // to bring all the already stored data in the set to the notes ArrayList
        }
        arrayAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            notes
        )
        listView.adapter = arrayAdapter
        listView.onItemClickListener =
            OnItemClickListener { parent, view, position, id ->
                val intent = Intent(applicationContext, NoteEditorActivity::class.java)
                intent.putExtra("noteID", position)
                startActivity(intent)
            }
        listView.onItemLongClickListener =
            OnItemLongClickListener { parent, view, position, id ->
                AlertDialog.Builder(this@MainActivity) // we can't use getApplicationContext() here as we want the activity to be the context, not the application
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Delete?")
                    .setMessage("Are you sure you want to delete this note?")
                    .setPositiveButton(
                        "Yes"
                    ) { dialog, which ->
                        // to remove the selected note once "Yes" is pressed
                        notes.removeAt(position)
                        arrayAdapter!!.notifyDataSetChanged()
                        val sharedPreferences =
                            applicationContext.getSharedPreferences(
                                "com.tanay.thunderbird.deathnote",
                                Context.MODE_PRIVATE
                            )
                        val set =
                            HashSet(notes)
                        sharedPreferences.edit().putStringSet("notes", set).apply()
                    }
                    .setNegativeButton("No", null)
                    .show()
                true // this was initially false but we change it to true as if false, the method assumes that we want to do a short click after the long click as well
            }
    }

    companion object {
        var notes = ArrayList<String>()
        var arrayAdapter: ArrayAdapter<String>? = null
    }
}
