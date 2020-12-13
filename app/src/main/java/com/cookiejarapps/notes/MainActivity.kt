package com.cookiejarapps.notes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cookiejarapps.notes.cards.CardAdapter
import com.cookiejarapps.notes.cards.OnCardButtonClickListener
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

        arrayAdapter = CardAdapter(notes, object: OnCardButtonClickListener {
             override fun onPositionClicked(position: Int) {
                 val intent = Intent(applicationContext, NoteEditorActivity::class.java)
                 intent.putExtra("noteID", position)
                 startActivity(intent)
            }

            override fun onLongClicked(position: Int) {
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
        listView.adapter = arrayAdapter

        itemTouchHelper.attachToRecyclerView(listView)

        val mLayoutManager = LinearLayoutManager(this)
        listView.setLayoutManager(mLayoutManager)


    }

    private val itemTouchHelper by lazy {
        val simpleItemTouchCallback =
            object : ItemTouchHelper.SimpleCallback(UP or
                    DOWN or
                    START or
                    END, 0) {

                override fun onMove(recyclerView: RecyclerView,
                                    viewHolder: RecyclerView.ViewHolder,
                                    target: RecyclerView.ViewHolder): Boolean {

                    val adapter = recyclerView.adapter as CardAdapter
                    val from = viewHolder.adapterPosition
                    val to = target.adapterPosition

                    //adapter.moveItem(from, to)
                    adapter.notifyItemMoved(from, to)

                    return true
                }    override fun onSwiped(viewHolder: RecyclerView.ViewHolder,
                                           direction: Int) {
                    Toast.makeText(this@MainActivity, direction, Toast.LENGTH_SHORT).show()
                }
            }
        ItemTouchHelper(simpleItemTouchCallback)
    }

    companion object {
        var notes = ArrayList<String>()
        var arrayAdapter: CardAdapter? = null
    }
}
