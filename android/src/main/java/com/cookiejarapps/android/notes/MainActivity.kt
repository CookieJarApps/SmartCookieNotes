package com.cookiejarapps.android.notes

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cookiejarapps.notes.BottomSheetFragment
import com.cookiejarapps.notes.Note
import com.cookiejarapps.notes.SheetButtonInterface
import com.cookiejarapps.notes.cards.CardAdapter
import com.cookiejarapps.notes.cards.OnCardButtonClickListener
import com.cookiejarapps.notes.database.DatabaseHelper
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import java.util.*
import kotlin.collections.ArrayList


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
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
            return true
        }
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        setExitSharedElementCallback(MaterialContainerTransformSharedElementCallback())
        window.sharedElementsUseOverlay = false

        setContentView(R.layout.activity_main)

        val database = DatabaseHelper(this)

        val listView =
            findViewById<View>(R.id.listView) as RecyclerView

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

                    adapter.notifyItemMoved(from, to)
                    moveItem(from, to)

                    return true
                }    override fun onSwiped(viewHolder: RecyclerView.ViewHolder,
                                           direction: Int) {
                    Toast.makeText(this@MainActivity, direction, Toast.LENGTH_SHORT).show()
                }
            }
        ItemTouchHelper(simpleItemTouchCallback)
    }


    fun moveItem(from: Int, to: Int) {
        val oldTab = notes.toMutableList().get(from)
        var oldTabList: MutableList<Note> = notes
        oldTabList.removeAt(from)
        oldTabList.add(to, oldTab)

        DatabaseHelper(this).deleteAll()

        for(i in oldTabList){
            i.id = oldTabList.indexOf(i) + 1
            DatabaseHelper(this).insertNote(i.title, i.content, i.colour)
        }

    }

    fun showBottomSheetDialogFragment(position: Int) {
        val bottomSheetFragment = BottomSheetFragment(object: SheetButtonInterface {
            override fun buttonClicked(v: View?) {
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

        })
        val bundle = Bundle()
        val title = notes[position].title
        bundle.putString("title", title)
        val id = notes[position].id
        bundle.putInt("id", id)
        bottomSheetFragment.setArguments(bundle)
        bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
    }

    companion object {
        var notes = ArrayList<Note>()
        var arrayAdapter: CardAdapter? = null
    }
}
