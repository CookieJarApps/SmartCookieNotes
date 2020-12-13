package com.cookiejarapps.notes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cookiejarapps.notes.cards.CardAdapter
import com.cookiejarapps.notes.cards.OnCardClickListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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

        listView.addOnItemTouchListener(
            OnCardClickListener(this, listView, object: OnCardClickListener.OnItemClickListener {
                override fun onItemClick(view: View?, position: Int) {
                    val intent = Intent(applicationContext, NoteEditorActivity::class.java)
                    intent.putExtra("noteID", position)
                    startActivity(intent)
                }

                override fun onLongItemClick(view: View?, position: Int) {
                       MaterialAlertDialogBuilder(this@MainActivity)
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
                                        "com.cookiejarapps.notes",
                                        Context.MODE_PRIVATE
                                    )
                                val set =
                                    HashSet(notes)
                                sharedPreferences.edit().putStringSet("notes", set).apply()
                            }
                            .setNegativeButton("No", null)
                            .show()
                        true
                }
            })
        )
    }

    companion object {
        var notes = ArrayList<String>()
        var arrayAdapter: CardAdapter? = null
    }
}
