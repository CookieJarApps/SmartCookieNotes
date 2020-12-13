package com.cookiejarapps.notes.cards

import android.content.ClipData
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.cookiejarapps.notes.R
import com.google.android.material.card.MaterialCardView
import java.lang.ref.WeakReference

/**
 * Provide a reference to the type of views that you are using
 * (custom ViewHolder).
 */
class CardAdapter(val items: ArrayList<String>, val listener: OnCardButtonClickListener):
    RecyclerView.Adapter<CardAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View, listener: OnCardButtonClickListener?) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val titleView: TextView
        val contentView: TextView
        val action_button_1: Button
        val cardView: MaterialCardView
        private var listenerRef: WeakReference<OnCardButtonClickListener>? = null

        init {
            listenerRef = WeakReference(listener!!)
            // Define click listener for the ViewHolder's View.
            titleView = view.findViewById(R.id.card_title)
            contentView = view.findViewById(R.id.card_subtitle)
            action_button_1 = view.findViewById(R.id.action_button_1)
            cardView = view.findViewById(R.id.card_view)
            action_button_1.setOnClickListener(this)
            cardView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            if(v == action_button_1){
                Toast.makeText(v?.getContext(), "BUTTON PRESSED", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(v?.getContext(), "ROW PRESSED = " + getAdapterPosition(), Toast.LENGTH_SHORT).show()
                listenerRef?.get()?.onPositionClicked(getAdapterPosition())
            }
        }

    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.note_card, viewGroup, false)

        return ViewHolder(view, listener)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.titleView.text = items[position]
        viewHolder.contentView.text = items[position]
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = items.size

}
