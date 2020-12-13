package com.cookiejarapps.notes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cookiejarapps.notes.cards.CardAdapter
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
            findViewById<View>(R.id.listView) as RecyclerView
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
                ArrayList(set)
        }

        arrayAdapter = CardAdapter(notes)
        listView.adapter = arrayAdapter

        val mLayoutManager = LinearLayoutManager(this)
        listView.setLayoutManager(mLayoutManager)

        /*listView.onItemClickListener =
            OnItemClickListener { parent, view, position, id ->
                val intent = Intent(applicationContext, NoteEditorActivity::class.java)
                intent.putExtra("noteID", position)
                startActivity(intent)
            }
        listView.onItemLongClickListener =
            OnItemLongClickListener { parent, view, position, id ->
                AlertDialog.Builder(this@MainActivity)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Delete?")
                    .setMessage("Are you sure you want to delete this note?")
                    .setPositiveButton(
                        "Yes"
                    ) { dialog, which ->
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
                true
            }*/
    }

    companion object {
        var notes = ArrayList<String>()
        var arrayAdapter: CardAdapter? = null
    }
}
