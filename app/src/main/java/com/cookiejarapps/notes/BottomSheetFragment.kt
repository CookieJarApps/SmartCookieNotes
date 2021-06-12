package com.cookiejarapps.notes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.cookiejarapps.notes.cards.OnCardButtonClickListener
import com.cookiejarapps.notes.database.DatabaseHelper
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.lang.ref.WeakReference


class BottomSheetFragment(val listener: SheetButtonInterface): BottomSheetDialogFragment() {
    private var listenerRef: WeakReference<SheetButtonInterface>? = null
    var title: String? = null
    var position: Int? = null

    fun setInterface() {
        listenerRef = WeakReference(listener!!)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setInterface()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.note_sheet, container, false)

        title = this.arguments!!.getString("title")
        position = this.arguments!!.getInt("id")

        view.findViewById<TextView>(R.id.note_name)?.text = title

        view.findViewById<Button>(R.id.delete).setOnClickListener{
            listener.buttonClicked(view.findViewById<Button>(R.id.delete))
        }

        return view
    }
}