package com.cookiejarapps.notes.cards

import android.view.View

interface OnCardButtonClickListener {
    fun onPositionClicked(position: Int)
    fun onLongClicked(position: Int)
    fun onButtonClicked(id: View, position: Int)
}