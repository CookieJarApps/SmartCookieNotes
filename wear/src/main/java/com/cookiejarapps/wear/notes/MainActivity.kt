package com.cookiejarapps.wear.notes

import android.app.ActivityOptions
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import android.view.View
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.wear.widget.WearableLinearLayoutManager
import androidx.wear.widget.WearableRecyclerView
import com.cookiejarapps.notes.BottomSheetFragment
import com.cookiejarapps.notes.Note
import com.cookiejarapps.notes.SheetButtonInterface
import com.cookiejarapps.notes.cards.CardAdapter
import com.cookiejarapps.notes.cards.OnCardButtonClickListener
import com.cookiejarapps.notes.database.DatabaseHelper
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : WearableActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Enables Always-on
        setAmbientEnabled()

        val database = DatabaseHelper(this)

        val listView =
            findViewById<View>(R.id.listView) as WearableRecyclerView

        if (database.numberOfRows() < 1) {
            database.insertNote("Example", "Example note!", R.color.purple_200)
        } else {
            notes =
                ArrayList(database.allNotes)
        }

        arrayAdapter = CardAdapter(notes, object: OnCardButtonClickListener {
            override fun onPositionClicked(id: View, position: Int) {
                val intent = Intent(applicationContext, NoteEditorActivity::class.java)
                intent.putExtra("noteID", position)
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this@MainActivity).toBundle())
            }

            override fun onLongClicked(position: Int) {
                true
            }

            override fun onButtonClicked(id: View, position: Int) {
                when (id.getId()) {
                    R.id.moreButton -> showBottomSheetDialogFragment(position)
                }
            }
        })
        listView.adapter = arrayAdapter
        listView.isCircularScrollingGestureEnabled = true


        val mLayoutManager = LinearLayoutManager(this)
        listView.setLayoutManager(mLayoutManager)

        findViewById<ImageButton>(R.id.addButton).setOnClickListener {
            val intent = Intent(applicationContext, NoteEditorActivity::class.java)
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        }
    }

    fun showBottomSheetDialogFragment(position: Int) {
        MaterialAlertDialogBuilder(this@MainActivity)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle("Delete?")
            .setMessage("Are you sure you want to delete this note?")
            .setPositiveButton(
                "Yes"
            ) { dialog, which ->
                DatabaseHelper(this@MainActivity).deleteNote(position)
                notes.removeAt(position)

                DatabaseHelper(this@MainActivity).deleteAll()

                for(i in notes){89
                    i.id = notes.indexOf(i) + 1
                    DatabaseHelper(this@MainActivity).insertNote(i.title, i.content, i.colour)
                }

                arrayAdapter?.notifyDataSetChanged()
                //activity.updateNotes()
            }
            .setNegativeButton("No", null)
            .show()
    }

    companion object {
        var notes = ArrayList<Note>()
        var arrayAdapter: CardAdapter? = null
    }
}