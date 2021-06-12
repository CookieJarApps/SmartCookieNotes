package com.cookiejarapps.notes.cards

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.ColorUtils
import androidx.recyclerview.widget.RecyclerView
import com.cookiejarapps.notes.Note
import com.cookiejarapps.notes.R
import com.google.android.material.card.MaterialCardView
import java.lang.ref.WeakReference
import kotlin.collections.ArrayList

/**
 * Provide a reference to the type of views that you are using
 * (custom ViewHolder).
 */
class CardAdapter(val items: ArrayList<Note>, val listener: OnCardButtonClickListener):
    RecyclerView.Adapter<CardAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View, listener: OnCardButtonClickListener?) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val titleView: TextView
        val contentView: TextView
        val imageButton: ImageButton
        val cardView: MaterialCardView
        private var listenerRef: WeakReference<OnCardButtonClickListener>? = null

        init {
            listenerRef = WeakReference(listener!!)
            // Define click listener for the ViewHolder's View.
            titleView = view.findViewById(R.id.card_title)
            contentView = view.findViewById(R.id.card_subtitle)
            imageButton = view.findViewById(R.id.moreButton)
            cardView = view.findViewById(R.id.card_view)
            cardView.setOnClickListener(this)
            imageButton.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            if(v?.id == R.id.card_view){
                listenerRef?.get()?.onPositionClicked(v, adapterPosition)
            }
            else{
                listenerRef?.get()?.onButtonClicked(v!!, adapterPosition)
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

        viewHolder.titleView.text = items[position].title
        viewHolder.contentView.text = items[position].content
        //val color = items[position].colour or -0x1000000
        val color = items[position].colour

        if(ColorUtils.calculateLuminance(color) < 0.5) {
            viewHolder.titleView.setTextColor(Color.WHITE)
            viewHolder.contentView.setTextColor(Color.WHITE)
            viewHolder.imageButton.setColorFilter(Color.WHITE)
        }

        viewHolder.cardView.background.setTint(color)
    }
    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = items.size

}
